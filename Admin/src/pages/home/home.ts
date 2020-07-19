import {Component} from '@angular/core';
import {AlertController, LoadingController, ModalController, NavController} from 'ionic-angular';
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {AuthenticationProvider} from "../../providers/firebase/authentication";
import {UserModel} from "../../models/users";
import {DatabaseProvider} from "../../providers/firebase/database";
import {EventModel} from "../../models/events";
import {GeneralProvider} from "../../providers/general/general";
import {AddEventsPage} from "../add-events/add-events";
import {NotificationProvider} from "../../providers/firebase/notification";
import {FilterSortPage} from "../filter-sort/filter-sort";

@Component({
  selector: 'page-home',
  templateUrl: 'home.html'
})
export class HomePage {

  user: UserModel;
  counts = {category: 0, subscriber: 0, event: 0};
  now_showing: EventModel;
  x = null;
  loading: any;
  limit: number = 50;
  count: number = 0;
  searchInput: string = "";
  filtered: boolean = false;
  events: [EventModel];
  alert: any;


  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public authProvider: AuthenticationProvider, public dataProvider: DatabaseProvider, public generalProvider: GeneralProvider, public modalCtrl: ModalController, public notificationProvider: NotificationProvider, public loadingCtrl: LoadingController, public alertCtrl: AlertController) {
    this.getterSetter.header = "Home";
    this.loading = this.loadingCtrl.create();

    this.loading.present();
    this.setUser();
    this.setUpHome();
  }



  ionViewDidLeave() {
    this.loading.dismiss().catch((err) => {});
  }


  /* Set Logged In User */
  setUser() {
    if (!this.getterSetter.user) {
      this.authProvider.getCurrentUser().then((user) => {
        if (user) {
          this.user = user;
        } else {
          this.authProvider.logoutUser()
        }
      });
    } else {
      this.user = this.getterSetter.user;
    }
  }

  setUpHome() {
    this.setCounts();
    this.getEvents();
  }

  setCounts() {

    this.dataProvider.getCategoriesCount().then((count) => {
      this.counts.category = count ? count as number : 0;
    });
    this.dataProvider.getEventsCounts().then((count) => {
      this.counts.event = count ? count as number : 0;
      this.count = count ? count as number : 0;
    });
    this.dataProvider.getSubscribersCount().then((count) => {
      this.counts.subscriber = count ? count as number : 0;
    });
  }


  /* stop event timer */
  stopTimer(event) {
    if (this.x) {
      clearInterval(this.x);
    }
    event.now_showing = false;
    this.dataProvider.saveEvents(event).then(() => {
      this.generalProvider.presentToast(`Successfully stopped event.`, "bottom");
      this.getEvents();
    }).catch((err) => {
      this.generalProvider.presentToast(`An error occurred when trying to stop. ${err.toString()}`, "bottom");
    });
  }


  goToEvent(data) {
    let eventModal = this.modalCtrl.create(AddEventsPage, data);
    eventModal.onDidDismiss(data => {
      if (data) {
        let description = data.type === "add" ? `Fight between ${data.player1fname} and ${data.player2fname} added.` : `Fight between ${data.player1fname} and ${data.player2fname} has been updated.`;
        description = description.trim();
        let title = data.type === "add" ? "New fight added." : "Updated fight.";
        let timeid = new Date().getTime();
        if (data.type === "add" || data.duplicate) {
          // this.events.push(data);
          this.notificationProvider.sendNotification({
            id: timeid.toString(),
            uid: data.id,
            title: "New Fight Added",
            creationdate: timeid,
            type: "user",
            topic: "users",
            description: description
          });

          this.generalProvider.presentToast("Successfully saved your event.", "bottom");
        } else if (data.type === "update") {
          this.notificationProvider.sendNotification({
            id: timeid.toString(),
            uid: data.id,
            description: description,
            title: title,
            topic: title,
            creationdate: timeid,
            type: "user"
          });

          this.generalProvider.presentToast("Successfully updated your event.", "bottom");
        }
      }
    });
    eventModal.present();
  }

  /* Open modal for event addition, if data is returned add new event to global category list */
  addEvent() {
    this.goToEvent({modal_name: "Add Event", type: "add"})
  }

  /* Open modal for category update, if data is returned add new category to global category list */
  updateEvent(event, duplicate) {
    if(duplicate){
      event.id = String(Math.floor(Date.now() / 1000));
      event.uid = event.id;
      event.now_showing = false;
      event.subscribers = 0;
    }
    this.goToEvent({modal_name: duplicate ? "Add Event" : "Update Event", type: "update", event: event, duplicate: duplicate})
  }

  // infinite scroll
  doInfinite(): Promise<any> {
    return new Promise((resolve) => {
      this.limit += 50; // or however many more you want to load
      setTimeout(() => {
        this.getEvents();
      }, 1000);
    })
  }

  // Show Filter page
  showFilter() {
    let filterModal = this.modalCtrl.create(FilterSortPage);
    filterModal.present();
    filterModal.onDidDismiss(data => {
      if (data) {
        this.filtered = true;
        this.sortEvent(Object.assign([], this.events), data["sortby"]).then((events) => {
          this.filterEventByCategory(events, data["selectedcategory"]).then((events) => {
            this.events = events as [EventModel];
          });
        });
      }
    });
  }

  presentPrompt(event) {
    let title = "Fight Started";
    let description = `${event.player1fname.trim()} vs ${event.player2fname.trim()}`;
    this.alert = this.alertCtrl.create({
      title: 'Enter your notification message: ',
      inputs: [
        {
          name: 'title',
          placeholder: 'Title',
          type: 'text',
          value: title
        },
        {
          name: 'message',
          placeholder: 'Your Notification Message',
          type: 'text',
          value: description
        }
      ],
      buttons: [
        {
          text: 'Cancel',
          role: 'cancel',
          handler: data => {
            this.presentToast();
          }
        },
        {
          text: 'Trigger',
          handler: data => {
            description = data.message ? data.message : description;
            title = data.title ? data.title : title;
            this.triggerAlarm(event, description, title);
          }
        }
      ]
    });
    this.alert.present();
  }

  presentToast() {
    this.alert.dismiss();
    this.generalProvider.presentToast(`Cancelled alarm trigger.`, "bottom");
  }

  /* trigger alarm and set event date */
  triggerAlarm(event, description, title) {
    let eventdate = new Date().getTime();
    event.now_showing = true;
    this.dataProvider.saveEvents(event).then(() => {
      this.generalProvider.presentToast(`Successfully triggered alarm.`, "bottom");
      this.notificationProvider.sendNotification({
        id: eventdate.toString(),
        uid: event.id,
        title: title,
        description: description,
        topic: event.id,
        creationdate: eventdate,
        type: "user",
      });
      this.getEvents();
      this.alert.dismiss();
    }).catch((err) => {
      this.generalProvider.presentToast(`An error occurred when trying to trigger this alarm ${err.toString()}`, "bottom");
      this.alert.dismiss();
    });
  }

  /*Delete Event */
  deleteEvent(event, index) {
    this.dataProvider.deleteEvents(event).then(() => {
      this.generalProvider.presentToast(`Deleted Event Successfully`, "bottom");
      // this.events.splice(index, 1);
    }).catch((err) => {
      this.generalProvider.presentToast(`An error occurred when trying to delete this event ${err.toString()}`, "bottom")
    });
  }

  getEvents() {
    this.filtered = false;
    this.dataProvider.getEvents(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_events = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        snapshotValue[index]["home"] = false;
        snapshotValue[index]["expired"] = this.generalProvider.checkTrigger(snapshotValue[index]);

        list_events.push(snapshotValue[index]);

      }
      this.generalProvider.sort("player1fname", "asc", list_events).then((data) => {
        if (data) {
          list_events = data as [any];
        }
      });
      this.events = list_events as [EventModel];
      this.setCounts();

      this.ionViewDidLeave()
    });
  }


  /* Detect user input on search bar and filter templates based on all fields */
  onSearchInput(event) {
    // set query to the value of the searchbar
    let query = event.srcElement.value;
    // if the value is an empty string don't filter the items
    if (!query) {
      this.getEvents();
      return;
    }
    //filter the template by name, author and helptext
    this.events = this.events.filter((event) => {
      let event_fname1 = event.player1fname;
      let event_fname2 = event.player2fname;
      let event_lname1 = event.player1lname;
      let event_lname2 = event.player2lname;
      let event_title = event.title;
      if (event_fname1 && query) {
        if (event_fname1.toLowerCase().indexOf(query.toLowerCase()) > -1) {
          return true;
        }
        else if (event_fname2 && query) {
          if (event_fname2.toLowerCase().indexOf(query.toLowerCase()) > -1) {
            return true;
          }
          else if (event_lname2 && query) {
            if (event_lname2.toLowerCase().indexOf(query.toLowerCase()) > -1) {
              return true;
            }
            else if (event_lname1 && query) {
              if (event_lname1.toLowerCase().indexOf(query.toLowerCase()) > -1) {
                return true;
              }
              else if (event_title && query) {
                if (event_title.toLowerCase().indexOf(query.toLowerCase()) > -1) {
                  return true;
                }
                return false;
              }
              return false;
            }
            return false;
          }
        }
      }
    }) as [EventModel];
  }

  sortEvent(events, value) {
    return new Promise((resolve) => {
      if (value === "all") {
        this.generalProvider.sort("player1fname", "asc", events).then((data) => {
          events = data;
        });
      } else {
        this.generalProvider.sort(value, "asc", events).then((data) => {
          events = data;
        });
      }
      resolve(events);
    });
  }

  filterEventByCategory(events, value) {
    return new Promise((resolve) => {
      //filter the event list by category
      if (value) {
        events = events.filter((event) => {
          let found = false;
          event.categories.forEach((category) => {
            if (category.title.toLowerCase().indexOf(value.toLowerCase()) > -1) {
              found = true;
            }
          });
          if (found) {
            return true;
          }
        });
      }
      resolve(events)
    });
  }
}

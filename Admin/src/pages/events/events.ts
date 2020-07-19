import {Component} from '@angular/core';
import {AlertController, LoadingController, ModalController, NavController} from 'ionic-angular';
import {AddEventsPage} from '../add-events/add-events';
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {GeneralProvider} from "../../providers/general/general";
import {EventModel} from "../../models/events";
import {DatabaseProvider} from "../../providers/firebase/database";
import {NotificationProvider} from "../../providers/firebase/notification";
import {FilterSortPage} from "../filter-sort/filter-sort";

@Component({
  selector: 'page-events',
  templateUrl: 'events.html'
})
export class EventsPage {

  limit: number = 50;
  count: number = 0;
  searchInput: string = "";
  filtered: boolean = false;
  events: [EventModel];
  loading: any;
  alert: any;

  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public general: GeneralProvider, public dataProvider: DatabaseProvider, public notificationProvider: NotificationProvider, public modalCtrl: ModalController, public loadingCtrl: LoadingController, public alertCtrl: AlertController) {
    this.getterSetter.header = "Events";
    this.loading = this.loadingCtrl.create();

    this.loading.present();
    this.ionViewWillAppear();
  }


  ionViewWillAppear() {
    this.dataProvider.getEventsCounts().then((count) => {
      this.count = count ? count as number : 0;

      this.getEvents();
    });
  }

  ionViewDidLeave() {
    this.loading.dismiss().catch((err) => {
    });
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

  goToEvent(data) {
    let eventModal = this.modalCtrl.create(AddEventsPage, data);
    eventModal.onDidDismiss(data => {
      if (data) {
        let description = data.type === "add" ? `Fight between ${data.player1fname} and ${data.player2fname} added.` : `Fight between ${data.player1fname} and ${data.player2fname} has been updated.`;
        description = description.trim();
        let title = data.type === "add" ? "New fight added." : "Updated fight.";
        let timeid = new Date().getTime();
        if (data.type === "add") {
          // this.events.push(data);
          this.notificationProvider.sendNotification({
            id: timeid.toString(),
            title: "New Fight Added",
            creationdate: timeid,
            type: "user",
            topic: "users",
            description: description
          });

          this.general.presentToast("Successfully saved your event.", "bottom");
        } else if (data.type === "update") {
          this.notificationProvider.sendNotification({
            id: timeid.toString(),
            description: description,
            title: title,
            topic: title,
            creationdate: timeid,
            type: "user"
          });

          this.general.presentToast("Successfully updated your event.", "bottom");
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
    }
    this.goToEvent({modal_name: "Update Event", type: "update", event: event})
  }

  presentPrompt(event) {
    let title = "Fight Started";
    let description = `${event.player1fname.trim()} vs ${event.player2fname.trim()}`;
    this.alert = this.alertCtrl.create({
      title: 'Enter your Notification message: ',
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
    this.general.presentToast(`Cancelled alarm trigger.`, "bottom");
  }

  /* trigger alarm and set event date */
  triggerAlarm(event, description, title) {
    let eventdate = new Date().getTime();
    event.now_showing = true;
    this.dataProvider.saveEvents(event).then(() => {
      this.general.presentToast(`Successfully triggered alarm.`, "bottom");
      this.notificationProvider.sendNotification({
        id: event.id,
        title: title,
        description: description,
        topic: event.id,
        creationdate: eventdate,
        type: "user",
      });
      this.getEvents();
      this.alert.dismiss();
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to trigger this alarm ${err.toString()}`, "bottom");
      this.alert.dismiss();
    });
  }

  /*Delete Event */
  deleteEvent(event, index) {
    this.dataProvider.deleteEvents(event).then(() => {
      this.general.presentToast(`Deleted Event Successfully`, "bottom");
      // this.events.splice(index, 1);
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to delete this event ${err.toString()}`, "bottom")
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
        snapshotValue[index]["expired"] = this.general.checkTrigger(snapshotValue[index]);

        list_events.push(snapshotValue[index]);

      }
      this.general.sort("player1fname", "asc", list_events).then((data) => {
        if (data) {
          list_events = data as [any];
        }
      });
      this.events = list_events as [EventModel];
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
        } else if (event_fname2 && query) {
          if (event_fname2.toLowerCase().indexOf(query.toLowerCase()) > -1) {
            return true;
          } else if (event_lname2 && query) {
            if (event_lname2.toLowerCase().indexOf(query.toLowerCase()) > -1) {
              return true;
            } else if (event_lname1 && query) {
              if (event_lname1.toLowerCase().indexOf(query.toLowerCase()) > -1) {
                return true;
              } else if (event_title && query) {
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

  /* stop event timer */
  stopTimer(event) {
    event.now_showing = false;
    this.dataProvider.saveEvents(event).then(() => {
      this.general.presentToast(`Successfully stopped timer.`, "bottom");
      this.getEvents();
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to stop this timer ${err.toString()}`, "bottom");
    });
  }

  sortEvent(events, value) {
    return new Promise((resolve) => {
      if (value === "all") {
        this.general.sort("player1fname", "asc", events).then((data) => {
          events = data;
        });
      } else {
        this.general.sort(value, "asc", events).then((data) => {
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

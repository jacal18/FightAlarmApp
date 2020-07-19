import {Component} from '@angular/core';
import {ModalController, NavController, NavParams, ViewController} from 'ionic-angular';
// import {TVStationModel} from "../../models/tvstations";
import {EventModel} from "../../models/events";
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";
import {FormBuilder, Validators} from "@angular/forms";
import {CategoryModel} from "../../models/categories";
// import {AddTVStationPage} from "../add-tvstation/add-tvstation";
import {AddCategoryPage} from "../add-category/add-category";
import {DatePicker} from '@ionic-native/date-picker';
import {NotificationProvider} from "../../providers/firebase/notification";
import {CalendarModal, CalendarModalOptions, CalendarResult} from "ion2-calendar";

@Component({
  selector: 'page-add-events',
  templateUrl: 'add-events.html'
})
export class AddEventsPage {

  modal_name: string = "";
  type: string = "";
  duplicate: boolean = false;
  event: EventModel;
  categories: [CategoryModel];
  // tvstations: [TVStationModel];
  location: string;
  dateselected: number;
  datestringselected: string;

  public eventForm;

  constructor(public navParams: NavParams, public navCtrl: NavController, public formBuilder: FormBuilder, public generalProvider: GeneralProvider, public dataProvider: DatabaseProvider, public modalCtrl: ModalController, public viewCtrl: ViewController, public datePicker: DatePicker, public notificationProvider: NotificationProvider) {

    this.modal_name = navParams.get('modal_name');
    this.type = navParams.get('type');
    this.event = navParams.get('event');
    this.duplicate = navParams.get('duplicate');
    // this.getTVStation();
    this.getCategories();


    this.eventForm = formBuilder.group({
      id: [this.type === "add" ? String(Math.floor(Date.now() / 1000)) : this.event.id],
      title: [this.type === "add" ? "" : this.event.title, Validators.compose([Validators.required])],
      player1fname: [this.type === "add" ? "" : this.event.player1fname, Validators.compose([Validators.required])],
      player1lname: [this.type === "add" ? "" : this.event.player1lname, Validators.compose([Validators.required])],
      player2fname: [this.type === "add" ? "" : this.event.player2fname, Validators.compose([Validators.required])],
      player2lname: [this.type === "add" ? "" : this.event.player2lname, Validators.compose([Validators.required])],
      now_showing: [this.type === "add" ? false : this.event.now_showing],
      subscribers: [this.type === "add" ? 0 : this.event.subscribers]
    });
    if(this.event){
      if(this.event.now_showing){
        this.eventForm.disable();
      }
    }

  }

  goToModal(data, type) {
    if (type === "category") {
      let categoryModal = this.modalCtrl.create(AddCategoryPage, data);
      categoryModal.onDidDismiss(data => {
        // if (data) {
        //   this.categories.push(data)
        // }
      });
      categoryModal.present();
    }
  }

  /* Open modal for category addition, if data is returned add new category to global category list */
  addCategory() {
    this.goToModal({modal_name: "Add Category", type: "add"}, "category")
  }


  /* Open modal for category addition, if data is returned add new category to global category list */
  // addTVStation() {
  //   this.goToModal({modal_name: "Add TV Station", type: "add"}, "tvstation")
  // }

  saveEvent() {
    let checked_categories = this.categories.filter(function (category) {
      if (category["checked"]) {
        return category;
      }
    });
    if (!this.eventForm.value.title) {
      this.generalProvider.presentToast("Please fill all the highlighted fields", "bottom")
    } else if (checked_categories.length < 1) {
      this.generalProvider.presentToast("Please select at least one category.", "bottom")
    } else {
      let event = this.eventForm.value as EventModel;
      event.categories = checked_categories as [CategoryModel];
      event.subscribed = false;
      this.dataProvider.saveEvents(event).then(() => {
        event["type"] = this.type;
        event["duplicate"] = this.duplicate;
        this.viewCtrl.dismiss(event);
      }).catch((err) => {
        this.generalProvider.presentToast(`An error occurred when trying to save this event ${err.toString()}`, "bottom")
      });
    }
  }

  getCategories() {
    this.dataProvider.getCategories(null).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_categories = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        list_categories.push(snapshotValue[index]);

      }
      this.generalProvider.sort("title", "asc", list_categories);
      this.categories = list_categories as [CategoryModel];
      if (this.type === "update") {
        this.setSelectedCategories();
      }
    });
  }


  // getTVStation() {
  //   this.dataProvider.getTVStations(null).on("value", (snapshot) => {
  //     let snapshotValue = snapshot.val();
  //     let list_tvstations = [];
  //
  //     for (let index in snapshotValue) {
  //       //Set index with index field in firebase, this will alow for easy updates and deletes.
  //       list_tvstations.push(snapshotValue[index]);
  //     }
  //     this.generalProvider.sort("title", "asc", list_tvstations);
  //     this.tvstations = list_tvstations as [TVStationModel];
  //     if (this.type === "update") {
  //       this.setSelectedTV();
  //     }
  //   });
  // }

  // setSelectedTV() {
  //   if (this.event && this.event.tvstations) {
  //     let ids = this.event.tvstations.map(tv => (tv.id));
  //     for (let tv of this.tvstations) {
  //       if (ids.indexOf(tv.id) >= 0) {
  //         tv["checked"] = true;
  //       } else {
  //         tv["checked"] = false;
  //       }
  //     }
  //   }
  // }

  setSelectedCategories() {
    let ids = this.event.categories.map(category => (category.id));
    for (let category of this.categories) {
      if (ids.indexOf(category.id) >= 0) {
        category["checked"] = true;
      } else {
        category["checked"] = false;
      }
    }
  }

  cancel() {
    this.viewCtrl.dismiss();
  }


}

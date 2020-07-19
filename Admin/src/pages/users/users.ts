import {Component} from '@angular/core';
import {LoadingController, NavController} from 'ionic-angular';
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";
import {CategoryModel} from "../../models/categories";
import {SubscriberModel} from "../../models/subscribers";

@Component({
  selector: 'page-users',
  templateUrl: 'users.html'
})
export class UsersPage {

  limit: number = 50;
  subscribers: [SubscriberModel];
  count: number = 0;
  searchInput: string = "";
  loading: any;

  constructor(public navCtrl: NavController, public dataProvider: DatabaseProvider, public general: GeneralProvider, public loadingCtrl: LoadingController) {
    this.loading = this.loadingCtrl.create();

    this.loading.present();
    this.ionViewWillAppear();
  }

  ionViewWillAppear() {
    this.dataProvider.getSubscribersCount().then((count) => {
      this.count = count ? count as number : 0;

      this.getSubscribers();
    });
  }

  ionViewDidLeave() {
    this.loading.dismiss().catch((err) => {});
  }

  // infinite scroll
  doInfinite(): Promise<any> {
    return new Promise((resolve) => {
      this.limit += 50; // or however many more you want to load
      setTimeout(() => {
        this.getSubscribers();
      }, 1000);
    })
  }

  getSubscribers() {
    this.dataProvider.getSubscribers(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_subscribers = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will allow for easy updates and deletes.
        list_subscribers.push(snapshotValue[index]);
      }
      this.general.sort("joineddate", "asc", list_subscribers).then((data) => {
        if (data) {
          list_subscribers = data as [any];
        }
      });
      this.subscribers = list_subscribers as [SubscriberModel];

      this.ionViewDidLeave();
    });
  }


  /* Detect user input on search bar and filter templates based on all fields */
  onSearchInput(event) {
    // set query to the value of the searchbar
    let query = event.srcElement.value;
    // if the value is an empty string don't filter the items
    if (!query) {
      this.getSubscribers();
      return;
    }
    //filter the category by title, description
    this.subscribers = this.subscribers.filter((subscriber) => {
      let subscriber_description = subscriber.email;
      if (subscriber_description && query) {
        if (subscriber_description.toLowerCase().indexOf(query.toLowerCase()) > -1) {
          return true;
        }
        return false;
      }
    }) as [CategoryModel];
  }

}

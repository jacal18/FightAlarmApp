import {Component} from '@angular/core';
import {LoadingController, NavController} from 'ionic-angular';
import {NotificationModel} from "../../models/notifications";
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";

@Component({
  selector: 'page-notifications',
  templateUrl: 'notifications.html'
})
export class NotificationsPage {

  limit: number = 50;
  count: number;
  notifications: [NotificationModel];
  loading: any;

  constructor(public navCtrl: NavController, public general: GeneralProvider, public dataProvider: DatabaseProvider, public loadingCtrl: LoadingController) {

    this.loading = this.loadingCtrl.create();

    this.loading.present();
    this.ionViewWillAppear()
  }

  ionViewWillAppear(){
    this.dataProvider.getNotificationsCount().then((count) => {
      this.count = count ? count as number : 0;

      this.getNotifications();
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
        this.getNotifications();
      }, 1000);
    })
  }

  getNotifications() {
    this.dataProvider.getNotifications(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_notifications = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        // if(snapshotValue[index]["type"] !== "user"){
          list_notifications.push(snapshotValue[index]);
        // }
      }
      this.general.sort("creationdate", "asc", list_notifications).then((data) =>{
        if (data){
          list_notifications = data as [any];
        }
      });
      this.notifications = list_notifications as [NotificationModel];

      this.ionViewDidLeave()
    });
  }

  /*Delete notification */
  deleteNotification(notification, index) {
    this.dataProvider.deleteNotifications(notification).then(() => {
      this.general.presentToast(`Deleted Notification Successfully`, "bottom");
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to delete this notification ${err.toString()}`, "bottom")
    });
  }

}

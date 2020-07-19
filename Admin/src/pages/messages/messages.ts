import {Component} from '@angular/core';
import {IonicPage, LoadingController, NavController} from 'ionic-angular';
import {DatabaseProvider} from "../../providers/firebase/database";
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {GeneralProvider} from "../../providers/general/general";
import {MessageModel} from "../../models/message";

/**
 * Generated class for the MessagesPage page.
 *
 * See https://ionicframework.com/docs/components/#navigation for more info on
 * Ionic pages and navigation.
 */

@IonicPage()
@Component({
  selector: 'page-messages',
  templateUrl: 'messages.html',
})
export class MessagesPage {

  limit: number = 50;
  loading: any;
  count: number = 0;

  messages: [MessageModel];

  constructor(public navCtrl: NavController, public general: GeneralProvider, public getterSetter: GetterSetterProvider, public dataProvider: DatabaseProvider, public loadingCtrl: LoadingController) {
    this.getterSetter.header = "Messages";

    this.loading = this.loadingCtrl.create();

    this.loading.present();

    this.ionViewWillAppear()
  }


  ionViewWillAppear() {
    this.dataProvider.getMessagesCounts().then((count) => {
      this.count = count ? count as number : 0;

      this.getMessages();
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
        this.getMessages();
      }, 1000);
    })
  }

  getMessages() {
    this.dataProvider.getMessages(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_messages = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        list_messages.push(snapshotValue[index]);

      }
      this.general.sort("player1fname", "asc", list_messages).then((data) => {
        if (data) {
          list_messages = data as [any];
        }
      });
      this.messages = list_messages as [MessageModel];

      this.ionViewDidLeave()
    });
  }

  /*Delete Message */
  deleteMessage(message) {
    this.dataProvider.deleteMessage(message).then(() => {
      this.general.presentToast(`Deleted Message Successfully`, "bottom");
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to delete this message ${err.toString()}`, "bottom")
    });
  }

}

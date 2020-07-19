import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import {NotificationsPage} from "../notifications/notifications";
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {DatabaseProvider} from "../../providers/firebase/database";

@Component({
  selector: 'page-header',
  templateUrl: 'header.html'
})
export class HeaderPage {

  header: string = "";
  count: number = 0;
  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public dataProvider: DatabaseProvider) {
    this.header = this.getterSetter.header;
    // this.dataProvider.getNotificationsCount().then((count) => {
    //   this.count = count ? count as number : 0;
    // });
  }

  goToNotifications(params){
    if (!params) params = {};
    this.navCtrl.push(NotificationsPage);
  }

}

import {HttpClient} from '@angular/common/http';
import {HttpHeaders} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {NotificationModel} from "../../models/notifications";
import {DatabaseProvider} from "./database";

/*
  Generated class for the NotificationProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class NotificationProvider {

  serverKey = "AAAAMlZQHX8:APA91bEHaJ1Wp_bAUd9sfNwZSbCoFRk_AA0xbAPjyPaB2oB-MrtQ1ffBePVMtMjEb88JywNNs7eVvHF6hi3Av7hfq82DoG7EWJGeLzf73ZnZ2MDraD6Lw4Bwc2h76u0Ev8rQa2qzDiVd";

  constructor(public http: HttpClient, private databaseModel: DatabaseProvider) {
  }

  //Add this function and call it where you want to send it.
  sendNotification(notification: NotificationModel) {
    // let sound = notification.title && notification.title.toLowerCase().includes("started") ? "bell.mp3" : "default";
    let title = `${notification.title}`;
    let body = {
      "data": {
        "title": title,
        "topic": `/topics/${notification.topic}`,
        "description": notification.description
      },
      "to": `/topics/${notification.topic}`,
      "priority": "high",
      "notification": {
        "body": notification.description,
        "title": title,
        "content_available": true,
        "sound": "bell.mp3"
      }
    };
    let options = new HttpHeaders().set('Content-Type', 'application/json');
    this.http.post("https://fcm.googleapis.com/fcm/send", body, {
      headers: options.set('Authorization', `key=${this.serverKey}`),
    }).subscribe((result) => {
      this.databaseModel.saveUserNotifications(notification);
    }, (error1) => {
      console.log(error1);
    });

  }

}

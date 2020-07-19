import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import firebase from 'firebase';
import {CategoryModel} from "../../models/categories";
import {EventModel} from "../../models/events";
import {TVStationModel} from "../../models/tvstations";
import {NotificationModel} from "../../models/notifications";
import {UserModel} from "../../models/users";
import {MessageModel} from "../../models/message";

/*
  Generated class for the DatabaseProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class DatabaseProvider {

  constructor(public http: HttpClient) {
  }

  /*Save new category into firebase by id*/
  saveCategories(category: CategoryModel) {
    return firebase.database().ref('/categories').child(category.id).set(category, function (err) {
      err && console.log(err);
    });
  }

  /*Save new tv station into firebase by id*/
  saveTVStation(tvstation: TVStationModel) {
    return firebase.database().ref('/tvstations').child(tvstation.id).set(tvstation, function (err) {
      err && console.log(err);
    });
  }

  /*Save new event into firebase by id*/
  saveEvents(event: EventModel) {
    return firebase.database().ref('/events').child(event.id).set(event, function (err) {
      err && console.log(err);
    });
  }

  /*Save new notification into firebase by id*/
  saveUserNotifications(notification: NotificationModel) {
    return firebase.database().ref('/notifications').child(notification.id).set(notification, function (err) {
      err && console.log(err);
    });
  }

  /*Delete new category into firebase by id*/
  deleteCategories(category: CategoryModel) {
    return firebase.database().ref("/categories").child(category.id).remove();
  }

  /*Delete new tv station into firebase by id*/
  deleteTVStation(tvstation: TVStationModel) {
    return firebase.database().ref("/tvstations").child(tvstation.id).remove();
  }

  /*Delete message into firebase by id*/
  deleteMessage(message: MessageModel) {
    return firebase.database().ref("/messages").child(message.id).remove();
  }

  /*Delete new event into firebase by id*/
  deleteEvents(event: EventModel) {
    return firebase.database().ref("/events").child(event.id).remove();
  }

  /*Delete new notifications into firebase by id*/
  deleteNotifications(notification: NotificationModel) {
    return firebase.database().ref("/notifications").child(notification.id).remove();
  }

  /*Save new user into firebase by id*/
  saveUser(user: UserModel) {
    return new Promise((resolve, reject) => {
      var userNow = firebase.auth().currentUser;
      userNow.updateProfile({
        displayName: `${user.firstName} ${user.lastName}`,
        photoURL: ""
      }).then(() => {
        if (user.password) {
          userNow.updatePassword(user.password).then(() => {
            resolve(userNow)
          }).catch((error) => {
            reject(error)
          });
        } else {
          resolve(userNow)
        }
      }).catch((error) => {
        reject(error)
      });
    })

  }

  /*Get all firebase subscribers*/
  getSubscribers(limit) {
    if (limit) {
      let categoryRef = firebase.database().ref("/subscribers").limitToLast(limit);
      return categoryRef;
    } else {
      let categoryRef = firebase.database().ref("/subscribers");
      return categoryRef;
    }
  }

  /*Get all firebase categories*/
  getCategories(limit) {
    if (limit) {
      let categoryRef = firebase.database().ref("/categories").limitToLast(limit);
      return categoryRef;
    } else {
      let categoryRef = firebase.database().ref("/categories");
      return categoryRef;
    }
  }

  /*Get all firebase tv stations*/
  getTVStations(limit) {
    if (limit) {
      let tvstationRef = firebase.database().ref("/tvstations").limitToLast(limit);
      return tvstationRef;
    } else {
      let tvstationRef = firebase.database().ref("/tvstations");
      return tvstationRef;
    }
  }

  /*Get all firebase messages*/
  getMessages(limit) {
    if (limit) {
      let messageRef = firebase.database().ref("/messages").limitToLast(limit);
      return messageRef;
    } else {
      let messageRef = firebase.database().ref("/messages");
      return messageRef;
    }
  }

  /*Get all firebase events*/
  getEvents(limit) {
    if (limit) {
      let eventRef = firebase.database().ref("/events").limitToLast(limit);
      return eventRef;
    } else {
      let eventRef = firebase.database().ref("/events");
      return eventRef;
    }
  }

  /*Get all firebase notifications*/
  getNotifications(limit) {
    if (limit) {
      let notificationRef = firebase.database().ref("/notifications").limitToLast(limit);
      return notificationRef;
    } else {
      let notificationRef = firebase.database().ref("/notifications");
      return notificationRef;
    }
  }

  /*Get the length of firebase subscribers */
  getSubscribersCount() {
    return new Promise((resolve) => {
      firebase.database().ref("/subscribers").on("value", function (snapshot) {
        resolve(snapshot.numChildren());
      });
    });
  }

  /*Get the length of all firebase categories*/
  getCategoriesCount() {
    return new Promise((resolve) => {
      firebase.database().ref("/categories").on("value", function (snapshot) {
        resolve(snapshot.numChildren())
      });
    });
  }

  /*Get the length of all firebase categories*/
  getTVStationCount() {
    return new Promise((resolve) => {
      firebase.database().ref("/tvstations").on("value", function (snapshot) {
        resolve(snapshot.numChildren())
      });
    });
  }

  /*Get the length of all firebase events*/
  getEventsCounts() {
    return new Promise((resolve) => {
      firebase.database().ref("/events").on("value", function (snapshot) {
        resolve(snapshot.numChildren());
      });
    });

  }

  /*Get the length of all firebase messages*/
  getMessagesCounts() {
    return new Promise((resolve) => {
      firebase.database().ref("/messages").on("value", function (snapshot) {
        resolve(snapshot.numChildren());
      });
    });

  }

  /*Get the length of all firebase notifications*/
  getNotificationsCount() {
    return new Promise((resolve) => {
      firebase.database().ref("/notifications").on("value", function (snapshot) {
        resolve(snapshot.numChildren());
      });
    });
  }

}

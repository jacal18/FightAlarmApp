import {Injectable} from '@angular/core';
import {UserModel} from "../../models/users";
import {CategoryModel} from "../../models/categories";
import {EventModel} from "../../models/events";
import {NotificationModel} from "../../models/notifications";
import {SubscriberModel} from "../../models/subscribers";
import {TVStationModel} from "../../models/tvstations";
import {Observable} from "rxjs/Observable";
import {ImageModel} from "../../models/image";

/*
  Generated class for the GetterSetterProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class GetterSetterProvider {

  header: string = "";
  private _tvimage: Observable<ImageModel> = new Observable((observer) => {
    observer.next(null);
    observer.complete()
  });
  private _categoryimage: Observable<ImageModel> = new Observable((observer) => {
    observer.next(null);
    observer.complete()
  });

  private _user?: UserModel;
  private _category: CategoryModel;
  private _event: EventModel;
  private _notification: NotificationModel;
  private _subscriber: SubscriberModel;
  private _tvstation: TVStationModel;


  get tvimage(): Observable<ImageModel> {
    return this._tvimage;
  }

  set tvimage(value: Observable<ImageModel>) {
    this._tvimage = value;
  }

  get categoryimage(): Observable<ImageModel> {
    return this._categoryimage;
  }

  set categoryimage(value: Observable<ImageModel>) {
    this._categoryimage = value;
  }

  get user(): UserModel {
    return this._user;
  }

  set user(value: UserModel) {
    this._user = value;
  }

  get category(): CategoryModel {
    return this._category;
  }

  set category(value: CategoryModel) {
    this._category = value;
  }

  get event(): EventModel {
    return this._event;
  }

  set event(value: EventModel) {
    this._event = value;
  }

  get notification(): NotificationModel {
    return this._notification;
  }

  set notification(value: NotificationModel) {
    this._notification = value;
  }

  get subscriber(): SubscriberModel {
    return this._subscriber;
  }

  set subscriber(value: SubscriberModel) {
    this._subscriber = value;
  }


  get tvstation(): TVStationModel {
    return this._tvstation;
  }

  set tvstation(value: TVStationModel) {
    this._tvstation = value;
  }

}

import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import firebase from 'firebase';
import {CookieService} from 'angular2-cookie/core';
import {UserModel} from "../../models/users";
import {GetterSetterProvider} from "../getter-setter/getter-setter";

/*
  Generated class for the AuthenticationProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class AuthenticationProvider {

  constructor(public http: HttpClient, public gettersetter: GetterSetterProvider, private _cookieService: CookieService) {

  }

  /**
   * [loginUser We'll take an email and password and log the user into the firebase app]
   * @param  {string} email    [User's email address]
   * @param  {string} password [User's password]
   */
  loginUser(email: string, password: string): Promise<any> {
    return firebase.auth().signInWithEmailAndPassword(email, password);
  }

  /**
   * This function doesn't take any params, it just logs the current user out of the app.
   */
  logoutUser(): Promise<any> {
    this._cookieService.remove("USDE");
    return firebase.auth().signOut();
  }

  /*Get current logged in user details from firebase*/
  getCurrentUser(): Promise<UserModel> {
    return new Promise((resolve) => {
      let current_user = firebase.auth().currentUser;

      let user_model = new UserModel();
      if (current_user && current_user.uid) {
        let current_user_object = JSON.stringify(current_user);
        user_model = <UserModel> JSON.parse(current_user_object);
        if (current_user && current_user.displayName) {
          let splitted_name = current_user.displayName.split(" ");
          user_model.firstName = splitted_name && splitted_name.length > 0 ? splitted_name[0] : "";
          user_model.lastName = splitted_name && splitted_name.length > 1 ? splitted_name[1] : "";
        }
        this.gettersetter.user = user_model;
        this.getCookie("USDE").then((result) => {
          if (result === null) {
            this.saveCookie("USDE", user_model, 7);
          }
        });
        resolve(user_model)
      }
      else {
        //Get User Details cookie and log user out if none exists
        this.getCookie("USDE").then((result) => {
          if (result === null) {
            this.logoutUser();
          } else {
            user_model = <UserModel> result;
            this.gettersetter.user = user_model;
            resolve(user_model)
          }
        });
      }
    });
  }

  saveCookie(name, data, days) {
    let date = new Date();
    date.setDate(date.getDate() + days);
    this._cookieService.put(name, btoa(JSON.stringify(data)), {expires: date});
  }

  getCookie(name) {
    return new Promise((resolve) => {
      let result = this._cookieService.get(name);
      if (result) {
        resolve(JSON.parse(atob(result)));
      }
      else {
        resolve(null)
      }
    });
  }


}

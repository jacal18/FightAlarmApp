import { Component } from '@angular/core';
import { NavController, LoadingController } from 'ionic-angular';

import {FormBuilder, Validators} from "@angular/forms";
import { EmailValidator } from '../../validators/email';

import {HomePage} from "../home/home";
import {GeneralProvider} from "../../providers/general/general";
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {AuthenticationProvider} from "../../providers/firebase/authentication";



@Component({
  selector: 'page-login',
  templateUrl: 'login.html'
})
export class LoginPage {

  public loginForm;
  loading: any;

  constructor(public navCtrl: NavController, public formBuilder: FormBuilder, public gettersetter: GetterSetterProvider, public generalProvider: GeneralProvider, public authProvider: AuthenticationProvider, public loadingCtrl: LoadingController) {
    this.loginForm = formBuilder.group({
      email: ['', Validators.compose([Validators.required, EmailValidator.isValid])],
      password: ['', Validators.compose([Validators.minLength(6), Validators.required])]
    });
  }

  /*If form is valid, login user and redirect to homepage*/
  loginUser(): void {
    if (!this.loginForm.valid) {
      this.generalProvider.presentToast("Please fill all the higlighted fields", "bottom")
    } else {
      this.authProvider.loginUser(this.loginForm.value.email, this.loginForm.value.password).then(authData => {
        this.loading.dismiss().then(() => {
          try {
            this.navCtrl.setRoot(HomePage, authData.user);
          } catch (e) {
            this.navCtrl.setRoot(HomePage);
          }
        });
      }, error => {
        this.loading.dismiss().then(() => {
          if("code" in error && error["code"] === "auth/internal-error") {
            this.generalProvider.presentAlert("An error occurred when trying to login.")
          }
          else{
            this.generalProvider.presentAlert(error.message)
          }
        });
      });

      this.loading = this.loadingCtrl.create();
      this.loading.present();
    }
  }

}

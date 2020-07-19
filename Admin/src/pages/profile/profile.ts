import { Component } from '@angular/core';
import { NavController } from 'ionic-angular';
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {UserModel} from "../../models/users";
import {LoginPage} from "../login/login";
import {AuthenticationProvider} from "../../providers/firebase/authentication";
import {FormBuilder, Validators} from "@angular/forms";
import {EmailValidator} from "../../validators/email";
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";

@Component({
  selector: 'page-profile',
  templateUrl: 'profile.html'
})
export class ProfilePage {

  user: UserModel;
  public profileForm;


  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public authProvider: AuthenticationProvider, public dataProvider: DatabaseProvider, public generalProvider: GeneralProvider, public formBuilder: FormBuilder) {
    this.getterSetter.header = "Profile";
    this.setUser();
    this.profileForm = formBuilder.group({
      email: [this.user.email, Validators.compose([Validators.required, EmailValidator.isValid])],
      firstName: [this.user.firstName ? this.user.firstName : "", Validators.compose([Validators.required])],
      lastName: [this.user.lastName ? this.user.lastName : "", Validators.compose([Validators.required])],
      password: [this.user.password, Validators.compose([Validators.minLength(6), Validators.required])],
      rpassword: [this.user.password, Validators.compose([Validators.minLength(6), Validators.required])]
    });
  }

  /* Set Logged In User */
  setUser() {
    if(!this.getterSetter.user){
      this.authProvider.getCurrentUser().then((user) => {
        if (user) {
          this.user = user;
        } else {
          this.authProvider.logoutUser()
        }
      });
    } else {
      this.user = this.getterSetter.user;
    }

  }

  updateUser() {
    if (!this.profileForm.value.email && !this.profileForm.value.firstName && !this.profileForm.value.lastName) {
      this.generalProvider.presentToast("Please fill all the highlighted fields", "bottom")
    }
    else if (this.profileForm.value.password !== this.profileForm.value.rpassword) {
      this.generalProvider.presentToast("Your passwords don't match", "bottom")
    } else {
      this.dataProvider.saveUser(this.profileForm.value as UserModel).then(() => {
        this.generalProvider.presentToast("Successfully updated your information.", "bottom")
      }).catch((err) => {
        this.generalProvider.presentToast(`An error occurred when trying to update your profile ${err.toString()}`, "bottom")
      });
    }
  }
}

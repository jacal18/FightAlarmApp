import {Component} from '@angular/core';
import {NavController, NavParams, ViewController} from 'ionic-angular';
import {CategoryModel} from "../../models/categories";
import {TVStationModel} from "../../models/tvstations";
import {FormBuilder, Validators} from "@angular/forms";
import {EmailValidator} from "../../validators/email";
import {UserModel} from "../../models/users";
import {GeneralProvider} from "../../providers/general/general";
import {DatabaseProvider} from "../../providers/firebase/database";
import {ImageModel} from "../../models/image";
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {StorageProvider} from "../../providers/firebase/storage";

@Component({
  selector: 'page-add-tvstation',
  templateUrl: 'add-tvstation.html'
})
export class AddTVStationPage {


  modal_name: string = "";
  type: string = "";
  tvstation: TVStationModel;
  image: ImageModel;
  location: string;

  public tvForm;

  constructor(public navParams: NavParams, public navCtrl: NavController, public formBuilder: FormBuilder, public generalProvider: GeneralProvider, public dataProvider: DatabaseProvider, public gettersetter: GetterSetterProvider, public storageProvider: StorageProvider, public viewCtrl: ViewController) {

    this.modal_name = navParams.get('modal_name');
    this.type = navParams.get('type');
    this.tvstation = navParams.get('tvstation');
    this.location = navParams.get('location');
    this.tvForm = formBuilder.group({
      id: [this.type === "add" ? String(Math.floor(Date.now() / 1000)) : this.tvstation.id],
      title: [this.type === "add" ? "" : this.tvstation.title, Validators.compose([Validators.required])],
      description: [this.type === "add" ? "" : this.tvstation.description],
      creationdate: [new Date().getTime()]
    });
  }

  saveTVStation() {
    if (!this.tvForm.value.title && !this.tvForm.value.description) {
      this.generalProvider.presentToast("Please fill all the highlighted fields", "bottom");
    } else {
      let tvstation = this.tvForm.value as TVStationModel;
      if(this.image && this.image.data){
        this.storageProvider.uploadImage(this.image).then((url) => {
          tvstation.imageURL = url;
          this.dataProvider.saveTVStation(tvstation).then(() => {
            let data = this.tvForm.value;
            data["type"] = this.type;
            this.generalProvider.presentToast("Successfully saved your TV Station.", "bottom");
            this.viewCtrl.dismiss(data);
          }).catch((err) => {
            this.generalProvider.presentToast(`An error occurred when trying to save this station ${err.toString()}`, "bottom")
          });
        }).catch((err) => {
          this.generalProvider.presentToast(`An error occurred when trying to save this category ${err.toString()}`, "bottom")
        });
      } else {
        this.dataProvider.saveTVStation(tvstation).then(() => {
          let data = this.tvForm.value;
          data["type"] = this.type;
          this.generalProvider.presentToast("Successfully saved your TV Station.", "bottom");
          this.viewCtrl.dismiss(data);
        }).catch((err) => {
          this.generalProvider.presentToast(`An error occurred when trying to save this station ${err.toString()}`, "bottom")
        });
      }
    }
  }

  getImage() {
    this.generalProvider.getPhoto("tvstation").then((result) => {
      this.image = result;
    }).catch((error) => {
      if(error.message){
        this.generalProvider.presentToast(`Failed: ${error.message.toString()}`, "bottom")
      } else {
        this.generalProvider.presentToast(`Failed: An error occured while trying to get image.`, "bottom")
      }
    });
  }

  cancel(){
    this.viewCtrl.dismiss();
  }

}

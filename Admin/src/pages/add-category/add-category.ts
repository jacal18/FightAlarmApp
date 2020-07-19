import {Component} from '@angular/core';
import {NavController, NavParams, ViewController} from 'ionic-angular';
import {CategoryModel} from "../../models/categories";

import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";
import {FormBuilder, Validators} from "@angular/forms";
import {StorageProvider} from "../../providers/firebase/storage";
import {ImageModel} from "../../models/image";
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {ImageSetup} from "../../interfaces/imagesetup";

@Component({
  selector: 'page-add-category',
  templateUrl: 'add-category.html'
})
export class AddCategoryPage {

  modal_name: string = "";
  type: string = "";
  category: CategoryModel;
  image: ImageModel;
  location: string;

  public categoryForm;

  constructor(public navParams: NavParams, public navCtrl: NavController, public formBuilder: FormBuilder, public generalProvider: GeneralProvider, public dataProvider: DatabaseProvider, public storageProvider: StorageProvider, public gettersetter: GetterSetterProvider, public viewCtrl: ViewController) {

    this.modal_name = navParams.get('modal_name');
    this.type = navParams.get('type');
    this.category = navParams.get('category');
    this.location = navParams.get('location');

    this.categoryForm = formBuilder.group({
      id: [this.type === "add" ? String(Math.floor(Date.now() / 1000)) : this.category.id],
      title: [this.type === "add" ? "" : this.category.title, Validators.compose([Validators.required])],
      description: [this.type === "add" ? "" : this.category.description],
      creationdate: [new Date().getTime()]
    });
  }

  saveCategory() {
    if (!this.categoryForm.value.title && !this.categoryForm.value.description) {
      this.generalProvider.presentToast("Please fill all the highlighted fields", "bottom")
    } else {
      let category = this.categoryForm.value as CategoryModel;
      this.dataProvider.saveCategories(category).then(() => {
        let data = this.categoryForm.value;
        data["type"] = this.type;
        this.generalProvider.presentToast("Successfully saved your Category.", "bottom")
        this.viewCtrl.dismiss(data);
      }).catch((err) => {
        this.generalProvider.presentToast(`An error occurred when trying to save this category ${err.toString()}`, "bottom")
      });
    }
  }

  getImage() {
    this.generalProvider.getPhoto("category").then((result) => {
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

import { Component } from '@angular/core';
import {ModalController, NavController, ViewController} from 'ionic-angular';
import {CategoryModel} from "../../models/categories";
import {DatabaseProvider} from "../../providers/firebase/database";
import {CalendarModal, CalendarModalOptions, CalendarResult} from "ion2-calendar";

@Component({
  selector: 'page-filter-sort',
  templateUrl: 'filter-sort.html'
})
export class FilterSortPage {

  categories: [CategoryModel];
  sortvalue: string = "all";
  categoryvalue: string = "";

  constructor(public navCtrl: NavController, public viewCtrl: ViewController, public dataProvider: DatabaseProvider, public modalCtrl: ModalController) {
    this.getCategories();
  }

  dismissModal(){
    this.viewCtrl.dismiss();
  }

  submitModal(){
    let data = {selectedcategory: this.categoryvalue.toLowerCase().trim(), sortby: this.sortvalue };
    this.viewCtrl.dismiss(data);
  }

  getCategories(){
    this.dataProvider.getCategories(null).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_categories = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        list_categories.push(snapshotValue[index]);

      }
      this.categories = list_categories as [CategoryModel];
      this.categoryvalue = this.categories.length > 0 ? this.categories[0].title : "";
    });
  }

}

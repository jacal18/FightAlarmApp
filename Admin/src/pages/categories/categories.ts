import { Component } from '@angular/core';
import {LoadingController, ModalController, NavController} from 'ionic-angular';
import { AddCategoryPage } from '../add-category/add-category';
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {CategoryModel} from "../../models/categories";
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";

@Component({
  selector: 'page-categories',
  templateUrl: 'categories.html'
})
export class CategoriesPage {

  limit: number = 50;
  count: number = 50;
  categories: [CategoryModel];
  searchInput: string = "";
  loading: any;

  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public modalCtrl: ModalController, public dataProvider: DatabaseProvider, public general: GeneralProvider, public loadingCtrl: LoadingController) {
    this.getterSetter.header = "Categories";
    this.loading = this.loadingCtrl.create();

    this.loading.present();
    this.ionViewWillAppear()
  }

  ionViewWillAppear(){
    this.dataProvider.getCategoriesCount().then((count) => {
      this.count = count ? count as number : 0;

      this.getCategories();
    });
  }

  ionViewDidLeave() {
    this.loading.dismiss().catch((err) => {});
  }


  goToCategory(data){
    let categoryModal = this.modalCtrl.create(AddCategoryPage, data);
    categoryModal.onDidDismiss(data => {
      if (data) {
        // if(data.type === "add"){
        //   let length = this.categories.length + 1;
        //   data["indexInParent"] = length - 1 > 0 ? length - 1 : 0;
        //   this.categories.push(data)
        // } else if(data.type === "update"){
        //   this.categories[data["indexInParent"]] = data;
        // }
      }
    });
    categoryModal.present();
  }

  /* Open modal for category addition, if data is returned add new category to global category list */
  addCategory(){
    this.goToCategory({modal_name: "Add Category", type: "add"})
  }

  /* Open modal for category update, if data is returned add new category to global category list */
  updateCategory(category) {
    this.goToCategory({modal_name: "Update Category", type: "update", category: category})
  }

  // infinite scroll
  doInfinite(): Promise<any> {
    return new Promise((resolve) => {
      this.limit += 50; // or however many more you want to load
      setTimeout(() => {
        this.getCategories();
      }, 1000);
    })
  }

  getCategories(){
    this.dataProvider.getCategories(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_categories = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        list_categories.push(snapshotValue[index]);

      }
      this.general.sort("title", "asc", list_categories).then((data) =>{
        if (data){
          list_categories = data as [any];
        }
      });
      this.categories = list_categories as [CategoryModel];

      this.ionViewDidLeave()
    });
  }

  /*Delete category */
  deleteCategory(category, index) {
    this.dataProvider.deleteCategories(category).then(() => {
      this.general.presentToast(`Deleted Category Successfully`, "bottom");
      // this.categories.splice(index, 1);
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to delete this category ${err.toString()}`, "bottom")
    });
  }




  /* Detect user input on search bar and filter templates based on all fields */
  onSearchInput(event) {
    // set query to the value of the searchbar
    let query = event.srcElement.value;
    // if the value is an empty string don't filter the items
    if (!query) {
      this.getCategories();
      return;
    }
    //filter the category by title, description
    this.categories = this.categories.filter((category) => {
      let category_description = category.description;
      let category_title = category.title;
      if (category_title && query) {
        if (category_title.toLowerCase().indexOf(query.toLowerCase()) > -1) {
          return true;
        }
        else if (category_description && query) {
          if (category_description.toLowerCase().indexOf(query.toLowerCase()) > -1) {
            return true;
          }
          return false;
        }
      }
    }) as [CategoryModel];
  }

}

import { Component } from '@angular/core';
import {LoadingController, ModalController, NavController} from 'ionic-angular';
import { AddTVStationPage } from '../add-tvstation/add-tvstation';
import {GetterSetterProvider} from "../../providers/getter-setter/getter-setter";
import {TVStationModel} from "../../models/tvstations";
import {DatabaseProvider} from "../../providers/firebase/database";
import {GeneralProvider} from "../../providers/general/general";

@Component({
  selector: 'page-t-vstations',
  templateUrl: 't-vstations.html'
})
export class TVStationsPage {

  limit: number = 50;
  count: number = 50;
  tvstations: [TVStationModel];
  searchInput: string = "";
  loading: any;

  constructor(public navCtrl: NavController, public getterSetter: GetterSetterProvider, public modalCtrl: ModalController, public general: GeneralProvider, public dataProvider: DatabaseProvider, public loadingCtrl: LoadingController) {
    this.getterSetter.header = "TV Stations";

    this.loading = this.loadingCtrl.create();

    this.loading.present();

    this.ionViewWillAppear();
  }

  ionViewWillAppear(){
    this.dataProvider.getTVStationCount().then((count) => {
      this.count = count ? count as number : 0;

      this.getTVStation();
    });
  }

  ionViewDidLeave() {
    this.loading.dismiss().catch((err) => {});
  }

  // infinite scroll
  doInfinite(): Promise<any> {
    return new Promise((resolve) => {
      this.limit += 50; // or however many more you want to load
      setTimeout(() => {
        this.getTVStation();
      }, 1000);
    })
  }


  goToTVStation(data){
    let tvstationModal = this.modalCtrl.create(AddTVStationPage, data);
    tvstationModal.onDidDismiss(data => {
      if (data) {
        // if(data.type === "add"){
        //   let length = this.tvstations.length + 1;
        //   data["indexInParent"] = length - 1 > 0 ? length - 1 : 0;
        //   this.tvstations.push(data)
        // } else if(data.type === "update"){
        //   this.tvstations[data["indexInParent"]] = data;
        // }
      }
    });
    tvstationModal.present();
  }

  /* Open modal for category addition, if data is returned add new category to global category list */
  addTVStation(){
    this.goToTVStation({modal_name: "Add TV Station", type: "add"})
  }

  /* Open modal for category update, if data is returned add new category to global category list */
  updateTVStation(tvstation) {
    this.goToTVStation({modal_name: "Update TV Station", type: "update", tvstation: tvstation})
  }

  getTVStation() {
    this.dataProvider.getTVStations(this.limit).on("value", (snapshot) => {
      let snapshotValue = snapshot.val();
      let list_tvstations = [];

      for (let index in snapshotValue) {
        //Set index with index field in firebase, this will alow for easy updates and deletes.
        list_tvstations.push(snapshotValue[index]);
      }
      this.general.sort("title", "asc", list_tvstations).then((data) =>{
        if (data){
          list_tvstations = data as [any];
        }
      });
      this.tvstations = list_tvstations as [TVStationModel];

      this.ionViewDidLeave()
    });
  }

  /*Delete TV Station */
  deleteTVstation(tvstation, index) {
    this.dataProvider.deleteTVStation(tvstation).then(() => {
      this.general.presentToast(`Deleted TV Station Successfully`, "bottom");
      // this.tvstations.splice(index, 1);
    }).catch((err) => {
      this.general.presentToast(`An error occurred when trying to delete this tv station ${err.toString()}`, "bottom")
    });
  }


  /* Detect user input on search bar and filter templates based on all fields */
  onSearchInput(event) {
    // set query to the value of the searchbar
    let query = event.srcElement.value;
    // if the value is an empty string don't filter the items
    if (!query) {
      this.getTVStation();
      return;
    }
    //filter the tvstation by title, description
    this.tvstations = this.tvstations.filter((tvstation) => {
      let tvstation_description = tvstation.description;
      let tvstation_title = tvstation.title;
      if (tvstation_title && query) {
        if (tvstation_title.toLowerCase().indexOf(query.toLowerCase()) > -1) {
          return true;
        }
        else if (tvstation_description && query) {
          if (tvstation_description.toLowerCase().indexOf(query.toLowerCase()) > -1) {
            return true;
          }
          return false;
        }
      }
    }) as [TVStationModel];
  }
}

import {Injectable} from '@angular/core';
import {
  ModalController,
  ToastController,
  AlertController,
  ActionSheetController,
  LoadingController
} from "ionic-angular";
import {FilterSortPage} from "../../pages/filter-sort/filter-sort";


import {Camera, CameraOptions} from "@ionic-native/camera";
import {File} from '@ionic-native/file';
import {GetterSetterProvider} from "../getter-setter/getter-setter";
import {Observable} from "rxjs/Observable";
import {P} from "@angular/core/src/render3";

/*
  Generated class for the GeneralProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class GeneralProvider {


  constructor(public modalCtrl: ModalController, public toastCtrl: ToastController, public alertCtrl: AlertController, private camera: Camera, public file: File, public actionSheetCtrl: ActionSheetController, public loadingCtrl: LoadingController, public gettersetter: GetterSetterProvider) {
  }

  presentFilter() {
      let filterModal = this.modalCtrl.create(FilterSortPage);
      filterModal.present();
  }

  /*Set up global toast function in class to be available to all functions*/
  presentToast(message, position) {
    let toast = this.toastCtrl.create({
      message: message,
      duration: 3000,
      position: position
    });
    toast.present();
  }

  presentAlert(message) {
    let alert = this.alertCtrl.create({
      message: message,
      buttons: [
        {
          text: "Ok",
          role: 'cancel'
        }
      ]
    });
    alert.present();
  }


  getImage(type) {
    return new Promise((resolve, reject) => {
      const options: CameraOptions = {
        quality: 100,
        destinationType: this.camera.DestinationType.DATA_URL,
        encodingType: this.camera.EncodingType.JPEG,
        sourceType: this.camera.PictureSourceType.CAMERA,
        correctOrientation: true,
        targetHeight: 200,
        targetWidth: 700,
      };

      this.camera.getPicture(options).then((image_data) => {
        // imageData is either a base64 encoded string or a file URI
        // If it's base64:
        let base64Image = 'data:image/jpeg;base64,' + image_data;
        if (type === "category") {
          resolve({extension: "jpg", data: base64Image});
        } else if (type === "tvstation") {
          resolve({extension: "jpg", data: base64Image});
        }
      }, (error) => {
        // Handle error
        reject(error)
      });
    });
  }

  getLibrary(type) {
    return new Promise((resolve, reject) => {
      const options: CameraOptions = {
        quality: 100,
        destinationType: this.camera.DestinationType.FILE_URI,
        mediaType: this.camera.MediaType.PICTURE,
        sourceType: this.camera.PictureSourceType.PHOTOLIBRARY
      };

      this.camera.getPicture(options).then((image_url) => {
        // imageData is either a base64 encoded string or a file URI
        // If it's base64:
        this.file.resolveLocalFilesystemUrl(image_url).then((file_detail: any) => {
          let name_output = file_detail.name;
          let extension = name_output.substring(name_output.lastIndexOf(".") + 1);
          file_detail.file((file) => {
            let reader = new FileReader();

            reader.onloadend = (encoded_file: any) => {
              let output = encoded_file.target.result;
              console.log("Reading file");
              resolve({extension: extension, data: output});
            };
            reader.onerror = (error: any) => {
              reject(error)
            };

            reader.readAsDataURL(file);
          }, (error) => {
            reject(error)
          });
        }, (error) => {
          reject(error)
        });
      }, (error) => {
        // Handle error
        reject(error)
      });
    });
  }

  getPhoto(type) {
    return new Promise((resolve, reject) => {
      let actionSheet = this.actionSheetCtrl.create({
        title: 'Select Image Source',
        buttons: [
          {
            text: 'Camera',
            handler: () => {
              this.getImage(type).then((result) => resolve(result)).catch((error) => reject(error));
            }
          },
          {
            text: 'Photo Library',
            handler: () => {
              this.getLibrary(type).then((result) => resolve(result)).catch((error) => reject(error));
            }
          },
          {
            text: 'Cancel',
            role: 'cancel'
          }
        ]
      });

      actionSheet.present();
    });
  }


  /* Sort list of data based on the type passed */
  sort(value, order, data_list) {
    return new Promise((resolve) => {
      if (order === "asc") {
        if (value.indexOf("date") != -1) {
          data_list.sort(function (data1, data2) {
            let data1_date = new Date(data1[value]).getTime();
            let data2_date = new Date(data2[value]).getTime();
            if (data1_date > data2_date) {
              return -1;
            } else if (data1_date < data2_date) {
              return 1;
            } else {
              return 0;
            }
          });
        }
        else {
          data_list.sort(function (data1, data2) {
            if (data1[value] > data2[value]) {
              return -1;
            } else if (data1[value] < data2[value]) {
              return 1;
            } else {
              return 0;
            }
          });
        }
      }
      else {
        data_list.sort(function (data1, data2) {
          if (data1[value] < data2[value]) {
            return -1;
          } else if (data1[value] > data2[value]) {
            return 1;
          } else {
            return 0;
          }
        });
      }
      resolve(data_list)
    });
  }

  getCountdown(date, type) {
    // Get todays date and time
    let now = new Date().getTime();
    let distance = 0;
    // Find the distance between now and the count down date
    if (type === "countdown") {
      distance = date.getTime() - now;
    } else {
      distance = now - date.getTime();
    }

    // Time calculations for days, hours, minutes and seconds
    let days = Math.floor(distance / (1000 * 60 * 60 * 24));
    let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
    let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
    let seconds = Math.floor((distance % (1000 * 60)) / 1000);
    return {distance: distance, days: days, hours: hours, minutes: minutes, seconds: seconds};
  };



  checkTrigger(cevent) {
    let now = new Date().getTime();
    let distance = new Date(cevent.eventdate).getTime() - now;

    // Time calculations for days, hours, minutes and seconds
    let days = Math.floor(distance / (1000 * 60 * 60 * 24));
    if (days < 0) {
      return true;
    } else {
      return false;
    }
  }

}

import {Injectable} from '@angular/core';
import firebase from 'firebase';
import {ImageModel} from "../../models/image";

/*
  Generated class for the DatabaseProvider provider.

  See https://angular.io/guide/dependency-injection for more info on providers
  and Angular DI.
*/
@Injectable()
export class StorageProvider {

  constructor() {
    console.log('Hello Storage Provider');
  }


  makeid() {
    var mixed_id = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < 10; i++)
      mixed_id += possible.charAt(Math.floor(Math.random() * possible.length));

    return mixed_id;
  }

  /*Save new image into firebase by id*/
  saveImage(image_extension: string, image: any) {
    let image_id = this.makeid();
    return firebase.storage().ref(image_id + '.' + image_extension).putString(image, firebase.storage.StringFormat.DATA_URL)
  }

  /*Delete an image into firebase by id*/
  deleteImage(image_url: string) {
    return new Promise((resolve, reject) => {
      // Create a reference to the file to delete
      let ref = firebase.storage().refFromURL(image_url);
      // Delete the file
      ref.delete().then(function () {
        // File deleted successfully
        resolve({deleted: true});
      }).catch(function (error) {
        // Uh-oh, an error occurred!
        reject(error)
      });
    });
  }

  uploadImage(image: ImageModel) {
    return new Promise((resolve, reject) => {
      this.saveImage(image.extension, image.data).then((result) => {
        result.ref.getDownloadURL().then((downloadURL) => {
          resolve(downloadURL);
        }).catch((error) => {
          reject(error.message);
        });
      }).catch((error) => {
        reject(error.message);
      });
    });
  }


}

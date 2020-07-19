import {Component, ViewChild} from '@angular/core';
import {Platform, Nav} from 'ionic-angular';
import {StatusBar} from '@ionic-native/status-bar';
import {FCM} from '@ionic-native/fcm';
import {SplashScreen} from '@ionic-native/splash-screen';

import {ProfilePage} from '../pages/profile/profile';
import {UsersPage} from '../pages/users/users';
import {LoginPage} from '../pages/login/login';
import {TVStationsPage} from '../pages/t-vstations/t-vstations';
import {AddTVStationPage} from '../pages/add-tvstation/add-tvstation';
import {CategoriesPage} from '../pages/categories/categories';
import {AddCategoryPage} from '../pages/add-category/add-category';
import {EventsPage} from '../pages/events/events';
import {AddEventsPage} from '../pages/add-events/add-events';
import {Network} from '@ionic-native/network';

import {HomePage} from '../pages/home/home';
import {NotificationsPage} from "../pages/notifications/notifications";

/*Providers import*/
import firebase from 'firebase';
import {AuthenticationProvider} from "../providers/firebase/authentication";
import {GeneralProvider} from "../providers/general/general";
import {MessagesPage} from "../pages/messages/messages";


@Component({
  templateUrl: 'app.html'
})
export class MyApp {
  @ViewChild(Nav) navCtrl: Nav;
  rootPage: any = HomePage;


  constructor(platform: Platform, statusBar: StatusBar, splashScreen: SplashScreen, private fcm: FCM, private authProvider: AuthenticationProvider, private generalProvider: GeneralProvider, private network: Network) {
    this.initializeApp();

    this.checkUser();

    platform.ready().then(() => {
      // Okay, so the platform is ready and our plugins are available.
      // Here you can do any higher level native things you might need.
      //Notifications
      // fcm.subscribeToTopic('all');
      fcm.subscribeToTopic('admin');
      fcm.getToken().then(token => {
      }).catch((err) => {
        console.log(err)
      });
      fcm.onNotification().subscribe(data => {
        if (data.wasTapped) {
          console.log("Received in background");
          console.log(data);
          this.navCtrl.setRoot(NotificationsPage);
        } else {
          console.log("Received in foreground");
        }
      });
      fcm.onTokenRefresh().subscribe(token => {
        console.log(token);
      });
      //send notifications.


      statusBar.styleDefault();
      statusBar.show();
      statusBar.overlaysWebView(false);

      splashScreen.hide();
    }).catch((err) => {
      console.log(err)
    });
  }

  // watch network for a disconnection
  disconnectSubscription = this.network.onDisconnect().subscribe(() => {
    console.log('network was disconnected :-(');
    this.generalProvider.presentToast("Network is not disconnected.", "top")
  });


  // watch network for a connection
  connectSubscription = this.network.onConnect().subscribe(() => {
    console.log('network connected!');
    // this.generalProvider.presentToast("Network connected.", "top")
  });

  goToProfile(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(ProfilePage);
  }

  goToHome(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(HomePage);
  }

  goToUsers(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(UsersPage);
  }

  goToMessages(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(MessagesPage);
  }

  goToLogin(params) {
    // if (!params) params = {};
    this.authProvider.logoutUser().then(() => {
    }).catch(err => {
    });
  }

  goToTVStations(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(TVStationsPage);
  }

  goToAddTVStation(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(AddTVStationPage);
  }

  goToCategories(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(CategoriesPage);
  }

  goToAddCategory(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(AddCategoryPage);
  }

  goToEvents(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(EventsPage);
  }

  goToAddEvents(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(AddEventsPage);
  }

  goToNotifications(params) {
    if (!params) params = {};
    this.navCtrl.setRoot(NotificationsPage);
  }

  /*Initialize firebase for application*/
  initializeApp() {
    // Initialize Firebase
    var config = {
      apiKey: "AIzaSyBy7st4AojJE0cW7bKKJJd-eqj5E7LY9zY",
      authDomain: "fightalarm-31326.firebaseapp.com",
      databaseURL: "https://fightalarm-31326.firebaseio.com",
      projectId: "fightalarm-31326",
      storageBucket: "fightalarm-31326.appspot.com",
      messagingSenderId: "216196455807"
    };
    firebase.initializeApp(config);

  }

  /*Check if user is signed in using firebase*/
  checkUser() {
    firebase.auth().onAuthStateChanged((user) => {
      if (!user) {
        // User is not signed in.
        this.rootPage = LoginPage;
      }
    });
  }


}

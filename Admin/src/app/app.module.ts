import { NgModule, ErrorHandler } from '@angular/core';
import {HttpClientModule} from '@angular/common/http';
import { BrowserModule } from '@angular/platform-browser';
import { IonicApp, IonicModule, IonicErrorHandler } from 'ionic-angular';
import { MyApp } from './app.component';
import { HomePage } from '../pages/home/home';
import { NotificationsPage } from '../pages/notifications/notifications';
import { UsersPage } from '../pages/users/users';
import { ProfilePage } from '../pages/profile/profile';
import { LoginPage } from '../pages/login/login';
import { AddCategoryPage } from '../pages/add-category/add-category';
import { AddTVStationPage } from '../pages/add-tvstation/add-tvstation';
import { AddEventsPage } from '../pages/add-events/add-events';
import { EventsPage } from '../pages/events/events';
import { CategoriesPage } from '../pages/categories/categories';
import { TVStationsPage } from '../pages/t-vstations/t-vstations';
import { FilterSortPage } from '../pages/filter-sort/filter-sort';


import { StatusBar } from '@ionic-native/status-bar';
import { SplashScreen } from '@ionic-native/splash-screen';
import { FCM } from '@ionic-native/fcm';
import {HeaderPage} from "../pages/header/header";
import { GetterSetterProvider } from '../providers/getter-setter/getter-setter';
import { NotificationProvider} from '../providers/firebase/notification';
import { GeneralProvider } from '../providers/general/general';
import {DatabaseProvider} from '../providers/firebase/database';
import {AuthenticationProvider} from "../providers/firebase/authentication";
import {UserModel} from "../models/users";
import {CookieService} from "angular2-cookie/core";
import {StorageProvider} from "../providers/firebase/storage";
import {Camera} from "@ionic-native/camera";
import {File} from "@ionic-native/file";
import { Network } from '@ionic-native/network';
import {ExtractDatePipe} from "../pipes/extract-date/extract-date";
import { DatePicker } from '@ionic-native/date-picker';
import { CalendarModule } from "ion2-calendar";
import {MessagesPage} from "../pages/messages/messages";

@NgModule({
  declarations: [
    MyApp,
    HeaderPage,
    HomePage,
    NotificationsPage,
    UsersPage,
    ProfilePage,
    LoginPage,
    AddCategoryPage,
    AddTVStationPage,
    AddEventsPage,
    EventsPage,
    CategoriesPage,
    TVStationsPage,
    FilterSortPage,
    MessagesPage,
    ExtractDatePipe
  ],
  imports: [
    BrowserModule,
    IonicModule.forRoot(MyApp),
    HttpClientModule,
    CalendarModule
  ],
  bootstrap: [IonicApp],
  entryComponents: [
    MyApp,
    HomePage,
    HeaderPage,
    NotificationsPage,
    UsersPage,
    ProfilePage,
    LoginPage,
    AddCategoryPage,
    AddTVStationPage,
    AddEventsPage,
    EventsPage,
    CategoriesPage,
    TVStationsPage,
    MessagesPage,
    FilterSortPage
  ],
  providers: [
    StatusBar,
    SplashScreen,
    {provide: ErrorHandler, useClass: IonicErrorHandler},
    GetterSetterProvider,
    FCM,
    DatabaseProvider,
    AuthenticationProvider,
    NotificationProvider,
    GeneralProvider,
    StorageProvider,
    UserModel,
    CookieService,
    Camera,
    File,
    Network,
    DatePicker
  ]
})
export class AppModule {}

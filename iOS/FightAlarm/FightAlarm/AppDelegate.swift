//
//  AppDelegate.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-12.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit
import Firebase
import FirebaseMessaging
import UserNotifications

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate, UNUserNotificationCenterDelegate, MessagingDelegate {

    var window: UIWindow?
    
    let gcmMessageIDKey = "216196455807"

    override init() {
        FirebaseApp.configure()
    }
    
    func showNotificationPage() {
        let storyBoard = UIStoryboard(name: "Main", bundle: nil)
        if let wd = self.window {
            if let vc = wd.rootViewController {
                if(vc is UINavigationController){
                    let nextViewController = storyBoard.instantiateViewController(withIdentifier: "NotificationsTableController") as! NotificationsTableController
                    let newvc = vc as! UINavigationController
                    
                    newvc.pushViewController(nextViewController, animated: false)
                    
                }
            }
        }
    }

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {
        // Override point for customization after application launch.
         assignMessaging(application: application)
        
        if (launchOptions?[UIApplicationLaunchOptionsKey.remoteNotification] as? NSDictionary) != nil {
            showNotificationPage()
        }
        return true
    }

    func assignMessaging(application: UIApplication) {
    
        if #available(iOS 10.0, *) {
            // For iOS 10 display notification (sent via APNS)
            UNUserNotificationCenter.current().delegate = self
            
            let authOptions: UNAuthorizationOptions = [.alert, .badge, .sound]
            UNUserNotificationCenter.current().requestAuthorization(
                options: authOptions,
                completionHandler: {_, _ in })
            Messaging.messaging().delegate = self
        } else {
            let settings: UIUserNotificationSettings =
                UIUserNotificationSettings(types: [.alert, .badge, .sound], categories: nil)
            application.registerUserNotificationSettings(settings)
        }
        
//        FirebaseApp.configure()
        application.registerForRemoteNotifications()
        
        
    }

    // Receive displayed notifications for iOS 10 devices.
     @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                willPresent notification: UNNotification,
                                withCompletionHandler completionHandler: @escaping (UNNotificationPresentationOptions) -> Void) {
        let userInfo = notification.request.content.userInfo
        
        // With swizzling disabled you must let Messaging know about the message, for Analytics
        // Messaging.messaging().appDidReceiveMessage(userInfo)
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        let state : UIApplicationState = UIApplication.shared.applicationState
        switch state {
        case UIApplicationState.active:
            print("If needed notify user about the message")
        default:
            print("Run code to download content")
        }
        // Change this to your preferred presentation option
        completionHandler([.alert, .badge, .sound])
    }
    @available(iOS 10.0, *)
    func userNotificationCenter(_ center: UNUserNotificationCenter,
                                didReceive response: UNNotificationResponse,
                                withCompletionHandler completionHandler: @escaping () -> Void) {
        let userInfo = response.notification.request.content.userInfo
        // Print message ID.
        if let messageID = userInfo[gcmMessageIDKey] {
            print("Message ID: \(messageID)")
        }
        showNotificationPage()
        completionHandler()
    }

    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        if let instanceIdToken = InstanceID.instanceID().token() {
            print("New token \(instanceIdToken)")
            Messaging.messaging().apnsToken = deviceToken
        }
    }
    
    func messaging(_ messaging: Messaging, didReceiveRegistrationToken fcmToken: String) {
        print("Firebase registration token: \(fcmToken)")
    }
    
    func messaging(_ messaging: Messaging, didReceive remoteMessage: MessagingRemoteMessage) {
        print("Received data message: \(remoteMessage.appData)")
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any], fetchCompletionHandler completionHandler: @escaping (UIBackgroundFetchResult) -> Void) {
        
        let state : UIApplicationState = application.applicationState
        switch state {
        case UIApplicationState.active:
            print("If needed notify user about the message")
        default:
            print("Run code to download content")
        }
        
        completionHandler(UIBackgroundFetchResult.newData)
    }
    
    func application(_ application: UIApplication, didFailToRegisterForRemoteNotificationsWithError error: Error) {
    print("Registration failed!")
    }
    func messaging(_ messaging: Messaging, didRefreshRegistrationToken fcmToken: String) {
        print("Token refreshed")
    }
    
    func updateBadgeCount()
    {
        var badgeCount = UIApplication.shared.applicationIconBadgeNumber
        if badgeCount > 0
        {
            badgeCount = badgeCount-1
        }
        
        UIApplication.shared.applicationIconBadgeNumber = badgeCount
    }
}


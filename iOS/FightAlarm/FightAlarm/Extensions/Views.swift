//
//  Views.swift
//  FightAlarm
//
//  Created by Presly on 2018-10-25.
//  Copyright © 2018 Presly. All rights reserved.
//

import UIKit
import Toaster
import MapKit
import FirebaseMessaging

extension UIViewController {
    @objc func displayNavBarActivity() {
        let indicator = UIActivityIndicatorView(activityIndicatorStyle: .white)
        indicator.startAnimating()
        let item = UIBarButtonItem(customView: indicator)
        
        self.navigationItem.leftBarButtonItem = item
    }
    
    @objc func dismissNavBarActivity() {
        self.navigationItem.leftBarButtonItem = nil
    }
    
    func sendNotification(title: String, body: String){
        let headers = [
            "Authorization": "key=AAAAMlZQHX8:APA91bEHaJ1Wp_bAUd9sfNwZSbCoFRk_AA0xbAPjyPaB2oB-MrtQ1ffBePVMtMjEb88JywNNs7eVvHF6hi3Av7hfq82DoG7EWJGeLzf73ZnZ2MDraD6Lw4Bwc2h76u0Ev8rQa2qzDiVd",
            "Content-Type": "application/json"
        ]
        let parameters = [
            "app_id": "4deabc18-8d55-41b4-944e-b59630e2deb0",
            "included_segments": ["All"],
            "contents": ["en": "\(body)"],
            "headings": ["en": "\(title)"]
            ] as [String : Any]
        do{
            let postData = try JSONSerialization.data(withJSONObject: parameters, options: [])
            //fcm = https://fcm.googleapis.com/fcm/send
            let request = NSMutableURLRequest(url: NSURL(string: "https://onesignal.com/api/v1/notifications")! as URL,
                                              cachePolicy: .useProtocolCachePolicy,
                                              timeoutInterval: 10.0)
            request.httpMethod = "POST"
            request.allHTTPHeaderFields = headers
            request.httpBody = postData as Data
            
            let session = URLSession.shared
            let dataTask = session.dataTask(with: request as URLRequest, completionHandler: { (data, response, error) -> Void in
                if (error != nil) {
                    print(error!)
                } else {
                    do{
                        let parsedData = try JSONSerialization.jsonObject(with: data!, options: []) as! [String:Any]
                        if parsedData["id"] != nil{
                            Toast(text: "Notification sent to users").show()
                        }
                    }
                    catch{
                        
                    }
                }
            })
            
            dataTask.resume()
        }
        catch{
            
        }
    }
    
    func filterByNowShowing(events: [EventModel]) -> [EventModel] {
        let filteredData = events.filter({(event: EventModel) -> Bool in
            return event.now_showing == false
        })
        return filteredData
    }
    
    func filterByID(events: [EventModel], id: String) -> EventModel {
        let filteredData = events.filter({(event: EventModel) -> Bool in
            return event.id == id
        })
        return filteredData[0]
    }
    
    func filterByNotSubscribed(events: [EventModel]) -> [EventModel] {
        let filteredData = events.filter({(event: EventModel) -> Bool in
            return event.subscribed == false
        })
        return filteredData
    }
    
    func filterBySubscribed(events: [EventModel]) -> [EventModel] {
        let filteredData = events.filter({(event: EventModel) -> Bool in
            return event.subscribed == true
        })
        return filteredData
    }

    func filterByCategories(filtercategory: CategoryModel, oldevents: [EventModel]) -> [EventModel]{
        var newEvents: [EventModel] = []
        for event in oldevents {
            var found = false
            for category in event.categories {
                if(category.title.lowercased() == filtercategory.title.lowercased()){
                    if(!found){
                        found = true
                        break
                    }
                }
            }
            if(found){
                newEvents.append(event)
            }
        }
        return newEvents
    }
    
    func filterBySubscriberID(notifications: [NotificationModel]) -> [NotificationModel]{
        var newNots: [NotificationModel] = []
        let notificationIDs = self.getSchedulesID()
        for notification in notifications {
            var found = false
            for notificationid in notificationIDs {
                if(notificationid == notification.uid && !notification.description.contains("added")){
                    if(!found){
                        found = true
                        break
                    }
                }
            }
            if(found){
                newNots.append(notification)
            }
        }
        return newNots
    }
    
    func searchField(searchText: String, events: [EventModel]) -> [EventModel] {
        let filteredData = searchText.isEmpty ? events : events.filter({(event: EventModel) -> Bool in
            return event.player1fname.range(of: searchText, options: .caseInsensitive) != nil || event.player2fname.range(of: searchText, options: .caseInsensitive) != nil || event.player1lname.range(of: searchText, options: .caseInsensitive) != nil || event.player2lname.range(of: searchText, options: .caseInsensitive) != nil || event.title.range(of: searchText, options: .caseInsensitive) != nil
        })
        return filteredData
    }
   
    func coordinates(forAddress address: String, completion: @escaping (CLLocationCoordinate2D?) -> Void) {
        let geocoder = CLGeocoder()
        geocoder.geocodeAddressString(address) {
            (placemarks, error) in
            guard error == nil else {
                print("Geocoding error: \(error!)")
                completion(nil)
                return
            }
            completion(placemarks?.first?.location?.coordinate)
        }
    }
    
    func getSchedules() -> [EventModel]{
        var schedules: [EventModel] = []
        guard let savedItems = UserDefaults.standard.array(forKey: LocalDBKeys.schedule) else { return []}
        for savedItem in savedItems {
            guard let event = NSKeyedUnarchiver.unarchiveObject(with: savedItem as! Data) as? EventModel else { continue }
            schedules.append(event)
        }
        return schedules
    }
    
    func saveSchedule(schedules: [EventModel]) {
        var items: [Data] = []
        for schedule in schedules{
            let item = NSKeyedArchiver.archivedData(withRootObject: schedule)
            items.append(item)
        }
        UserDefaults.standard.set(items, forKey: LocalDBKeys.schedule)
    }
    
    func updateSubscribed(events: [EventModel]) -> [EventModel] {
        var newEvent: [EventModel] = []
        
        for event in events {
            if(!event.subscribed){
                newEvent.append(event)
            }
        }
        return newEvent
    }
    
    func updateSubscriptions(events: [EventModel]) -> [EventModel] {
        var newSchedules: [EventModel] = []
        let schedules = getSchedules()
        for schedule in schedules {
            var found = false
            var newschedule: EventModel!
            for event in events {
                if(event.id == schedule.id){
                    found = true
                    newschedule = event
                    newschedule.subscribed = true
                    break
                }
            }
            if(found){
                newSchedules.append(newschedule)
            }
        }
        self.saveSchedule(schedules: newSchedules)
        return newSchedules
    }
    
    func getUpdatedSchedules(callback: @escaping(_ events: [EventModel]?) -> Void){
        var schedules = self.getSchedules()
        let databaseService: DatabaseService = DatabaseService()
        databaseService.getEvents() { (data) in
            if let eventData = data {
                schedules = self.updateSubscriptions(events: eventData)
            }
            callback(schedules)
        }
    }
    
    func removeEventFromSubscription(event: EventModel) {
        
        let userInfo = UserDefaults.standard.string(forKey: LocalDBKeys.userID);
    
        let databaseService: DatabaseService = DatabaseService()
        let schedules = self.getSchedules()
        var newEvents: [EventModel] = []
        for cevent in schedules {
            if(cevent.id != event.id){
                newEvents.append(cevent)
            }
        }
        
        self.saveSchedule(schedules: newEvents)
        self.unsubscribeFromTopic(topic: event.id)
        
        databaseService.updateEvent(event: event, type: "subscribers", increment: false)
        databaseService.updateUser(userid: String(describing: userInfo!), type: "subscriptions", increment: false)
        let timeInterval = CLong(NSDate().timeIntervalSince1970 * 1000);
        let random = "\(String(describing: timeInterval))"
        let description = "\(String(describing: userInfo!)) Unsubscribed from Event \(event.title)."
        let notification = NotificationModel(id: random, uid: random, title: "Event Subscription", description: description, creationdate: timeInterval, type: "all", topic: "admin")
        notification.description = description
        databaseService.saveNotification(notification: notification)
        
        self.alert(title: "", message: "You have successfully unsubscribed from the \(event.player1fname) vs \(event.player2fname) fight")
        
    }
    
    func addEventToSubscription(event: EventModel) {
        
        let userInfo = UserDefaults.standard.string(forKey: LocalDBKeys.userID);
        
        let databaseService: DatabaseService = DatabaseService()
        var actualSchedule = self.getSchedules()
        var found = false
        for cevent in actualSchedule {
            if(cevent.id == event.id){
                found = true
            }
        }
        
        if !(found) {
            event.subscribed = true
            actualSchedule.append(event)
        }
        self.saveSchedule(schedules: actualSchedule)
    
        self.subscribeToTopic(topic: event.id)
        
        databaseService.updateEvent(event: event, type: "subscribers", increment: true)
        databaseService.updateUser(userid: String(describing: userInfo!), type: "subscriptions", increment: true)
        let timeInterval = CLong(NSDate().timeIntervalSince1970 * 1000);
        let random = "\(String(describing: timeInterval))"
        let description = "\(String(describing: userInfo!)) Subscribed to Event \(event.title)."
        let notification = NotificationModel(id: random, uid: random, title: "Event Subscription", description: description, creationdate: timeInterval, type: "all", topic: "admin")
        notification.description = description
        databaseService.saveNotification(notification: notification)
        
    }
    
    func setSubscribedEvents(events: [EventModel]) -> [EventModel] {
        let subscriptions = getSchedules()
        var new_events:[EventModel] = []
        for event in events {
            var found = false
            for schedule in subscriptions {
                if schedule.id == event.id {
                    found = true
                }
            }
            if(found){
                event.subscribed = true
            } 
            new_events.append(event)
        }
        
        return new_events
    }
    
    
    func getSchedulesID() -> [String]{
        var schedules: [String] = []
        guard let savedItems = UserDefaults.standard.array(forKey: LocalDBKeys.scheduleID) else { return []}
        for savedItem in savedItems {
            guard let event = NSKeyedUnarchiver.unarchiveObject(with: savedItem as! Data) as? String else { continue }
            schedules.append(event)
        }
        return schedules
    }
    
    func saveScheduleID(schedules: [String]) {
        var items: [Data] = []
        for schedule in schedules {
            let item = NSKeyedArchiver.archivedData(withRootObject: schedule)
            items.append(item)
        }
        UserDefaults.standard.set(items, forKey: LocalDBKeys.scheduleID)
    }
    
    func addIDToSubscription(id: String) {
        var actualSchedule = self.getSchedulesID()
        var found = false
        for ids in actualSchedule {
            if(ids == id){
                found = true
            }
        }
        
        if !(found) {
            actualSchedule.append(id)
        }
        self.saveScheduleID(schedules: actualSchedule)
        
    }
    
    func removeIDFromSubscription(id: String) {
        let actualSchedule = self.getSchedulesID()
        var newid: [String] = []
        for ids in actualSchedule {
            if(id != ids){
                newid.append(id)
            }
        }
        self.saveScheduleID(schedules: newid)
    }
    
    func subscribeToTopic(topic: String) {
        Messaging.messaging().subscribe(toTopic: topic)
    }
    
    func unsubscribeFromTopic(topic: String) {
        Messaging.messaging().unsubscribe(fromTopic: topic)
    }
    
    func alert(title: String, message: String){
        let alert = UIAlertController(title: title, message: message, preferredStyle: UIAlertControllerStyle.alert)
        alert.addAction(UIAlertAction(title: "OK", style: UIAlertActionStyle.default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    func hideKeyboardWhenTappedAround() {
        let tap: UITapGestureRecognizer = UITapGestureRecognizer(target: self, action: #selector(dismissKeyboard(_:)))
        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }
    
    @objc func dismissKeyboard(_ sender: UITapGestureRecognizer) {
        view.endEditing(true)
        
        if let nav = self.navigationController {
            nav.view.endEditing(true)
        }
    }
}

extension UIView {
    func setView(){
        self.layer.cornerRadius = 15
        self.layer.masksToBounds = true
        self.layer.shadowColor = UIColor.black.cgColor
        self.layer.shadowOpacity = 1
        self.layer.shadowOffset = CGSize.zero
        self.layer.shadowRadius = 10
    }
}

extension UIButton {
    func setButtonColor(event: EventModel){
        if(event.now_showing) {
            self.setTitle("Currently showing", for: .normal)
            self.isEnabled = false
            self.backgroundColor = UIColor.lightGray
        } else if(event.subscribed){
            self.backgroundColor = UIColor.red
            self.setTitle("Alarm Set", for: .normal)
            self.isEnabled = true
        } else {
            self.backgroundColor = UIColor(red: 83/255, green: 119/255, blue: 137/255, alpha: 1.0)
            self.setTitle("Set Alarm", for: .normal)
            self.isEnabled = true
        }
        
    }

}

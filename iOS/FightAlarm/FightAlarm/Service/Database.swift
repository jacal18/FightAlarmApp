//
//  Event.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-20.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//


import Firebase
import FirebaseMessaging

class DatabaseService {
    
    let ref: DatabaseReference!
    private var databaseHandle: DatabaseHandle!
    private var database: Database!
    
    
    init() {
        if(database == nil){
            database = Database.database()
        }
        ref = database.reference()
    }
    
    func getEvents(callback: @escaping(_ events: [EventModel]?) -> Void){
        databaseHandle = ref.child("/events").observe(.value, with: { (snapshot) in
            var newEvents = [EventModel]()
            
            for eventSnapShot in snapshot.children {
                if let event = EventModel(snapshot: eventSnapShot as! DataSnapshot){
                    newEvents.append(event)
                }
            }
            callback(newEvents)
        })
    }
    
    func getCategories(callback: @escaping(_ events: [CategoryModel]?) -> Void){
        databaseHandle = ref.child("/categories").observe(.value, with: { (snapshot) in
            var newCategories = [CategoryModel]()
            
            for categorySnapShot in snapshot.children {
                if let category = CategoryModel(snapshot: categorySnapShot as! DataSnapshot){
                    newCategories.append(category)
                }
            }
            callback(newCategories)
        })
    }
    
    func getNotifications(callback: @escaping(_ events: [NotificationModel]?) -> Void){
        databaseHandle = ref.child("/notifications").observe(.value, with: { (snapshot) in
            var newNotifications = [NotificationModel]()
            
            for notificationSnapShot in snapshot.children {
                if let notification = NotificationModel(snapshot: notificationSnapShot as! DataSnapshot){
                    if (notification.type.lowercased() == "user") {
                        newNotifications.append(notification)
                    }
                }
            }
            callback(newNotifications)
        })
    }
    
    func updateEvent(event: EventModel, type: String, increment: Bool){
        ref.child("/events").child(event.id).child(type).runTransactionBlock { (data: MutableData) -> TransactionResult in
        
            let newValue: Int
            if let existingValue = (data.value as? NSNumber)?.intValue {
                if (increment) {
                    newValue = existingValue + 1
                } else {
                    newValue = existingValue - 1
                }
            } else {
                if (increment) {
                    newValue = 1
                } else {
                    newValue = 0
                }
                
            }
            
            data.value = NSNumber(value: newValue)
            
            return TransactionResult.success(withValue: data)
        }
    }
    
    func updateUser(userid: String, type: String, increment: Bool){
        ref.child("/subscribers").child(userid).child(type).runTransactionBlock { (data: MutableData) -> TransactionResult in
            
            let newValue: Int
            if let existingValue = (data.value as? NSNumber)?.intValue {
                if (increment) {
                    newValue = existingValue + 1
                } else {
                    newValue = existingValue - 1
                }
            } else {
                if (increment) {
                    newValue = 1
                } else {
                    newValue = 0
                }
                
            }
            
            data.value = NSNumber(value: newValue)
            
            return TransactionResult.success(withValue: data)
        }
    }
    
    func saveNotification(notification: NotificationModel) {
        ref.child("/notifications").child(notification.id).setValue(notification.convertIntoDict())
//        Messaging.messaging().sendMessage(<#T##message: [AnyHashable : Any]##[AnyHashable : Any]#>, to: <#T##String#>, withMessageID: <#T##String#>, timeToLive: <#T##Int64#>)
    }
    
    func saveSubscribers(subscriber: SubscriberModel) {
        ref.child("/subscribers").child(subscriber.id).setValue(subscriber.convertIntoDict())
    }
    
}

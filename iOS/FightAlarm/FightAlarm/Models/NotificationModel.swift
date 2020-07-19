//
//  NotificationModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit
import FirebaseDatabase

struct NotificationModelKeys {
    static let id = "id"
    static let uid = "uid"
    static let title = "title"
    static let description = "description"
    static let creationdate = "creationdate"
    static let type = "type"
    static let topic = "topic"
}

class NotificationModel: NSObject, NSCoding{
    var id: String
    var uid: String
    var title: String
    var cdescription: String
    override var description: String {
        get {
            return cdescription
        }
        set(newValue) {
            cdescription = newValue
        }
    }
    var creationdate: CLong
    var type: String
    var topic: String
    
    init?(snapshot: DataSnapshot){
        
        let object = snapshot.value as! Dictionary<String, Any>
        guard let id = object["id"] as? String else {
            return nil
        }
        guard let title = object["title"] as? String else {
            return nil
        }
        guard let description = object["description"] as? String else {
            return nil
        }
        guard let creationdate = object["creationdate"] as? CLong else {
            return nil
        }
        
        self.id = id
        self.title = title
        self.cdescription = description
        self.creationdate = creationdate
        
        self.type = object["type"] as? String ?? "user"
        self.topic = object["topic"] as? String ?? "user"
        self.uid = object["uid"] as? String ?? id
    }
    
    
    init?(object: [String: Any]){
        
        guard let id = object["id"] as? String else {
            return nil
        }
        guard let title = object["title"] as? String else {
            return nil
        }
        guard let description = object["description"] as? String else {
            return nil
        }
        guard let creationdate = object["creationdate"] as? CLong else {
            return nil
        }
        
        self.id = id
        self.title = title
        self.cdescription = description
        self.creationdate = creationdate
        
        self.type = object["type"] as? String ?? "user"
        self.topic = object["topic"] as? String ?? "user"
        self.uid = object["uid"] as? String ?? id
    }
    
    
    init(id: String,uid: String, title: String, description: String, creationdate: CLong, type: String, topic: String) {
        self.id = id
        self.uid = uid
        self.title = title
        self.cdescription = description
        self.creationdate = creationdate
        self.type = type
        self.topic = topic
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        id = decoder.decodeObject(forKey: NotificationModelKeys.id) as! String
        uid = decoder.decodeObject(forKey: NotificationModelKeys.uid) as! String
        title = decoder.decodeObject(forKey: NotificationModelKeys.title) as! String
        cdescription = decoder.decodeObject(forKey: NotificationModelKeys.description) as! String
        creationdate = decoder.decodeInteger(forKey: NotificationModelKeys.creationdate) as! CLong
        type = decoder.decodeObject(forKey: NotificationModelKeys.type) as! String
        topic = decoder.decodeObject(forKey: NotificationModelKeys.topic) as! String
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: NotificationModelKeys.id)
        coder.encode(uid, forKey: NotificationModelKeys.uid)
        coder.encode(title, forKey: NotificationModelKeys.title)
        coder.encode(cdescription, forKey: NotificationModelKeys.description)
        coder.encode(creationdate, forKey: NotificationModelKeys.creationdate)
        coder.encode(type, forKey: NotificationModelKeys.type)
        coder.encode(topic, forKey: NotificationModelKeys.topic)
    }
    
    
}


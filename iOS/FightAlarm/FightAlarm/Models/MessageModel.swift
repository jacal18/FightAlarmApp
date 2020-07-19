//
//  MessageModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-21.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

struct MessageModelKeys {
    static let id = "id"
    static let name = "name"
    static let email = "email"
    static let creationdate = "creationdate"
    static let message = "message"
}

class MessageModel: NSObject, NSCoding{
    var id: String
    var name: String
    var email: String
    var creationdate: CLong
    var message: String
    
    
    
    init?(object: [String: Any]){
        
        guard let id = object["id"] as? String else {
            return nil
        }
        guard let name = object["title"] as? String else {
            return nil
        }
        guard let email = object["email"] as? String else {
            return nil
        }
        guard let creationdate = object["creationdate"] as? CLong else {
            return nil
        }
        
        self.id = id
        self.name = name
        self.email = email
        self.creationdate = creationdate
        
        self.message = object["message"] as? String ?? ""
    }
    
    
    init(id: String, name: String, email: String, creationdate: CLong, message: String) {
        self.id = id
        self.name = name
        self.email = email
        self.creationdate = creationdate
        self.message = message
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        id = decoder.decodeObject(forKey: MessageModelKeys.id) as! String
        name = decoder.decodeObject(forKey: MessageModelKeys.name) as! String
        email = decoder.decodeObject(forKey: MessageModelKeys.email) as! String
        creationdate = decoder.decodeInteger(forKey: MessageModelKeys.creationdate) as! CLong
        message = decoder.decodeObject(forKey: MessageModelKeys.message) as! String
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: MessageModelKeys.id)
        coder.encode(name, forKey: MessageModelKeys.name)
        coder.encode(email, forKey: MessageModelKeys.email)
        coder.encode(creationdate, forKey: MessageModelKeys.creationdate)
        coder.encode(message, forKey: MessageModelKeys.message)
    }
    
    
}


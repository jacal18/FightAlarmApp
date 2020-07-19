//
//  SubscriberModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//


import UIKit

struct SubscriberModelKeys {
    static let id = "id"
    static let subscriptions = "subscriptions"
    static let email = "email"
    static let joineddate = "joineddate"
}

class SubscriberModel: NSObject, NSCoding{
    var id: String
    var subscriptions: Int
    var email: String
    var joineddate: CLong
    
    
    
    init?(object: [String: Any]){
        
        guard let id = object["id"] as? String else {
            return nil
        }
    
        guard let joineddate = object["joineddate"] as? CLong else {
            return nil
        }
        
        self.id = id
        self.joineddate = joineddate
        
        self.subscriptions = object["subscriptions"] as? Int ?? 0
        self.email = object["email"] as? String ?? ""
        
    }
    
    
    init(id: String, subscriptions: Int, email: String, joineddate: CLong) {
        self.id = id
        self.subscriptions = subscriptions
        self.email = email
        self.joineddate = joineddate
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        id = decoder.decodeObject(forKey: SubscriberModelKeys.id) as! String
        subscriptions = decoder.decodeInteger(forKey: SubscriberModelKeys.subscriptions) as! Int
        email = decoder.decodeObject(forKey: SubscriberModelKeys.email) as! String
        joineddate = decoder.decodeInteger(forKey: SubscriberModelKeys.joineddate) as! CLong
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: SubscriberModelKeys.id)
        coder.encode(subscriptions, forKey: SubscriberModelKeys.subscriptions)
        coder.encode(email, forKey: SubscriberModelKeys.email)
        coder.encode(joineddate, forKey: SubscriberModelKeys.joineddate)
    }
    
}

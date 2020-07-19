//
//  EventModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//


import UIKit
import FirebaseDatabase

struct EventModelKeys {
    static let id = "id"
    static let title = "title"
    static let subscribers = "subscribers"
    static let player1fname = "player1fname"
    static let player2fname = "player2fname"
    static let player1lname = "player1lname"
    static let player2lname = "player2lname"
    static let categories = "categories"
    static let now_showing = "now_showing"
    static let subscribed = "subscribed"
}

class EventModel: NSObject, NSCoding{
    var id: String
    var title: String
    var player1fname: String
    var player2fname: String
    var player1lname: String
    var player2lname: String
    var categories: [CategoryModel]
    var now_showing: Bool
    var subscribed: Bool
    
    
    init?(snapshot: DataSnapshot){
        
        let object = snapshot.value as! Dictionary<String, Any>
        guard let id = object["id"] as? String else {
            return nil
        }
        guard let title = object["title"] as? String else {
            return nil
        }
        guard let player1fname = object["player1fname"] as? String else {
            return nil
        }
        guard let player2fname = object["player2fname"] as? String else {
            return nil
        }
        guard let player1lname = object["player1lname"] as? String else {
            return nil
        }
        guard let player2lname = object["player2lname"] as? String else {
            return nil
        }
        
        self.id = id
        self.title = title
        self.player1fname = player1fname
        self.player2fname = player2fname
        self.player1lname = player1lname
        self.player2lname = player2lname
        
    
        self.now_showing = object["now_showing"] as? Bool ?? false
        self.subscribed = object["subscribed"] as? Bool ?? false
        
        var newCategories: [CategoryModel] = []
        if let categorys = object["categories"] as? [[String: Any]] {
            for category in categorys{
                if let newCategory = CategoryModel(object: category){
                    newCategories.append(newCategory)
                }
            }
        }
        
        self.categories = newCategories
    }
    
    
    
    init(id: String, title: String, subscribers: Int, player1fname: String, player2fname: String, player1lname: String, player2lname: String, categories: [CategoryModel], now_showing: Bool, subscribed: Bool) {
        self.id = id
        self.title = title
        self.player1fname = player1fname
        self.player2fname = player2fname
        self.player1lname = player1lname
        self.player2lname = player2lname
        self.categories = categories
        self.now_showing = now_showing
        self.subscribed = subscribed
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        id = decoder.decodeObject(forKey: EventModelKeys.id) as! String
        title = decoder.decodeObject(forKey: EventModelKeys.title) as! String
        player1fname = decoder.decodeObject(forKey: EventModelKeys.player1fname) as! String
        player2fname = decoder.decodeObject(forKey: EventModelKeys.player2fname) as! String
        player1lname = decoder.decodeObject(forKey: EventModelKeys.player1lname) as! String
        player2lname = decoder.decodeObject(forKey: EventModelKeys.player2lname) as! String
        categories = decoder.decodeObject(forKey: EventModelKeys.categories) as! [CategoryModel]
        now_showing = decoder.decodeBool(forKey: EventModelKeys.now_showing) as! Bool
        subscribed = decoder.decodeBool(forKey: EventModelKeys.subscribed) as! Bool
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: EventModelKeys.id)
        coder.encode(title, forKey: EventModelKeys.title)
        coder.encode(player1fname, forKey: EventModelKeys.player1fname)
        coder.encode(player2fname, forKey: EventModelKeys.player2fname)
        coder.encode(player1lname, forKey: EventModelKeys.player1lname)
        coder.encode(player2lname, forKey: EventModelKeys.player2lname)
        coder.encode(categories, forKey: EventModelKeys.categories)
        coder.encode(now_showing, forKey: EventModelKeys.now_showing)
        coder.encode(subscribed, forKey: EventModelKeys.subscribed)
    }
    
    
}

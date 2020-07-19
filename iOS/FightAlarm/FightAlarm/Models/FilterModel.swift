//
//  FilterModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

struct FilterModelKeys {
    static let sortfield = "sortfield"
    static let categories = "categories"
    static let startdate = "startdate"
    static let enddate = "enddate"
    static let subscribers = "subscribers"
    static let noofdays = "noofdays"
    static let subscribersright = "subscribersright"
    static let noofdaysright = "noofdaysright"
}

class FilterModel: NSObject, NSCoding{
    var sortfield: Int?
    var categories: CategoryModel?
    var startdate: CLong?
    var enddate: CLong?
    var subscribers: Int?
    var noofdays: Int?
    var subscribersright: Int?
    var noofdaysright: Int?
    
    init(categories: CategoryModel) {
        self.categories = categories
    }
    
    init(sortfield: Int, categories: CategoryModel, startdate: CLong, enddate: CLong, subscribers: Int, noofdays: Int, subscribersright: Int, noofdaysright: Int) {
        self.sortfield = sortfield
        self.categories = categories
        self.startdate = startdate
        self.enddate = enddate
        self.subscribers = subscribers
        self.noofdays = noofdays
        self.subscribersright = subscribersright
        self.noofdaysright = noofdaysright
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        sortfield = decoder.decodeInteger(forKey: FilterModelKeys.sortfield) as! Int
        categories = decoder.decodeObject(forKey: FilterModelKeys.categories) as! CategoryModel
        startdate = decoder.decodeInteger(forKey: FilterModelKeys.startdate) as! CLong
        enddate = decoder.decodeInteger(forKey: FilterModelKeys.enddate) as! CLong
        subscribers = decoder.decodeInteger(forKey: FilterModelKeys.subscribers) as! Int
        noofdays = decoder.decodeInteger(forKey: FilterModelKeys.noofdays) as! Int
        subscribersright = decoder.decodeInteger(forKey: FilterModelKeys.subscribersright) as! Int
        noofdaysright = decoder.decodeInteger(forKey: FilterModelKeys.noofdaysright) as! Int
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(sortfield, forKey: FilterModelKeys.sortfield)
        coder.encode(categories, forKey: FilterModelKeys.categories)
        coder.encode(startdate, forKey: FilterModelKeys.startdate)
        coder.encode(enddate, forKey: FilterModelKeys.enddate)
        coder.encode(subscribers, forKey: FilterModelKeys.subscribers)
        coder.encode(noofdays, forKey: FilterModelKeys.noofdays)
        coder.encode(subscribersright, forKey: FilterModelKeys.subscribersright)
        coder.encode(noofdaysright, forKey: FilterModelKeys.noofdaysright)
    }
    
    
}


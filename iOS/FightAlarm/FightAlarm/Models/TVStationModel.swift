//
//  TVStationModel.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

struct TVStationModelKeys {
    static let id = "id"
    static let title = "title"
    static let description = "description"
    static let creationdate = "creationdate"
    static let imageURL = "imageURL"
}

class TVStationModel: NSObject, NSCoding{
    var id: String
    var title: String
    var cdescription: String
    var creationdate: CLong
    var imageURL: String
    
    
    
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
        
        self.imageURL = object["imageURL"] as? String ?? ""
    }
    
    
    init(id: String, title: String, description: String, creationdate: CLong, imageURL: String) {
        self.id = id
        self.title = title
        self.cdescription = description
        self.creationdate = creationdate
        self.imageURL = imageURL
    }
    
    
    //required for NSCoding
    required init?(coder decoder: NSCoder) {
        id = decoder.decodeObject(forKey: TVStationModelKeys.id) as! String
        title = decoder.decodeObject(forKey: TVStationModelKeys.title) as! String
        cdescription = decoder.decodeObject(forKey: TVStationModelKeys.description) as! String
        creationdate = decoder.decodeInteger(forKey: TVStationModelKeys.creationdate) as! CLong
        imageURL = decoder.decodeObject(forKey: TVStationModelKeys.imageURL) as! String
    }
    
    func encode(with coder: NSCoder) {
        coder.encode(id, forKey: TVStationModelKeys.id)
        coder.encode(title, forKey: TVStationModelKeys.title)
        coder.encode(cdescription, forKey: TVStationModelKeys.description)
        coder.encode(creationdate, forKey: TVStationModelKeys.creationdate)
        coder.encode(imageURL, forKey: TVStationModelKeys.imageURL)
    }
    
    
}


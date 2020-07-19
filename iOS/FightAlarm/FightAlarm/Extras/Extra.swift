//
//  Extra.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-26.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import Foundation

func sort(index: Int, events: [EventModel], callback: @escaping(_ events: [EventModel]?) -> Void) {
    var newevents = events
    if(index == 0) {
        newevents = events.sorted(by: { $0.player1fname.lowercased() < $1.player1fname.lowercased() })
        newevents = newevents.sorted(by: { $0.player2fname.lowercased() < $1.player2fname.lowercased() })
    } else if(index == 1) {
        newevents = events.sorted(by: { $0.player1lname.lowercased() < $1.player1lname.lowercased() })
        newevents = newevents.sorted(by: { $0.player2lname.lowercased() < $1.player2lname.lowercased() })
    }
    callback(newevents)
    
}

func sortNot(index: Int, notifications: [NotificationModel], callback: @escaping(_ notifications: [NotificationModel]?) -> Void) {
    var newnot = notifications
    newnot = notifications.sorted(by: { $0.creationdate > $1.creationdate })

    callback(newnot)
    
}

func generateRandomDigits(_ digitNumber: Int) -> String {
    var number = ""
    for i in 0..<digitNumber {
        var randomNumber = arc4random_uniform(10)
        while randomNumber == 0 && i == 0 {
            randomNumber = arc4random_uniform(10)
        }
        number += "\(randomNumber)"
    }
    return number
}

struct LocalDBKeys {
    static let userID = "userID"
    static let schedule = "scheduleList"
    static let scheduleID = "scheduleIDList"
}


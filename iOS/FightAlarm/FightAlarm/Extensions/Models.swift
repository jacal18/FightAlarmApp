//
//  Models.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-11-16.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

extension NSObject {
    func convertIntoDict() -> [String:Any] {
        var dict = [String:Any]()
        let otherSelf = Mirror(reflecting: self)
        for child in otherSelf.children {
            if let key = child.label {
                dict[key] = child.value
            }
        }
        return dict
    }
}

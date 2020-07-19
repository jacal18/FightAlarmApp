//
//  FilterProtocols.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-11-01.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import Foundation


protocol HandleFilter {
    func applyFilter(filtermodel: FilterModel)
    func cancelFilter()
}

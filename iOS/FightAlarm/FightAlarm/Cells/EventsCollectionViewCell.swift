//
//  EventsCollectionViewCell.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//
import UIKit

class EventsCollectionViewCell: UICollectionViewCell {
    @IBOutlet weak var dayLabel: UILabel!
    
    @IBOutlet weak var dateLabel: UILabel!
    
    override var isSelected: Bool {
        didSet {
            if isSelected {
                dayLabel!.textColor = UIColor.black
                dayLabel.font = UIFont.boldSystemFont(ofSize: 8)
                dateLabel!.textColor = UIColor.white
                dateLabel.font = UIFont.boldSystemFont(ofSize: 12)
                dateLabel.backgroundColor = UIColor.darkGray
            } else {
                dayLabel!.textColor = UIColor.gray
                dayLabel.font = UIFont.systemFont(ofSize: 8)
                dateLabel!.textColor = UIColor.black
                dateLabel.font = UIFont.systemFont(ofSize: 12)
                dateLabel.backgroundColor = UIColor.lightGray
            }
        }
    }
}

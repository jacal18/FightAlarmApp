//
//  EventsTableCell.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class EventsTableCell: UITableViewCell {
    
    
    @IBOutlet weak var playerOneFirstName: UILabel!
    @IBOutlet weak var playerOneLastName: UILabel!
    @IBOutlet weak var playerTwoFirstName: UILabel!
    @IBOutlet weak var playerTwoLastName: UILabel!
    @IBOutlet weak var title: UILabel!
    @IBOutlet weak var subscribeButton: SubscribeButton!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

    
//
    
}

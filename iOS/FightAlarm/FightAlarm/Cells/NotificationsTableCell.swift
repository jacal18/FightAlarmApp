//
//  NotificationsTableCell.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class NotificationsTableCell: UITableViewCell {

    @IBOutlet weak var monthLabel: UILabel!
    @IBOutlet weak var dayLabel: UILabel!
    @IBOutlet weak var yearLabel: UILabel!
    @IBOutlet weak var titleLabel: UILabel!
    
    @IBOutlet weak var descriptionLabel: UITextView!
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }

}

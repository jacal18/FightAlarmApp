//
//  SchedulesTableCell.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class SchedulesTableCell: UITableViewCell {

    @IBOutlet weak var playerOneFirstName: UILabel!
    @IBOutlet weak var playerOneLastName: UILabel!
    @IBOutlet weak var playerTwoFirstName: UILabel!
    @IBOutlet weak var playerTwoLastName: UILabel!
    @IBOutlet weak var Location: UILabel!
    
    @IBOutlet weak var noOfSubscribers: UILabel!
    
    @IBOutlet weak var noOfDays: UILabel!
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }
    
    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)
        
        // Configure the view for the selected state
    }
    
    @IBAction func showMore(_ sender: UIButton) {
    }
    

}

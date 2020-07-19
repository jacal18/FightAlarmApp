//
//  NotificationsTableController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class NotificationsTableController: UITableViewController {
    
    
    var notifications:[NotificationModel] = []

    override func viewDidLoad() {
        super.viewDidLoad()

        self.displayNavBarActivity()
        startObservingDatabase()
    }
    
    func startObservingDatabase () {
        
        let databaseService: DatabaseService = DatabaseService()
        let notifications = self.getSchedulesID()
        databaseService.getNotifications { (data) in
            if let notificationData = data {
                self.notifications = notificationData
                self.notifications = self.filterBySubscriberID(notifications: self.notifications)
                sortNot(index: 0, notifications: self.notifications, callback: { (newNotifications) in
                    if let newn = newNotifications {
                        self.notifications = newn
                    }
                })
                
                
                
                self.tableView.reloadData()
            }
            self.dismissNavBarActivity()
        }
    }
   
    
    // MARK: - Table view data source

    override func numberOfSections(in tableView: UITableView) -> Int {
        // #warning Incomplete implementation, return the number of sections
        return 1
    }

    override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        // #warning Incomplete implementation, return the number of rows
        return self.notifications.count
    }

    
    override func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "notificationsCell", for: indexPath) as! NotificationsTableCell

        let notification = self.notifications[(indexPath as NSIndexPath).row]
        let date = Date(timeIntervalSince1970: TimeInterval(notification.creationdate) / 1000)
        cell.descriptionLabel.text = notification.description
        cell.titleLabel.text = notification.title
        cell.yearLabel.text = date.getYear()
        cell.dayLabel.text = date.getDay()
        cell.monthLabel.text = date.getMonthName()
        

        return cell
    }

}

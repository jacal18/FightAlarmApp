//
//  MainTabController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class MainTabController: UITabBarController, UITabBarControllerDelegate {

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.delegate = self
        self.navigationItem.title = "Home"

        // Do any additional setup after loading the view.
    }
    
    @IBAction func goToNotifications(_ sender: UIBarButtonItem) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let nextViewController = storyBoard.instantiateViewController(withIdentifier: "NotificationsTableController") as! NotificationsTableController
        self.navigationController?.pushViewController(nextViewController, animated: false)
    }
    
    // UITabBarControllerDelegate
    func tabBarController(_ tabBarController: UITabBarController, didSelect viewController: UIViewController) {
        let index = tabBarController.selectedIndex
        if index == 0 {
            self.navigationItem.title = "Home"
        } else if index == 1 {
            self.navigationItem.title = "Events"
        } else if index == 2 {
            self.navigationItem.title = "My Schedules"
        } else if index == 3 {
            self.navigationItem.title = "About"
        }
        
    }

}

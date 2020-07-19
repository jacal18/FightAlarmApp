//
//  AboutViewController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit
import Toaster

class AboutViewController: UIViewController {

    @IBOutlet weak var nameField: UITextField!
    @IBOutlet weak var emailAddress: UITextField!
    @IBOutlet weak var message: UITextView!
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        self.hideKeyboardWhenTappedAround()
    }

    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    

    @IBAction func submitMessage(_ sender: Any) {
        let name = self.nameField.text
        let emailAddressValue = self.emailAddress.text
        let messageValue = self.message.text
        if let email = emailAddressValue, let nameValue = name, let messageVal = messageValue {
            if(email.isEmpty || nameValue.isEmpty || messageVal.isEmpty){
                Toast.init(text: "One or more fields are empty.", delay: 0.0, duration: 4.0).show()
            } else {
                if(self.isValidEmail(testStr: email)){
                    let messageID = generateRandomDigits(13);
                    let timeInterval = CLong(NSDate().timeIntervalSince1970 * 1000);
                    let messageModel = MessageModel(id: messageID, name: nameValue, email: email, creationdate: timeInterval, message: messageVal)
                    let databaseService: DatabaseService = DatabaseService();
                    databaseService.saveMessage(message: messageModel)
                    Toast.init(text: "Sucessfully submitted your message.", delay: 0.0, duration: 4.0).show()
                    resetFields()
                } else {
                    Toast.init(text: "Your email address is not in the right format.", delay: 0.0, duration: 4.0).show()
                }
            }
        } else {
            Toast.init(text: "One or more fields is empty.", delay: 0.0, duration: 4.0).show()
        }
    }
    
    func resetFields() {
        self.nameField.text = ""
        self.emailAddress.text = ""
        self.emailAddress.text = ""
    }

}

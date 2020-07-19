//
//  Utils.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-11-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import Foundation
import UIKit
extension Date {
    
    func getDayName() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "EEEE"
        let strMonth = dateFormatter.string(from: self)
        return strMonth
    }
    
    func getFullMonthName() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMMM"
        let strMonth = dateFormatter.string(from: self)
        return strMonth
    }
    
    func getMonthName() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "MMM"
        let strMonth = dateFormatter.string(from: self)
        return strMonth
    }
    
    func getYear() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy"
        let strYear = dateFormatter.string(from: self)
        return strYear
    }
    
    func getDay() -> String {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "d"
        let strDay = dateFormatter.string(from: self)
        return strDay
    }
    
    
}

extension CLong {
    func getDaysDifference(type: String) -> String {
        let calendar = NSCalendar.current
        let currentTime = CLong(NSDate().timeIntervalSince1970 * 1000);
        let date1 = calendar.startOfDay(for: Date(timeIntervalSince1970: TimeInterval(currentTime) / 1000))
        
        let date2 = calendar.startOfDay(for: Date(timeIntervalSince1970: TimeInterval(self) / 1000))
        let components = calendar.dateComponents([.hour, .day, .second, .minute], from: date1, to: date2)
        let days = components.day ?? 0
        if(type == "label"){
            return "day(s)"
        }
        var timestring = "00"
        if days < 10 {
            timestring = "0\(String(describing: days))"
        } else {
            timestring = "\(String(describing: days))"
        }
        return timestring
    }
    
    func getCountDownNumbers(type: String) -> String  {
        let calendar = NSCalendar.current
        let currentTime = CLong(NSDate().timeIntervalSince1970 * 1000);
        
        var difference = currentTime - self
        
        let days = difference / (24 * 60 * 60 * 1000)
        difference -= days * (24 * 60 * 60 * 1000);
        let hour = difference / (60 * 60 * 1000)
        difference -= hour * (60 * 60 * 1000);
        let minute = difference / (60 * 1000)
        difference -= minute * (60 * 1000);
        let second = difference / 1000
        
        var timestring = "00"
        
        if(type == "difference"){
            timestring = "\(String(describing: difference))"
        } else if(type == "days"){
            if days < 0 {
                timestring = "00"
            } else if days < 10 {
                timestring = "0\(String(describing: days))"
            } else {
                timestring = "\(String(describing: days))"
            }
        } else if(type == "hours"){
            if hour < 0 {
                timestring = "00"
            } else if hour < 10 {
                timestring = "0\(String(describing: hour))"
            } else {
                timestring = "\(String(describing: hour))"
            }
        } else if(type == "minutes"){
            if minute < 0 {
                timestring = "00"
            } else if minute < 10 {
                timestring = "0\(String(describing: minute))"
            } else {
                timestring = "\(String(describing: minute))"
            }
        } else {
            if second < 0 {
                timestring = "00"
            } else if second < 10 {
                timestring = "0\(String(describing: second))"
            } else {
                timestring = "\(String(describing: second))"
            }
        }
        return timestring
    }
    
    func getCountDown(type: String) -> String  {
        let calendar = NSCalendar.current
        let currentTime = CLong(NSDate().timeIntervalSince1970 * 1000);
        var difference = currentTime - self
        
        let days = difference / (24 * 60 * 60 * 1000)
        difference -= days * (24 * 60 * 60 * 1000);
        let hour = difference / (60 * 60 * 1000)
        difference -= hour * (60 * 60 * 1000);
        let minute = difference / (60 * 1000)
        difference -= minute * (60 * 1000);
        let second = difference / 1000
        if(days < 1){
            if(hour < 1){
                if(minute < 1){
                    if(type == "label"){
                        return "second(s)"
                    }
                    return self.getCountDownNumbers(type: "seconds")
                } else {
                    if(type == "label"){
                        return "minute(s)"
                    }
                    return self.getCountDownNumbers(type: "minutes")
                }
            } else {
                if(type == "label"){
                    return "hour(s)"
                }
                return self.getCountDownNumbers(type: "hours")
            }
        } else {
            if(type == "label"){
                return "day(s)"
            }
            return self.getCountDownNumbers(type: "days")
        }
    }
    
    func getDate() -> Date  {
        let date = Date(timeIntervalSince1970: TimeInterval(self) / 1000)
        return date
    }
}

extension String {
    func dateToLong() -> CLong {
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "dd-mm-yyyy" //Your date format
        dateFormatter.timeZone = TimeZone(abbreviation: "GMT+0:00") //Current time zone
        //according to date format your date string
        if let date = dateFormatter.date(from: self) {
            let dateLong = CLong(NSDate().timeIntervalSince1970)
            return dateLong
        }
        return 0
    }
}

class SubscribeButton: UIButton {
    var buttonID: String?
}

extension UITableView {
    
    func showEmptyMessage(_ message: String) {
        let messageLabel = UILabel(frame: CGRect(x: 0, y: 0, width: self.bounds.size.width, height: self.bounds.size.height))
        messageLabel.text = message
        messageLabel.textColor = .black
        messageLabel.numberOfLines = 0;
        messageLabel.textAlignment = .center;
        messageLabel.font = UIFont(name: "TrebuchetMS", size: 15)
        messageLabel.sizeToFit()
        
        self.backgroundView = messageLabel;
        self.separatorStyle = .none;
    }
    
    func restore() {
        self.backgroundView = nil
        self.separatorStyle = .singleLine
    }
}

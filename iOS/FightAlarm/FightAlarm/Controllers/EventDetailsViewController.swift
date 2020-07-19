//
//  EventDetailsViewController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-10-14.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class EventDetailsViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource {

    var event: EventModel!
    
    @IBOutlet weak var dataStack: UIStackView!
    @IBOutlet weak var tvCollectionView: UICollectionView!
    
    @IBOutlet weak var dateLabel: UILabel!
    
    @IBOutlet weak var subscribeButton: UIButton!
    @IBOutlet weak var subscribersNo: UILabel!
    @IBOutlet weak var daysNo: UILabel!
    
    @IBOutlet weak var countdownNo: UILabel!
    @IBOutlet weak var hourNumber: UILabel!
    
    @IBOutlet weak var minuteNumber: UILabel!
    
    @IBOutlet weak var secondNumber: UILabel!
    
    @IBOutlet weak var countdownStack: UIStackView!
    @IBOutlet weak var normalStack: UIStackView!
    
    @IBOutlet weak var eventLocationLabel: UILabel!
    
    var tvstations:[TVStationModel] = []
    
    private var countDownTimer = Timer()
    private var timerValue = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        tvCollectionView.delegate = self
        tvCollectionView.dataSource = self
        self.displayNavBarActivity()
        setupView()
    }
    
    
    
    func setupView(){
        
        if(event != nil){
            self.navigationItem.title = "\(event.player1fname) vs \(event.player2fname)"
            self.tvstations = event.tvstations
            dataStack.isHidden = false
            subscribersNo.text = String(event.subscribers)
            daysNo.text = event.eventdate.getDaysDifference(type: "number")
            eventLocationLabel.text = "\(event.eventaddress), \(event.eventcity), \(event.eventstate). \(event.eventcountry)"
            dateLabel.text = event.date()
            if(event.subscribed){
                subscribeButton.setTitle("UNSUBSCRIBE", for: UIControlState.normal)
            } else {
                subscribeButton.setTitle("SUBSCRIBE", for: UIControlState.normal)
            }
            if(event.now_showing){
                self.timerValue = Int(event.eventdate.getCountDownNumbers(type: "difference")) ?? 0
                self.startClockTimer()
                countdownStack.isHidden = false
                normalStack.isHidden = true
                subscribeButton.isHidden = true
            } else {
                self.countDownTimer.invalidate()
                normalStack.isHidden = false
                countdownStack.isHidden = true
                subscribeButton.isHidden = false
            }
            self.tvCollectionView.reloadData()
        } else {
            dataStack.isHidden = true
        }
        self.dismissNavBarActivity()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return tvstations.count
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "tvCell", for: indexPath) as! CategoryCell
        let tvstation = self.tvstations[(indexPath as NSIndexPath).row]
        
        cell.categoryTitle.text = tvstation.title
        if let imageUrl = NSURL(string: tvstation.imageURL) {
            if let imageData = NSData(contentsOf: imageUrl as URL) {
                let imageCell = UIImage(data: (imageData) as Data)
                
                cell.categoryImageView.image = imageCell
            }
        }
        
        
        return cell
    }
    
    func setDateTitles() {
        if(event != nil){
            countdownNo.text = event.eventdate.getCountDownNumbers(type: "days")
            hourNumber.text = event.eventdate.getCountDownNumbers(type: "hours")
            minuteNumber.text = event.eventdate.getCountDownNumbers(type: "minutes")
            secondNumber.text = event.eventdate.getCountDownNumbers(type: "seconds")
        } else {
            self.countDownTimer.invalidate()
        }
    }
    
    
    func startClockTimer() {
        self.countDownTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(countdown), userInfo: nil, repeats: true)
    }
    
    @objc func countdown() {
        self.timerValue -= 1
        if self.timerValue > 0 {
            self.setDateTitles()
        } else {
            countdownStack.isHidden = true
            normalStack.isHidden = false
        }
    }
    

    @IBAction func subscribeEvent(_ sender: Any) {
        if(event.subscribed){
            removeEventFromSubscription(event: event)
        } else {
            addEventToSubscription(event: event)
        }
        event.subscribed = !event.subscribed
        setupView()
    }
    
    @IBAction func getDirections(_ sender: Any) {
        openMaps(event: event)
    }
    
}

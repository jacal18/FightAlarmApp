//
//  SecondViewController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-12.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

class HomeViewController: UIViewController, UICollectionViewDelegate, UICollectionViewDataSource, UITableViewDelegate, UITableViewDataSource , UISearchBarDelegate {
    
    @IBOutlet weak var categoriesLabel: UILabel!
    
    
    @IBOutlet weak var categoryCollectionView: UICollectionView!
    
    @IBOutlet weak var searchField: UISearchBar!
    
    @IBOutlet weak var tableView: UITableView!
    
    
    var filterDelegate:HandleFilter? = nil
    
    var events:[EventModel] = []
    var oldevents:[EventModel] = []
    var filter: FilterModel?
    
    var sections = Dictionary<String, Array<EventModel>>()
    var sortedSections = [String]()
    
    
    var categories:[CategoryModel] = []
    
    @IBOutlet weak var noCategory: UILabel!

    
    override func viewDidLoad() {
        super.viewDidLoad()
        // Do any additional setup after loading the view, typically from a nib.
        categoryCollectionView.delegate = self
        categoryCollectionView.dataSource = self
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.searchField.delegate = self
        self.hideKeyboardWhenTappedAround()
        
        self.displayNavBarActivity()
        startObservingDatabase()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if let filtercategory = self.filter?.categories {
            self.filterCategories(filtercategory: filtercategory)
            self.tableView.reloadData()
        }
    }
    
    func startObservingDatabase() {
        
        let databaseService: DatabaseService = DatabaseService();
        
        setUser(databaseService: databaseService)
        databaseService.getEvents() { (data) in
            if let eventData = data {
                var newEventData = self.setSubscribedEvents(events: eventData)
                self.oldevents = newEventData
                self.events = newEventData
                
                if let filtercategory = self.filter?.categories {
                    self.filterCategories(filtercategory: filtercategory)
                }
                if let searchText = self.searchField.text {
                    if(!searchText.isEmpty){
                       self.events = self.searchField(searchText: searchText, events: self.events)
                    }
                }
                
                sort(index: 0, events: self.events, callback: { (newEvent) in
                    if let newe = newEvent {
                        self.events = newe
                    }
                })
                self.tableView.reloadData()
                
            }
            self.dismissNavBarActivity()
        }
        
        
        databaseService.getCategories{ (data) in
            if let categoryData = data {
                self.categories = categoryData
                self.categoryCollectionView.reloadData()
            }
            
            if(self.categories.count > 0){
                self.categoryCollectionView.isHidden = false
                self.noCategory.isHidden = true
            } else {
                self.categoryCollectionView.isHidden = true
                self.noCategory.isHidden = false
            }
        }
        
        self.dismissNavBarActivity()
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        if collectionView == categoryCollectionView {
            return categories.count
        }
        return 0
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        if collectionView == categoryCollectionView {
            let cell = collectionView.dequeueReusableCell(withReuseIdentifier: "categoryCell", for: indexPath) as! CategoryCell
            let category = self.categories[(indexPath as NSIndexPath).row]
            
            cell.categoryTitle.text = category.title
        
            return cell
        }
        
        return UICollectionViewCell()
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        if collectionView == categoryCollectionView {
            let category = self.categories[(indexPath as NSIndexPath).row]
            self.filterCategories(filtercategory: category)
            self.tableView.reloadData()
        }
    }
    
    func setUser(databaseService: DatabaseService){
        let userInfo = UserDefaults.standard.string(forKey: LocalDBKeys.userID);
        if(userInfo == nil){
            let timeInterval = CLong(NSDate().timeIntervalSince1970 * 1000);
            let subscriber:SubscriberModel = SubscriberModel(id: "\(timeInterval)", subscriptions: 0, email: "User\(timeInterval)", joineddate: timeInterval)
            databaseService.saveSubscribers(subscriber: subscriber);
            let random = "\(String(describing: timeInterval))"
            
            let notification = NotificationModel(id: random, uid: random, title: "New User Joined", description: "New User \(String(describing: subscriber.email)) joined", creationdate: timeInterval, type: "all", topic: "admin")
            databaseService.saveNotification(notification: notification)
            
            UserDefaults.standard.set(subscriber.id, forKey: LocalDBKeys.userID);
            
        }
    }
    
    func numberOfSections(in tableView: UITableView) -> Int {
        return 1
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if events.count == 0 {
            self.tableView.showEmptyMessage("No Event Currently Listed.")
        } else {
            self.tableView.restore()
        }
        
        return events.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "eventCell") as! EventsTableCell
        let event = events[(indexPath as NSIndexPath).row]
        cell.playerOneFirstName.text = event.player1fname
        cell.playerOneLastName.text = event.player1lname
        cell.playerTwoFirstName.text = event.player2fname
        cell.playerTwoLastName.text = event.player2lname
        cell.title.text = event.title
        cell.subscribeButton.addTarget(self, action: #selector(eventAction), for: .touchUpInside)
        cell.subscribeButton.buttonID = event.id
        cell.subscribeButton.setButtonColor(event: event)
        
        return cell
    }
    
    
    
    @objc func eventAction(sender: SubscribeButton){
        let index = sender.buttonID
        let event: EventModel = self.filterByID(events: self.events, id: index ?? "")
        
        if(event.subscribed){
            event.subscribed = false
            removeEventFromSubscription(event: event)
            removeIDFromSubscription(id: event.id)
        } else {
            event.subscribed = true
            addEventToSubscription(event: event)
            addIDToSubscription(id: event.id)
        }
    }
    
    func filterCategories(filtercategory: CategoryModel) {
        if(self.oldevents.count > 0){
            self.events = self.filterByCategories(filtercategory: filtercategory, oldevents: self.oldevents)
            if let searchText = self.searchField.text {
                self.events = self.searchField(searchText: searchText, events: self.events)
            }
        }
    }
    // This method updates filteredData based on the text in the Search Box
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        self.events = self.searchField(searchText: searchText, events: self.oldevents)
        tableView.reloadData()
    }
    
    @IBAction func goToNotifications(_ sender: UIBarButtonItem) {
        let storyBoard: UIStoryboard = UIStoryboard(name: "Main", bundle: nil)
        let nextViewController = storyBoard.instantiateViewController(withIdentifier: "NotificationsTableController") as! NotificationsTableController
        self.navigationController?.pushViewController(nextViewController, animated: false)
    }
    
    
}



extension HomeViewController: HandleFilter {
    
    func cancelFilter() {
        self.events = self.oldevents
    }
    
    func applyFilter(filtermodel:FilterModel){
        if let filtercategory = filtermodel.categories {
            self.filterCategories(filtercategory: filtercategory)
        }
        
        if let sortValue = filtermodel.sortfield {
            sort(index: sortValue, events: events, callback: { (newEvent) in
                if let newevents = newEvent{
                    self.events = newevents
                }
            })
        }
        
        self.tableView.reloadData()
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.searchField.endEditing(true)
    }
    
    func searchBarSearchButtonClicked(_ searchBar: UISearchBar) {
        self.searchField.endEditing(true)
    }
    
    func searchBarCancelButtonClicked(_ searchBar: UISearchBar) {
        self.searchField.endEditing(true)
    }
}

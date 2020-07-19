//
//  FirstViewController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-12.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit

import MapKit

class EventsViewController: UIViewController, UITableViewDelegate, UITableViewDataSource , UISearchBarDelegate  {
    

    @IBOutlet weak var monthShownLabel: UILabel!
    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var calendarCollectionView: UICollectionView!
    
    @IBOutlet weak var calendarStack: UIStackView!
    @IBOutlet weak var homeViewChange: UISegmentedControl!
    @IBOutlet weak var searchField: UISearchBar!
    var calendarPicker: CalendarPicker?
    var cellIdentifier = "calendarViewCell"
    var selectedDate: Int = 0
    var currentDate = Date()
    var calendarArray: NSArray?
    
    
    var events:[EventModel] = []
    var oldevents:[EventModel] = []
    var filter: FilterModel?
    
    var sections = Dictionary<String, Array<EventModel>>()
    var sortedSections = [String]()
    
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(true)
        self.calendarArray = getCalendar().arrayOfDates()
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        self.calendarCollectionView.delegate = self
        self.calendarCollectionView.dataSource = self
        self.searchField.delegate = self
        self.hideKeyboardWhenTappedAround()
        
        self.displayNavBarActivity()
        startObservingDatabase()
    }
    
    func getCalendar() -> CalendarPicker {
        
        if calendarPicker == nil {
            calendarPicker = CalendarPicker()
            calendarPicker?.delegate = self
        }
        return calendarPicker!
    }
    
    override func viewDidAppear(_ animated: Bool) {
        if let filtercategory = self.filter?.categories {
            self.filterCategories(filtercategory: filtercategory)
            
            self.setupSecs()
            self.tableView.reloadData()
        }
        
        checkSegment()
    }
    
    func startObservingDatabase () {
        let databaseService: DatabaseService = DatabaseService()
        databaseService.getEvents() { (data) in
            if let eventData = data {
                var newEventData = self.setSuscribedEvents(events: eventData)
                newEventData = self.updateExpired(events: newEventData)
                self.oldevents = newEventData
                self.events = newEventData
                
                if let filtercategory = self.filter?.categories {
                    self.filterCategories(filtercategory: filtercategory)
                }
                sort(index: 8, events: self.events, callback: { (newEvent) in
                    if let newe = newEvent {
                        self.events = newe
                    }
                })
                
                self.setupSecs()
                self.tableView.reloadData()
            
            }
            self.dismissNavBarActivity()
        }
    }
    
    func setupSecs(){
        self.sections = self.setupSections(events: self.events)
        self.sortedSections = self.sections.keys.sorted()
    }
    @IBAction func showFilter(_ sender: UIButton) {
        self.filter = nil
        let filterViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: "FilterViewController") as! FilterViewController
        filterViewController.filterDelegate = self
        self.present(filterViewController, animated: true, completion: nil)

    }
    
    
    func scrollViewDidEndDecelerating(_ scrollView: UIScrollView) {
        if calendarCollectionView == scrollView {
            setSelectedItemFromScrollView(scrollView)
        }
    }
    
    func setSelectedItemFromScrollView(_ scrollView: UIScrollView) {
        if calendarCollectionView == scrollView {
            let center = CGPoint(x: scrollView.center.x + scrollView.contentOffset.x, y: scrollView.center.y + scrollView.contentOffset.y)
            let index = calendarCollectionView.indexPathForItem(at: center)
            if index != nil {
                calendarCollectionView.scrollToItem(at: index!, at: .centeredHorizontally, animated: true)
                self.calendarCollectionView.selectItem(at: index, animated: false, scrollPosition: [])
                self.collectionView(self.calendarCollectionView, didSelectItemAt: index!)
                
                self.selectedDate = (index?.row)!
            }
            else {
            }
        }
    }
    
    func scrollViewDidEndDragging(_ scrollView: UIScrollView, willDecelerate decelerate: Bool) {
        
        if calendarCollectionView == scrollView && !decelerate  {
            setSelectedItemFromScrollView(scrollView)
        }
    }
    
    
    func numberOfSections(in collectionView: UICollectionView) -> Int {
        return 1
    }
    
    func collectionView(_ collectionView: UICollectionView, numberOfItemsInSection section: Int) -> Int {
        return (calendarArray?.count)!
    }
    
    func collectionView(_ collectionView: UICollectionView, cellForItemAt indexPath: IndexPath) -> UICollectionViewCell {
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! EventsCollectionViewCell
        
        let cellData = self.calendarArray?[indexPath.row] as! [String: Any]
        if let dayValue = cellData["day"] as? String {
            cell.dayLabel.text = "\(String(describing: dayValue.prefix(1)))"
        }
        cell.dateLabel.layer.cornerRadius = cell.dateLabel.frame.width/2
        cell.dateLabel.layer.masksToBounds = true
        cell.dateLabel.text =  cellData["date"] as? String
        monthShownLabel.text = "\(String(describing: cellData["monthValue"] as! String)) \(cellData["year"] as! String)"
        return cell
    }
    
    func collectionView(_ collectionView: UICollectionView, didSelectItemAt indexPath: IndexPath) {
        self.selectedDate = indexPath.row
        let centeredIndexPath = IndexPath.init(item: selectedDate, section: 0)
        collectionView.scrollToItem(at: centeredIndexPath, at: .centeredHorizontally, animated: true)
        if indexPath == centeredIndexPath {
            collectionView.scrollToItem(at: indexPath, at: .centeredHorizontally, animated: true)
        }
        let cellData = self.calendarArray?[indexPath.row] as! [String: Any]
        monthShownLabel.text = "\(String(describing: cellData["monthValue"] as! String)) \(cellData["year"] as! String)"
       
        self.searchDate(day: cellData["date"] as! String, month: cellData["monthValue"] as! String, year: cellData["year"] as! String)
        
    }
    
    func collectionView(_ collectionView: UICollectionView, didDeselectItemAt indexPath: IndexPath) {
        
        let cell = collectionView.dequeueReusableCell(withReuseIdentifier: cellIdentifier, for: indexPath) as! EventsCollectionViewCell
        cell.dayLabel!.textColor = UIColor.black
        cell.dateLabel!.textColor = UIColor.black
        let cellData = self.calendarArray?[indexPath.row] as! [String: Any]
        monthShownLabel.text = "\(String(describing: cellData["monthValue"] as! String)) \(cellData["year"] as! String)"
    }

    func numberOfSections(in tableView: UITableView) -> Int {
        return sections.count
    }
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        if sections[sortedSections[section]]!.count == 0 {
            self.tableView.showEmptyMessage("No Event Currently Listed.")
        } else {
            self.tableView.restore()
        }

        return sections[sortedSections[section]]!.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "eventCell") as! EventsTableCell
        let eventSection = sections[sortedSections[indexPath.section]]
        let event = eventSection![indexPath.row]
        cell.playerOneFirstName.text = event.player1fname
        cell.playerOneLastName.text = event.player1lname
        cell.playerTwoFirstName.text = event.player2fname
        cell.playerTwoLastName.text = event.player2lname
//        cell.Location.text = event.eventstate + ", " + event.eventcountry
        
//        cell.noOfSubscribers.text = String(event.subscribers)
        cell.subscribeButton.setTitle(self.setString(event: event), for: UIControlState.normal)
        cell.subscribeButton.addTarget(self, action: #selector(eventAction), for: .touchUpInside)
        cell.subscribeButton.buttonID = event.id
        if(event.now_showing){
            cell.buttonView.isHidden = true
        } else {
            cell.buttonView.isHidden = false
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, titleForHeaderInSection section: Int) -> String? {
        return sortedSections[section]
    }
    
    func setString(event: EventModel) -> String {
        
        if(event.subscribed){
            return "Alarm Set"
        } else {
            return "Set Alarm"
        }
        
    }
    
    @objc func eventAction(sender: SubscribeButton){
        let index = sender.buttonID
        let event: EventModel = self.filterByID(events: self.events, id: index ?? "")
        
        if(event.subscribed){
            event.subscribed = false
            removeEventFromSubscription(event: event)
        } else {
            event.subscribed = true
            addEventToSubscription(event: event)
            sender.backgroundColor = UIColor.red
        }
    }
    
    func filterCategories(filtercategory: CategoryModel) {
        if(self.oldevents.count > 0){
            self.events = self.filterByCategories(filtercategory: filtercategory, oldevents: self.oldevents)
        }
    }
    // This method updates filteredData based on the text in the Search Box
    func searchBar(_ searchBar: UISearchBar, textDidChange searchText: String) {
        self.events = self.searchField(searchText: searchText, events: self.oldevents)
        self.setupSecs()
        tableView.reloadData()
    }
    
    func searchDate(day: String, month: String, year: String){
        self.events = self.searchByDate(day: day, month: month, year: year, events: self.oldevents)
        self.setupSecs()
        tableView.reloadData()
    }
    
    
    
    func checkSegment() {
        switch homeViewChange.selectedSegmentIndex
        {
            case 0:
                searchField.isHidden = false
                calendarStack.isHidden = true
            case 1:
                searchField.isHidden = true
                calendarStack.isHidden = false
            default:
                break
        }
    }
    
    
    @IBAction func viewChange(_ sender: UISegmentedControl) {
        checkSegment()
        self.events = self.oldevents
        self.setupSecs()
        tableView.reloadData()
    }

}

extension EventsViewController: HandleFilter {
    
    func cancelFilter() {
        self.events = self.oldevents
    }
    
    func applyFilter(filtermodel:FilterModel){
        if let filtercategory = filtermodel.categories {
            self.filterCategories(filtercategory: filtercategory)
        }
        if let start = filtermodel.startdate, let end = filtermodel.enddate {
            if start > 0 && end > start && end > 0 {
                self.events = self.filterByDateRange(range1: start, range2: end, events: self.events)
            }
        }
        
        if let noofday = filtermodel.noofdays, let noofdayright = filtermodel.noofdaysright {
             if noofday >= 0 && noofdayright > noofday {
                self.events = self.filterByDaysRange(range1: CLong(noofday), range2: CLong(noofdayright), events: self.events)
            }
        }
        
        if let sortValue = filtermodel.sortfield {
            sort(index: sortValue, events: events, callback: { (newEvent) in
                if let newevents = newEvent{
                    self.events = newevents
                }
            })
        }
        
        self.setupSecs()
        
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

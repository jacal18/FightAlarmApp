//
//  FilterViewController.swift
//  FightAlarm
//
//  Created by Adeniran  Abisola on 2018-09-15.
//  Copyright © 2018 Adeniran  Abisola. All rights reserved.
//

import UIKit
import SwiftRangeSlider

class FilterViewController: UIViewController, UIPickerViewDelegate, UIPickerViewDataSource {
  

    @IBOutlet weak var sortPicker: UIPickerView!
    
    @IBOutlet weak var categoryPicker: UIPickerView!
    var categories:[CategoryModel] = []
    var selectedSort: Int = 0
    var selectedCategory: Int = 0
    
    let sortFields = ["First Name", "Last Name", "Event Date"]
    
    @IBOutlet weak var dateRangePicker: UITextField!
    
    @IBOutlet weak var endDatePicker: UITextField!
    
    @IBOutlet weak var daysRange: RangeSlider!
    
    @IBOutlet weak var subscriberRange: RangeSlider!
    var filterDelegate:HandleFilter? = nil
    
    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
        sortPicker.dataSource = self
        sortPicker.delegate = self
        categoryPicker.dataSource = self
        categoryPicker.delegate = self
        
        self.startObservingDatabase()
    }
    
    func startObservingDatabase() {
        
        let databaseService: DatabaseService = DatabaseService();
        
        databaseService.getCategories{ (data) in
            if let categoryData = data {
                self.categories = categoryData
                self.categoryPicker.reloadAllComponents()
            }
            self.dismissNavBarActivity()
        }
    }

    @IBAction func cancel(_ sender: UIBarButtonItem) {
        filterDelegate?.cancelFilter()
        self.dismiss(animated: true, completion: nil)
    }
    
    
    @IBAction func applyFilter(_ sender: Any) {
        if let rowIndex = sortPicker?.selectedRow(inComponent: 0) {
            self.selectedSort = rowIndex
        }
        if let rowCategoryIndex = categoryPicker?.selectedRow(inComponent: 0) {
            self.selectedCategory = rowCategoryIndex
        }
        let startdate = dateRangePicker.text?.dateToLong() ?? 0
        let enddate = endDatePicker.text?.dateToLong() ?? 0
        
        let subscribers = 0
        let subscribersright = 10
        let noofdays = Int(daysRange.lowerValue)
        let noofdaysright = Int(daysRange.upperValue)
        
        
        let filterModel = FilterModel(sortfield: selectedSort, categories: categories[selectedCategory], startdate: startdate, enddate: enddate, subscribers: subscribers, noofdays: noofdays, subscribersright: subscribersright, noofdaysright: noofdaysright)
        filterDelegate?.applyFilter(filtermodel: filterModel)
        
        self.dismiss(animated: true, completion: nil)
    }
    
    func numberOfComponents(in pickerView: UIPickerView) -> Int {
        return 1
    }
    
    func pickerView(_ pickerView: UIPickerView, numberOfRowsInComponent component: Int) -> Int {
        if pickerView == sortPicker {
            return sortFields.count
        } else if pickerView == categoryPicker {
            return categories.count
        } else {
            return 0
        }
    }
    
    func pickerView(_ pickerView: UIPickerView, titleForRow row: Int, forComponent component: Int) -> String? {
        if pickerView == sortPicker {
            return sortFields[row]
        } else if pickerView == categoryPicker {
            return categories[row].title
        } else {
            return nil
        }
    }
    
    @IBAction func startDateEditing(_ sender: UITextField) {
        let datePickerView:UIDatePicker = UIDatePicker()
        
        datePickerView.datePickerMode = UIDatePickerMode.date
        
        sender.inputView = datePickerView
        
        datePickerView.addTarget(self, action: #selector(self.startDatePickerValueChanged), for: UIControlEvents.valueChanged)
        
    }
    
    @IBAction func endDateEditing(_ sender: UITextField) {
        let datePickerView:UIDatePicker = UIDatePicker()
        
        datePickerView.datePickerMode = UIDatePickerMode.date
        
        sender.inputView = datePickerView
        
        datePickerView.addTarget(self, action: #selector(self.endDatePickerValueChanged), for: UIControlEvents.valueChanged)
    }
    
    @objc func startDatePickerValueChanged(sender:UIDatePicker) {
        
        let dateFormatter = DateFormatter()
        
        dateFormatter.dateFormat = "dd/MM/yyyy"
        
        dateRangePicker.text = dateFormatter.string(from: sender.date)
        
    }
    
    @objc func endDatePickerValueChanged(sender:UIDatePicker) {
        
        let dateFormatter = DateFormatter()
        
        dateFormatter.dateFormat = "dd/MM/yyyy"
        
        endDatePicker.text = dateFormatter.string(from: sender.date)
        
    }


}

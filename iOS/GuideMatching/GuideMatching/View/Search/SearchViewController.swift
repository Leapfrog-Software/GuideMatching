//
//  SearchViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SearchViewController: UIViewController {
    
    struct SearchCondition {
        let language: String
        let keyword: String?
        let date: Date?
        let time: Int?
        let category: String
        let order: OrderType
    }
    
    enum OrderType: Int {
        case login = 0
        case estimate = 1
        case number = 2
        
        func toString() -> String {
            switch self {
            case .login:
                return "Login time"
            case .estimate:
                return "Customer review"
            case .number:
                return "Number of transactions"
            }
        }
    }
    
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var keywordTextField: UITextField!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var timeLabel: UILabel!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var orderLabel: UILabel!
    
    private var languages: [String] = ["English", "Chinese", "Korean", "Thai", "Malay", "Indonesian", "Vietnamese", "Hindi", "French", "German", "Italian", "Spanish", "Arabic", "Portuguese"]
    private var categories: [String] = ["Food", "Nature", "Historical site", "Traditional culture", "Music", "Art", "Subculture"]
    
    private var selectedLanguageIndex = 0
    private var selectedDay: Date?
    private var selectedTime: Int?
    private var selectedCategoryIndex = 0
    private var selectedOrderType = OrderType.login
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.reset()
    }
    
    private func reset() {
        self.languageLabel.text = self.languages[0]
        self.keywordTextField.text = ""
        self.dayLabel.text = ""
        self.timeLabel.text = ""
        self.categoryLabel.text = self.categories[0]
        self.orderLabel.text = OrderType.login.toString()
        
        self.selectedLanguageIndex = 0
        self.selectedDay = nil
        self.selectedTime = nil
        self.selectedCategoryIndex = 0
        self.selectedOrderType = .login
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapLanguage(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Language", dataArray: self.languages, defaultIndex: self.selectedLanguageIndex, completion: { index in
            self.selectedLanguageIndex = index
            self.languageLabel.text = self.languages[index]
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapDay(_ sender: Any) {
        
        self.view.endEditing(true)

        var dates = [Date]()
        for i in 0..<28 {
            dates.append(Date().add(day: i))
        }
        let dateFormatter = DateFormatter(dateFormat: "M/d(E)")
        let dataArray = dates.map { dateFormatter.string(from: $0) }
        var defaultIndex = 0
        if let day = self.selectedDay {
            if let index = dataArray.index(of: dateFormatter.string(from: day)) {
                defaultIndex = index
            }
        }
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Day", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            self?.dayLabel.text = dataArray[index]
            self?.selectedDay = dates[index]
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapTime(_ sender: Any) {
        
        self.view.endEditing(true)

        var times = [Int]()
        for i in 0..<48 {
            times.append(i)
        }
        let dataArray = times.map { CommonUtility.timeOffsetToString(offset: $0) }
        let defaultIndex = self.selectedTime ?? 0
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Time", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            self?.timeLabel.text = dataArray[index]
            self?.selectedTime = index
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapCategory(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Category", dataArray: self.categories, defaultIndex: self.selectedCategoryIndex, completion: { index in
            self.selectedCategoryIndex = index
            self.categoryLabel.text = self.categories[index]
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapOrder(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        let orderTypes: [OrderType] = [.login, .estimate, .number]
        picker.set(title: "Order", dataArray: orderTypes.compactMap { $0.toString() }, defaultIndex: self.selectedOrderType.rawValue, completion: { [weak self] index in
            if let orderType = OrderType(rawValue: index) {
                self?.selectedOrderType = orderType
                self?.orderLabel.text = orderType.toString()
            }
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapSearch(_ sender: Any) {

        self.view.endEditing(true)
        
        var keyword: String? = nil
        if (self.keywordTextField.text ?? "").count > 0 {
            keyword = self.keywordTextField.text
        }
        
        let condition = SearchCondition(language: self.languages[self.selectedLanguageIndex],
                                        keyword: keyword,
                                        date: self.selectedDay,
                                        time: self.selectedTime,
                                        category: self.categories[self.selectedCategoryIndex],
                                        order: self.selectedOrderType)
        let guideList = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        guideList.set(searchCondition: condition)
        self.tabbarViewController()?.stack(viewController: guideList, animationType: .horizontal)
    }

    @IBAction func onTapReset(_ sender: Any) {
        self.reset()
    }
}

extension SearchViewController: UIScrollViewDelegate {
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
}

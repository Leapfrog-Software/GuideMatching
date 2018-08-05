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
        let language: String?
        let keyword: String?
        let date: Date?
        let time: Int?
        let nationality: String?
        let category: String?
        let order: OrderType
    }
    
    enum OrderType: Int {
        case login = 0
        case estimate = 1
        case number = 2
        
        func toString() -> String {
            switch self {
            case .login:
                return "ログイン"
            case .estimate:
                return "評価"
            case .number:
                return "実績"
            }
        }
    }
    
    @IBOutlet private weak var languageTextField: UITextField!
    @IBOutlet private weak var keywordTextField: UITextField!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var timeLabel: UILabel!
    @IBOutlet private weak var nationalityTextField: UITextField!
    @IBOutlet private weak var categoryTextField: UITextField!
    @IBOutlet private weak var orderLabel: UILabel!

    private var selectedDay: Date?
    private var selectedTime: Int?
    private var selectedOrderType = OrderType.login
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapDay(_ sender: Any) {

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
    
    @IBAction func onTapOrder(_ sender: Any) {
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
        
        let language = self.languageTextField.text ?? ""
        let keyword = self.keywordTextField.text ?? ""
        let nationality = self.nationalityTextField.text ?? ""
        let category = self.categoryTextField.text ?? ""
        let condition = SearchCondition(language: (language.count > 0) ? language : nil,
                                        keyword: (keyword.count > 0) ? keyword : nil,
                                        date: self.selectedDay,
                                        time: self.selectedTime,
                                        nationality: (nationality.count > 0) ? nationality : nil,
                                        category: (category.count > 0) ? category : nil,
                                        order: self.selectedOrderType)
        let guideList = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        guideList.set(searchCondition: condition)
        self.tabbarViewController()?.stack(viewController: guideList, animationType: .horizontal)
    }

    @IBAction func onTapReset(_ sender: Any) {
        self.languageTextField.text = ""
        self.keywordTextField.text = ""
        self.dayLabel.text = ""
        self.timeLabel.text = ""
        self.nationalityTextField.text = ""
        self.categoryTextField.text = ""
        self.orderLabel.text = OrderType.login.toString()
        
        self.selectedDay = nil
        self.selectedTime = nil
        self.selectedOrderType = .login
    }
}

extension SearchViewController: UIScrollViewDelegate {
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
}

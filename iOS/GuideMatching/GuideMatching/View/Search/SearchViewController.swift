//
//  SearchViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SearchViewController: UIViewController {
    
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
        
        var guides = GuideRequester.shared.dataList.filter { guideData -> Bool in
            let language = self.languageTextField.text ?? ""
            if language.count > 0 && !guideData.language.contains(language) {
                return false
            }
            
            let keyword = self.keywordTextField.text ?? ""
            if keyword.count > 0 {
                if !guideData.email.contains(keyword)
                    && !guideData.name.contains(keyword)
                    && !guideData.nationality.contains(keyword)
                    && !guideData.category.contains(keyword)
                    && !guideData.specialty.contains(keyword)
                    && !guideData.notes.contains(keyword) {
                    return false
                }
            }
            
            if let day = self.selectedDay {
                if let schedules = (guideData.schedules.filter { $0.date.isSameDay(with: day) }).first {
                    if !schedules.isFreeList.contains(true) {
                        return false
                    }
                } else {
                    return false
                }
            }
            
            if let time = self.selectedTime {
                let isFreeList = guideData.schedules.map { $0.isFreeList[time] }
                if isFreeList.isEmpty {
                    return false
                }
            }
            
            let nationality = self.nationalityTextField.text ?? ""
            if nationality.count > 0 && !guideData.nationality.contains(nationality) {
                return false
            }
            
            let category = self.categoryTextField.text ?? ""
            if category.count > 0 && !guideData.category.contains(category) {
                return false
            }
            return true
        }
        
        guides.sort(by: { guide1, guide2 in
            // TODO 3パターンある
            return guide1.loginDate > guide2.loginDate
        })
        
        let guideList = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        guideList.set(searchResult: guides)
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

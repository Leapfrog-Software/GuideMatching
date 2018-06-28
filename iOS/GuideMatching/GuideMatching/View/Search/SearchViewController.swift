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
    @IBOutlet private weak var dayTextField: UITextField!
    @IBOutlet private weak var timeTextField: UITextField!
    @IBOutlet private weak var nationalityTextField: UITextField!
    @IBOutlet private weak var categoryTextField: UITextField!
    @IBOutlet private weak var orderLabel: UILabel!

    private var selectedOrderType = OrderType.login
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
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
            if guideData.language.contains(language) {
                return true
            }
            let category = self.categoryTextField.text ?? ""
            if guideData.category.contains(category) {
                return true
            }
            return false
        }
        
        guides.sort(by: { guide1, guide2 in
            return guide1.loginDate > guide2.loginDate
        })
        
        let guideList = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        guideList.set(searchResult: guides)
        self.tabbarViewController()?.stack(viewController: guideList, animationType: .horizontal)
    }

    @IBAction func onTapReset(_ sender: Any) {
        self.languageTextField.text = ""
        self.keywordTextField.text = ""
        self.dayTextField.text = ""
        self.timeTextField.text = ""
        self.nationalityTextField.text = ""
        self.categoryTextField.text = ""
        self.orderLabel.text = OrderType.login.toString()
        self.selectedOrderType = .login
    }
}

extension SearchViewController: UIScrollViewDelegate {
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
}

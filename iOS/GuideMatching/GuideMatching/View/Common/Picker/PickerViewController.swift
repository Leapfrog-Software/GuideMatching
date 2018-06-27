//
//  PickerViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class PickerViewController: UIViewController {
    
    struct Const {
        static let topMargin = CGFloat(120)
    }
    
    @IBOutlet private weak var pickerTitleLabel: UILabel!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var contentsTopConstraint: NSLayoutConstraint!
    @IBOutlet private weak var contentsHeaderHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var contentsHeightConstraint: NSLayoutConstraint!
    
    private var pickerTitle = ""
    fileprivate var dateArray = [String]()
    fileprivate var selectedIndex: Int?
    fileprivate var completion: ((Int) -> ())?
    
    func set(title: String, dataArray: [String], defaultIndex: Int?, completion: ((Int) -> ())?) {
        self.pickerTitle = title
        self.dateArray = dataArray
        self.selectedIndex = defaultIndex
        self.completion = completion
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.contentsTopConstraint.constant = UIScreen.main.bounds.size.height
        
        var height = self.contentsHeaderHeightConstraint.constant + CGFloat(self.dateArray.count) * self.tableView.rowHeight
        if height > UIScreen.main.bounds.size.height - Const.topMargin {
            height = UIScreen.main.bounds.size.height - Const.topMargin
        }
        self.contentsHeightConstraint.constant = height - self.contentsHeaderHeightConstraint.constant
        
        self.pickerTitleLabel.text = self.pickerTitle
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        let topConstraint = UIScreen.main.bounds.size.height - self.contentsHeaderHeightConstraint.constant - self.contentsHeightConstraint.constant
        self.contentsTopConstraint.constant = topConstraint
        
        UIView.animate(withDuration: 0.2) { [weak self] in
            self?.view.layoutIfNeeded()
        }
    }
    
    @IBAction func onTapCancel(_ sender: Any) {
        self.close()
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.close()
    }
    
    fileprivate func close() {
        
        self.contentsTopConstraint.constant = UIScreen.main.bounds.size.height
        UIView.animate(withDuration: 0.2, animations: { [weak self] in
            self?.view.layoutIfNeeded()
        }) { [weak self] _ in
            self?.pop(animationType: .none)
        }
    }
}

extension PickerViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.dateArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PickerTableViewCell", for: indexPath) as! PickerTableViewCell
        cell.configure(title: self.dateArray[indexPath.row], isSelected: indexPath.row == self.selectedIndex)
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        self.selectedIndex = indexPath.row
        tableView.reloadData()
        self.completion?(indexPath.row)
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) { [weak self] in
            self?.close()
        }
    }
}


//
//  MultiPickerViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MultiPickerViewController: UIViewController {

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
    fileprivate var selectedIndexes = [Int]()
    fileprivate var completion: (([Int]) -> ())?
    
    func set(title: String, dataArray: [String], defaultIndexes: [Int], completion: (([Int]) -> ())?) {
        self.pickerTitle = title
        self.dateArray = dataArray
        self.selectedIndexes = defaultIndexes
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
    
    @IBAction func onTapOk(_ sender: Any) {
        self.completion?(self.selectedIndexes)
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.2) { [weak self] in
            self?.close()
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

extension MultiPickerViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.dateArray.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "PickerTableViewCell", for: indexPath) as! PickerTableViewCell
        cell.configure(title: self.dateArray[indexPath.row], isSelected: self.selectedIndexes.contains(indexPath.row))
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        if let index = self.selectedIndexes.index(of: indexPath.row) {
            self.selectedIndexes.remove(at: index)
        } else {
            self.selectedIndexes.append(indexPath.row)
        }
        tableView.reloadData()
    }
}

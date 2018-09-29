//
//  GuideViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideViewController: UIViewController {

    @IBOutlet private weak var backButton: UIButton!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var searchCondition: SearchViewController.SearchCondition?
    private var guideDatas = [GuideData]()

    func set(searchCondition: SearchViewController.SearchCondition) {
        self.searchCondition = searchCondition
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.reload()
        
        NotificationCenter.default.addObserver(forName: .guide, object: nil, queue: nil, using: { [weak self] _ in
            self?.reload()
        })
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        NotificationCenter.default.removeObserver(self, name: .guide, object: nil)
    }
    
    private func reload() {
        
        if let condition = self.searchCondition {
            
            let guides = GuideRequester.shared.dataList.filter { guideData -> Bool in
                
                if !guideData.language.contains(condition.language) {
                    return false
                }
                if let keyword = condition.keyword?.lowercased() {
                    if !guideData.email.lowercased().contains(keyword)
                        && !guideData.name.lowercased().contains(keyword)
                        && !guideData.nationality.lowercased().contains(keyword)
                        && !guideData.category.lowercased().contains(keyword)
                        && !guideData.keyword.lowercased().contains(keyword)
                        && !guideData.notes.lowercased().contains(keyword) {
                        return false
                    }
                }
                if let date = condition.date {
                    if let schedules = (guideData.schedules.filter { $0.date.isSameDay(with: date) }).first {
                        if !schedules.isFreeList.contains(true) {
                            return false
                        }
                    } else {
                        return false
                    }
                }
                if let time = condition.time {
                    let isFreeList = guideData.schedules.map { $0.isFreeList[time] }
                    if isFreeList.contains(true) {
                        return false
                    }
                }
                if !guideData.category.contains(condition.category) {
                    return false
                }
                return true
            }
            
            let sorted = guides.sorted(by: { guide1, guide2 -> Bool in
                switch condition.order {
                case .login:
                    return guide1.loginDate > guide2.loginDate
                case .estimate:
                    let score1 = EstimateRequester.shared.queryAverage(guideId: guide1.id)
                    let score2 = EstimateRequester.shared.queryAverage(guideId: guide2.id)
                    return score1 > score2
                case .number:
                    let number1 = ReserveRequester.shared.dataList.filter { $0.guideId == guide1.id }.count
                    let number2 = ReserveRequester.shared.dataList.filter { $0.guideId == guide2.id }.count
                    return number1 > number2
                }
            })
            self.guideDatas = sorted
            
        } else {
            self.backButton.isHidden = true
            self.guideDatas = GuideRequester.shared.dataList
        }
        
        ImageStorage.shared.removeAll()
        
        self.tableView.reloadData()
        self.noDataLabel.isHidden = !self.guideDatas.isEmpty
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension GuideViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.guideDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "GuideTableViewCell", for: indexPath) as! GuideTableViewCell
        cell.configure(guideData: self.guideDatas[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let detail = self.viewController(storyboard: "Guide", identifier: "GuideDetailViewController") as! GuideDetailViewController
        detail.set(guideData: self.guideDatas[indexPath.row])
        self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
    }
}

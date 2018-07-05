//
//  MyPageViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.rowHeight = UITableViewAutomaticDimension
        self.tableView.estimatedRowHeight = 100
    }

    @IBAction func onTapHistory(_ sender: Any) {
        
    }
    
    @IBAction func onTapSchedule(_ sender: Any) {
        let schedule = self.viewController(storyboard: "MyPage", identifier: "MyPageScheduleViewController") as! MyPageScheduleViewController
        self.stack(viewController: schedule, animationType: .horizontal)
    }
    
    @IBAction func onTapProfile(_ sender: Any) {
        
    }
    
    @IBAction func onTapPayment(_ sender: Any) {
        
    }
    
    @IBAction func onTapReview(_ sender: Any) {
        let review = self.viewController(storyboard: "MyPage", identifier: "CustomerReviewViewController") as! CustomerReviewViewController
        self.stack(viewController: review, animationType: .horizontal)
    }
}

extension MyPageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageReservationTitleTableViewCell", for: indexPath) as! MyPageReservationTitleTableViewCell
            return cell
        } else if indexPath.row == 1 {
            return tableView.dequeueReusableCell(withIdentifier: "MyPageReservationNoneTableViewCell", for: indexPath) as! MyPageReservationNoneTableViewCell
        } else {
            return tableView.dequeueReusableCell(withIdentifier: "MyPageButtonTableViewCell", for: indexPath) as! MyPageButtonTableViewCell
        }
    }
}

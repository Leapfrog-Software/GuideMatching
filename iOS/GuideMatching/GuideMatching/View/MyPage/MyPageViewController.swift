//
//  MyPageViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageViewController: UIViewController {

    @IBAction func onTapCustomerReview(_ sender: Any) {
        
    }
    
    @IBAction func onTapMyAccount(_ sender: Any) {
        
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
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        if indexPath.row == 0 {
            return 50
        } else if indexPath.row == 1 {
            return 100
        } else {
            return 200
        }
    }    
}

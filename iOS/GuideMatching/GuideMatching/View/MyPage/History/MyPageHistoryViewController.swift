//
//  MyPageHistoryViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageHistoryViewController: UIViewController {

    private var histories = [ReserveData]()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let today = Date()
        self.histories = ReserveRequester.shared.dataList.filter { reserveData in
            if reserveData.guideId == SaveData.shared.guideId {
                if let endDate = reserveData.toEndDate() {
                    return endDate < today
                }
            }
            return false
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPageHistoryViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.histories.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageHistoryTableViewCell", for: indexPath) as! MyPageHistoryTableViewCell
        cell.configure(reserveData: self.histories[indexPath.row])
        return cell
    }
}


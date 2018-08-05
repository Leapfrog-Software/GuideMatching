//
//  MyPagePaymentViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPagePaymentViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    
    func reloadTable() {
        self.tableView.reloadData()
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }

}

extension MyPagePaymentViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 3
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        if indexPath.row == 0 {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentTotalTableViewCell", for: indexPath) as! MyPagePaymentTotalTableViewCell
            return cell
        } else if indexPath.row == 1 {
            // TODO
            return tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentNoHistoryTableViewCell", for: indexPath)
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentAccountTableViewCell", for: indexPath) as! MyPagePaymentAccountTableViewCell
            cell.configure(didTapEdit: {
                
            })
            return cell
        }
    }
}

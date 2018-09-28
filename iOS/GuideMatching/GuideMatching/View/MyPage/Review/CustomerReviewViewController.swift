//
//  CustomerReviewViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CustomerReviewViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var estimates = [EstimateData]()
    
    func set(guideId: String) {
        self.estimates = EstimateRequester.shared.dataList.filter { estimate -> Bool in
            return estimate.guideId == guideId
        }
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.isHidden = self.estimates.isEmpty
        self.noDataLabel.isHidden = !self.estimates.isEmpty
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension CustomerReviewViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.estimates.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "CustomerReviewTableViewCell", for: indexPath) as! CustomerReviewTableViewCell
        cell.configure(estimateData: self.estimates[indexPath.row])
        return cell
    }
}

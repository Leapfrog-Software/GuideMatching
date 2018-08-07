//
//  MyPagePaymentHistoryTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPagePaymentHistoryTableViewCell: UITableViewCell {

    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var paymentTitleLabel: UILabel!
    @IBOutlet private weak var amountLabel: UILabel!
    
    func configure(month: Date, amount: Int) {
        
        self.dateLabel.text = DateFormatter(dateFormat: "M/25").string(from: month)
        self.paymentTitleLabel.text = DateFormatter(dateFormat: "M月分").string(from: month)
        self.amountLabel.text = "¥" + CommonUtility.digit3Format(value: amount)
    }
}

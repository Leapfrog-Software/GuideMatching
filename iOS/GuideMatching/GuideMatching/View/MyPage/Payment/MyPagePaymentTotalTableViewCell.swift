//
//  MyPagePaymentTotalTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPagePaymentTotalTableViewCell: UITableViewCell {

    @IBOutlet private weak var amountLabel: UILabel!
    
    func configure(amount: Int) {
        self.amountLabel.text = "¥" + CommonUtility.digit3Format(value: amount)
    }
}

//
//  MyPageReservationTitleTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageReservationTitleTableViewCell: UITableViewCell {

    @IBOutlet private weak var reservationTitleLabel: UILabel!
    
    func configure(title: String) {
        self.reservationTitleLabel.text = title
    }
}

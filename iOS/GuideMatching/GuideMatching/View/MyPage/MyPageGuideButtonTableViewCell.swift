//
//  MyPageGuideButtonTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageGuideButtonTableViewCell: UITableViewCell {
    
    enum ButtonType {
        case profile
        case tour
        case schedule
        case history
        case review
        case payment
    }
    
    private var didTap: ((ButtonType) -> ())?
    
    func configure(didTap: @escaping ((ButtonType) -> ())) {
        self.didTap = didTap
    }

    @IBAction func onTapHistory(_ sender: Any) {
        self.didTap?(.history)
    }
    
    @IBAction func onTapSchedule(_ sender: Any) {
        self.didTap?(.schedule)
    }
    
    @IBAction func onTapProfile(_ sender: Any) {
        self.didTap?(.profile)
    }
    
    @IBAction func onTapTour(_ sender: Any) {
        self.didTap?(.tour)
    }
    
    @IBAction func onTapReview(_ sender: Any) {
        self.didTap?(.review)
    }
    
    @IBAction func onTapPayment(_ sender: Any) {
        self.didTap?(.payment)
    }
}

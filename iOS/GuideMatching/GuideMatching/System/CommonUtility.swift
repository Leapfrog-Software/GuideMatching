//
//  CommonUtility.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CommonUtility {
    
    class func createEstimateImages(_ evaluate: Int) -> [UIImage] {
        
        return [0, 1, 2, 3, 4].compactMap { index -> UIImage? in
            if evaluate < index * 10 + 5 {
                return UIImage(named: "star_empty_14_14")
            } else if evaluate < index * 10 + 10 {
                return UIImage(named: "star_half_14_14")
            } else {
                return UIImage(named: "star_full_14_14")
            }
        }
    }
    
    class func setOnLineState(loginDate: Date, view: UIView, label: UILabel) {
        
        let timeInterval = Date().timeIntervalSince(loginDate)
        if timeInterval > 7 * 24 * 60 * 60 {
            view.backgroundColor = UIColor.loginStateOver1w
            label.text = "over a week"
        } else if timeInterval > 3 * 24 * 60 * 60 {
            view.backgroundColor = UIColor.loginStateWithin1w
            label.text = "within a week"
        } else if timeInterval > 1 * 24 * 60 * 60 {
            view.backgroundColor = UIColor.loginStateWithin3d
            label.text = "within a few days"
        } else if timeInterval > 3 * 60 * 60 {
            view.backgroundColor = UIColor.loginStateWithin24h
            label.text = "within 24 hours"
        } else {
            view.backgroundColor = UIColor.loginStateOnline
            label.text = "online"
        }
    }
    
    class func digit3Format(value: Int) -> String {

        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSize = 3
        formatter.groupingSeparator = ","
        return formatter.string(from: NSNumber(value: value)) ?? ""
    }
}

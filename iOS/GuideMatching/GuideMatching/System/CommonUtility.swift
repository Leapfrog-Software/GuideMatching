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
}

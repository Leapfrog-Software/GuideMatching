//
//  UIColor+Original.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIColor {
    
    class var placeholder: UIColor {
        return UIColor(red: 146 / 255, green: 146 / 255, blue: 146 / 255, alpha: 1)
    }
    
    class var loginStateOnline: UIColor {
        return UIColor(red: 28 / 255, green: 164 / 255, blue: 252 / 255, alpha: 1)
    }
    
    class var loginStateWithin24h: UIColor {
        return UIColor(red: 41 / 255, green: 157 / 255, blue: 29 / 255, alpha: 1)
    }
    
    class var loginStateWithin3d: UIColor {
        return UIColor(red: 253 / 255, green: 147 / 255, blue: 38 / 255, alpha: 1)
    }
    
    class var loginStateWithin1w: UIColor {
        return UIColor(red: 237 / 255, green: 98 / 255, blue: 167 / 255, alpha: 1)
    }
    
    class var loginStateOver1w: UIColor {
        return UIColor(red: 94 / 255, green: 94 / 255, blue: 94 / 255, alpha: 1)
    }
}

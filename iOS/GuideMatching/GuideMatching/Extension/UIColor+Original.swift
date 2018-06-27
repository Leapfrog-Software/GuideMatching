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
        return .blue
    }
    
    class var loginStateWithin24h: UIColor {
        return .green
    }
    
    class var loginStateWithin3d: UIColor {
        return .orange
    }
    
    class var loginStateWithin1w: UIColor {
        return .purple
    }
    
    class var loginStateOver1w: UIColor {
        return .black
    }
}

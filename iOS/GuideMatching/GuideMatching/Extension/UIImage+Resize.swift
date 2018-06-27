//
//  UIImage+Resize.swift
//  GuideMatching
//
//  Created by 藤田 祥一 on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIImage {
    
    func resize(size: CGSize) -> UIImage? {

        UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
        draw(in: CGRect(origin: .zero, size: size))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return resizedImage
    }
    
    func trim(rect: CGRect) -> UIImage? {
        
        UIGraphicsBeginImageContextWithOptions(rect.size, false, 0)
        draw(at: CGPoint(x: -rect.origin.x, y: -rect.origin.y))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return image
    }
}

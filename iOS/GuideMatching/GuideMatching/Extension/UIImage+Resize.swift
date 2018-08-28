//
//  UIImage+Resize.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

extension UIImage {
    
    func toFaceImage() -> UIImage? {
        
        let trimmedImage: UIImage?
        
        if self.size.width < self.size.height {
            trimmedImage = self.trim(rect: CGRect(x: 0,
                                                   y: (self.size.height - self.size.width) / 2,
                                                   width: self.size.width,
                                                   height: self.size.width))
        } else {
            trimmedImage = self.trim(rect: CGRect(x: (self.size.width - self.size.height) / 2,
                                                   y: 0,
                                                   width: self.size.height,
                                                   height: self.size.height))
        }
        return trimmedImage?.resize(size: CGSize(width: 200, height: 200))
    }
    
    func toTourImage() -> UIImage? {
        
        var ratio = CGFloat(1.0)
        
        if self.size.width > self.size.height {
            ratio = 300.0 / self.size.width
        } else {
            ratio = 300.0 / self.size.height
        }
        return self.resize(size: CGSize(width: self.size.width * ratio, height: self.size.height * ratio))
    }
    
    private func resize(size: CGSize) -> UIImage? {

        UIGraphicsBeginImageContextWithOptions(size, false, 0.0)
        draw(in: CGRect(origin: .zero, size: size))
        let resizedImage = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        return resizedImage
    }
    
    private func trim(rect: CGRect) -> UIImage? {
        
        UIGraphicsBeginImageContextWithOptions(rect.size, false, 0)
        draw(at: CGPoint(x: -rect.origin.x, y: -rect.origin.y))
        let image = UIGraphicsGetImageFromCurrentImageContext()
        UIGraphicsEndImageContext()
        
        return image
    }
}

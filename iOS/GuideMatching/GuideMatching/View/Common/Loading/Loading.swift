//
//  Loading.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class Loading: UIView {
    
    @IBOutlet private weak var loadingImageView: UIImageView!
    
    private var timerCount = 0
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        Timer.scheduledTimer(withTimeInterval: 0.1, repeats: true, block: { _ in
            self.timerProc()
        })
    }
    
    private func timerProc() {
        
        self.timerCount += 1
        if self.timerCount >= 8 {
            self.timerCount = 0
        }
        
        let angle = 45 * CGFloat(self.timerCount) * CGFloat.pi / 180
        self.loadingImageView.transform = CGAffineTransform(rotationAngle: angle)
    }
    
    class func start() {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        if !(window.subviews.compactMap { $0 as? Loading }).isEmpty {
            return
        }
        guard let loadingView = UINib(nibName: "Loading", bundle: nil).instantiate(withOwner: self, options: nil).first as? Loading else {
            return
        }
        window.addSubview(loadingView)
        loadingView.frame = CGRect(origin: .zero, size: window.frame.size)
    }
    
    class func stop() {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        let loadingViews = window.subviews.compactMap { $0 as? Loading }
        loadingViews.forEach { $0.removeFromSuperview() }
    }
}


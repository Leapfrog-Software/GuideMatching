//
//  GuideRegisterTourView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideRegisterTourView: UIView {

    @IBOutlet private weak var tourImageView: UIImageView!
    @IBOutlet private weak var tourTitleLabel: UILabel!

    private var tourData: GuideTourData!
    private var didTap: ((GuideTourData) -> ())?
    
    func set(tourData: GuideTourData, didTap: @escaping ((GuideTourData) -> ())) {
        self.tourData = tourData
        self.didTap = didTap
    }
    
    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + self.tourData.id, imageView: self.tourImageView)
        self.tourTitleLabel.text = self.tourData.name
    }
    
    @IBAction func onTapButton(_ sender: Any) {
        self.didTap?(self.tourData)
    }
}

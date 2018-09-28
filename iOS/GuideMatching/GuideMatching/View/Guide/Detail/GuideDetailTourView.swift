//
//  GuideDetailTourView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailTourView: UIView {

    @IBOutlet private weak var tourImageView: UIImageView!
    @IBOutlet private weak var tourImageHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var tourTitleLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    @IBOutlet private weak var feeLabel: UILabel!
    
    private var tourData: GuideTourData!
    private var didTap: ((GuideTourData) -> ())?
    
    func set(tourData: GuideTourData, didTap: @escaping ((GuideTourData) -> ())) {
        self.tourData = tourData
        self.didTap = didTap
    }
    
    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        self.tourTitleLabel.text = self.tourData.name
        ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + self.tourData.id + "-t", imageView: self.tourImageView, defaultImage: UIImage(named: "no_image"), completion: {
            if let image = self.tourImageView.image {
                self.tourImageHeightConstraint.constant = (UIScreen.main.bounds.size.width - 40) * image.size.height / image.size.width
            }
        })
        self.descriptionLabel.text = self.tourData.description
        self.feeLabel.text = CommonUtility.digit3Format(value: self.tourData.fee) + " JPY"
    }
    
    @IBAction func onTapButton(_ sender: Any) {
        self.didTap?(self.tourData)
    }
}

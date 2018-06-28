//
//  GuideTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideTableViewCell: UITableViewCell {
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var estimate1ImageView: UIImageView!
    @IBOutlet private weak var estimate2ImageView: UIImageView!
    @IBOutlet private weak var estimate3ImageView: UIImageView!
    @IBOutlet private weak var estimate4ImageView: UIImageView!
    @IBOutlet private weak var estimate5ImageView: UIImageView!
    @IBOutlet private weak var estimateNumberLabel: UILabel!
    @IBOutlet private weak var specialtyLabel: UILabel!
    @IBOutlet private weak var loginStateView: UIView!
    @IBOutlet private weak var loginStateLabel: UILabel!
    @IBOutlet private weak var feeLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }

    func configure(guideData: GuideData) {
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + guideData.id + "-0", imageView: self.faceImageView, defaultImage: nil)
        
        self.nameLabel.text = guideData.name
        
        let estimateDatas = EstimateRequester.shared.dataList.filter { $0.targetId == guideData.id && $0.isGuide == true }
        var score = 0
        estimateDatas.forEach {
            score += $0.score
        }
        let estimateImages = CommonUtility.createEstimateImages(score)
        self.estimate1ImageView.image = estimateImages[0]
        self.estimate2ImageView.image = estimateImages[1]
        self.estimate3ImageView.image = estimateImages[2]
        self.estimate4ImageView.image = estimateImages[3]
        self.estimate5ImageView.image = estimateImages[4]
        self.estimateNumberLabel.text = "(\(estimateDatas.count))"
        
        self.specialtyLabel.text = guideData.specialty
        
        let timeInterval = Date().timeIntervalSince(guideData.loginDate)
        if timeInterval > 7 * 24 * 60 * 60 {
            self.loginStateView.backgroundColor = UIColor.loginStateOver1w
            self.loginStateLabel.text = "over a week"
        } else if timeInterval > 3 * 24 * 60 * 60 {
            self.loginStateView.backgroundColor = UIColor.loginStateWithin1w
            self.loginStateLabel.text = "within a week"
        } else if timeInterval > 1 * 24 * 60 * 60 {
            self.loginStateView.backgroundColor = UIColor.loginStateWithin3d
            self.loginStateLabel.text = "within a few days"
        } else if timeInterval > 3 * 60 * 60 {
            self.loginStateView.backgroundColor = UIColor.loginStateWithin24h
            self.loginStateLabel.text = "within 24 hours"
        } else {
            self.loginStateView.backgroundColor = UIColor.loginStateOnline
            self.loginStateLabel.text = "online"
        }
        
        self.feeLabel.text = guideData.fee + " JPY/h"
    }
}

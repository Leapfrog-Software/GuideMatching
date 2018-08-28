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
    @IBOutlet private weak var keywordLabel: UILabel!
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
        
        let score = EstimateRequester.shared.queryAverage(guideId: guideData.id)
        let estimateImages = CommonUtility.createEstimateImages(score)
        self.estimate1ImageView.image = estimateImages[0]
        self.estimate2ImageView.image = estimateImages[1]
        self.estimate3ImageView.image = estimateImages[2]
        self.estimate4ImageView.image = estimateImages[3]
        self.estimate5ImageView.image = estimateImages[4]
        let estimates = EstimateRequester.shared.query(guideId: guideData.id)
        self.estimateNumberLabel.text = "(\(estimates.count))"
        
        self.keywordLabel.text = guideData.keyword
        
        CommonUtility.setOnLineState(loginDate: guideData.loginDate, view: self.loginStateView, label: self.loginStateLabel)
        
        self.feeLabel.text = CommonUtility.digit3Format(value: guideData.fee) + " JPY/30min"
    }
}

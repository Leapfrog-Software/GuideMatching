//
//  CustomerReviewTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CustomerReviewTableViewCell: UITableViewCell {

    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var scoreLabel: UILabel!
    @IBOutlet private weak var estimate1ImageView: UIImageView!
    @IBOutlet private weak var estimate2ImageView: UIImageView!
    @IBOutlet private weak var estimate3ImageView: UIImageView!
    @IBOutlet private weak var estimate4ImageView: UIImageView!
    @IBOutlet private weak var estimate5ImageView: UIImageView!
    @IBOutlet private weak var commentLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(estimateData: EstimateData) {
        
        guard let guestData = GuestRequester.shared.query(id: estimateData.guestId) else {
            self.faceImageView.image = nil
            self.nameLabel.text = ""
            self.scoreLabel.text = ""
            self.estimate1ImageView.image = nil
            self.estimate2ImageView.image = nil
            self.estimate3ImageView.image = nil
            self.estimate4ImageView.image = nil
            self.estimate5ImageView.image = nil
            self.commentLabel.text = ""
            return
        }
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + estimateData.guestId + "-0", imageView: self.faceImageView)
        
        self.nameLabel.text = guestData.name
        self.scoreLabel.text = "\(estimateData.score)"
        
        let estimateImages = CommonUtility.createEstimateImages(estimateData.score)
        self.estimate1ImageView.image = estimateImages[0]
        self.estimate2ImageView.image = estimateImages[1]
        self.estimate3ImageView.image = estimateImages[2]
        self.estimate4ImageView.image = estimateImages[3]
        self.estimate5ImageView.image = estimateImages[4]
        
        self.commentLabel.text = estimateData.comment
    }
}

//
//  MessageTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageTableViewCell: UITableViewCell {
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var loginLabel: UILabel!

    func configure(guideData: GuideData) {
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + guideData.id + "-0", imageView: self.faceImageView)
        
        self.nameLabel.text = guideData.name
        self.messageLabel.text = guideData.message
        self.loginLabel.text = DateFormatter(dateFormat: "MM/dd").string(from: guideData.loginDate)
    }
}

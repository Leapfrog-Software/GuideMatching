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
    @IBOutlet private weak var dateLabel: UILabel!

    func configure(guideId: String, guideName: String, message: String, latestDate: Date) {
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + guideId + "-0", imageView: self.faceImageView)
        
        self.nameLabel.text = guideName
        self.messageLabel.text = message
        self.dateLabel.text = DateFormatter(dateFormat: "MM/dd").string(from: latestDate)
    }
    
    func configure(guestId: String, guestName: String, message: String, latestDate: Date) {
        
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + guestId + "-0", imageView: self.faceImageView)
        
        self.nameLabel.text = guestName
        self.messageLabel.text = message
        self.dateLabel.text = DateFormatter(dateFormat: "MM/dd").string(from: latestDate)
    }
}

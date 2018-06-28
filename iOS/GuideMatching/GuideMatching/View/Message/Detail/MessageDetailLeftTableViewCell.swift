//
//  MessageDetailLeftTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageDetailLeftTableViewCell: UITableViewCell {

    struct Const {
        static let bottomMargin = CGFloat(26)
    }
    
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var baloonView: UIView!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    
    private var messageId = ""
    private var didTap: (() -> ())?
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        self.didTap = nil
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(data: MessageData, didTap: (() -> ())? = nil) {
        
        let url: String
        if data.senderId.contains("guide_") {
            url = Constants.ServerGuideImageRootUrl + data.senderId + "-0"
        } else {
            url = Constants.ServerGuestImageRootUrl + data.senderId + "-0"
        }
        
        ImageStorage.shared.fetch(url: url, imageView: self.faceImageView)
        self.messageLabel.text = data.message
        
        let today = Date()
        if data.datetime.isSameDay(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "HH:mm").string(from: data.datetime)
        } else if data.datetime.isSameYear(with: today) {
            self.dateLabel.text = DateFormatter(dateFormat: "M月d日 HH:mm").string(from: data.datetime)
        } else {
            self.dateLabel.text = DateFormatter(dateFormat: "yyyy年M月d日 HH:mm").string(from: data.datetime)
        }
        
        self.messageId = data.messageId
        self.didTap = didTap
    }
    
    func getMessageId() -> String {
        return self.messageId
    }
    
    func height(data: MessageData) -> CGFloat {
        
        self.configure(data: data)
        
        self.setNeedsLayout()
        self.layoutIfNeeded()
        
        return self.baloonView.frame.origin.y + self.baloonView.frame.size.height + Const.bottomMargin
    }
    
    @IBAction func onTapFace(_ sender: Any) {
        self.didTap?()
    }

}

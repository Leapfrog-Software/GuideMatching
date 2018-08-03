//
//  MyPageHistoryTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageHistoryTableViewCell: UITableViewCell {

    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var dateLabel: UILabel!
    @IBOutlet private weak var timeLabel: UILabel!
    @IBOutlet private weak var meetingPlaceLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    
    override func prepareForReuse() {
        super.prepareForReuse()
        
        ImageStorage.shared.cancelRequest(imageView: self.faceImageView)
    }
    
    func configure(reserveData: ReserveData) {
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + reserveData.guideId + "-0", imageView: self.faceImageView)
        
        if let guideData = GuideRequester.shared.query(id: reserveData.guideId) {
            self.nameLabel.text = guideData.name
            
            let guideFee = guideData.fee * (reserveData.endTime - reserveData.startTime)
            let transactionFee = CommonUtility.calculateTransactionFee(of: guideFee)
            self.priceLabel.text = CommonUtility.digit3Format(value: guideFee + transactionFee)
        } else {
            self.nameLabel.text = ""
            self.priceLabel.text = ""
        }
        
        self.dateLabel.text = reserveData.day.toDayMonthYearText()
        self.timeLabel.text = CommonUtility.timeOffsetToString(offset: reserveData.startTime) + " - " + CommonUtility.timeOffsetToString(offset: reserveData.endTime)
        self.meetingPlaceLabel.text = reserveData.meetingPlace
    }
}

//
//  ReserveDetailViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/07.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ReserveDetailViewController: UIViewController {

    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var score1ImageView: UIImageView!
    @IBOutlet private weak var score2ImageView: UIImageView!
    @IBOutlet private weak var score3ImageView: UIImageView!
    @IBOutlet private weak var score4ImageView: UIImageView!
    @IBOutlet private weak var score5ImageView: UIImageView!
    @IBOutlet private weak var reviewLabel: UILabel!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var onlineStateView: UIView!
    @IBOutlet private weak var onlineStateLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var timeStartLabel: UILabel!
    @IBOutlet private weak var timeEndLabel: UILabel!
    @IBOutlet private weak var placeLabel: UILabel!
    @IBOutlet private weak var guideFeeLabel: UILabel!
    @IBOutlet private weak var transactionFeeLabel: UILabel!
    @IBOutlet private weak var totalFeeLabel: UILabel!
    @IBOutlet private weak var notesLabel: UILabel!
    
    private var reserveData: ReserveData!
    
    func set(reserveData: ReserveData) {
        self.reserveData = reserveData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        guard let guideData = GuideRequester.shared.query(id: self.reserveData.guideId) else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + guideData.id + "-0", imageView: self.faceImageView)
        self.nameLabel.text = guideData.name
        
        let estimateDatas = EstimateRequester.shared.dataList.filter { $0.guideId == guideData.id }
        var score = 0
        estimateDatas.forEach {
            score += $0.score
        }
        
        let scoreImages = CommonUtility.createEstimateImages(score)
        self.score1ImageView.image = scoreImages[0]
        self.score2ImageView.image = scoreImages[1]
        self.score3ImageView.image = scoreImages[2]
        self.score4ImageView.image = scoreImages[3]
        self.score5ImageView.image = scoreImages[4]
        self.reviewLabel.text = "(\(estimateDatas.count))"
        
        CommonUtility.setOnLineState(loginDate: guideData.loginDate, view: self.onlineStateView, label: self.onlineStateLabel)
        self.priceLabel.text = CommonUtility.digit3Format(value: guideData.fee)
        self.dayLabel.text = self.reserveData.day.toDayMonthYearText()

        self.timeStartLabel.text = CommonUtility.timeOffsetToString(offset: self.reserveData.startTime)
        self.timeEndLabel.text = CommonUtility.timeOffsetToString(offset: self.reserveData.endTime)
        self.placeLabel.text = self.reserveData.meetingPlace
            
        let fee = guideData.fee * (self.reserveData.endTime - self.reserveData.startTime)
        self.guideFeeLabel.text = CommonUtility.digit3Format(value: fee) + " JPY/30min"
        let transactionFee = CommonUtility.calculateTransactionFee(of: fee)
        self.transactionFeeLabel.text = CommonUtility.digit3Format(value: transactionFee) + "JPY/30min"
        self.totalFeeLabel.text = CommonUtility.digit3Format(value: fee + transactionFee) + " JPY"
            
        self.notesLabel.text = guideData.notes
    }
    
    @IBAction func onTapFace(_ sender: Any) {
        if let guideData = GuideRequester.shared.query(id: self.reserveData.guideId) {
            let detail = self.viewController(storyboard: "Guide", identifier: "GuideDetailViewController") as! GuideDetailViewController
            detail.set(guideData: guideData)
            self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

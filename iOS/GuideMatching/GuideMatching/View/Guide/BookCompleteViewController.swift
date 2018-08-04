//
//  BookCompleteViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/24.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class BookCompleteViewController: UIViewController {

    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var startTimeLabel: UILabel!
    @IBOutlet private weak var endTimeLabel: UILabel!
    @IBOutlet private weak var placeViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var placeTitleLabel: UILabel!
    @IBOutlet private weak var placeLabel: UILabel!
    @IBOutlet private weak var guideFeeLabel: UILabel!
    @IBOutlet private weak var transactionFeeLabel: UILabel!
    @IBOutlet private weak var totalFeeLabel: UILabel!
    
    private var guideData: GuideData!
    private var date: Date!
    private var startTimeIndex: Int!
    private var endTimeIndex: Int!
    private var meetingPlace: String!
    
    func set(guideData: GuideData, date: Date, startTimeIndex: Int, endTimeIndex: Int, meetingPlace: String) {
        self.guideData = guideData
        self.date = date
        self.startTimeIndex = startTimeIndex
        self.endTimeIndex = endTimeIndex
        self.meetingPlace = meetingPlace
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.adjustLayout()
    }
    
    private func initContents() {
        
        self.nameLabel.text = self.guideData.name
        self.dayLabel.text = self.date.toDayMonthYearText()
        self.startTimeLabel.text = CommonUtility.timeOffsetToString(offset: self.startTimeIndex)
        self.endTimeLabel.text = CommonUtility.timeOffsetToString(offset: self.endTimeIndex)
        self.placeLabel.text = self.meetingPlace
        let guideFee = (self.endTimeIndex - self.startTimeIndex) * self.guideData.fee
        self.guideFeeLabel.text = CommonUtility.digit3Format(value: guideFee) + " JPY"
        let transactionFee = CommonUtility.calculateTransactionFee(of: guideFee)
        self.transactionFeeLabel.text = CommonUtility.digit3Format(value: transactionFee) + " JPY"
        self.totalFeeLabel.text = CommonUtility.digit3Format(value: guideFee + transactionFee) + " JPY"
    }
    
    private func adjustLayout() {
        
        let placeTitleHeight = self.placeTitleLabel.frame.size.height
        let placeHeight = self.placeLabel.frame.size.height
        self.placeViewHeightConstraint.constant = ((placeTitleHeight > placeHeight) ? placeTitleHeight : placeHeight) + 28
    }
    
    @IBAction func onTapOk(_ sender: Any) {
        
        if let tabbar = self.tabbarViewController() {
            Loading.start()
            
            tabbar.refreshData(completion: {
                Loading.stop()
                
                if let guideDetail = (tabbar.childViewControllers.compactMap { $0 as? GuideDetailViewController }).first {
                    guideDetail.pop(animationType: .horizontal)
                }
            })
        }
    }
}

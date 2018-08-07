//
//  MyPageHistoryDetailViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/07.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageHistoryDetailViewController: KeyboardRespondableViewController {

    @IBOutlet private weak var headerTitleLabel: UILabel!
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var timeLabel: UILabel!
    @IBOutlet private weak var placeLabel: UILabel!
    @IBOutlet private weak var feeLabel: UILabel!
    @IBOutlet private weak var commentTextView: UITextView!
    @IBOutlet private weak var scrollViewBottomConstraint: NSLayoutConstraint!
    
    private var reserveData: ReserveData!
    
    func set(reserveData: ReserveData) {
        self.reserveData = reserveData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        if let guestData = GuestRequester.shared.query(id: self.reserveData.guestId) {
            self.headerTitleLabel.text = guestData.name
        }
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + self.reserveData.guestId + "-0", imageView: self.faceImageView)
        
        self.dayLabel.text = self.reserveData.day.toDayMonthYearText()
        self.timeLabel.text = CommonUtility.timeOffsetToString(offset: self.reserveData.startTime) + " - " + CommonUtility.timeOffsetToString(offset: self.reserveData.endTime)
        self.placeLabel.text = self.reserveData.meetingPlace
        
        let fee = GuideRequester.shared.query(id: self.reserveData.guideId)?.fee ?? 0
        let amount = fee * (self.reserveData.endTime - self.reserveData.startTime)
        self.feeLabel.text = "¥" + CommonUtility.digit3Format(value: amount)
        
        self.commentTextView.text = self.reserveData.guideComment
    }
    
    override func animate(with: KeyboardAnimation) {
        
        self.scrollViewBottomConstraint.constant = with.height
        
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: {
            self.view.layoutIfNeeded()
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
        
        let comment = self.commentTextView.text ?? ""
        if comment != self.reserveData.guideComment {
            ReserveRequester.comment(reserveId: self.reserveData.id, comment: comment)
        }
    }
}

extension MyPageHistoryDetailViewController: UIScrollViewDelegate {
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
}

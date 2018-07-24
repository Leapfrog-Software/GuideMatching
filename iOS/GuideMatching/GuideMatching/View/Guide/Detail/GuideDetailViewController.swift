//
//  GuideDetailViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailViewController: UIViewController {
    
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var imageScrollView: UIScrollView!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var specialtyLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var timeZoneLabel: UILabel!
    @IBOutlet private weak var applicableNumberLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var notesLabel: UILabel!
    @IBOutlet private weak var scoreLabel: UILabel!
    @IBOutlet private weak var estimateNumberLabel: UILabel!
    @IBOutlet private weak var scheduleBaseView: UIView!
    
    private var guideData: GuideData!
    
    func set(guideData: GuideData) {
        self.guideData = guideData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.nameLabel.text = self.guideData.name
        
        let windowWidth = UIApplication.shared.keyWindow?.frame.size.width ?? 0
        for i in 0..<3 {
            let imageView = UIImageView()
            imageView.frame = CGRect(x: CGFloat(i) * windowWidth, y: 0,
                                     width: windowWidth, height: windowWidth)
            self.imageScrollView.addSubview(imageView)
            ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + self.guideData.id + "-\(i)", imageView: imageView)
        }
        self.imageScrollView.contentSize = CGSize(width: windowWidth, height: windowWidth)
        
        self.languageLabel.text = self.guideData.language
        self.categoryLabel.text = self.guideData.category
        self.specialtyLabel.text = self.guideData.specialty
        self.messageLabel.text = self.guideData.message
        self.timeZoneLabel.text = self.guideData.timeZone
        self.applicableNumberLabel.text = "\(self.guideData.applicableNumber)人"
        self.priceLabel.text = CommonUtility.digit3Format(value: self.guideData.fee)
        self.notesLabel.text = self.guideData.notes
        
        let estimateDatas = EstimateRequester.shared.dataList.filter { $0.targetId == guideData.id && $0.isGuide == true }
        var score = 0
        estimateDatas.forEach {
            score += $0.score
        }
        self.scoreLabel.text = "\(score)"
        self.estimateNumberLabel.text = "(\(estimateDatas.count))"
        
        if let scheduleView = UINib(nibName: "GuideDetailScheduleView", bundle: nil).instantiate(withOwner: nil, options: nil).first as? GuideDetailScheduleView {
            scheduleView.set(schedules: self.guideData.schedules, didSelect: { [weak self] targetDate, timeOffset in
                self?.didSelectSchedule(targetDate: targetDate, timeOffset: timeOffset)
            })
            self.scheduleBaseView.addSubview(scheduleView)
            scheduleView.translatesAutoresizingMaskIntoConstraints = false
            scheduleView.topAnchor.constraint(equalTo: self.scheduleBaseView.topAnchor).isActive = true
            scheduleView.leadingAnchor.constraint(equalTo: self.scheduleBaseView.leadingAnchor).isActive = true
            scheduleView.trailingAnchor.constraint(equalTo: self.scheduleBaseView.trailingAnchor).isActive = true
            scheduleView.bottomAnchor.constraint(equalTo: self.scheduleBaseView.bottomAnchor).isActive = true
        }
    }
    
    private func didSelectSchedule(targetDate: Date, timeOffset: Int) {
        
        if let schedule = (self.guideData.schedules.filter { $0.date.isSameDay(with: targetDate) }).first {
            if schedule.isFreeList[timeOffset] {
                if SaveData.shared.guestId.count == 0 {
                    Dialog.show(style: .error, title: "エラー", message: "ガイドは予約できません", actions: [DialogAction(title: "OK", action: nil)])
                    return
                }
                let book = self.viewController(storyboard: "Guide", identifier: "BookViewController") as! BookViewController
                book.set(guideData: self.guideData, targetDate: targetDate, startTimeIndex: timeOffset)
                self.stack(viewController: book, animationType: .horizontal)
            }
        }
    }
    
    @IBAction func onTapImageLeft(_ sender: Any) {
        let page = Int(self.imageScrollView.contentOffset.x / self.imageScrollView.frame.size.width)
        if page >= 1 {
            self.imageScrollView.setContentOffset(CGPoint(x: CGFloat(page - 1) * self.imageScrollView.frame.size.width, y: 0), animated: true)
        }
    }
    
    @IBAction func onTapImageRight(_ sender: Any) {
        let page = Int(self.imageScrollView.contentOffset.x / self.imageScrollView.frame.size.width)
        if page <= 1 {
            self.imageScrollView.setContentOffset(CGPoint(x: CGFloat(page + 1) * self.imageScrollView.frame.size.width, y: 0), animated: true)
        }
    }

    @IBAction func onTapInquiry(_ sender: Any) {
        let messageDetail = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        messageDetail.set(targetUserId: self.guideData.id)
        self.stack(viewController: messageDetail, animationType: .horizontal)
    }
    
    @IBAction func onTapNextMonth1(_ sender: Any) {
        if let scheduleView = (self.scheduleBaseView.subviews.compactMap { $0 as? GuideDetailScheduleView }).first {
            scheduleView.changeToNext()
        }
    }
    
    @IBAction func onTapPreviousMonth1(_ sender: Any) {
        if let scheduleView = (self.scheduleBaseView.subviews.compactMap { $0 as? GuideDetailScheduleView }).first {
            scheduleView.changeToPrevious()
        }
    }
    
    @IBAction func onTapNextMonth2(_ sender: Any) {
        if let scheduleView = (self.scheduleBaseView.subviews.compactMap { $0 as? GuideDetailScheduleView }).first {
            scheduleView.changeToNext()
        }
    }
    
    @IBAction func onTapPreviousMonth2(_ sender: Any) {
        if let scheduleView = (self.scheduleBaseView.subviews.compactMap { $0 as? GuideDetailScheduleView }).first {
            scheduleView.changeToPrevious()
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

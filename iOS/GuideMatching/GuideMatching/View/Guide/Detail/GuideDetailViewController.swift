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
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var keywordLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var applicableNumberLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var notesLabel: UILabel!
    @IBOutlet private weak var scoreLabel: UILabel!
    @IBOutlet private weak var estimateNumberLabel: UILabel!
    @IBOutlet private weak var starBarView: UIView!
    @IBOutlet private weak var star5BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star4BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star3BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star2BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star1BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star0BarWidthConstraint: NSLayoutConstraint!
    @IBOutlet private weak var star5RateLabel: UILabel!
    @IBOutlet private weak var star4RateLabel: UILabel!
    @IBOutlet private weak var star3RateLabel: UILabel!
    @IBOutlet private weak var star2RateLabel: UILabel!
    @IBOutlet private weak var star1RateLabel: UILabel!
    @IBOutlet private weak var star0RateLabel: UILabel!
    @IBOutlet private weak var tourStackView: UIStackView!
    @IBOutlet private weak var scheduleBaseView: UIView!
    
    private var guideData: GuideData!
    
    func set(guideData: GuideData) {
        self.guideData = guideData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.initScore()
    }
    
    private func initContents() {
        
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
        self.areaLabel.text = self.guideData.area
        self.categoryLabel.text = self.guideData.category
        self.keywordLabel.text = self.guideData.keyword
        self.messageLabel.text = self.guideData.message
        self.applicableNumberLabel.text = "\(self.guideData.applicableNumber) person"
        self.priceLabel.text = CommonUtility.digit3Format(value: self.guideData.fee) + " JPY/30min"
        self.notesLabel.text = self.guideData.notes
        
        self.guideData.tours.forEach { tourData in
            let tourView = UINib(nibName: "GuideDetailTourView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! GuideDetailTourView
            tourView.set(tourData: tourData, didTap: { [weak self] tourData in
                self?.didTapTour(tourData: tourData)
            })
            self.tourStackView.addArrangedSubview(tourView)
        }
        
        if let scheduleView = UINib(nibName: "GuideDetailScheduleView", bundle: nil).instantiate(withOwner: nil, options: nil).first as? GuideDetailScheduleView {
            scheduleView.set(guideId: self.guideData.id, schedules: self.guideData.schedules, didSelect: { [weak self] targetDate, timeOffset in
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
    
    private func initScore() {
        
        let score = Double(EstimateRequester.shared.queryAverage(guideId: guideData.id))
        self.scoreLabel.text = String(format: "%.1f", score / 10)
        let estimates = EstimateRequester.shared.query(guideId: guideData.id)
        
        let totalCnt = estimates.count
        self.estimateNumberLabel.text = "(\(estimates.count))"
        
        var score0Rate = 0
        var score1Rate = 0
        var score2Rate = 0
        var score3Rate = 0
        var score4Rate = 0
        var score5Rate = 0
        if totalCnt > 0 {
            score0Rate = estimates.filter { $0.score < 10 }.count * 100 / totalCnt
            score1Rate = estimates.filter { $0.score >= 10 && $0.score < 20 }.count * 100 / totalCnt
            score2Rate = estimates.filter { $0.score >= 20 && $0.score < 30 }.count * 100 / totalCnt
            score3Rate = estimates.filter { $0.score >= 30 && $0.score < 40 }.count * 100 / totalCnt
            score4Rate = estimates.filter { $0.score >= 40 && $0.score < 50 }.count * 100 / totalCnt
            score5Rate = estimates.filter { $0.score >= 50 }.count * 100 / totalCnt
            
            var maxRate = score0Rate
            var maxIndex = 0
            if score1Rate > maxRate {
                maxRate = score1Rate
                maxIndex = 1
            }
            if score2Rate > maxRate {
                maxRate = score2Rate
                maxIndex = 2
            }
            if score3Rate > maxRate {
                maxRate = score3Rate
                maxIndex = 3
            }
            if score4Rate > maxRate {
                maxRate = score4Rate
                maxIndex = 4
            }
            if score5Rate > maxRate {
                maxRate = score5Rate
                maxIndex = 5
            }
            if maxIndex == 0 {
                score0Rate = 100 - score1Rate - score2Rate - score3Rate - score4Rate - score5Rate
            } else if maxIndex == 1 {
                score1Rate = 100 - score0Rate - score2Rate - score3Rate - score4Rate - score5Rate
            } else if maxIndex == 2 {
                score2Rate = 100 - score0Rate - score1Rate - score3Rate - score4Rate - score5Rate
            } else if maxIndex == 3 {
                score3Rate = 100 - score0Rate - score1Rate - score2Rate - score4Rate - score5Rate
            } else if maxIndex == 4 {
                score4Rate = 100 - score0Rate - score1Rate - score2Rate - score3Rate - score5Rate
            } else if maxIndex == 5 {
                score5Rate = 100 - score0Rate - score1Rate - score2Rate - score3Rate - score4Rate
            }
        }

        let barWidth = self.starBarView.frame.size.width
        self.star0BarWidthConstraint.constant = (barWidth * CGFloat(score0Rate) / 100) - 2
        self.star1BarWidthConstraint.constant = (barWidth * CGFloat(score1Rate) / 100) - 2
        self.star2BarWidthConstraint.constant = (barWidth * CGFloat(score2Rate) / 100) - 2
        self.star3BarWidthConstraint.constant = (barWidth * CGFloat(score3Rate) / 100) - 2
        self.star4BarWidthConstraint.constant = (barWidth * CGFloat(score4Rate) / 100) - 2
        self.star5BarWidthConstraint.constant = (barWidth * CGFloat(score5Rate) / 100) - 2
        
        self.star0RateLabel.text = "\(score0Rate)%"
        self.star1RateLabel.text = "\(score1Rate)%"
        self.star2RateLabel.text = "\(score2Rate)%"
        self.star3RateLabel.text = "\(score3Rate)%"
        self.star4RateLabel.text = "\(score4Rate)%"
        self.star5RateLabel.text = "\(score5Rate)%"
    }
    
    private func didTapTour(tourData: GuideTourData) {
        let tour = self.viewController(storyboard: "Guide", identifier: "GuideDetailTourViewController") as! GuideDetailTourViewController
        tour.set(guideId: self.guideData.id, tourData: tourData)
        self.stack(viewController: tour, animationType: .horizontal)
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
    
    @IBAction func onTapReview(_ sender: Any) {
        let review = self.viewController(storyboard: "MyPage", identifier: "CustomerReviewViewController") as! CustomerReviewViewController
        review.set(guideId: self.guideData.id)
        self.tabbarViewController()?.stack(viewController: review, animationType: .horizontal)
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
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

//
//  GuideDetailViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailViewController: UIViewController {
    
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
    
    private var guideData: GuideData!
    
    func set(guideData: GuideData) {
        self.guideData = guideData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
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
        self.priceLabel.text = self.guideData.fee + " JPY/円"
        self.notesLabel.text = self.guideData.notes
        
        let estimateDatas = EstimateRequester.shared.dataList.filter { $0.targetId == guideData.id && $0.isGuide == true }
        var score = 0
        estimateDatas.forEach {
            score += $0.score
        }
        self.scoreLabel.text = "\(score)"
        self.estimateNumberLabel.text = "(\(estimateDatas.count))"
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
        messageDetail.set(guideData: self.guideData)
        self.stack(viewController: messageDetail, animationType: .horizontal)
    }
    
    @IBAction func onTapBook(_ sender: Any) {
        let book = self.viewController(storyboard: "Guide", identifier: "BookViewController") as! BookViewController
        self.stack(viewController: book, animationType: .horizontal)
    }
        
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

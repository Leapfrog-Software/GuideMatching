//
//  EstimateViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class EstimateViewController: UIViewController {

    @IBOutlet private weak var starsView: UIView!
    @IBOutlet private weak var estimate1ImageView: UIImageView!
    @IBOutlet private weak var estimate2ImageView: UIImageView!
    @IBOutlet private weak var estimate3ImageView: UIImageView!
    @IBOutlet private weak var estimate4ImageView: UIImageView!
    @IBOutlet private weak var estimate5ImageView: UIImageView!
    @IBOutlet private weak var commentTextView: UITextView!
    
    private var reserveData: ReserveData!
    private var isTouching = false
    private var currentScore = 30
    
    func set(reserveData: ReserveData) {
        self.reserveData = reserveData
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        
        guard let location = touches.first?.location(in: self.starsView) else {
            return
        }
        if CGRect(origin: .zero, size: self.starsView.frame.size).contains(location) {
            self.isTouching = true
            self.changeValue(location: location)
        }
    }
    
    override func touchesMoved(_ touches: Set<UITouch>, with event: UIEvent?) {
        if isTouching {
            guard let location = touches.first?.location(in: self.starsView) else {
                return
            }
            self.changeValue(location: location)
        }
    }
    
    override func touchesCancelled(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.isTouching = false
    }
    
    override func touchesEnded(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.isTouching = false
    }
    
    private func changeValue(location: CGPoint) {
        
        var score = 0
        let viewWidth = self.starsView.frame.size.width
        for i in 0..<10 {
            if location.x < viewWidth * (2 * CGFloat(i) + 1) / 20 {
                score = i * 5
                break
            } else if i == 9 {
                score = 50
            }
        }
        
        self.currentScore = score
        
        let estimateImages = CommonUtility.createEstimateImages(score)
        self.estimate1ImageView.image = estimateImages[0]
        self.estimate2ImageView.image = estimateImages[1]
        self.estimate3ImageView.image = estimateImages[2]
        self.estimate4ImageView.image = estimateImages[3]
        self.estimate5ImageView.image = estimateImages[4]
    }

    @IBAction func onTapSend(_ sender: Any) {
        
        let score = self.currentScore
        let comment = self.commentTextView.text ?? ""

        Loading.start()
        
        EstimateRequester.post(reserveId: self.reserveData.id, guestId: SaveData.shared.guestId, guideId: self.reserveData.guideId, score: score, comment: comment, completion: { result in
            Loading.stop()
            
            if result {
                Dialog.show(style: .success, title: "確認", message: "送信しました", actions: [DialogAction(title: "OK", action: nil)])
            } else {
                Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

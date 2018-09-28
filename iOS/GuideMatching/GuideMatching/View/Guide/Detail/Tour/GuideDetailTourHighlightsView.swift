//
//  GuideDetailTourHighlightsView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailTourHighlightsView: UIView {

    @IBOutlet private weak var highlightsTitleLabel: UILabel!
    @IBOutlet private weak var highlightsImageView: UIImageView!
    @IBOutlet private weak var bodyLabel: UILabel!

    private var titleString: String!
    private var imageUrl: String!
    private var bodyString: String!
    
    func set(title: String, imageUrl: String, bodyString: String) {
        self.titleString = title
        self.imageUrl = imageUrl
        self.bodyString = bodyString
    }
    
    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        self.highlightsTitleLabel.text = self.titleString
        ImageStorage.shared.fetch(url: self.imageUrl, imageView: self.highlightsImageView, defaultImage: UIImage(named: "no_image"))
        self.bodyLabel.text = self.bodyString
    }
}

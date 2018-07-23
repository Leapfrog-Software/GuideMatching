//
//  DialogButton.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class DialogButton: UIView {
    
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var button: UIButton!
    
    private var didTap: (() -> ())?
    
    func configure(title: String, color: UIColor, didTap: @escaping (() -> ())) {
        self.titleLabel.text = title
        self.button.backgroundColor = color
        self.didTap = didTap
    }
    
    @IBAction func onTap(_ sender: Any) {
        self.didTap?()
    }
}

//
//  MyPageGuestButtonTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/03.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageGuestButtonTableViewCell: UITableViewCell {
    
    private var didTap: (() -> ())?

    func configure(didTap: @escaping (() -> ())) {
        self.didTap = didTap
    }
    
    @IBAction func onTapMyAccount(_ sender: Any) {
        self.didTap?()
    }
}

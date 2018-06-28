//
//  BookViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class BookViewController: UIViewController {

    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }

}

//
//  SearchViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SearchViewController: UIViewController {

    @IBAction func onTapSearch(_ sender: Any) {
        let guideList = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        guideList.set(searchResult: [])
        self.tabbarViewController()?.stack(viewController: guideList, animationType: .horizontal)
    }

    @IBAction func onTapReset(_ sender: Any) {
        
    }
}

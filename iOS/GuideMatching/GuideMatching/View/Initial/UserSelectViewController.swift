//
//  UserSelectViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class UserSelectViewController: UIViewController {

    @IBAction func onTapGuest(_ sender: Any) {
        let guestRegisterViewController = self.viewController(storyboard: "Initial", identifier: "GuestRegisterViewController") as! GuestRegisterViewController
        self.stack(viewController: guestRegisterViewController, animationType: .horizontal)
    }
    
    @IBAction func onTapGuide(_ sender: Any) {
        let guideRegisterViewController = self.viewController(storyboard: "Initial", identifier: "GuideRegisterViewController") as! GuideRegisterViewController
        self.stack(viewController: guideRegisterViewController, animationType: .horizontal)
    }
}

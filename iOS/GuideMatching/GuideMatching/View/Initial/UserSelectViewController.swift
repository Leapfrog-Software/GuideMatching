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
        self.stack(viewController: guestRegisterViewController, animationType: .vertical)
    }
    
    @IBAction func onTapGuide(_ sender: Any) {
        let guideRegisterViewController = self.viewController(storyboard: "Initial", identifier: "GuideRegisterViewController") as! GuideRegisterViewController
        self.stack(viewController: guideRegisterViewController, animationType: .vertical)
    }
    
    @IBAction func onTapTerms(_ sender: Any) {
        if let path = Bundle.main.path(forResource: "terms", ofType: "pdf") {
            let webView = self.viewController(storyboard: "Common", identifier: "WebViewController") as! WebViewController
            webView.set(title: "our terms of services", path: path)
            self.stack(viewController: webView, animationType: .horizontal)
        }
    }
    
    @IBAction func onTapPrivacyPolicy(_ sender: Any) {
        if let path = Bundle.main.path(forResource: "privacypolicy", ofType: "pdf") {
            let webView = self.viewController(storyboard: "Common", identifier: "WebViewController") as! WebViewController
            webView.set(title: "privacypolicy", path: path)
            self.stack(viewController: webView, animationType: .horizontal)
        }
    }
}

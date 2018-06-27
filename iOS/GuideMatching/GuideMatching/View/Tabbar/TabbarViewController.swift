//
//  TabbarViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class TabbarViewController: UIViewController {

    @IBOutlet private weak var containerView: UIView!
    @IBOutlet private weak var tab1ImageView: UIImageView!
    @IBOutlet private weak var tab2ImageView: UIImageView!
    @IBOutlet private weak var tab3ImageView: UIImageView!
    @IBOutlet private weak var tab4ImageView: UIImageView!
    
    private var guideViewController: GuideViewController!
    private var searchViewController: SearchViewController!
    private var messageViewController: MessageViewController!
    private var myPageViewController: MyPageViewController!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        self.guideViewController = self.viewController(storyboard: "Guide", identifier: "GuideViewController") as! GuideViewController
        self.addContents(self.guideViewController)
        self.searchViewController = self.viewController(storyboard: "Search", identifier: "SearchViewController") as! SearchViewController
        self.addContents(self.searchViewController)
        self.messageViewController = self.viewController(storyboard: "Message", identifier: "MessageViewController") as! MessageViewController
        self.addContents(self.messageViewController)
        self.myPageViewController = self.viewController(storyboard: "MyPage", identifier: "MyPageViewController") as! MyPageViewController
        self.addContents(self.myPageViewController)
        
        self.changeContents(index: 0)
    }
    
    private func addContents(_ viewController: UIViewController) {
        
        self.containerView.addSubview(viewController.view)
        self.addChildViewController(viewController)
        viewController.didMove(toParentViewController: self)
        
        viewController.view.translatesAutoresizingMaskIntoConstraints = false
        viewController.view.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        viewController.view.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        viewController.view.trailingAnchor.constraint(equalTo: self.containerView.trailingAnchor).isActive = true
        viewController.view.bottomAnchor.constraint(equalTo: self.containerView.bottomAnchor).isActive = true
    }
    
    func changeContents(index: Int) {
        
        self.guideViewController.view.isHidden = (index != 0)
        self.searchViewController.view.isHidden = (index != 1)
        self.messageViewController.view.isHidden = (index != 2)
        self.myPageViewController.view.isHidden = (index != 3)
        
        self.tab1ImageView.image = UIImage(named: (index == 0) ? "tab1_on" : "tab1_off")
        self.tab2ImageView.image = UIImage(named: (index == 1) ? "tab2_on" : "tab2_off")
        self.tab3ImageView.image = UIImage(named: (index == 2) ? "tab3_on" : "tab3_off")
        self.tab4ImageView.image = UIImage(named: (index == 3) ? "tab4_on" : "tab4_off")
    }
    
    @IBAction func onTapTab1(_ sender: Any) {
        self.changeContents(index: 0)
    }
    
    @IBAction func onTapTab2(_ sender: Any) {
        self.changeContents(index: 1)
    }
    
    @IBAction func onTapTab3(_ sender: Any) {
        self.changeContents(index: 2)
    }
    
    @IBAction func onTapTab4(_ sender: Any) {
        self.changeContents(index: 3)
    }
}

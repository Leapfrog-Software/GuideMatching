//
//  WebViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/09/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class WebViewController: UIViewController {

    @IBOutlet private weak var headerTitleLabel: UILabel!
    @IBOutlet private weak var webView: UIWebView!

    private var headerTitle: String!
    private var path: String!
    
    func set(title: String, path: String) {
        self.headerTitle = title
        self.path = path
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.headerTitleLabel.text = self.headerTitle
        
        if let url = URL(string: self.path) {
            self.webView.loadRequest(URLRequest(url: url))
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }

}

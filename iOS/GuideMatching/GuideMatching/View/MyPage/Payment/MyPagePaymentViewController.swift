//
//  MyPagePaymentViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import WebKit

class MyPagePaymentViewController: UIViewController {
    
    @IBOutlet private weak var containerView: UIView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        let webView = WKWebView()
        self.containerView.addSubview(webView)
        webView.translatesAutoresizingMaskIntoConstraints = false
        webView.topAnchor.constraint(equalTo: self.containerView.topAnchor).isActive = true
        webView.leadingAnchor.constraint(equalTo: self.containerView.leadingAnchor).isActive = true
        webView.trailingAnchor.constraint(equalTo: self.containerView.trailingAnchor).isActive = true
        webView.bottomAnchor.constraint(equalTo: self.containerView.bottomAnchor).isActive = true
        
        webView.navigationDelegate = self

        let url = URL(string: Constants.Stripe.dashboardUrl)!
        let request = URLRequest(url: url, cachePolicy: .reloadIgnoringLocalAndRemoteCacheData, timeoutInterval: 10)
        webView.load(request)
        
        Loading.start()
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPagePaymentViewController: WKNavigationDelegate {

    func webView(_ webView: WKWebView, didFinish navigation: WKNavigation!) {
        Loading.stop()
    }
    
    func webView(_ webView: WKWebView, didFail navigation: WKNavigation!, withError error: Error) {
        Loading.stop()
    }
}

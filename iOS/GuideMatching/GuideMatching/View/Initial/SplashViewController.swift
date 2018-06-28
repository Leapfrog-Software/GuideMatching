//
//  SplashViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class SplashViewController: UIViewController {

    enum ResultKey: String {
        case guide = "Guide"
        case guest = "Guest"
        case message = "Message"
        case estimate = "Estimate"
    }
    
    private var results = Dictionary<ResultKey, Bool>()

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.fetch()
    }
    
    private func fetch() {
        
        if self.results[.guide] != true {
            GuideRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.guide] = result
                self?.checkResult()
            })
        }
        if self.results[.guest] != true {
            GuestRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.guest] = result
                self?.checkResult()
            })
        }
        
        if self.results[.message] != true {
            MessageRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.message] = result
                self?.checkResult()
            })
        }
        if self.results[.estimate] != true {
            EstimateRequester.shared.fetch(completion: { [weak self] result in
                self?.results[.estimate] = result
                self?.checkResult()
            })
        }
    }
    
    private func checkResult() {
        
        let keys: [ResultKey] = [.guide, .estimate, .guest, .message]
        let results = keys.map { self.results[$0] }
        if results.contains(where: { $0 == nil }) {
            return
        }
        if results.contains(where: { $0 == false }) {
            self.showError()
        } else {
            let saveData = SaveData.shared
            if saveData.guestId.count > 0 || saveData.guideId.count > 0 {
                let tabbar = self.viewController(storyboard: "Initial", identifier: "TabbarViewController") as! TabbarViewController
                self.stack(viewController: tabbar)
                
                AccountRequester.login()
                
            } else {
                let userSelect = self.viewController(storyboard: "Initial", identifier: "UserSelectViewController") as! UserSelectViewController
                self.stack(viewController: userSelect)
            }
        }
    }
    
    private func showError() {
        let alert = UIAlertController(title: "Error", message: "Failed to communicate", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "Retry", style: .default, handler: { [weak self] _ in
            self?.fetch()
        }))
        self.present(alert, animated: true, completion: nil)
    }
    
    private func stack(viewController: UIViewController) {
        
        let blackView = UIView(frame: CGRect(origin: .zero, size: self.view.frame.size))
        blackView.backgroundColor = .black
        blackView.alpha = 0.0
        self.view.addSubview(blackView)
        
        UIView.animate(withDuration: 0.2, animations: {
            blackView.alpha = 1.0
        }, completion: { [weak self] _ in
            self?.stack(viewController: viewController, animationType: .none)
            self?.view.bringSubview(toFront: blackView)
            
            UIView.animate(withDuration: 0.2, animations: {
                blackView.alpha = 0.0
            }, completion: { _ in
                blackView.removeFromSuperview()
            })
        })
    }
}

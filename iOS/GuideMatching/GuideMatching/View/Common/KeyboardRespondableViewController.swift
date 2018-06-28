//
//  KeyboardRespondableViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

struct KeyboardAnimation {
    let height: CGFloat
    let duration: TimeInterval
    let curve: UIViewAnimationOptions
}

class KeyboardRespondableViewController: UIViewController {
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.addObserver(self, selector: #selector(keyboardWillShow(notification:)), name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        notificationCenter.addObserver(self, selector: #selector(keyboardWillHide(notification:)), name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        let notificationCenter = NotificationCenter.default
        notificationCenter.removeObserver(self, name: NSNotification.Name.UIKeyboardWillShow, object: nil)
        notificationCenter.removeObserver(self, name: NSNotification.Name.UIKeyboardWillHide, object: nil)
    }
    
    @objc func keyboardWillShow(notification: NSNotification) {
        self.animate(with: self.animation(from: notification))
    }
    
    @objc func keyboardWillHide(notification: NSNotification) {
        self.animate(with: self.animation(from: notification))
    }
    
    private func animation(from: NSNotification) -> KeyboardAnimation {
        
        let userInfo = from.userInfo
        let frameEnd = (userInfo?[UIKeyboardFrameEndUserInfoKey] as? NSValue)?.cgRectValue ?? .zero
        let height = UIScreen.main.bounds.size.height - frameEnd.origin.y
        let duration = (userInfo?[UIKeyboardAnimationDurationUserInfoKey] as? TimeInterval) ?? 0
        let curve = (userInfo?[UIKeyboardAnimationCurveUserInfoKey] as? UIViewAnimationOptions) ?? .curveEaseOut
        return KeyboardAnimation(height: height, duration: duration, curve: curve)
    }
    
    func animate(with: KeyboardAnimation) {}
}

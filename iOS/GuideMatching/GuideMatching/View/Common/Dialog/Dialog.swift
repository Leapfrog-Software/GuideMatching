//
//  Dialog.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/23.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

struct DialogAction {
    let title: String
    let action: (() -> ())?
}

class Dialog: UIView {
    
    enum Style {
        case success
        case error
    }
    
    @IBOutlet private weak var iconImageView: UIImageView!
    @IBOutlet private weak var titleLabel: UILabel!
    @IBOutlet private weak var messageLabel: UILabel!
    @IBOutlet private weak var buttonsStackView: UIStackView!
    
    class func show(style: Style, title: String, message: String, actions: [DialogAction]) {
        
        guard let window = UIApplication.shared.keyWindow else {
            return
        }
        
        let dialog = UINib(nibName: "Dialog", bundle: nil).instantiate(withOwner: nil, options: nil).first as! Dialog
        window.addSubview(dialog)
        dialog.translatesAutoresizingMaskIntoConstraints = false
        dialog.topAnchor.constraint(equalTo: window.topAnchor).isActive = true
        dialog.leadingAnchor.constraint(equalTo: window.leadingAnchor).isActive = true
        dialog.trailingAnchor.constraint(equalTo: window.trailingAnchor).isActive = true
        dialog.bottomAnchor.constraint(equalTo: window.bottomAnchor).isActive = true
        
        dialog.configure(style: style, title: title, message: message, actions: actions)
        
        dialog.alpha = 0
        UIView.animate(withDuration: 0.15) {
            dialog.alpha = 1.0
        }
    }
    
    private func configure(style: Style, title: String, message: String, actions: [DialogAction]) {
        
        self.iconImageView.image = UIImage(named: (style == .success) ? "dialog_success" : "dialog_error")
        self.titleLabel.text = title
        self.messageLabel.text = message
        
        actions.forEach { action in
            let button = UINib(nibName: "DialogButton", bundle: nil).instantiate(withOwner: nil, options: nil).first as! DialogButton
            let color = (style == .success) ? UIColor.dialogActionSuccess : UIColor.dialogActionError
            button.configure(title: action.title, color: color, didTap: { [weak self] in
                action.action?()
                UIView.animate(withDuration: 0.15, animations: {
                    self?.alpha = 0
                }, completion: { [weak self] _ in
                    self?.removeFromSuperview()
                })
            })
            self.buttonsStackView.addArrangedSubview(button)
        }
    }
}


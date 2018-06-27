//
//  PlaceholderTextView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class PlaceholderTextView: UITextView {
    
    @IBInspectable var placeholder: String = ""
    
    private weak var placeholderLabel: UILabel!
    
    func setText(_ text: String) {
        self.text = text
        self.placeholderLabel.isHidden = self.text.count > 0
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        
        let placeholderLabel = UILabel()
        placeholderLabel.text = self.placeholder
        placeholderLabel.textColor = .placeholder
        placeholderLabel.font = self.font
        placeholderLabel.numberOfLines = 0
        placeholderLabel.isUserInteractionEnabled = false
        self.addSubview(placeholderLabel)
        placeholderLabel.translatesAutoresizingMaskIntoConstraints = false
        placeholderLabel.topAnchor.constraint(equalTo: self.topAnchor, constant: 0).isActive = true
        placeholderLabel.leadingAnchor.constraint(equalTo: self.leadingAnchor, constant: 0).isActive = true
        placeholderLabel.trailingAnchor.constraint(equalTo: self.trailingAnchor, constant: 0).isActive = true
        placeholderLabel.bottomAnchor.constraint(equalTo: self.bottomAnchor, constant: 0).isActive = true
        
        self.placeholderLabel = placeholderLabel
        
        NotificationCenter.default.addObserver(self, selector: #selector(textDidChange), name: Notification.Name.UITextViewTextDidChange, object: nil)
    }
    
    @objc func textDidChange(notification: Notification) {
        self.placeholderLabel.isHidden = self.text.count > 0
    }
    
    deinit {
        NotificationCenter.default.removeObserver(self, name: Notification.Name.UITextViewTextDidChange, object: nil)
    }
}

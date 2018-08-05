//
//  MyPagePaymentAccountTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPagePaymentAccountTableViewCell: UITableViewCell {

    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var kanaLabel: UILabel!
    @IBOutlet private weak var bankNameLabel: UILabel!
    @IBOutlet private weak var branchNameLabel: UILabel!
    @IBOutlet private weak var typeLabel: UILabel!
    @IBOutlet private weak var numberLabel: UILabel!
    
    private var didTapEdit: (() -> ())?
    
    func configure(didTapEdit: @escaping (() -> ())) {
        
        let bankAccounts = SaveData.shared.bankAccount.components(separatedBy: ",")
        if bankAccounts.count == 6 {
            self.nameLabel.text = bankAccounts[0]
            self.kanaLabel.text = bankAccounts[1]
            self.bankNameLabel.text = bankAccounts[2]
            self.branchNameLabel.text = bankAccounts[3]
            self.typeLabel.text = bankAccounts[4]
            self.numberLabel.text = bankAccounts[5]
        } else {
            self.nameLabel.text = ""
            self.kanaLabel.text = ""
            self.bankNameLabel.text = ""
            self.branchNameLabel.text = ""
            self.typeLabel.text = ""
            self.numberLabel.text = ""
        }
        
        self.didTapEdit = didTapEdit
    }
    
    @IBAction func onTapEdit(_ sender: Any) {
        self.didTapEdit?()
    }
}

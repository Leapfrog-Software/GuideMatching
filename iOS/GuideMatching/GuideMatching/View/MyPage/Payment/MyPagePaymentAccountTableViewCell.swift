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
        
        guard let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            self.nameLabel.text = ""
            self.kanaLabel.text = ""
            self.bankNameLabel.text = ""
            self.branchNameLabel.text = ""
            self.typeLabel.text = ""
            self.numberLabel.text = ""
            return
        }
        self.nameLabel.text = myGuideData.bankAccountData.name
        self.kanaLabel.text = myGuideData.bankAccountData.kana
        self.bankNameLabel.text = myGuideData.bankAccountData.bankName
        self.branchNameLabel.text = myGuideData.bankAccountData.bankBranchName
        self.typeLabel.text = myGuideData.bankAccountData.accountType
        self.numberLabel.text = myGuideData.bankAccountData.accountNumber
        
        self.didTapEdit = didTapEdit
    }
    
    @IBAction func onTapEdit(_ sender: Any) {
        self.didTapEdit?()
    }
}

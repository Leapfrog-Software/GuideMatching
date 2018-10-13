//
//  MyPageAccountEditViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageAccountEditViewController: UIViewController {

    @IBOutlet private weak var nameTextField: UITextField!
    @IBOutlet private weak var kanaTextField: UITextField!
    @IBOutlet private weak var bankNameTextField: UITextField!
    @IBOutlet private weak var branchNameTextField: UITextField!
    @IBOutlet private weak var typeTextField: UITextField!
    @IBOutlet private weak var numberTextField: UITextField!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        guard let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        self.nameTextField.text = myGuideData.bankAccountData.name
        self.kanaTextField.text = myGuideData.bankAccountData.kana
        self.bankNameTextField.text = myGuideData.bankAccountData.bankName
        self.branchNameTextField.text = myGuideData.bankAccountData.bankBranchName
        self.typeTextField.text = myGuideData.bankAccountData.accountType
        self.numberTextField.text = myGuideData.bankAccountData.accountNumber
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        
        guard var myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        myGuideData.bankAccountData.name = self.nameTextField.text ?? ""
        myGuideData.bankAccountData.kana = self.kanaTextField.text ?? ""
        myGuideData.bankAccountData.bankName = self.bankNameTextField.text ?? ""
        myGuideData.bankAccountData.bankBranchName = self.branchNameTextField.text ?? ""
        myGuideData.bankAccountData.accountType = self.typeTextField.text ?? ""
        myGuideData.bankAccountData.accountNumber = self.numberTextField.text ?? ""
        
        Loading.start()
        
        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
            if resultUpdate {
                GuideRequester.shared.fetch(completion: { _ in
                    Loading.stop()
                    
//                    (self.parent as? MyPagePaymentViewController)?.reloadTable()
                    
                    let action = DialogAction(title: "OK", action: {
                        self.pop(animationType: .horizontal)
                    })
                    Dialog.show(style: .success, title: "確認", message: "登録が完了しました", actions: [action])
                })
            } else {
                Loading.stop()
                Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

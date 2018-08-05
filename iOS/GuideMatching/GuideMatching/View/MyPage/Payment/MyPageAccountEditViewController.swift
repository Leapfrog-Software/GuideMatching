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
        
        let bankAccounts = SaveData.shared.bankAccount.components(separatedBy: ",")
        if bankAccounts.count == 6 {
            self.nameTextField.text = bankAccounts[0]
            self.kanaTextField.text = bankAccounts[1]
            self.bankNameTextField.text = bankAccounts[2]
            self.branchNameTextField.text = bankAccounts[3]
            self.typeTextField.text = bankAccounts[4]
            self.numberTextField.text = bankAccounts[5]
        } else {
            self.nameTextField.text = ""
            self.kanaTextField.text = ""
            self.bankNameTextField.text = ""
            self.branchNameTextField.text = ""
            self.typeTextField.text = ""
            self.numberTextField.text = ""
        }
    }
    
    override func touchesBegan(_ touches: Set<UITouch>, with event: UIEvent?) {
        self.view.endEditing(true)
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapRegister(_ sender: Any) {
        
        let name = self.nameTextField.text ?? ""
        let kana = self.kanaTextField.text ?? ""
        let bankName = self.bankNameTextField.text ?? ""
        let branchName = self.branchNameTextField.text ?? ""
        let type = self.typeTextField.text ?? ""
        let number = self.numberTextField.text ?? ""
        
        let saveData = SaveData.shared
        saveData.bankAccount = name + "," + kana + "," + bankName + "," + branchName + "," + type + "," + number
        saveData.save()
        
        (self.parent as? MyPagePaymentViewController)?.reloadTable()
        
        Loading.start()
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 1, execute: {
            Loading.stop()
            
            let action = DialogAction(title: "OK", action: {
                self.pop(animationType: .horizontal)
            })
            Dialog.show(style: .success, title: "確認", message: "登録が完了しました", actions: [action])
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

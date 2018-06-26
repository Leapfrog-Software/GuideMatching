//
//  GuestRegisterViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuestRegisterViewController: UIViewController {

    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapDone(_ sender: Any) {
        self.view.endEditing(true)
        
        AccountRequester.createGuest(name: "test", nationality: "test", completion: { result, guestId in

        })
    }
}

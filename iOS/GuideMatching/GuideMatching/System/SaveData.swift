//
//  SaveData.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class SaveData {
    
    static let shared = SaveData()
    
    var guestId = ""
    var guideId = ""
    var bankAccount = ""        // TODO 削除
    
    init() {
        let userDefaults = UserDefaults()
//        self.guestId = userDefaults.string(forKey: Constants.UserDefaultsKey.GuestId) ?? ""
//        self.guideId = userDefaults.string(forKey: Constants.UserDefaultsKey.GuideId) ?? ""
        self.guestId = ""
        self.guideId = "aaa"
        self.bankAccount = userDefaults.string(forKey: Constants.UserDefaultsKey.BankAccount) ?? ""
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.guestId, forKey: Constants.UserDefaultsKey.GuestId)
        userDefaults.set(self.guideId, forKey: Constants.UserDefaultsKey.GuideId)
        userDefaults.set(self.bankAccount, forKey: Constants.UserDefaultsKey.BankAccount)
        
        userDefaults.synchronize()
    }
}

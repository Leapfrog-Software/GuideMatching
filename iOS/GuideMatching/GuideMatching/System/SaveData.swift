//
//  SaveData.swift
//  GuideMatching
//
//  Created by 藤田 祥一 on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class SaveData {
    
    static let shared = SaveData()
    
    var guestId = ""
    
    init() {
        let userDefaults = UserDefaults()
        self.guestId = userDefaults.string(forKey: Constants.UserDefaultsKey.GuestId) ?? ""
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.guestId, forKey: Constants.UserDefaultsKey.GuestId)
        
        userDefaults.synchronize()
    }
}

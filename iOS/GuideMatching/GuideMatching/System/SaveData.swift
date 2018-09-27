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
    
    private var currentVersion = "2"
    
    var guestId = ""
    var guideId = ""
    
    init() {
        let userDefaults = UserDefaults()
        
        if userDefaults.string(forKey: Constants.UserDefaultsKey.Version) != self.currentVersion {
            return
        }
        
        self.guestId = userDefaults.string(forKey: Constants.UserDefaultsKey.GuestId) ?? ""
        self.guideId = userDefaults.string(forKey: Constants.UserDefaultsKey.GuideId) ?? ""
    }
    
    func save() {
        
        let userDefaults = UserDefaults()
        
        userDefaults.set(self.currentVersion, forKey: Constants.UserDefaultsKey.Version)
        userDefaults.set(self.guestId, forKey: Constants.UserDefaultsKey.GuestId)
        userDefaults.set(self.guideId, forKey: Constants.UserDefaultsKey.GuideId)
        
        userDefaults.synchronize()
    }
}

//
//  Constants.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class Constants {
    
    static let ServerRootUrl = "http://localhost/tourguide/"
    static let ServerApiUrl = Constants.ServerRootUrl + "srv.php"
    static let StringEncoding = String.Encoding.utf8
    static let HttpTimeOutInterval = TimeInterval(10)
    
    struct UserDefaultsKey {
        static let GuestId = "GuestId"
    }
}

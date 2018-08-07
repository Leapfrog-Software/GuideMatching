//
//  Constants.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class Constants {
    
//    static let ServerRootUrl = "http://localhost/guide_matching/"
    static let ServerRootUrl = "http://lfrogs.sakura.ne.jp/guide_matching/"
    static let ServerApiUrl = Constants.ServerRootUrl + "srv.php"
    static let StripeBackendUrl = Constants.ServerRootUrl + "stripe/backend.php"
    static let ServerGuideImageRootUrl = Constants.ServerRootUrl + "data/image/guide/"
    static let ServerGuestImageRootUrl = Constants.ServerRootUrl + "data/image/guest/"
    static let StringEncoding = String.Encoding.utf8
    static let HttpTimeOutInterval = TimeInterval(10)
    
    struct UserDefaultsKey {
        static let GuestId = "GuestId"
        static let GuideId = "GuideId"
    }
    
    struct Stripe {
        static let Key = "pk_test_YA3x9LrmFX1C7annyyM1iEg3"
//        static let Key = "pk_test_PMO8LzsVdmpwxs0s3GXnRoRa"     // 開発用
    }
}

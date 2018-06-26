//
//  AccountRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class AccountRequester {
    
    class func createGuest(name: String, nationality: String, completion: @escaping ((Bool, String?) -> ())) {
        
        let params = [
            "command": "createGuest",
            "name": name,
            "nationality": nationality
        ]
        ApiManager.post(params: params) { (result, data) in
            if result, let guestId = (data as? NSDictionary)?.object(forKey: "guestId") as? String {
                completion(true, guestId)
            } else {
                completion(false, nil)
            }
        }
    }
}

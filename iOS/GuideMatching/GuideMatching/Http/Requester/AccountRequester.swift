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
            "name": name.base64Encode() ?? "",
            "nationality": nationality.base64Encode() ?? ""
        ]
        ApiManager.post(params: params) { (result, data) in
            if result, let guestId = (data as? NSDictionary)?.object(forKey: "guestId") as? String {
                completion(true, guestId)
            } else {
                completion(false, nil)
            }
        }
    }
    
    class func createGuide(name: String, nationality: String, language: String, specialty: String,
                           category: String, message: String, timeZone: String, applicableNumber: Int,
                           fee: String, notes: String, completion: @escaping ((Bool, String?) -> ())) {
        
        var params: [String: String] = ["command": "createGuide"]
        params["name"] = name.base64Encode() ?? ""
        params["nationality"] = nationality.base64Encode() ?? ""
        params["language"] = language.base64Encode() ?? ""
        params["specialty"] = specialty.base64Encode() ?? ""
        params["category"] = category.base64Encode() ?? ""
        params["message"] = message.base64Encode() ?? ""
        params["timeZone"] = timeZone.base64Encode() ?? ""
        params["applicableNumber"] = "\(applicableNumber)"
        params["fee"] = fee.base64Encode() ?? ""
        params["notes"] = notes.base64Encode() ?? ""
        
        ApiManager.post(params: params) { result, data in
            if result, let guideId = (data as? NSDictionary)?.object(forKey: "guideId") as? String {
                completion(true, guideId)
            } else {
                completion(false, nil)
            }
        }
    }
    
    class func login() {
        
        let params = [
            "command": "login",
            "guideId": SaveData.shared.guideId,
            "guestId": SaveData.shared.guestId
        ]
        ApiManager.post(params: params, completion: { _, _ in })
    }
}

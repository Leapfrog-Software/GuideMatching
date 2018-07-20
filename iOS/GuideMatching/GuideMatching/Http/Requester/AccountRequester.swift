//
//  AccountRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class AccountRequester {
    
    class func createGuest(email: String, name: String, nationality: String, completion: @escaping ((Bool, String?) -> ())) {
        
        let params = [
            "command": "createGuest",
            "email": email,
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
    
    class func updateGuest(guestData: GuestData, completion: @escaping ((Bool) -> ())) {
        
        var params: [String: String] = ["command": "updateGuest"]
        params["id"] = guestData.id
        params["name"] = guestData.name.base64Encode() ?? ""
        params["nationality"] = guestData.nationality.base64Encode() ?? ""
        params["stripeCustomerId"] = guestData.stripeCustomerId
        
        ApiManager.post(params: params, completion: { (result, data) in
            if result, ((data as? NSDictionary)?.object(forKey: "result") as? String) == "0" {
                completion(true)
            } else {
                completion(false)
            }
        })
    }
    
    class func createGuide(email: String, name: String, nationality: String, language: String, specialty: String,
                           category: String, message: String, timeZone: String, applicableNumber: Int,
                           fee: String, notes: String, completion: @escaping ((Bool, String?) -> ())) {
        
        var params: [String: String] = ["command": "createGuide"]
        params["email"] = email
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
    
    class func updateGuide(guideData: GuideData, completion: @escaping ((Bool) -> ())) {
        
        var params: [String: String] = ["command": "updateGuide"]
        params["id"] = guideData.id
        params["name"] = guideData.name.base64Encode() ?? ""
        params["nationality"] = guideData.nationality.base64Encode() ?? ""
        params["language"] = guideData.language.base64Encode() ?? ""
        params["specialty"] = guideData.specialty.base64Encode() ?? ""
        params["category"] = guideData.category.base64Encode() ?? ""
        params["message"] = guideData.message.base64Encode() ?? ""
        params["timeZone"] = guideData.timeZone.base64Encode() ?? ""
        params["applicableNumber"] = "\(guideData.applicableNumber)"
        params["fee"] = guideData.fee
        params["notes"] = guideData.notes
        params["schedules"] = guideData.schedules.compactMap { $0.toString() }.joined(separator: "/")
        params["stripeAccountId"] = guideData.stripeAccountId
        
        ApiManager.post(params: params, completion: { (result, data) in
            if result, ((data as? NSDictionary)?.object(forKey: "result") as? String) == "0" {
                completion(true)
            } else {
                completion(false)
            }
        })
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

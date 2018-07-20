//
//  StripeManager.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/20.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

class StripeManager {
    
    static func createAccount(email: String, completion: @escaping (Bool, String?) -> ()) {
        
        let params = [
            "command": "createAccount",
            "email": email
        ]
        HttpManager.post(url: Constants.StripeBackendUrl, params: params, completion: { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, AnyObject> {
                        if let accountId = json["accountId"] as? String {
                            completion(true, accountId)
                            return
                        }
                    }
                } catch {}
            }
            completion(false, nil)
        })
    }
    
    static func createCustomer(email: String, completion: @escaping (Bool, String?) -> ()) {
        
        let params = [
            "command": "createCustomer",
            "email": email
        ]
        HttpManager.post(url: Constants.StripeBackendUrl, params: params, completion: { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, AnyObject> {
                        if let customerId = json["customerId"] as? String {
                            completion(true, customerId)
                            return
                        }
                    }
                } catch {}
            }
            completion(false, nil)
        })
    }
    
    static func charge(customerId: String, cardId: String, amount: Int, applicationFee: Int, destination: String, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "createCharge",
            "customerId": customerId,
            "cardId": cardId,
            "amount": "\(amount)",
            "applicationFee": "\(applicationFee)",
            "destination": destination
        ]
        HttpManager.post(url: Constants.StripeBackendUrl, params: params, completion: { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, AnyObject> {
                        if (json["result"] as? String) == "0" {
                            completion(true)
                            return
                        }
                    }
                } catch {}
            }
            completion(false)
        })
    }
}

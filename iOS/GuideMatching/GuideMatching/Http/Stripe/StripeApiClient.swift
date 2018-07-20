//
//  StripeApiClient.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/20.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation
import Stripe

class StripeApiClient: NSObject, STPEphemeralKeyProvider {
    
    private let customerId: String
    
    init(customerId: String) {
        self.customerId = customerId
        super.init()
    }

    func createCustomerKey(withAPIVersion apiVersion: String, completion: @escaping STPJSONResponseCompletionBlock) {
        
        let params = [
            "command": "createEphemeralKey",
            "apiVersion": apiVersion,
            "customerId": self.customerId
        ]
        HttpManager.post(url: Constants.StripeBackendUrl, params: params, completion: { result, data in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, AnyObject> {
                        completion(json, nil)
                        return
                    }
                } catch {}
            }
            completion(nil, NSError(domain: "", code: 500, userInfo: nil))
        })
    }
    
}

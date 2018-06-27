//
//  EstimateRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct EstimateData {
    
    let senderId: String
    let targetId: String
    let isGuide: Bool
    let score: Int
    let comment: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let senderId = data["senderId"] as? String else {
            return nil
        }
        self.senderId = senderId
        
        guard let targetId = data["targetId"] as? String else {
            return nil
        }
        self.targetId = targetId
        
        self.isGuide = (data["isGuide"] as? String) == "1"
        self.score = Int(data["score"] as? String ?? "") ?? 0
        self.comment = (data["comment"] as? String)?.base64Decode() ?? ""
    }
}

class EstimateRequester {
    
    static let shared = EstimateRequester()
    
    var dataList = [EstimateData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getEstimate"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let estimates = (data as? Dictionary<String, Any>)?["estimates"] as? Array<Dictionary<String, Any>> {
                self?.dataList = estimates.compactMap { EstimateData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
}

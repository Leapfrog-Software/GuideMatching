//
//  EstimateRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct EstimateData {
    
    let reserveId: String
    let guestId: String
    let guideId: String
    let score: Int
    let comment: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let reserveId = data["reserveId"] as? String else {
            return nil
        }
        self.reserveId = reserveId
        
        guard let guestId = data["guestId"] as? String else {
            return nil
        }
        self.guestId = guestId
        
        guard let guideId = data["guideId"] as? String else {
            return nil
        }
        self.guideId = guideId
        
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
    
    class func post(reserveId: String, guestId: String, guideId: String, score: Int, comment: String, completion: @escaping ((Bool) -> ())) {
        
        let params = [
            "command": "postEstimate",
            "reserveId": reserveId,
            "guestId": guestId,
            "guideId": guideId,
            "score": "\(score)",
            "comment": comment.base64Encode() ?? ""
        ]
        ApiManager.post(params: params) { (result, data) in
            completion(result)
        }
    }
    
    func queryAverage(guideId: String) -> Int {
        
        var count = 0
        var total = 0
        self.dataList.forEach { estimate in
            if estimate.guideId == guideId {
                count += 1
                total += estimate.score
            }
        }
        if count == 0 {
            return 0
        }
        return total / count
    }
}

//
//  ReserveRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct ReserveData {
    let id: String
    let requesterId: String
    let guideId: String
    let area: String
    let date: Date
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.requesterId = data["requesterId"] as? String ?? ""
        self.guideId = data["guideId"] as? String ?? ""
        self.area = (data["area"] as? String)?.base64Decode() ?? ""
        
        let dateString = data["date"] as? String ?? ""
        guard dateString.count == 14, let date = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: dateString) else {
            return nil
        }
        self.date = date
    }
}

class ReserveRequester {
    
    static let shared = ReserveRequester()
    
    var dataList = [ReserveData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getReserve"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let guides = (data as? Dictionary<String, Any>)?["reserves"] as? Array<Dictionary<String, Any>> {
                self?.dataList = guides.compactMap { ReserveData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    
    class func reserve(requesterId: String, guideId: String, area: String, completion: @escaping ((Bool) -> ())) {

        var params: [String: String] = ["command": "createReserve"]
        params["requesterId"] = requesterId
        params["guideId"] = guideId
        params["area"] = area
        
        ApiManager.post(params: params) { result, data in
            if result, ((data as? NSDictionary)?.object(forKey: "result") as? String) == "0" {
                completion(true)
            } else {
                completion(false)
            }
        }
    }
}

//
//  GuestRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct GuestData {
    
    let id: String
    let name: String
    let nationality: String
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.name = (data["name"] as? String)?.base64Decode() ?? ""
        self.nationality = (data["nationality"] as? String)?.base64Decode() ?? ""
    }
}

class GuestRequester {
    
    static let shared = GuestRequester()
    
    var dataList = [GuestData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getGuest"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let guides = (data as? Dictionary<String, Any>)?["guests"] as? Array<Dictionary<String, Any>> {
                self?.dataList = guides.compactMap { GuestData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
}

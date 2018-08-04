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
    let guestId: String
    let guideId: String
    let meetingPlace: String
    let day: Date
    let startTime: Int
    let endTime: Int
    let reserveDate: Date
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.guestId = data["guestId"] as? String ?? ""
        self.guideId = data["guideId"] as? String ?? ""
        self.meetingPlace = (data["meetingPlace"] as? String)?.base64Decode() ?? ""
        
        guard let day = DateFormatter(dateFormat: "yyyyMMdd").date(from: data["day"] as? String ?? "") else {
            return nil
        }
        self.day = day
        
        self.startTime = Int(data["startTime"] as? String ?? "") ?? 0
        self.endTime = Int(data["endTime"] as? String ?? "") ?? 0
        
        guard let reserveDate = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: data["reserveDate"] as? String ?? "") else {
            return nil
        }
        self.reserveDate = reserveDate
    }

    func toStartDate() -> Date? {
        let yyyymmdd = DateFormatter(dateFormat: "yyyyMMdd").string(from: self.day)
        let hhmm = CommonUtility.timeOffsetToString(offset: self.startTime)
        return DateFormatter(dateFormat: "yyyyMMddHH:mm").date(from: yyyymmdd + hhmm)
    }
    
    func toEndDate() -> Date? {
        let yyyymmdd = DateFormatter(dateFormat: "yyyyMMdd").string(from: self.day)
        let hhmm = CommonUtility.timeOffsetToString(offset: self.endTime)
        return DateFormatter(dateFormat: "yyyyMMddHH:mm").date(from: yyyymmdd + hhmm)
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
    
    class func reserve(guestId: String, guideId: String, meetingPlace: String, day: Date, startTime: Int, endTime: Int, completion: @escaping ((Bool) -> ())) {

        var params: [String: String] = ["command": "createReserve"]
        params["guestId"] = guestId
        params["guideId"] = guideId
        params["meetingPlace"] = meetingPlace.base64Encode() ?? ""
        params["day"] = DateFormatter(dateFormat: "yyyyMMdd").string(from: day)
        params["startTime"] = "\(startTime)"
        params["endTime"] = "\(endTime)"
        
        ApiManager.post(params: params) { result, data in
            if result, ((data as? NSDictionary)?.object(forKey: "result") as? String) == "0" {
                completion(true)
            } else {
                completion(false)
            }
        }
    }
}

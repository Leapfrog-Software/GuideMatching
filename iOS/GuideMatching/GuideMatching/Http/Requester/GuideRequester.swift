//
//  GuideRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct GuideScheduleData {
    let date: Date
    var isFreeList: [Bool]
    
    init?(data: String) {
        let datas = data.components(separatedBy: "_")
        if datas.count == 49 {
            guard let date = DateFormatter(dateFormat: "yyyyMMdd").date(from: datas[0]) else {
                return nil
            }
            self.date = date

            var isFreeList: [Bool] = []
            for i in 1..<datas.count {
                if datas[i] == "1" {
                    isFreeList.append(true)
                } else {
                    isFreeList.append(false)
                }
            }
            self.isFreeList = isFreeList
            return
        }
        return nil
    }
    
    init?(date: Date, isFreeList: [Bool]) {
        self.date = date
        self.isFreeList = isFreeList
    }
    
    func toString() -> String {
        let dateStr = DateFormatter(dateFormat: "yyyyMMdd").string(from: self.date)
        let isFrees = self.isFreeList.map { $0 ? "1" : "0" }
        return dateStr + "_" + isFrees.joined(separator: "_")
    }
}

struct GuideData {
    
    let id: String
    let name: String
    let nationality: String
    let language: String
    let specialty: String
    let category: String
    let message: String
    let timeZone: String
    let applicableNumber: Int
    let fee: String
    let notes: String
    var schedules: [GuideScheduleData]
    let loginDate: Date
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.name = (data["name"] as? String)?.base64Decode() ?? ""
        self.nationality = (data["nationality"] as? String)?.base64Decode() ?? ""
        self.language = (data["language"] as? String)?.base64Decode() ?? ""
        self.specialty = (data["specialty"] as? String)?.base64Decode() ?? ""
        self.category = (data["category"] as? String)?.base64Decode() ?? ""
        self.message = (data["message"] as? String)?.base64Decode() ?? ""
        self.timeZone = (data["timeZone"] as? String)?.base64Decode() ?? ""
        self.applicableNumber = Int(data["applicableNumber"] as? String ?? "") ?? 0
        self.fee = data["fee"] as? String ?? ""
        self.notes = (data["notes"] as? String)?.base64Decode() ?? ""
        
        self.schedules = (data["schedules"] as? String ?? "").components(separatedBy: "/").compactMap { GuideScheduleData(data: $0) }

        let loginDateString = data["loginDate"] as? String ?? ""
        guard loginDateString.count == 14, let loginDate = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: loginDateString) else {
            return nil
        }
        self.loginDate = loginDate
    }
}

class GuideRequester {
    
    static let shared = GuideRequester()
    
    var dataList = [GuideData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let params = ["command": "getGuide"]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let guides = (data as? Dictionary<String, Any>)?["guides"] as? Array<Dictionary<String, Any>> {
                self?.dataList = guides.compactMap { GuideData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    
    func query(id: String) -> GuideData? {
        return self.dataList.filter { $0.id == id }.first
    }
}

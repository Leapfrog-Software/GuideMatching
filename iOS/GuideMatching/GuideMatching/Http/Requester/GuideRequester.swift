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

struct GuideTourData {
    var id: String
    var name: String
    var area: String
    var description: String
    var fee: Int
    var highlights1Title: String
    var highlights1Body: String
    var highlights2Title: String
    var highlights2Body: String
    var highlights3Title: String
    var highlights3Body: String
    var days: [Date]
    var startTime: Int
    var endTime: Int
    var departurePoint: String
    var returnDetail: String
    var inclusions: String
    var exclusions: String
    
    init?(data: String) {
        
        let datas = data.components(separatedBy: "-")
        if datas.count == 18 {
            self.id = datas[0]
            self.name = datas[1].base64Decode() ?? ""
            self.area = datas[2].base64Decode() ?? ""
            self.description = datas[3].base64Decode() ?? ""
            self.fee = Int(datas[4]) ?? 0
            self.highlights1Title = datas[5].base64Decode() ?? ""
            self.highlights1Body = datas[6].base64Decode() ?? ""
            self.highlights2Title = datas[7].base64Decode() ?? ""
            self.highlights2Body = datas[8].base64Decode() ?? ""
            self.highlights3Title = datas[9].base64Decode() ?? ""
            self.highlights3Body = datas[10].base64Decode() ?? ""
            self.days = datas[11].components(separatedBy: "|").compactMap { DateFormatter(dateFormat: "yyyyMMdd").date(from: $0) }
            self.startTime = Int(datas[12]) ?? 0
            self.endTime = Int(datas[13]) ?? 0
            self.departurePoint = datas[14].base64Decode() ?? ""
            self.returnDetail = datas[15].base64Decode() ?? ""
            self.inclusions = datas[16].base64Decode() ?? ""
            self.exclusions = datas[17].base64Decode() ?? ""
            return
        }
        return nil
    }
    
    init() {
        self.id = ""
        self.name = ""
        self.area = ""
        self.description = ""
        self.fee = 0
        self.highlights1Title = ""
        self.highlights1Body = ""
        self.highlights2Title = ""
        self.highlights2Body = ""
        self.highlights3Title = ""
        self.highlights3Body = ""
        self.days = []
        self.startTime = 0
        self.endTime = 0
        self.departurePoint = ""
        self.returnDetail = ""
        self.inclusions = ""
        self.exclusions = ""
    }
    
    func toString() -> String {
        
        let name = self.name.base64Encode() ?? ""
        let area = self.area.base64Encode() ?? ""
        let description = self.description.base64Encode() ?? ""
        let fee = "\(self.fee)"
        let highlights1Title = self.highlights1Title.base64Encode() ?? ""
        let highlights1Body = self.highlights1Body.base64Encode() ?? ""
        let highlights2Title = self.highlights2Title.base64Encode() ?? ""
        let highlights2Body = self.highlights2Body.base64Encode() ?? ""
        let highlights3Title = self.highlights3Title.base64Encode() ?? ""
        let highlights3Body = self.highlights3Body.base64Encode() ?? ""
        let days = self.days.map { DateFormatter(dateFormat: "yyyyMMdd").string(from: $0) }.joined(separator: "|")
        let startTime = "\(self.startTime)"
        let endTime = "\(self.endTime)"
        let departurePoint = self.departurePoint.base64Encode() ?? ""
        let returnDetail = self.returnDetail.base64Encode() ?? ""
        let inclusions = self.inclusions.base64Encode() ?? ""
        let exclusions = self.exclusions.base64Encode() ?? ""
        
        return [self.id, name, area, description, fee, highlights1Title, highlights1Body, highlights2Title, highlights2Body, highlights3Title, highlights3Body,
                days, startTime, endTime, departurePoint, returnDetail, inclusions, exclusions].joined(separator: "-")
    }
}

struct BankAccountData {
    var name: String
    var kana: String
    var bankName: String
    var bankBranchName: String
    var accountType: String
    var accountNumber: String
    
    init(data: String) {
        
        let decoded = data.base64Decode() ?? ""
        let components = decoded.components(separatedBy: ",")
        if components.count == 6 {
            self.name = components[0]
            self.kana = components[1]
            self.bankName = components[2]
            self.bankBranchName = components[3]
            self.accountType = components[4]
            self.accountNumber = components[5]
        } else {
            self.name = ""
            self.kana = ""
            self.bankName = ""
            self.bankBranchName = ""
            self.accountType = ""
            self.accountNumber = ""
        }
    }
    
    func toString() -> String {
        let joined = [self.name, self.kana, self.bankName, self.bankBranchName, self.accountType, self.accountNumber].joined(separator: ",")
        return joined.base64Encode() ?? ""
    }
}

struct GuideData {
    
    let id: String
    var email: String
    var name: String
    var nationality: String
    var language: String
    var area: String
    var keyword: String
    var category: String
    var message: String
    var applicableNumber: Int
    var fee: Int
    var notes: String
    var schedules: [GuideScheduleData]
    var tours: [GuideTourData]
    let loginDate: Date
    var stripeAccountId: String
    var bankAccountData: BankAccountData
    
    init?(data: Dictionary<String, Any>) {
        
        guard let id = data["id"] as? String else {
            return nil
        }
        self.id = id
        
        self.email = data["email"] as? String ?? ""
        self.name = (data["name"] as? String)?.base64Decode() ?? ""
        self.nationality = (data["nationality"] as? String)?.base64Decode() ?? ""
        self.language = (data["language"] as? String)?.base64Decode() ?? ""
        self.area = (data["area"] as? String)?.base64Decode() ?? ""
        self.keyword = (data["keyword"] as? String)?.base64Decode() ?? ""
        self.category = (data["category"] as? String)?.base64Decode() ?? ""
        self.message = (data["message"] as? String)?.base64Decode() ?? ""
        self.applicableNumber = Int(data["applicableNumber"] as? String ?? "") ?? 0
        self.fee = Int(data["fee"] as? String ?? "") ?? 0
        self.notes = (data["notes"] as? String)?.base64Decode() ?? ""
        
        self.schedules = (data["schedules"] as? String ?? "").components(separatedBy: "/").compactMap { GuideScheduleData(data: $0) }
        self.tours = (data["tours"] as? String ?? "").components(separatedBy: "/").compactMap { GuideTourData(data: $0) }

        let loginDateString = data["loginDate"] as? String ?? ""
        guard loginDateString.count == 14, let loginDate = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: loginDateString) else {
            return nil
        }
        self.loginDate = loginDate
        
        self.stripeAccountId = data["stripeAccountId"] as? String ?? ""
        
        self.bankAccountData = BankAccountData(data: data["bankAccount"] as? String ?? "")
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

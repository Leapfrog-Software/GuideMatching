//
//  MessageRequester.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

struct MessageData {
    
    let messageId: String
    let senderId: String
    let receiverId: String
    let message: String
    let datetime: Date
    
    init?(data: Dictionary<String, Any>) {
        
        self.messageId = data["messageId"] as? String ?? ""
        self.senderId = data["senderId"] as? String ?? ""
        self.receiverId = data["receiverId"] as? String ?? ""
        self.message = (data["message"] as? String)?.base64Decode() ?? ""
        
        guard let datetimeStr = data["date"] as? String,
            let datetime = DateFormatter(dateFormat: "yyyyMMddHHmmss").date(from: datetimeStr) else {
                return nil
        }
        self.datetime = datetime
    }
    
    init(messageId: String, senderId: String, message: String, datetime: Date) {
        self.messageId = messageId
        self.senderId = senderId
        self.receiverId = ""
        self.message = message
        self.datetime = datetime
    }
}

class MessageRequester {
    
    static let shared = MessageRequester()
    
    var dataList = [MessageData]()
    
    func fetch(completion: @escaping ((Bool) -> ())) {
        
        let userId: String
        if SaveData.shared.guestId.count > 0 {
            userId = SaveData.shared.guestId
        } else {
            userId = SaveData.shared.guideId
        }
        if userId.count == 0 {
            self.dataList = []
            completion(true)
            return
        }
        
        let params = [
            "command": "getMessage",
            "userId": userId
        ]
        ApiManager.post(params: params) { [weak self] result, data in
            if result, let messages = (data as? Dictionary<String, Any>)?["messages"] as? Array<Dictionary<String, Any>> {
                self?.dataList = messages.compactMap { MessageData(data: $0) }
                completion(true)
            } else {
                completion(false)
            }
        }
    }
    
    func query(userId: String) -> [MessageData] {
        
        var messages = [MessageData]()
        
        self.dataList.forEach { message in
            if message.senderId == userId || message.receiverId == userId {
                messages.append(message)
            }
        }
        return messages
    }
    
    class func post(messageId: String, targetId: String, message: String, completion: @escaping ((Bool) -> ())) {
        
        let userId: String
        if SaveData.shared.guestId.count > 0 {
            userId = SaveData.shared.guestId
        } else {
            userId = SaveData.shared.guideId
        }
        
        let params = [
            "command": "postMessage",
            "messageId": messageId,
            "senderId": userId,
            "receiverId": targetId,
            "message": message.base64Encode() ?? ""
        ]
        ApiManager.post(params: params) { (result, data) in
            completion(result)
        }
    }
}

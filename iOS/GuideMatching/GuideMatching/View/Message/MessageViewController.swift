//
//  MessageViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageViewController: UIViewController {
    
    struct CellData {
        let userData: Any
        let latestMessage: String
        let latestDate: Date
    }

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var cellDatas = [CellData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        Timer.scheduledTimer(withTimeInterval: 5, repeats: true, block: { [weak self] _ in
            self?.timerProc()
        })
    }
    
    private func timerProc() {
        MessageRequester.shared.fetch(completion: { result in
            if result {
                let myUserId: String
                if SaveData.shared.guestId.count > 0 {
                    myUserId = SaveData.shared.guestId
                } else {
                    myUserId = SaveData.shared.guideId
                }
                let messages = MessageRequester.shared.query(userId: myUserId)
                var userIds = [String]()
                messages.forEach {
                    let targetId: String
                    if $0.senderId == myUserId {
                        targetId = $0.receiverId
                    } else {
                        targetId = $0.senderId
                    }
                    if !userIds.contains(targetId) {
                        userIds.append(targetId)
                    }
                }
                self.cellDatas = userIds.compactMap { userId -> CellData? in
                    guard let latestMessageData = self.latestMessageData(userId: userId) else {
                        return nil
                    }
                    if let guideData = GuideRequester.shared.query(id: userId) {
                        return CellData(userData: guideData, latestMessage: latestMessageData.message, latestDate: latestMessageData.datetime)
                    } else if let guestData = GuestRequester.shared.query(id: userId) {
                        return CellData(userData: guestData, latestMessage: latestMessageData.message, latestDate: latestMessageData.datetime)
                    } else {
                        return nil
                    }
                }
                self.tableView.reloadData()
                self.noDataLabel.isHidden = !self.cellDatas.isEmpty
            }
        })
    }
    
    private func latestMessageData(userId: String) -> MessageData? {
        
        let filterred = MessageRequester.shared.dataList.filter { messageData -> Bool in
            return messageData.senderId == userId || messageData.receiverId == userId
        }
        let sorted = filterred.sorted(by: { (message1, message2) -> Bool in
            return message1.datetime > message2.datetime
        })
        return sorted.last
    }
}

extension MessageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        let cellData = self.cellDatas[indexPath.row]
        
        if let guideData = cellData.userData as? GuideData {
            cell.configure(guideId: guideData.id, guideName: guideData.name, message: cellData.latestMessage, latestDate: cellData.latestDate)
        } else if let guestData = cellData.userData as? GuestData {
            cell.configure(guestId: guestData.id, guestName: guestData.name, message: cellData.latestMessage, latestDate: cellData.latestDate)
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let cellData = self.cellDatas[indexPath.row]

        var targetUserId = ""
        if let guideData = cellData.userData as? GuideData {
            targetUserId = guideData.id
        } else if let guestData = cellData.userData as? GuestData {
            targetUserId = guestData.id
        } else {
            return
        }
        
        let detail = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        detail.set(targetUserId: targetUserId)
        self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
    }
}


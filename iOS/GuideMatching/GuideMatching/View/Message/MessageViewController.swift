//
//  MessageViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageViewController: UIViewController {

    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var noDataLabel: UILabel!
    
    private var userIds = [String]()
    
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
                self.userIds = userIds
                
                self.tableView.reloadData()
                self.noDataLabel.isHidden = !self.userIds.isEmpty
            }
        })
    }
}

extension MessageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.userIds.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MessageTableViewCell", for: indexPath) as! MessageTableViewCell
        let userId = self.userIds[indexPath.row]
        if userId.contains("guide_") {
            if let guideData = (GuideRequester.shared.dataList.filter { $0.id == userId }).first {
                cell.configure(guideData: guideData)
            }
        }
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let detail = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        detail.set(targetUserId: self.userIds[indexPath.row])
        self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
    }
}


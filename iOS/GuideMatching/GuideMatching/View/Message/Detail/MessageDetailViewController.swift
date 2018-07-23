//
//  MessageDetailViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MessageDetailViewController: KeyboardRespondableViewController {
    
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var textField: UITextField!
    @IBOutlet private weak var inputViewBottomConstraint: NSLayoutConstraint!
    
    private var targetUserId: String!
    private var messages = [MessageData]()
    private var dummyLeftCell: MessageDetailLeftTableViewCell?
    private var dummyRightCell: MessageDetailRightTableViewCell?
    private var tmpMessageIds = [String]()
    
    func set(targetUserId: String) {
        self.targetUserId = targetUserId
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.dummyLeftCell = self.tableView.dequeueReusableCell(withIdentifier: "MessageDetailLeftTableViewCell") as? MessageDetailLeftTableViewCell
        self.dummyRightCell = self.tableView.dequeueReusableCell(withIdentifier: "MessageDetailRightTableViewCell") as? MessageDetailRightTableViewCell
        
        if self.targetUserId.contains("guide_") {
            if let guideData = (GuideRequester.shared.dataList.filter { $0.id == self.targetUserId }).first {
                self.nameLabel.text = guideData.name
            }
        } else {
            if let guestData = (GuestRequester.shared.dataList.filter { $0.id == self.targetUserId }).first {
                self.nameLabel.text = guestData.name
            }
        }
        
        Timer.scheduledTimer(withTimeInterval: 5, repeats: true, block: { [weak self] _ in
            MessageRequester.shared.fetch(completion: { [weak self] result in
                if result {
                    self?.refresh()
                }
            })
        })
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.refresh()
    }
    
    func refresh() {
        
        let messages = MessageRequester.shared.query(userId: self.targetUserId).filter { messageData -> Bool in
            let userId: String
            if SaveData.shared.guestId.count > 0 {
                userId = SaveData.shared.guestId
            } else {
                userId = SaveData.shared.guideId
            }
            return messageData.senderId == userId || messageData.receiverId == userId
        }
        self.messages = messages.sorted(by: { (msg1, msg2) -> Bool in
            return msg1.datetime < msg2.datetime
        })
        
        self.tableView.reloadData()
        self.scrollToBottom()
    }
    
    private func isVisibleBottom() -> Bool {
        
        var lastMessageId = ""
        if self.messages.count >= 2 {
            lastMessageId = self.messages[self.messages.count - 2].messageId
        }
        for i in 0..<self.tableView.visibleCells.count {
            let cell = self.tableView.visibleCells[i]
            if let leftCell = cell as? MessageDetailLeftTableViewCell {
                if lastMessageId == leftCell.getMessageId() {
                    return true
                }
            } else if let rightCell = cell as? MessageDetailRightTableViewCell {
                if lastMessageId == rightCell.getMessageId() {
                    return true
                }
            }
        }
        return false
    }
    
    private func scrollToBottom() {
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1) {
            if self.tableView.contentSize.height > self.tableView.frame.size.height {
                let offset = CGPoint(x: 0, y: self.tableView.contentSize.height - self.tableView.frame.size.height)
                self.tableView.setContentOffset(offset, animated: true)
            }
        }
    }
    
    @IBAction func didExitTextField(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapSend(_ sender: Any) {
        
        guard let message = self.textField.text, message.count > 0 else {
            return
        }
        
        let messageId = DateFormatter(dateFormat: "yyyyMMddHHmmssSSS").string(from: Date())
        
        MessageRequester.post(messageId: messageId, targetId: self.targetUserId, message: message, completion: { [weak self] result in
            if result {
                MessageRequester.shared.fetch(completion: { [weak self] result in
                    if result {
                        self?.tmpMessageIds.removeAll()
                        self?.refresh()
                    } else {
                        Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
                    }
                })
            } else {
                Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
        self.textField.text = ""
        
        let userId: String
        if SaveData.shared.guestId.count > 0 {
            userId = SaveData.shared.guestId
        } else {
            userId = SaveData.shared.guideId
        }

        let temporaryMessageData = MessageData(messageId: messageId, senderId: userId, message: message, datetime: Date())
        self.messages.append(temporaryMessageData)
        self.tmpMessageIds.append(messageId)
        self.tableView.reloadData()
        
        if self.isVisibleBottom() {
            self.scrollToBottom()
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    override func animate(with: KeyboardAnimation) {
        self.inputViewBottomConstraint.constant = with.height
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: { [weak self] in
            self?.view.layoutIfNeeded()
        })
        if self.isVisibleBottom() {
            self.scrollToBottom()
        }
    }
}

extension MessageDetailViewController: UITableViewDataSource, UITableViewDelegate {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.messages.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let userId: String
        if SaveData.shared.guestId.count > 0 {
            userId = SaveData.shared.guestId
        } else {
            userId = SaveData.shared.guideId
        }
        
        let message = self.messages[indexPath.row]
        if message.senderId == userId {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MessageDetailRightTableViewCell", for: indexPath) as! MessageDetailRightTableViewCell
            let isTemporary = self.tmpMessageIds.contains(message.messageId)
            cell.configure(data: message, isTemporary: isTemporary)
            return cell
        } else {
            let cell = tableView.dequeueReusableCell(withIdentifier: "MessageDetailLeftTableViewCell", for: indexPath) as! MessageDetailLeftTableViewCell
            cell.configure(data: message)
            return cell
        }
    }
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        
        let userId: String
        if SaveData.shared.guestId.count > 0 {
            userId = SaveData.shared.guestId
        } else {
            userId = SaveData.shared.guideId
        }
        
        let messageData = self.messages[indexPath.row]
        if messageData.senderId == userId {
            return self.dummyRightCell?.height(data: messageData) ?? 0
        } else {
            return self.dummyLeftCell?.height(data: messageData) ?? 0
        }
    }
}

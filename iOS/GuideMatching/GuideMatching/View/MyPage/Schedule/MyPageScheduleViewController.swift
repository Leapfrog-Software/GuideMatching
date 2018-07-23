//
//  MyPageScheduleViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/05.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageScheduleViewController: UIViewController {
    
    struct EditedScheduleData {
        let date: String
        let timeIndex: Int
        let isFree: Bool
    }
    
    @IBOutlet private weak var date1Label: UILabel!
    @IBOutlet private weak var date2Label: UILabel!
    @IBOutlet private weak var date3Label: UILabel!
    @IBOutlet private weak var date4Label: UILabel!
    @IBOutlet private weak var date5Label: UILabel!
    @IBOutlet private weak var date6Label: UILabel!
    @IBOutlet private weak var date7Label: UILabel!
    @IBOutlet private weak var tableView: UITableView!

    private var weekOffset = 0
    private var editedSchedules = [EditedScheduleData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.reload()
    }
    
    private func reload() {

        let latestSunday = Date().latestSunday()
        
        let dateFormatter = DateFormatter(dateFormat: "d\nE")
        self.date1Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7))
        self.date2Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 1))
        self.date3Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 2))
        self.date4Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 3))
        self.date5Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 4))
        self.date6Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 5))
        self.date7Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 6))
        
        self.tableView.reloadData()
    }
    
    private func showCommunicateError() {
        let alert = UIAlertController(title: "エラー", message: "通信に失敗しました", preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    @IBAction func onTapPreviousWeek(_ sender: Any) {
        
        if self.weekOffset > 0 {
            self.weekOffset -= 1
            self.reload()
        }
    }
    
    @IBAction func onTapNextWeek(_ sender: Any) {
        
        if self.weekOffset < 5 {
            self.weekOffset += 1
            self.reload()
        }
    }
    
    @IBAction func onTapUpdate(_ sender: Any) {
        
        guard var myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }

        let dateFormatter = DateFormatter(dateFormat: "yyyyMMdd")
        
        self.editedSchedules.forEach { editedSchedule in
            if let index = myGuideData.schedules.index(where: { dateFormatter.string(from: $0.date) == editedSchedule.date }) {
                myGuideData.schedules[index].isFreeList[editedSchedule.timeIndex] = editedSchedule.isFree
            } else {
                guard let date = dateFormatter.date(from: editedSchedule.date) else {
                    return
                }
                var isFreeList = [Bool]()
                for _ in 0..<48 {
                    isFreeList.append(false)
                }
                isFreeList[editedSchedule.timeIndex] = editedSchedule.isFree
                if let scheduleData = GuideScheduleData(date: date, isFreeList: isFreeList) {
                    myGuideData.schedules.append(scheduleData)
                }
            }
        }
        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
            if resultUpdate {
                GuideRequester.shared.fetch(completion: { resultFetch in
                    if resultFetch {
                        let alert = UIAlertController(title: "確認", message: "スケジュールを更新しました", preferredStyle: .alert)
                        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: { _ in
                            self.pop(animationType: .horizontal)
                        }))
                        self.present(alert, animated: true, completion: nil)
                    } else {
                        self.showCommunicateError()
                    }
                })
            } else {
                self.showCommunicateError()
            }
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPageScheduleViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 48
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageScheduleTableViewCell", for: indexPath) as! MyPageScheduleTableViewCell
        
        var states = [MyPageScheduleTableViewCell.State]()

        let dateFormatter = DateFormatter(dateFormat: "yyyyMMdd")
        let today = Date()
        
        let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId)
        
        for i in 0..<7 {
            let targetDate = Date().latestSunday().add(day: self.weekOffset * 7 + i)
            let targetDateStr = dateFormatter.string(from: targetDate)

            var stateType = MyPageScheduleTableViewCell.StateType.free
            var edited = false
            if let editedSchedule = (self.editedSchedules.filter { return $0.date == targetDateStr && indexPath.row == $0.timeIndex }).first {
                edited = true
                stateType = editedSchedule.isFree ? .free : .ng
            } else if let scheduleData = (myGuideData?.schedules.filter { $0.date.isSameDay(with: targetDate) })?.first {
                stateType = scheduleData.isFreeList[indexPath.row] ? .free : .ng
            } else {
                stateType = .unselected
            }
            
            let isPast = (!targetDate.isSameDay(with: today)) && (targetDate < today)
            
            let state = MyPageScheduleTableViewCell.State(type: stateType, edited: edited, isPast: isPast)
            states.append(state)
        }
        
        cell.configure(cellIndex: indexPath.row, states: states, onTap: { [weak self] dateOffset, timeOffset in
            self?.onTapSchedule(dateOffset: dateOffset, timeOffset: timeOffset)
        })
        
        return cell
    }
    
    private func onTapSchedule(dateOffset: Int, timeOffset: Int) {
        
        let targetDate = Date().latestSunday().add(day: self.weekOffset * 7 + dateOffset)
        let targetDateStr = DateFormatter(dateFormat: "yyyyMMdd").string(from: targetDate)
        
        var isFree = true
        
        if let index = self.editedSchedules.index(where: { return $0.date == targetDateStr && $0.timeIndex == timeOffset }) {
            isFree = !self.editedSchedules[index].isFree
            self.editedSchedules.remove(at: index)
        }
        
        let editedSchedule = EditedScheduleData(date: targetDateStr, timeIndex: timeOffset, isFree: isFree)
        self.editedSchedules.append(editedSchedule)
        
        self.tableView.reloadData()
    }
}

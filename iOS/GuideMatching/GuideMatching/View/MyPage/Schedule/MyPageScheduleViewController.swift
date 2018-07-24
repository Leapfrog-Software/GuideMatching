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
    
    @IBOutlet private weak var monthBaseView: UIView!
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
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.reload()
        
        self.tableView.contentOffset = CGPoint(x: 0, y: 44 * 24)
    }
    
    private func reload() {

        let latestSunday = Date().latestSunday()
        
        let dateFormatter = DateFormatter(dateFormat: "d\nE")
        let dates = [0, 1, 2, 3, 4, 5, 6].map { latestSunday.add(day: self.weekOffset * 7 + $0) }
        [self.date1Label, self.date2Label, self.date3Label, self.date4Label, self.date5Label, self.date6Label, self.date7Label].enumerated().forEach { index, label in
            let date = dates[index]
            label?.text = dateFormatter.string(from: date)
            if date.isSunday() {
                label?.textColor = .weekdaySunday
            } else if date.isSuturday() {
                label?.textColor = .weekdaySuturday
            } else {
                label?.textColor = .black
            }
        }
        
        self.monthBaseView.subviews.forEach { $0.removeFromSuperview() }
        if dates[0].isSameMonth(with: dates[6]) {
            let label = self.createMonthLabel(text: dates[0].toMonthYearText(),
                                              frame: CGRect(origin: .zero, size: self.monthBaseView.frame.size))
            self.monthBaseView.addSubview(label)
        } else {
            var firstMonthLength = 1
            for i in 1..<7 {
                if dates[0].isSameMonth(with: dates[i]) {
                    firstMonthLength += 1
                }
            }
            let frame1 = CGRect(x: 0, y: 0,
                                width: CGFloat(firstMonthLength) * self.monthBaseView.frame.size.width / 7,
                                height: self.monthBaseView.frame.size.height)
            let label1 = self.createMonthLabel(text: dates[0].toMonthYearText(),
                                               frame: frame1)
            self.monthBaseView.addSubview(label1)
            
            let frame2 = CGRect(x: label1.frame.size.width, y: 0,
                                width: self.monthBaseView.frame.size.width - label1.frame.size.width,
                                height: self.monthBaseView.frame.size.height)
            let label2 = self.createMonthLabel(text: dates[6].toMonthYearText(),
                                               frame: frame2)
            self.monthBaseView.addSubview(label2)
            
            let separator = UIView()
            separator.backgroundColor = .scheduleSeparator
            separator.frame = CGRect(x: label1.frame.size.width, y: 0, width: 1, height: self.monthBaseView.frame.size.height)
            self.monthBaseView.addSubview(separator)
        }
        
        self.tableView.reloadData()
    }
    
    private func createMonthLabel(text: String, frame: CGRect) -> UILabel {

        let label = UILabel()
        label.text = text
        label.textColor = .white
        label.textAlignment = .center
        label.font = UIFont.boldSystemFont(ofSize: 14)
        label.frame = frame
        return label
    }
    
    private func showCommunicateError() {
        Dialog.show(style: .error, title: "エラー", message: "通信に失敗しました", actions: [DialogAction(title: "OK", action: nil)])
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
        
        Loading.start()
        
        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
            if resultUpdate {
                GuideRequester.shared.fetch(completion: { resultFetch in
                    Loading.stop()
                    
                    if resultFetch {
                        let action = DialogAction(title: "OK", action: { [weak self] in
                            self?.pop(animationType: .horizontal)
                        })
                        Dialog.show(style: .success, title: "確認", message: "スケジュールを更新しました", actions: [action])
                    } else {
                        self.showCommunicateError()
                    }
                })
            } else {
                Loading.stop()
                self.showCommunicateError()
            }
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPageScheduleViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 44
    }
    
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

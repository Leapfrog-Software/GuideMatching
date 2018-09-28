//
//  GuideDetailScheduleView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/04.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailScheduleView: UIView {
    
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
    
    private var scheduleDatas = [GuideScheduleData]()
    private var guideId: String!
    private var didSelect: ((Date, Int) -> ())?
    
    func set(guideId: String, schedules: [GuideScheduleData], didSelect: @escaping ((Date, Int) -> ())) {
        self.guideId = guideId
        self.scheduleDatas = schedules
        self.didSelect = didSelect
    }

    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        self.tableView.register(UINib(nibName: "GuideDetailScheduleTableViewCell", bundle: nil), forCellReuseIdentifier: "GuideDetailScheduleTableViewCell")
        
        self.reload()
        
        DispatchQueue.main.asyncAfter(deadline: .now() + 0.1, execute: {
            self.tableView.contentOffset = CGPoint(x: 0, y: 44 * 24)
        })
    }
    
    func changeToNext() {
        if self.weekOffset < 5 {
            self.weekOffset += 1
            self.reload()
        }
    }
    
    func changeToPrevious() {
        if self.weekOffset > 0 {
            self.weekOffset -= 1
            self.reload()
        }
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
}

extension GuideDetailScheduleView: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, heightForRowAt indexPath: IndexPath) -> CGFloat {
        return 44
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 48
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "GuideDetailScheduleTableViewCell", for: indexPath) as! GuideDetailScheduleTableViewCell
        
        let today = Date()
        
        var states = [GuideDetailScheduleTableViewCell.State]()
        for i in 0..<7 {
            let targetDate = Date().latestSunday().add(day: self.weekOffset * 7 + i)
            
            var type = GuideDetailScheduleTableViewCell.StateType.free
            
            let isReserved = ReserveRequester.shared.dataList.contains(where: { reserveData -> Bool in
                if reserveData.guideId == self.guideId {
                    if reserveData.day.isSameDay(with: targetDate) {
                        if indexPath.row >= reserveData.startTime && indexPath.row <= reserveData.endTime {
                            return true
                        }
                    }
                }
                return false
            })
            
            if isReserved {
                type = .reserved
                
            } else if let scheduleData = (self.scheduleDatas.filter { $0.date.isSameDay(with: targetDate) }).first {
                if scheduleData.isFreeList[indexPath.row] {
                    type = .free
                } else {
                    type = .ng
                }
            } else {
                type = .unselected
            }
            
            let isPast = (!targetDate.isSameDay(with: today)) && (targetDate < today)
            
            let state = GuideDetailScheduleTableViewCell.State(type: type, isPast: isPast)
            states.append(state)
        }
        
        cell.configure(cellIndex: indexPath.row, states:states, onTap: { [weak self] dateOffset, timeOffset in
            let weekOffset = self?.weekOffset ?? 0
            let targetDate = Date().latestSunday().add(day: weekOffset * 7 + dateOffset)
            self?.didSelect?(targetDate, timeOffset)
        })
        
        return cell
    }
}

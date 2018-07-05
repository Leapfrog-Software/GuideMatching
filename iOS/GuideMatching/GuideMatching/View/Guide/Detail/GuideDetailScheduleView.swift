//
//  GuideDetailScheduleView.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/04.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailScheduleView: UIView {
    
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
    
    func set(schedules: [GuideScheduleData]) {
        self.scheduleDatas = schedules
    }

    override func didMoveToSuperview() {
        super.didMoveToSuperview()
        
        self.tableView.register(UINib(nibName: "GuideDetailScheduleTableViewCell", bundle: nil), forCellReuseIdentifier: "GuideDetailScheduleTableViewCell")
        
        self.reload()
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
        self.date1Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7))
        self.date2Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 1))
        self.date3Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 2))
        self.date4Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 3))
        self.date5Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 4))
        self.date6Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 5))
        self.date7Label.text = dateFormatter.string(from: latestSunday.add(day: self.weekOffset * 7 + 6))
        
        self.tableView.reloadData()
    }
}

extension GuideDetailScheduleView: UITableViewDelegate, UITableViewDataSource {
    
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
            if let scheduleData = (self.scheduleDatas.filter { $0.date.isSameDay(with: targetDate) }).first {
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
        
        cell.configure(cellIndex: indexPath.row, states:states, onTap: { dateOffset, timeOffset in
            
        })
        
        return cell
    }
}

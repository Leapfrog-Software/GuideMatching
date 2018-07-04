//
//  GuideDetailScheduleView.swift
//  GuideMatching
//
//  Created by 藤田 祥一 on 2018/07/04.
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
        
        let today = Date()
        let dateFormatter = DateFormatter(dateFormat: "d\nE")
        self.date1Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7))
        self.date2Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 1))
        self.date3Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 2))
        self.date4Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 3))
        self.date5Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 4))
        self.date6Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 5))
        self.date7Label.text = dateFormatter.string(from: today.add(day: self.weekOffset * 7 + 6))
        
        self.tableView.reloadData()
    }
}

extension GuideDetailScheduleView: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return 48
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "GuideDetailScheduleTableViewCell", for: indexPath) as! GuideDetailScheduleTableViewCell
        
        var states = [GuideDetailScheduleTableViewCell.ScheduleState]()
        for i in 0..<7 {
            let targetDate = Date().add(day: self.weekOffset * 7 + i)
            if let scheduleData = (self.scheduleDatas.filter { $0.date.isSameDay(with: targetDate) }).first {
                if scheduleData.isFreeList[indexPath.row] {
                    states.append(.free)
                } else {
                    states.append(.ng)
                }
            } else {
                states.append(.unselected)
            }
        }
        
        cell.configure(cellIndex: indexPath.row, states:states, onTap: { dateOffset, timeOffset in
            
        })
        
        return cell
    }
}

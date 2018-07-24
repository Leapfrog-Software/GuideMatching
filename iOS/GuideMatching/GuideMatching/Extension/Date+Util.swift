//
//  Date+Util.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import Foundation

extension Date {
    
    private var calendar: Calendar {
        return Calendar(identifier: .gregorian)
    }
    
    func add(day: Int) -> Date {
        return calendar.date(byAdding: .day, value: day, to: self) ?? self
    }
    
    func add(month: Int) -> Date {
        return calendar.date(byAdding: .month, value: month, to: self) ?? self
    }
    
    func latestSunday() -> Date {
        
        for i in 0..<7 {
            let shifted = self.add(day: -i)
            if calendar.dateComponents([.weekday], from: shifted).weekday == 1 {
                return shifted
            }
        }
        return self
    }
    
    func isSameDay(with: Date) -> Bool {
        
        let components1 = calendar.dateComponents([.year, .month, .day], from: self)
        let components2 = calendar.dateComponents([.year, .month, .day], from: with)
        return components1.year == components2.year
            && components1.month == components2.month
            && components1.day == components2.day
    }
    
    func isSameMonth(with: Date) -> Bool {
        
        let components1 = calendar.dateComponents([.year, .month], from: self)
        let components2 = calendar.dateComponents([.year, .month], from: with)
        return components1.year == components2.year
            && components1.month == components2.month
    }
    
    func isSameYear(with: Date) -> Bool {
        
        let components1 = calendar.dateComponents([.year, .month], from: self)
        let components2 = calendar.dateComponents([.year, .month], from: with)
        return components1.year == components2.year
    }
    
    func isSunday() -> Bool {
        return calendar.dateComponents([.weekday], from: self).weekday == 1
    }
    
    func isSuturday() -> Bool {
        return calendar.dateComponents([.weekday], from: self).weekday == 7
    }
    
    func toMonthYearText() -> String {
        let monthAry = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"]
        let components = calendar.dateComponents([.year, .month], from: self)
        let month = (components.month ?? 0) - 1
        let year = components.year ?? 0
        return monthAry[month] + ". \(year)"
    }
}

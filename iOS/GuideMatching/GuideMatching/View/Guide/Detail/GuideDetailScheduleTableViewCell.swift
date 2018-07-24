//
//  GuideDetailScheduleTableViewCell.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/07/04.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideDetailScheduleTableViewCell: UITableViewCell {
    
    enum StateType {
        case free
        case ng
        case unselected
        case reserved
    }
    
    struct State {
        let type: StateType
        let isPast: Bool
    }

    @IBOutlet private weak var timeLabel: UILabel!
    @IBOutlet private weak var date1Label: UILabel!
    @IBOutlet private weak var date2Label: UILabel!
    @IBOutlet private weak var date3Label: UILabel!
    @IBOutlet private weak var date4Label: UILabel!
    @IBOutlet private weak var date5Label: UILabel!
    @IBOutlet private weak var date6Label: UILabel!
    @IBOutlet private weak var date7Label: UILabel!
    
    private var cellIndex = 0
    private var onTap: ((Int, Int) -> ())?
    
    func configure(cellIndex: Int, states: [State], onTap: @escaping ((Int, Int) -> ())) {
        
        self.timeLabel.text = String(format: "%02d", Int(cellIndex / 2)) + ":" + String(format: "%02d", 30 * Int(cellIndex % 2))
        
        for i in 0..<7 {
            let label = [date1Label, date2Label, date3Label, date4Label, date5Label, date6Label, date7Label][i]
            switch states[i].type {
            case .free:
                label?.text = "○"
            case .ng:
                label?.text = "-"
            case .unselected:
                label?.text = "-"
            case .reserved:
                label?.text = "■"
            }
            if states[i].isPast {
                label?.backgroundColor = .scheduleIsPast
            } else if states[i].type == .reserved {
                label?.backgroundColor = .scheduleReserved
            } else {
                label?.backgroundColor = .scheduleNone
            }
        }

        self.cellIndex = cellIndex
        self.onTap = onTap
    }

    @IBAction func onTapDate1(_ sender: Any) {
        self.onTap?(0, self.cellIndex)
    }
    
    @IBAction func onTapDate2(_ sender: Any) {
        self.onTap?(1, self.cellIndex)
    }
    
    @IBAction func onTapDate3(_ sender: Any) {
        self.onTap?(2, self.cellIndex)
    }
    
    @IBAction func onTapDate4(_ sender: Any) {
        self.onTap?(3, self.cellIndex)
    }
    
    @IBAction func onTapDate5(_ sender: Any) {
        self.onTap?(4, self.cellIndex)
    }
    
    @IBAction func onTapDate6(_ sender: Any) {
        self.onTap?(5, self.cellIndex)
    }
    
    @IBAction func onTapDate7(_ sender: Any) {
        self.onTap?(6, self.cellIndex)
    }
}

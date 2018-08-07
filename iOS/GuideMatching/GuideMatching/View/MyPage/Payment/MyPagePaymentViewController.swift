//
//  MyPagePaymentViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPagePaymentViewController: UIViewController {
    
    enum CellType {
        case total
        case history
        case noData
        case account
    }
    
    struct CellData {
        let cellType: CellType
        let totalAmount: Int?
        let month: Date?
        let monthAmount: Int?
    }

    @IBOutlet private weak var tableView: UITableView!
    
    private var cellDatas = [CellData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        reloadTable()
    }
    
    func reloadTable() {
        
        self.cellDatas.removeAll()
        
        guard let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        var totalAmount = 0
        ReserveRequester.shared.dataList.forEach { reserveData in
            if reserveData.guideId == myGuideData.id {
                totalAmount += myGuideData.fee * (reserveData.endTime - reserveData.startTime)
            }
        }
        let totalCellData = CellData(cellType: .total, totalAmount: totalAmount, month: nil, monthAmount: nil)
        self.cellDatas.append(totalCellData)
        
        let monthList = self.getMonthList()
        if monthList.isEmpty {
            let noDataCellData = CellData(cellType: .noData, totalAmount: nil, month: nil, monthAmount: nil)
            self.cellDatas.append(noDataCellData)
        } else {
            monthList.forEach { month in
                var amount = 0
                ReserveRequester.shared.dataList.forEach { reserveData in
                    if reserveData.guideId == SaveData.shared.guideId {
                        if reserveData.day.isSameMonth(with: month) {
                            amount += myGuideData.fee * (reserveData.endTime - reserveData.startTime)
                        }
                    }
                }
                let historyCellData = CellData(cellType: .history, totalAmount: nil, month: month, monthAmount: amount)
                self.cellDatas.append(historyCellData)
            }
        }
        
        let accountTableCellData = CellData(cellType: .account, totalAmount: nil, month: nil, monthAmount: nil)
        self.cellDatas.append(accountTableCellData)
        
        self.tableView.reloadData()
    }
    
    private func getMonthList() -> [Date] {
        
        var monthList = [Date]()
        
        ReserveRequester.shared.dataList.filter { $0.guideId == SaveData.shared.guideId }.forEach { reserveData in
            if !monthList.contains(where: { $0.isSameMonth(with: reserveData.day) }) {
                monthList.append(reserveData.day)
            }
        }
        return monthList
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPagePaymentViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cellData = self.cellDatas[indexPath.row]
        
        switch cellData.cellType {
        case .total:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentTotalTableViewCell", for: indexPath) as! MyPagePaymentTotalTableViewCell
            cell.configure(amount: cellData.totalAmount!)
            return cell
        case .history:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentHistoryTableViewCell", for: indexPath) as! MyPagePaymentHistoryTableViewCell
            cell.configure(month: cellData.month!, amount: cellData.monthAmount!)
            return cell
        case .noData:
            return tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentNoHistoryTableViewCell", for: indexPath)
        case .account:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPagePaymentAccountTableViewCell", for: indexPath) as! MyPagePaymentAccountTableViewCell
            cell.configure(didTapEdit: { [weak self] in
                let edit = self?.viewController(storyboard: "MyPage", identifier: "MyPageAccountEditViewController") as! MyPageAccountEditViewController
                self?.stack(viewController: edit, animationType: .horizontal)
            })
            return cell
        }
    }
}

//
//  MyPageViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageViewController: UIViewController {
    
    enum CellType {
        case reservationTitle
        case reservationData
        case reservationNoData
        case guideButton
        case guestButton
    }
    
    struct CellData {
        let type: CellType
        let title: String?
        let reserveData: ReserveData?
        let needEstimate: Bool?
    }
    
    @IBOutlet private weak var tableView: UITableView!
    
    private var cellDatas = [CellData]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.tableView.rowHeight = UITableViewAutomaticDimension
        self.tableView.estimatedRowHeight = 100
        
        self.resetCellDatas()
        
        NotificationCenter.default.addObserver(forName: .reserve, object: nil, queue: nil, using: { [weak self] _ in
            self?.resetCellDatas()
            self?.tableView.reloadData()
        })
    }
    
    override func viewWillDisappear(_ animated: Bool) {
        super.viewWillDisappear(animated)
        
        NotificationCenter.default.removeObserver(self, name: .reserve, object: nil)
    }
    
    private func resetCellDatas() {
        
        var cellDatas = [CellData]()
        let today = Date()
        
        if SaveData.shared.guideId.count > 0 {
            let futurereserves = ReserveRequester.shared.dataList.filter { reserveData -> Bool in
                if reserveData.guideId != SaveData.shared.guideId {
                    return false
                }
                guard let endDate = reserveData.toEndDate() else {
                    return false
                }
                if endDate < today {
                    return false
                }
                return true
            }
            
            cellDatas.append(CellData(type: .reservationTitle, title: "Reservation", reserveData: nil, needEstimate: nil))
            
            if !futurereserves.isEmpty {
                let sortedReserves = self.sortReserveDataByStartDate(reserveDatas: futurereserves)
                sortedReserves.forEach { reserveData in
                    cellDatas.append(CellData(type: .reservationData, title: nil, reserveData: reserveData, needEstimate: false))
                }
            } else {
                cellDatas.append(CellData(type: .reservationNoData, title: nil, reserveData: nil, needEstimate: nil))
            }
            cellDatas.append(CellData(type: .guideButton, title: nil, reserveData: nil, needEstimate: nil))
        } else {
            var futureReserves = [ReserveData]()
            var pastReserves = [ReserveData]()
            ReserveRequester.shared.dataList.forEach { reserveData in
                if reserveData.guestId != SaveData.shared.guestId {
                    return
                }
                guard let startDate = reserveData.toStartDate() else {
                    return
                }
                if startDate < today {
                    pastReserves.append(reserveData)
                } else {
                    futureReserves.append(reserveData)
                }
            }
            
            cellDatas.append(CellData(type: .reservationTitle, title: "Reservation", reserveData: nil, needEstimate: nil))
            
            if futureReserves.isEmpty {
                cellDatas.append(CellData(type: .reservationNoData, title: nil, reserveData: nil, needEstimate: nil))
            } else {
                futureReserves.forEach { reserveData in
                    cellDatas.append(CellData(type: .reservationData, title: nil, reserveData: reserveData, needEstimate: nil))
                }
            }
            
            cellDatas.append(CellData(type: .reservationTitle, title: "Order History", reserveData: nil, needEstimate: nil))
            
            if pastReserves.isEmpty {
                cellDatas.append(CellData(type: .reservationNoData, title: nil, reserveData: nil, needEstimate: nil))
            } else {
                pastReserves.forEach { reserveData in
                    var needEstimate = true
                    EstimateRequester.shared.dataList.forEach { estimateData in
                        if estimateData.reserveId == reserveData.id && estimateData.guestId == SaveData.shared.guestId {
                            needEstimate = false
                        }
                    }
                    cellDatas.append(CellData(type: .reservationData, title: nil, reserveData: reserveData, needEstimate: needEstimate))
                }
            }
            cellDatas.append(CellData(type: .guestButton, title: nil, reserveData: nil, needEstimate: nil))
        }
        self.cellDatas = cellDatas
    }
    
    private func sortReserveDataByStartDate(reserveDatas: [ReserveData]) -> [ReserveData] {
        
        return reserveDatas.sorted(by: { reserve1, reserve2 -> Bool in
            guard let startDate1 = reserve1.toStartDate() else {
                return false
            }
            guard let startDate2 = reserve2.toStartDate() else {
                return true
            }
            return startDate1 < startDate2
        })
    }
}

extension MyPageViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.cellDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {

        let cellData = self.cellDatas[indexPath.row]
        
        switch cellData.type {
        case .reservationTitle:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageReservationTitleTableViewCell", for: indexPath) as! MyPageReservationTitleTableViewCell
            cell.configure(title: cellData.title!)
            return cell
        case .reservationData:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageReservationTableViewCell", for: indexPath) as! MyPageReservationTableViewCell
            cell.configure(reserveData: cellData.reserveData!, needEstimate: cellData.needEstimate ?? false)
            return cell
        case .reservationNoData:
            return tableView.dequeueReusableCell(withIdentifier: "MyPageReservationNoneTableViewCell", for: indexPath) as! MyPageReservationNoneTableViewCell
        case .guideButton:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageGuideButtonTableViewCell", for: indexPath) as! MyPageGuideButtonTableViewCell
            cell.configure(didTap: { [weak self] buttonType in
                self?.didTapGuideButton(type: buttonType)
            })
            return cell
        case .guestButton:
            let cell = tableView.dequeueReusableCell(withIdentifier: "MyPageGuestButtonTableViewCell", for: indexPath) as! MyPageGuestButtonTableViewCell
            cell.configure(didTap: { [weak self] in
                let profile = self?.viewController(storyboard: "Initial", identifier: "GuestRegisterViewController") as! GuestRegisterViewController
                profile.set(isEdit: true)
                self?.tabbarViewController()?.stack(viewController: profile, animationType: .horizontal)
            })
            return cell
        }
    }

    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        
        let cellData = self.cellDatas[indexPath.row]
        
        if cellData.type == .reservationData {
            if cellData.needEstimate == true {
                let estimate = self.viewController(storyboard: "MyPage", identifier: "EstimateViewController") as! EstimateViewController
                estimate.set(reserveData: cellData.reserveData!)
                self.tabbarViewController()?.stack(viewController: estimate, animationType: .horizontal)
            } else {
                let detail = self.viewController(storyboard: "MyPage", identifier: "ReserveDetailViewController") as! ReserveDetailViewController
                detail.set(reserveData: cellData.reserveData!)
                self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
            }
        }
    }
    
    private func didTapGuideButton(type: MyPageGuideButtonTableViewCell.ButtonType) {
        
        switch type {
        case .history:
            let history = self.viewController(storyboard: "MyPage", identifier: "MyPageHistoryViewController") as! MyPageHistoryViewController
            self.tabbarViewController()?.stack(viewController: history, animationType: .horizontal)
            
        case .schedule:
            let schedule = self.viewController(storyboard: "MyPage", identifier: "MyPageScheduleViewController") as! MyPageScheduleViewController
            self.tabbarViewController()?.stack(viewController: schedule, animationType: .horizontal)
            
        case .profile:
            let profile = self.viewController(storyboard: "Initial", identifier: "GuideRegisterViewController") as! GuideRegisterViewController
            profile.set(isEdit: true)
            self.tabbarViewController()?.stack(viewController: profile, animationType: .horizontal)
            
        case .payment:
            let payment = self.viewController(storyboard: "MyPage", identifier: "MyPagePaymentViewController") as! MyPagePaymentViewController
            self.tabbarViewController()?.stack(viewController: payment, animationType: .horizontal)
            
        case .review:
            let review = self.viewController(storyboard: "MyPage", identifier: "CustomerReviewViewController") as! CustomerReviewViewController
            self.tabbarViewController()?.stack(viewController: review, animationType: .horizontal)
        }
    }
}

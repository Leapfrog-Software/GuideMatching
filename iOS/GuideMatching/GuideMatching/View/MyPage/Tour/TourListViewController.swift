//
//  TourListViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/09/30.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class TourListViewController: UIViewController {

    @IBOutlet private weak var tourBaseStackView: UIStackView!

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.resetContents()
    }
    
    func resetContents() {
        
        guard let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        self.tourBaseStackView.arrangedSubviews.forEach {
            self.tourBaseStackView.removeArrangedSubview($0)
            $0.isHidden = true
        }
        myGuideData.tours.forEach { tourData in
            let tourView = UINib(nibName: "GuideRegisterTourView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! GuideRegisterTourView
            tourView.set(tourData: tourData, didTap: { [weak self] tourData in
                self?.onTapTour(tourData: tourData)
            })
            self.tourBaseStackView.addArrangedSubview(tourView)
        }
    }
    
    private func onTapTour(tourData: GuideTourData) {
        let tour = self.viewController(storyboard: "MyPage", identifier: "CreateTourViewController") as! CreateTourViewController
        tour.set(guideTourData: tourData)
        self.stack(viewController: tour, animationType: .horizontal)
    }

    @IBAction func onTapCreateTour(_ sender: Any) {
        self.view.endEditing(true)
        
        let tour = self.viewController(storyboard: "MyPage", identifier: "CreateTourViewController") as! CreateTourViewController
        self.stack(viewController: tour, animationType: .horizontal)
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

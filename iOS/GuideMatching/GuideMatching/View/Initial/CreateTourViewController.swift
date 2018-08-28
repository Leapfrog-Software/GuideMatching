//
//  CreateTourViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CreateTourViewController: UIViewController {
    
    @IBOutlet private weak var tourImageView: UIImageView!
    @IBOutlet private weak var tourTitleTextField: UITextField!
    @IBOutlet private weak var areaTextField: UITextField!
    @IBOutlet private weak var descriptionTextField: UITextField!
    @IBOutlet private weak var feeTextField: UITextField!
    @IBOutlet private weak var highlights1View: UIView!
    @IBOutlet private weak var highlights1ViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var highlights1TitleTextField: UITextField!
    @IBOutlet private weak var highlights1ImageView: UIImageView!
    @IBOutlet private weak var highlights1BodyTextField: UITextField!
    @IBOutlet private weak var highlights2View: UIView!
    @IBOutlet private weak var highlights2ViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var highlights2TitleTextField: UITextField!
    @IBOutlet private weak var highlights2ImageView: UIImageView!
    @IBOutlet private weak var highlights2BodyTextField: UITextField!
    @IBOutlet private weak var highlights3View: UIView!
    @IBOutlet private weak var highlights3ViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var highlights3TitleTextField: UITextField!
    @IBOutlet private weak var highlights3ImageView: UIImageView!
    @IBOutlet private weak var highlights3BodyTextField: UITextField!
    @IBOutlet private weak var addHighlightsButton: UIButton!
    @IBOutlet private weak var daysLabel: UILabel!
    @IBOutlet private weak var startTimeLabel: UILabel!
    @IBOutlet private weak var endTimeLabel: UILabel!
    @IBOutlet private weak var departurePointTextField: UITextField!
    @IBOutlet private weak var returnDetailTextField: UITextField!
    @IBOutlet private weak var inclusionsTextField: UITextField!
    @IBOutlet private weak var exclusionsTextField: UITextField!
    @IBOutlet private weak var deleteButton: UIButton!
    
    private var guideTourData: GuideTourData?
    private var highlights1Image: UIImage?
    private var highlights2Image: UIImage?
    private var highlights3Image: UIImage?
    private var selectedDays: [Date]?
    private var selectedStartTime: Int?
    private var selectedEndTime: Int?
    
    func set(guideTourData: GuideTourData) {
        self.guideTourData = guideTourData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
        
        if self.guideTourData == nil {
            self.deleteButton.isHidden = true
        }
    }
    
    private func initContents() {
        
        guard let tourData = self.guideTourData else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id, imageView: self.tourImageView)
        
        self.tourTitleTextField.text = tourData.name
        self.areaTextField.text = tourData.area
        self.feeTextField.text = CommonUtility.digit3Format(value: tourData.fee) + " JPY"
        self.descriptionTextField.text = tourData.description
        
        var highlightsCnt = 0
        
        if tourData.highlights1Title.isEmpty && tourData.highlights1Body.isEmpty {
            self.highlights1View.isHidden = true
            self.highlights1ViewHeightConstraint.constant = 0
        } else {
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h1", imageView: self.highlights1ImageView)
            self.highlights1TitleTextField.text = tourData.highlights1Title
            self.highlights1BodyTextField.text = tourData.highlights1Body
            
            highlightsCnt += 1
        }
        
        if tourData.highlights2Title.isEmpty && tourData.highlights2Body.isEmpty {
            self.highlights2View.isHidden = true
            self.highlights2ViewHeightConstraint.constant = 0
        } else {
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h2", imageView: self.highlights2ImageView)
            self.highlights2TitleTextField.text = tourData.highlights2Title
            self.highlights2BodyTextField.text = tourData.highlights2Body
            
            highlightsCnt += 1
        }
        
        if tourData.highlights3Title.isEmpty && tourData.highlights3Body.isEmpty {
            self.highlights3View.isHidden = true
            self.highlights3ViewHeightConstraint.constant = 0
        } else {
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h3", imageView: self.highlights3ImageView)
            self.highlights3TitleTextField.text = tourData.highlights3Title
            self.highlights3BodyTextField.text = tourData.highlights3Body
            highlightsCnt += 1
        }
        
        if highlightsCnt == 3 {
            self.addHighlightsButton.isHidden = true
        }
    }
    
    private func showError(message: String) {
        Dialog.show(style: .error, title: "エラー", message: message, actions: [DialogAction(title: "OK", action: nil)])
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapAddHighlights(_ sender: Any) {
        
        let highlightsViewHeight = CGFloat(200)
        
        if self.highlights1View.isHidden {
            self.highlights1View.isHidden = false
            self.highlights1ViewHeightConstraint.constant = highlightsViewHeight
            self.highlights1ImageView.image = nil
            self.highlights1Image = nil
            self.highlights1TitleTextField.text = ""
            self.highlights1BodyTextField.text = ""
            
            if self.highlights2View.isHidden && self.highlights3View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
            
        } else if self.highlights2View.isHidden {
            self.highlights2View.isHidden = false
            self.highlights2ViewHeightConstraint.constant = highlightsViewHeight
            self.highlights2ImageView.image = nil
            self.highlights2Image = nil
            self.highlights2TitleTextField.text = ""
            self.highlights2BodyTextField.text = ""

            if self.highlights1View.isHidden && self.highlights3View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
            
        } else if self.highlights3View.isHidden {
            self.highlights3View.isHidden = false
            self.highlights3ViewHeightConstraint.constant = highlightsViewHeight
            self.highlights3ImageView.image = nil
            self.highlights3Image = nil
            self.highlights3TitleTextField.text = ""
            self.highlights3BodyTextField.text = ""

            if self.highlights1View.isHidden && self.highlights2View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
        }
    }
    
    @IBAction func onTapUpdate(_ sender: Any) {

        var newTourData = GuideTourData()
        newTourData.name = self.tourTitleTextField.text ?? ""
        newTourData.area = self.areaTextField.text ?? ""
        newTourData.description = self.descriptionTextField.text ?? ""
        
        guard let fee = Int(self.feeTextField.text ?? "") else {
            self.showError(message: "不適切な料金設定です")
            return
        }
        newTourData.fee = fee
        
        if !self.highlights1View.isHidden {
            newTourData.highlights1Title = self.highlights1TitleTextField.text ?? ""
            newTourData.highlights1Body = self.highlights1BodyTextField.text ?? ""
        }
        if !self.highlights2View.isHidden {
            newTourData.highlights2Title = self.highlights2TitleTextField.text ?? ""
            newTourData.highlights2Body = self.highlights2BodyTextField.text ?? ""
        }
        if !self.highlights3View.isHidden {
            newTourData.highlights3Title = self.highlights3TitleTextField.text ?? ""
            newTourData.highlights3Body = self.highlights3BodyTextField.text ?? ""
        }
        
        guard let selectedDays = self.selectedDays else {
            self.showError(message: "日付の入力がありません")
            return
        }
        newTourData.days = selectedDays
        
        guard let selectedStartTime = self.selectedStartTime else {
            self.showError(message: "開始時刻の入力がありません")
            return
        }
        newTourData.startTime = selectedStartTime
        
        guard let selectedEndTime = self.selectedEndTime else {
            self.showError(message: "終了時刻の入力がありません")
            return
        }
        newTourData.endTime = selectedEndTime
        
        newTourData.departurePoint = self.departurePointTextField.text ?? ""
        newTourData.returnDetail = self.returnDetailTextField.text ?? ""
        newTourData.inclusions = self.inclusionsTextField.text ?? ""
        newTourData.exclusions = self.exclusionsTextField.text ?? ""
        
        guard var myGUideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        if let oldTourData = self.guideTourData {
            newTourData.id = oldTourData.id
            if let index = myGUideData.tours.index(where: { $0.id == oldTourData.id }) {
                myGUideData.tours[index] = newTourData
            }
        } else {
            newTourData.id = myGUideData.id + "_" + "\(myGUideData.tours.count)"
            myGUideData.tours.append(newTourData)
        }
        
        Loading.start()
        
        AccountRequester.updateGuide(guideData: myGUideData, completion: { resultUpdate in
            GuideRequester.shared.fetch(completion: { resultFetch in
                Loading.stop()
                
                let message = (self.guideTourData != nil) ? "ツアーを更新しました" : "ツアーを作成しました"
                let action = DialogAction(title: "OK", action: { [weak self] in
                    self?.pop(animationType: .horizontal)
                })
                Dialog.show(style: .success, title: "確認", message: message, actions: [action])
                
                if let guideRegister = self.parent as? GuideRegisterViewController {
                    guideRegister.resetContents()
                }
            })
        })
    }
    
    @IBAction func onTapDelete(_ sender: Any) {
        
        guard var myGUideData = GuideRequester.shared.query(id: SaveData.shared.guideId),
            let oldTourData = self.guideTourData else {
            return
        }
        if let index = myGUideData.tours.index(where: { $0.id == oldTourData.id }) {
            myGUideData.tours.remove(at: index)
            
            let action = DialogAction(title: "OK", action: { [weak self] in
                self?.pop(animationType: .horizontal)
            })
            Dialog.show(style: .success, title: "確認", message: "ツアーを削除しました", actions: [action])
            
            if let guideRegister = self.parent as? GuideRegisterViewController {
                guideRegister.resetContents()
            }
        }
    }
}

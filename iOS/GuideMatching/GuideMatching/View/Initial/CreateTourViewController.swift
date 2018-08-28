//
//  CreateTourViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class CreateTourViewController: UIViewController {
    
    enum ImageType {
        case tour
        case highlights1
        case highlights2
        case highlights3
    }
    
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
    private var tourImage: UIImage?
    private var highlights1Image: UIImage?
    private var highlights2Image: UIImage?
    private var highlights3Image: UIImage?
    private var selectedDays: [Int]?
    private var selectedStartTime: Int?
    private var selectedEndTime: Int?
    private var pickerTarget: ImageType?
    
    private var highlightsViewHeight = CGFloat(320)
    
    func set(guideTourData: GuideTourData) {
        self.guideTourData = guideTourData
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initHighlights()
        self.initContents()
        
        if self.guideTourData == nil {
            self.deleteButton.isHidden = true
        }
    }
    
    private func initHighlights() {
        self.highlights1View.isHidden = true
        self.highlights1ViewHeightConstraint.constant = 0
        self.highlights2View.isHidden = true
        self.highlights2ViewHeightConstraint.constant = 0
        self.highlights3View.isHidden = true
        self.highlights3ViewHeightConstraint.constant = 0
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
        
        if tourData.highlights1Title.isEmpty && tourData.highlights1Body.isEmpty {
            self.highlights1View.isHidden = true
            self.highlights1ViewHeightConstraint.constant = 0
        } else {
            self.highlights1View.isHidden = false
            self.highlights1ViewHeightConstraint.constant = self.highlightsViewHeight
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h1", imageView: self.highlights1ImageView)
            self.highlights1TitleTextField.text = tourData.highlights1Title
            self.highlights1BodyTextField.text = tourData.highlights1Body
        }
        
        if tourData.highlights2Title.isEmpty && tourData.highlights2Body.isEmpty {
            self.highlights2View.isHidden = true
            self.highlights2ViewHeightConstraint.constant = 0
        } else {
            self.highlights2View.isHidden = false
            self.highlights2ViewHeightConstraint.constant = self.highlightsViewHeight
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h2", imageView: self.highlights2ImageView)
            self.highlights2TitleTextField.text = tourData.highlights2Title
            self.highlights2BodyTextField.text = tourData.highlights2Body
        }
        
        if tourData.highlights3Title.isEmpty && tourData.highlights3Body.isEmpty {
            self.highlights3View.isHidden = true
            self.highlights3ViewHeightConstraint.constant = 0
        } else {
            self.highlights3View.isHidden = false
            self.highlights3ViewHeightConstraint.constant = self.highlightsViewHeight
            ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-h3", imageView: self.highlights3ImageView)
            self.highlights3TitleTextField.text = tourData.highlights3Title
            self.highlights3BodyTextField.text = tourData.highlights3Body
        }
        
        if self.highlights3View.isHidden == false {
            self.addHighlightsButton.isHidden = true
        }
    }
    
    private func showError(message: String) {
        Dialog.show(style: .error, title: "エラー", message: message, actions: [DialogAction(title: "OK", action: nil)])
    }
    
    private func createDays() -> [Date] {

        var days = [Date]()
        for i in 0..<14 {
            days.append(Date().add(day: i))
        }
        return days
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
    
    @IBAction func onTapAddHighlights(_ sender: Any) {
        
        if self.highlights1View.isHidden {
            self.highlights1View.isHidden = false
            self.highlights1ViewHeightConstraint.constant = self.highlightsViewHeight
            self.highlights1ImageView.image = UIImage(named: "image_guide")
            self.highlights1Image = nil
            self.highlights1TitleTextField.text = ""
            self.highlights1BodyTextField.text = ""
            
            if !self.highlights2View.isHidden && !self.highlights3View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
            
        } else if self.highlights2View.isHidden {
            self.highlights2View.isHidden = false
            self.highlights2ViewHeightConstraint.constant = self.highlightsViewHeight
            self.highlights2ImageView.image = UIImage(named: "image_guide")
            self.highlights2Image = nil
            self.highlights2TitleTextField.text = ""
            self.highlights2BodyTextField.text = ""

            if !self.highlights1View.isHidden && !self.highlights3View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
            
        } else if self.highlights3View.isHidden {
            self.highlights3View.isHidden = false
            self.highlights3ViewHeightConstraint.constant = self.highlightsViewHeight
            self.highlights3ImageView.image = UIImage(named: "image_guide")
            self.highlights3Image = nil
            self.highlights3TitleTextField.text = ""
            self.highlights3BodyTextField.text = ""

            if !self.highlights1View.isHidden && !self.highlights2View.isHidden {
                self.addHighlightsButton.isHidden = true
            }
        }
    }
    
    @IBAction func onTapDays(_ sender: Any) {
        let multiPicker = self.viewController(storyboard: "Common", identifier: "MultiPickerViewController") as! MultiPickerViewController
        let days = self.createDays()
        let dataArray = days.map { DateFormatter(dateFormat: "MM/dd(E)").string(from: $0) }
        multiPicker.set(title: "Days", dataArray: dataArray, defaultIndexes: self.selectedDays ?? [], completion: { [weak self] selectedIndexes in
            self?.selectedDays = selectedIndexes
            
            let daysStr = selectedIndexes.map { dataArray[$0] }.joined(separator: " ")
            self?.daysLabel.text = daysStr
        })
        self.stack(viewController: multiPicker, animationType: .none)
    }
    
    @IBAction func onTapStartTime(_ sender: Any) {
        var times = [Int]()
        for i in 0..<48 {
            times.append(i)
        }
        let dataArray = times.map { CommonUtility.timeOffsetToString(offset: $0) }
        let defaultIndex = self.selectedStartTime ?? 0
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Time", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            self?.startTimeLabel.text = dataArray[index]
            self?.selectedStartTime = index
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapEndTime(_ sender: Any) {
        var times = [Int]()
        for i in 0..<48 {
            times.append(i)
        }
        let dataArray = times.map { CommonUtility.timeOffsetToString(offset: $0) }
        let defaultIndex = self.selectedEndTime ?? 0
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Time", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            self?.endTimeLabel.text = dataArray[index]
            self?.selectedEndTime = index
        })
        self.tabbarViewController()?.stack(viewController: picker, animationType: .none)
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
        newTourData.days = selectedDays.map { self.createDays()[$0] }
        
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
        
        if selectedStartTime >= selectedEndTime {
            self.showError(message: "不適切な時刻設定です")
            return
        }
        
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
        
        self.uploadAllImage(tourId: newTourData.id, completion: { resultImage in
            AccountRequester.updateGuide(guideData: myGUideData, completion: { resultUpdate in
                GuideRequester.shared.fetch(completion: { resultFetch in
                    Loading.stop()
                    
                    if resultImage && resultUpdate && resultFetch {
                        let message = (self.guideTourData != nil) ? "ツアーを更新しました" : "ツアーを作成しました"
                        let action = DialogAction(title: "OK", action: { [weak self] in
                            self?.pop(animationType: .horizontal)
                        })
                        Dialog.show(style: .success, title: "確認", message: message, actions: [action])
                        
                        if let guideRegister = self.parent as? GuideRegisterViewController {
                            guideRegister.resetContents()
                        }
                    } else {
                        self.showError(message: "通信に失敗しました")
                    }
                })
            })
        })
    }
    
    @IBAction func onTapTourImage(_ sender: Any) {
        self.pickerTarget = .tour
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapHighlights1Image(_ sender: Any) {
        self.pickerTarget = .highlights1
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapHighlights2Image(_ sender: Any) {
        self.pickerTarget = .highlights2
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapHighlights3Image(_ sender: Any) {
        self.pickerTarget = .highlights3
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapDeleteHighlights1(_ sender: Any) {
        self.highlights1View.isHidden = true
        self.highlights1ViewHeightConstraint.constant = 0
    }
    
    @IBAction func onTapDeleteHighlights2(_ sender: Any) {
        self.highlights2View.isHidden = true
        self.highlights2ViewHeightConstraint.constant = 0
    }
    
    @IBAction func onTapDeleteHighlights3(_ sender: Any) {
        self.highlights3View.isHidden = true
        self.highlights3ViewHeightConstraint.constant = 0
    }
    
    @IBAction func onTapDelete(_ sender: Any) {
        
        guard var myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId),
            let oldTourData = self.guideTourData else {
            return
        }
        if let index = myGuideData.tours.index(where: { $0.id == oldTourData.id }) {
            myGuideData.tours.remove(at: index)
            
            Loading.start()
            
            AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
                GuideRequester.shared.fetch(completion: { resultFetch in
                    Loading.stop()
                    if resultUpdate && resultFetch {
                        let action = DialogAction(title: "OK", action: { [weak self] in
                            self?.pop(animationType: .horizontal)
                        })
                        Dialog.show(style: .success, title: "確認", message: "ツアーを削除しました", actions: [action])
                        
                        if let guideRegister = self.parent as? GuideRegisterViewController {
                            guideRegister.resetContents()
                        }
                    } else {
                        self.showError(message: "通信に失敗しました")
                    }
                })
            })
        }
    }
}

extension CreateTourViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
    private func showImagePicker(sourceType: UIImagePickerControllerSourceType) {
        
        if UIImagePickerController.isSourceTypeAvailable(sourceType) {
            let picker = UIImagePickerController()
            picker.sourceType = sourceType
            picker.delegate = self
            self.present(picker, animated: true, completion: nil)
        }
    }
    
    func imagePickerController(_ picker: UIImagePickerController, didFinishPickingMediaWithInfo info: [String : Any]) {
        
        if let rawImage = info[UIImagePickerControllerOriginalImage] as? UIImage {
            
            let image = rawImage.toTourImage()
            
            guard let pickerTarget = self.pickerTarget else {
                return
            }
            switch pickerTarget {
            case .tour:
                self.tourImageView.image = image
                self.tourImage = image
            case .highlights1:
                self.highlights1ImageView.image = image
                self.highlights1Image = image
            case .highlights2:
                self.highlights2ImageView.image = image
                self.highlights2Image = image
            case .highlights3:
                self.highlights3ImageView.image = image
                self.highlights3Image = image
            }
        }
        picker.dismiss(animated: true, completion: nil)
    }
    
    private func uploadAllImage(tourId: String, completion: @escaping ((Bool) -> ())) {
        
        self.uploadImage(tourId: tourId, type: .tour, completion: { resultTour in
            self.uploadImage(tourId: tourId, type: .highlights1, completion: { resultH1 in
                self.uploadImage(tourId: tourId, type: .highlights2, completion: { resultH2 in
                    self.uploadImage(tourId: tourId, type: .highlights3, completion: { resultH3 in
                        if resultTour && resultH1 && resultH2 && resultH3 {
                            completion(true)
                        } else {
                            completion(false)
                        }
                    })
                })
            })
        })
    }
    
    private func uploadImage(tourId: String, type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadTourImage"]
        params["tourId"] = tourId
        
        switch type {
        case .tour:
            image = self.tourImage
            params["suffix"] = "t"
        case .highlights1:
            image = self.highlights1Image
            params["suffix"] = "h1"
        case .highlights2:
            image = self.highlights2Image
            params["suffix"] = "h2"
        case .highlights3:
            image = self.highlights2Image
            params["suffix"] = "h1"
        }
        
        guard let img = image else {
            completion(true)
            return
        }
        ImageUploader.post(url: Constants.ServerApiUrl, image: img, params: params, completion: { result in
            completion(result)
        })
    }
}


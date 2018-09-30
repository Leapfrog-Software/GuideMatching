//
//  GuideRegisterViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

import UIKit

class GuideRegisterViewController: KeyboardRespondableViewController {
    
    enum ImageType {
        case face1
        case face2
        case face3
    }
    
    @IBOutlet private weak var headerTitleLabel: UILabel!
    @IBOutlet private weak var scrollView: UIScrollView!
    @IBOutlet private weak var face1ImageView: UIImageView!
    @IBOutlet private weak var face2ImageView: UIImageView!
    @IBOutlet private weak var face3ImageView: UIImageView!
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var nameTextField: UITextField!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var areaTextField: UITextField!
    @IBOutlet private weak var keywordTextField: UITextField!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var applicableNumberLabel: UILabel!
    @IBOutlet private weak var feeTextField: UITextField!
    @IBOutlet private weak var notesTextView: UITextView!
    @IBOutlet private weak var scrollViewBottomConstraint: NSLayoutConstraint!
    
    private var pickerTarget: ImageType?
    private var face1Image: UIImage?
    private var face2Image: UIImage?
    private var face3Image: UIImage?
    
    private var languages: [String] = ["English", "Chinese", "Korean", "Thai", "Malay", "Indonesian", "Vietnamese", "Hindi", "French", "German", "Italian", "Spanish", "Arabic", "Portuguese"]
    private var languageIndex = 0
    private var categories: [String] = ["Food", "Nature", "Historical site", "Traditional culture", "Music", "Art", "Subculture"]
    private var categoryIndexes = [Int]()
    private var applicableNumbers: [Int] = (Array<Int>)(1...20)
    private var applicableNumberIndex = 0
    
    private var isEdit = false
    
    func set(isEdit: Bool) {
        self.isEdit = true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.resetContents()
        
        if self.isEdit {
            self.headerTitleLabel.text = "Edit Profile"
        } else {
            self.headerTitleLabel.text = "New Registration"
        }
    }
    
    func resetContents() {
        
        guard let myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + myGuideData.id + "-0", imageView: self.face1ImageView)
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + myGuideData.id + "-1", imageView: self.face2ImageView)
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + myGuideData.id + "-2", imageView: self.face3ImageView)
        
        self.emailTextField.text = myGuideData.email
        self.nameTextField.text = myGuideData.name
        self.languageLabel.text = myGuideData.language
        self.areaTextField.text = myGuideData.area
        self.keywordTextField.text = myGuideData.keyword
        self.categoryLabel.text = myGuideData.category
        self.messageTextView.text = myGuideData.message
        self.applicableNumberLabel.text = "\(myGuideData.applicableNumber)"
        self.feeTextField.text = "\(myGuideData.fee)"
        self.notesTextView.text = myGuideData.notes
    }
    
    private func stackTabbar() {
        if let splashViewController = self.parent?.parent {
            let tabbar = self.viewController(storyboard: "Initial", identifier: "TabbarViewController") as! TabbarViewController
            splashViewController.stack(viewController: tabbar, animationType: .none)
            splashViewController.view.bringSubview(toFront: self.view)
            self.parent?.pop(animationType: .vertical)
        }
    }
    
    private func showError(message: String) {
        Dialog.show(style: .error, title: "エラー", message: message, actions: [DialogAction(title: "OK", action: nil)])
    }
    
    private func showCommunicateError() {
        self.showError(message: "通信に失敗しました")
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapFace1(_ sender: Any) {
        self.pickerTarget = .face1
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapFace2(_ sender: Any) {
        self.pickerTarget = .face2
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapFace3(_ sender: Any) {
        self.pickerTarget = .face3
        self.showImagePicker(sourceType: .photoLibrary)
    }
    
    @IBAction func onTapLanguage(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "言語", dataArray: self.languages, defaultIndex: self.languageIndex, completion: { [weak self] index in
            self?.languageIndex = index
            self?.languageLabel.text = self?.languages[index]
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapCategory(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "MultiPickerViewController") as! MultiPickerViewController
        picker.set(title: "カテゴリー", dataArray: self.categories, defaultIndexes: self.categoryIndexes, completion: { [weak self] indexes in
            self?.categoryIndexes = indexes
            if let categories = self?.categories {
                var selectedCaetgories = [String]()
                for i in 0..<categories.count {
                    if indexes.contains(i) {
                        selectedCaetgories.append(categories[i])
                    }
                }
                self?.categoryLabel.text = selectedCaetgories.joined(separator: ", ")
            }
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapApplicableNumber(_ sender: Any) {
        self.view.endEditing(true)
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "対応人数", dataArray: self.applicableNumbers.compactMap { "\($0)" }, defaultIndex: self.applicableNumberIndex, completion: { [weak self] index in
            self?.applicableNumberIndex = index
            if let applicableNumber = self?.applicableNumbers[index] {
                self?.applicableNumberLabel.text = "\(applicableNumber)"
            }
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapDone(_ sender: Any) {
        self.view.endEditing(true)
        
        let email = self.emailTextField.text ?? ""
        if email.count == 0 {
            self.showError(message: "メールアドレスが入力されていません")
            return
        }
        if email.contains(",") || !email.contains("@") {
            self.showError(message: "不正なメールアドレスです")
            return
        }

        let name = self.nameTextField.text ?? ""
        if name.count == 0 {
            self.showError(message: "名前が入力されていません")
            return
        }
        
        let fee = Int(self.feeTextField.text ?? "0") ?? 0
        if fee <= 0 {
            self.showError(message: "不適切な料金設定です")
            return
        }
        
        Loading.start()

        if self.isEdit {
            self.updateGuide()
        } else {
            self.createGuide()
        }
    }
    
    private func updateGuide() {
        
        var myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId)!
        myGuideData.email = self.emailTextField.text ?? ""
        myGuideData.name = self.nameTextField.text ?? ""
        myGuideData.language = self.languages[self.languageIndex]
        myGuideData.area = self.areaTextField.text ?? ""
        myGuideData.keyword = self.keywordTextField.text ?? ""
        
        var selectedCategories = [String]()
        for i in 0..<self.categories.count {
            if self.categoryIndexes.contains(i) {
                selectedCategories.append(self.categories[i])
            }
        }
        myGuideData.category = selectedCategories.joined(separator: ", ")
        
        myGuideData.message = self.messageTextView.text ?? ""
        myGuideData.applicableNumber = self.applicableNumbers[self.applicableNumberIndex]
        myGuideData.fee = Int(self.feeTextField.text ?? "0") ?? 0
        myGuideData.notes = self.notesTextView.text ?? ""
        
        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
            if resultUpdate {
                self.uploadAllImage(guideId: SaveData.shared.guideId, completion: { resultImage in
                    GuideRequester.shared.fetch(completion: { resultFetch in
                        Loading.stop()
                        
                        if resultImage && resultFetch {
                            NotificationCenter.default.post(name: .guide, object: nil, userInfo: nil)
                            Dialog.show(style: .success, title: "確認", message: "更新しました", actions: [DialogAction(title: "OK", action: nil)])
                        } else {
                            self.showCommunicateError()
                        }
                    })
                })
            } else {
                Loading.stop()
                self.showCommunicateError()
            }
        })
    }
    
    private func createGuide() {
        
        self.view.endEditing(true)
        
        let email = self.emailTextField.text ?? ""
        let name = self.nameTextField.text ?? ""
        let language = self.languages[self.languageIndex]
        let area = self.areaTextField.text ?? ""
        let keyword = self.keywordTextField.text ?? ""

        var selectedCategories = [String]()
        for i in 0..<self.categories.count {
            if self.categoryIndexes.contains(i) {
                selectedCategories.append(self.categories[i])
            }
        }
        let category = selectedCategories.joined(separator: ", ")
        
        let message = self.messageTextView.text ?? ""
        let applicableNumber = self.applicableNumbers[self.applicableNumberIndex]
        let fee = Int(self.feeTextField.text ?? "0") ?? 0
        let notes = self.notesTextView.text ?? ""
        
        AccountRequester.createGuide(email: email, name: name, language: language, area: area, keyword: keyword, category: category, message: message, applicableNumber: applicableNumber, fee: fee, notes: notes, completion: { resultCreate, guideId in
            if resultCreate, let guideId = guideId {
                self.uploadAllImage(guideId: guideId, completion: { resultImage in
                    if resultImage {
                        self.refetchGuide(guideId: guideId)
                    } else {
                        Loading.stop()
                        self.showCommunicateError()
                    }
                })
            } else {
                Loading.stop()
                self.showCommunicateError()
            }
        })
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
    
    override func animate(with: KeyboardAnimation) {
        
        self.scrollViewBottomConstraint.constant = with.height
        UIView.animate(withDuration: with.duration, delay: 0, options: with.curve, animations: {
            self.view.layoutIfNeeded()
        })
        
        let views: [UIView] = [self.emailTextField, self.nameTextField, self.areaTextField, self.keywordTextField, self.messageTextView, self.feeTextField, self.notesTextView]
        if let firstResponder = (views.filter { $0.isFirstResponder }).first, let window = UIApplication.shared.keyWindow {
            let absRect = firstResponder.convert(firstResponder.bounds, to: window)
            let bottom = absRect.origin.y + absRect.size.height
            if bottom > window.frame.size.height - with.height {
                self.scrollView.setContentOffset(CGPoint(x: self.scrollView.contentOffset.x,
                                                         y: self.scrollView.contentOffset.y + 50),
                                                 animated: true)
            }
        }
    }
}

extension GuideRegisterViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
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
            
            let image = rawImage.toFaceImage()
            
            guard let pickerTarget = self.pickerTarget else {
                return
            }
            switch pickerTarget {
            case .face1:
                self.face1ImageView.image = image
                self.face1Image = image
            case .face2:
                self.face2ImageView.image = image
                self.face2Image = image
            case .face3:
                self.face3ImageView.image = image
                self.face3Image = image
            }
        }
        picker.dismiss(animated: true, completion: nil)
    }
}

extension GuideRegisterViewController {
    
    private func refetchGuide(guideId: String) {
        
        GuideRequester.shared.fetch(completion: { resultFetch in
            if resultFetch {
                guard var myGuideData = GuideRequester.shared.query(id: guideId) else {
                    self.showCommunicateError()
                    return
                }
                StripeManager.createAccount(email: myGuideData.email, completion: { resultStripe, accountId in
                    if resultStripe, let accountId = accountId {
                        myGuideData.stripeAccountId = accountId
                        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
                            if resultUpdate {
                                GuideRequester.shared.fetch(completion: { _ in
                                    Loading.stop()
                                    
                                    let saveData = SaveData.shared
                                    saveData.guideId = guideId
                                    saveData.save()
                                    
                                    self.stackTabbar()
                                })
                            } else {
                                Loading.stop()
                                self.showCommunicateError()
                            }
                        })
                    } else {
                        Loading.stop()
                        self.showCommunicateError()
                    }
                })
            } else {
                Loading.stop()
                self.showCommunicateError()
            }
        })
    }
    
    private func uploadAllImage(guideId: String, completion: @escaping ((Bool) -> ())) {
        
        self.uploadImage(guideId: guideId, type: .face1, completion: { resultFace1 in
            self.uploadImage(guideId: guideId, type: .face2, completion: { resultFace2 in
                self.uploadImage(guideId: guideId, type: .face3, completion: { resultFace3 in
                    if resultFace1 && resultFace2 && resultFace3 {
                        completion(true)
                    } else {
                        completion(false)
                    }
                })
            })
        })
    }
    
    private func uploadImage(guideId: String, type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadGuideImage"]
        params["guideId"] = guideId
        
        switch type {
        case .face1:
            image = self.face1Image
            params["suffix"] = "0"
        case .face2:
            image = self.face2Image
            params["suffix"] = "1"
        case .face3:
            image = self.face3Image
            params["suffix"] = "2"
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

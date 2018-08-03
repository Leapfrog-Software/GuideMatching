//
//  MyPageGuideProfileViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/02.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class MyPageGuideProfileViewController: UIViewController {

    enum ImageType {
        case face1
        case face2
        case face3
    }
    
    @IBOutlet private weak var face1ImageView: UIImageView!
    @IBOutlet private weak var face2ImageView: UIImageView!
    @IBOutlet private weak var face3ImageView: UIImageView!
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var nameTextField: UITextField!
    @IBOutlet private weak var nationalityTextField: UITextField!
    @IBOutlet private weak var languageLabel: UILabel!
    @IBOutlet private weak var specialtyTextView: UITextView!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var messageTextView: UITextView!
    @IBOutlet private weak var timeZoneTextView: UITextView!
    @IBOutlet private weak var applicableNumberLabel: UILabel!
    @IBOutlet private weak var feeTextField: UITextField!
    @IBOutlet private weak var notesTextView: UITextView!
    
    private var pickerTarget: ImageType?
    private var face1Image: UIImage?
    private var face2Image: UIImage?
    private var face3Image: UIImage?
    
    private var languages: [String] = ["English", "Chinese", "Korean", "Thai", "Malay", "Indonesian", "Vietnamese", "Hindi", "French", "German", "Italian", "Spanish", "Arabic", "Portuguese"]
    private var languageIndex = 0
    private var categories: [String] = ["Food", "Traditional", "culture", "Nature"]
    private var categoryIndex = 0
    private var applicableNumbers: [Int] = (Array<Int>)(1...20)
    private var applicableNumberIndex = 0
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        // TODO
    }
    
    private func uploadImage(type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadGuideImage"]
        
        switch type {
        case .face1:
            image = self.face1Image
            params["type"] = "face1"
        case .face2:
            image = self.face2Image
            params["type"] = "face2"
        case .face3:
            image = self.face3Image
            params["type"] = "face3"
        }
        
        guard let img = image else {
            completion(true)
            return
        }
        ImageUploader.post(url: Constants.ServerApiUrl, image: img, params: params, completion: { result in
            completion(result)
        })
    }
    
    private func showError(message: String) {
        Dialog.show(style: .error, title: "エラー", message: message, actions: [DialogAction(title: "OK", action: nil)])
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
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "言語", dataArray: self.languages, defaultIndex: self.languageIndex, completion: { [weak self] index in
            self?.languageIndex = index
            self?.languageLabel.text = self?.languages[index]
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapCategory(_ sender: Any) {
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "カテゴリー", dataArray: self.categories, defaultIndex: self.categoryIndex, completion: { [weak self] index in
            self?.categoryIndex = index
            self?.categoryLabel.text = self?.categories[index]
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapApplicableNumber(_ sender: Any) {
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
        
        guard var myGuideData = GuideRequester.shared.query(id: SaveData.shared.guideId) else {
            return
        }
        
        let email = self.emailTextField.text ?? ""
        if email.count == 0 {
            self.showError(message: "メールアドレスが入力されていません")
            return
        }
        if email.contains(",") || !email.contains("@") {
            self.showError(message: "不正なメールアドレスです")
            return
        }
        myGuideData.email = email
        
        let name = self.nameTextField.text ?? ""
        if name.count == 0 {
            self.showError(message: "名前が入力されていません")
            return
        }
        myGuideData.name = name
        
        let nationality = self.nationalityTextField.text ?? ""
        if nationality.count == 0 {
            self.showError(message: "国籍が入力されていません")
            return
        }
        myGuideData.nationality = nationality
        
        myGuideData.language = self.languages[self.languageIndex]
        myGuideData.specialty = self.specialtyTextView.text ?? ""
        myGuideData.category = self.categories[self.categoryIndex]
        myGuideData.message = self.messageTextView.text ?? ""
        myGuideData.timeZone = self.timeZoneTextView.text ?? ""
        myGuideData.applicableNumber = self.applicableNumbers[self.applicableNumberIndex]
        myGuideData.fee = Int(self.feeTextField.text ?? "0") ?? 0
        myGuideData.notes = self.notesTextView.text ?? ""
        
        Loading.start()
        
        self.uploadImage(type: .face1, completion: { resultFace1 in
            self.uploadImage(type: .face2, completion: { resultFace2 in
                self.uploadImage(type: .face3, completion: { resultFace3 in
                    if resultFace1 && resultFace2 && resultFace3 {
                        AccountRequester.updateGuide(guideData: myGuideData, completion: { resultUpdate in
                            Loading.stop()
                            
                            if resultUpdate {
                                Dialog.show(style: .success, title: "確認", message: "更新しました", actions: [DialogAction(title: "OK", action: nil)])
                            } else {
                                self.showError(message: "通信に失敗しました")
                            }
                        })
                    } else {
                        Loading.stop()
                        self.showError(message: "通信に失敗しました")
                    }
                })
            })
        })
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension MyPageGuideProfileViewController: UIScrollViewDelegate {
    
    func scrollViewWillBeginDragging(_ scrollView: UIScrollView) {
        self.view.endEditing(true)
    }
}

extension MyPageGuideProfileViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
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

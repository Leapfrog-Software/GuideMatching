//
//  GuestRegisterViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuestRegisterViewController: UIViewController {
    
    enum ImageType {
        case face1
        case face2
        case face3
        case passport
    }
    
    @IBOutlet private weak var face1ImageView: UIImageView!
    @IBOutlet private weak var face2ImageView: UIImageView!
    @IBOutlet private weak var face3ImageView: UIImageView!
    @IBOutlet private weak var nameTextField: UITextField!
    @IBOutlet private weak var nationalityTextField: UITextField!
    @IBOutlet private weak var passportImageView: UIImageView!
    @IBOutlet private weak var doneButton: UIButton!
    
    private var pickerTarget: ImageType?
    private var face1Image: UIImage?
    private var face2Image: UIImage?
    private var face3Image: UIImage?
    private var passportImage: UIImage?
    
    private func uploadImage(type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadImage"]
        
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
        case .passport:
            image = self.passportImage
            params["type"] = "passport"
        }
        
        guard let img = image else {
            completion(true)
            return
        }
        ImageUploader.post(url: Constants.ServerApiUrl, image: img, params: params, completion: { result in
            completion(result)
        })
    }
    
    private func stackTabbar() {
        let tabbar = self.viewController(storyboard: "Initial", identifier: "TabbarViewController") as! TabbarViewController
        self.stack(viewController: tabbar, animationType: .none)
    }
    
    private func showError(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
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
    
    @IBAction func onTapPassport(_ sender: Any) {
        self.pickerTarget = .passport
        self.showImagePicker(sourceType: .camera)
    }
    
    @IBAction func onTapDone(_ sender: Any) {
        self.view.endEditing(true)
        
        let name = self.nameTextField.text ?? ""
        if name.count == 0 {
            self.showError(message: "Name is not entered")
            return
        }
        
        let nationality = self.nationalityTextField.text ?? ""
        if nationality.count == 0 {
            self.showError(message: "Nationality is not entered")
            return
        }
        
        if self.passportImage == nil {
            self.showError(message: "Passport is not captured")
            return
        }
        
        self.uploadImage(type: .face1, completion: { resultFace1 in
            self.uploadImage(type: .face2, completion: { resultFace2 in
                self.uploadImage(type: .face3, completion: { resultFace3 in
                    self.uploadImage(type: .passport, completion: { resultPassport in
                        if resultFace1 && resultFace2 && resultFace3 && resultPassport {
                            AccountRequester.createGuest(name: name, nationality: nationality, completion: { resultCreate, guestId in
                                if resultCreate, let guestId = guestId {
                                    let saveData = SaveData.shared
                                    saveData.guestId = guestId
                                    saveData.save()
                                    
                                    self.stackTabbar()
                                } else {
                                    self.showError(message: "Failed to communicate")
                                }
                            })
                        } else {
                            self.showError(message: "Failed to communicate")
                        }
                    })
                })
            })
        })
    }
}

extension GuestRegisterViewController: UIImagePickerControllerDelegate, UINavigationControllerDelegate {
    
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
            
            let image = self.resizeImage(image: rawImage)
            
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
            case .passport:
                self.passportImageView.image = image
                self.passportImage = image
            }
        }
        picker.dismiss(animated: true, completion: nil)
    }
    
    private func resizeImage(image: UIImage) -> UIImage? {
        
        let trimmedImage: UIImage?
        
        if image.size.width < image.size.height {
            trimmedImage = image.trim(rect: CGRect(x: 0,
                                                   y: (image.size.height - image.size.width) / 2,
                                                   width: image.size.width,
                                                   height: image.size.width))
        } else {
            trimmedImage = image.trim(rect: CGRect(x: (image.size.width - image.size.height) / 2,
                                                   y: 0,
                                                   width: image.size.height,
                                                   height: image.size.height))
        }
        return trimmedImage?.resize(size: CGSize(width: 400, height: 400))
    }
}

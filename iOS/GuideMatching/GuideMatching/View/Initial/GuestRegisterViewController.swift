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
    @IBOutlet private weak var emailTextField: UITextField!
    @IBOutlet private weak var nameTextField: UITextField!
    @IBOutlet private weak var nationalityTextField: UITextField!
    @IBOutlet private weak var passportImageView: UIImageView!
    @IBOutlet private weak var doneButton: UIButton!
    
    private var pickerTarget: ImageType?
    private var face1Image: UIImage?
    private var face2Image: UIImage?
    private var face3Image: UIImage?
    private var passportImage: UIImage?
    
    private func stackTabbar() {
        if let splashViewController = self.parent?.parent {
            let tabbar = self.viewController(storyboard: "Initial", identifier: "TabbarViewController") as! TabbarViewController
            splashViewController.stack(viewController: tabbar, animationType: .none)
            splashViewController.view.bringSubview(toFront: self.view)
            self.parent?.pop(animationType: .vertical)
        }
    }
    
    private func showError(message: String) {
        let alert = UIAlertController(title: "Error", message: message, preferredStyle: .alert)
        alert.addAction(UIAlertAction(title: "OK", style: .default, handler: nil))
        self.present(alert, animated: true, completion: nil)
    }
    
    private func showCommunicateError() {
        self.showError(message: "Failed to communicate")
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
        
        let email = self.emailTextField.text ?? ""
        if email.count == 0 {
            self.showError(message: "Email is not entered")
            return
        }
        if email.contains(",") || !email.contains("@") {
            self.showError(message: "Email is invalid")
            return
        }
        
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
                            AccountRequester.createGuest(email: email, name: name, nationality: nationality, completion: { resultCreate, guestId in
                                if resultCreate, let guestId = guestId {                                    
                                    self.refetchGuest(guestId: guestId)
                                } else {
                                    self.showCommunicateError()
                                }
                            })
                        } else {
                            self.showCommunicateError()
                        }
                    })
                })
            })
        })
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .vertical)
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
            case .passport:
                self.passportImageView.image = image
                self.passportImage = image
            }
        }
        picker.dismiss(animated: true, completion: nil)
    }
}

extension GuestRegisterViewController {
    
    private func refetchGuest(guestId: String) {
        
        GuestRequester.shared.fetch(completion: { resultFetch in
            if resultFetch {
                guard var myGuestData = GuestRequester.shared.query(id: guestId) else {
                    self.showCommunicateError()
                    return
                }
                StripeManager.createCustomer(email: myGuestData.email, completion: { resultStripe, customerId in
                    if resultStripe, let customerId = customerId {
                        myGuestData.stripeCustomerId = customerId
                        AccountRequester.updateGuest(guestData: myGuestData, completion: { resultUpdate in
                            if resultUpdate {
                                let saveData = SaveData.shared
                                saveData.guestId = guestId
                                saveData.save()
                                
                                self.stackTabbar()
                                
                            } else {
                                self.showCommunicateError()
                            }
                        })
                    } else {
                        self.showCommunicateError()
                    }
                })
            } else {
                self.showCommunicateError()
            }
        })
    }
    
    private func uploadImage(type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadGuestImage"]
        
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
}

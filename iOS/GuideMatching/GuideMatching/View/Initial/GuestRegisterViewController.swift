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
    
    @IBOutlet private weak var headerTitleLabel: UILabel!
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
    
    private var isEdit = false
    
    func set(isEdit: Bool) {
        self.isEdit = true
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if self.isEdit {
            self.initContents()
            self.headerTitleLabel.text = "Edit Profile"
        } else {
            self.headerTitleLabel.text = "New Registration"
        }        
    }
    
    private func initContents() {
        
        guard let myGuestData = GuestRequester.shared.query(id: SaveData.shared.guestId) else {
            return
        }
        
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + myGuestData.id + "-0", imageView: self.face1ImageView)
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + myGuestData.id + "-1", imageView: self.face2ImageView)
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + myGuestData.id + "-2", imageView: self.face3ImageView)
        ImageStorage.shared.fetch(url: Constants.ServerGuestImageRootUrl + myGuestData.id + "-p", imageView: self.passportImageView)
        
        self.emailTextField.text = myGuestData.email
        self.nameTextField.text = myGuestData.name
        self.nationalityTextField.text = myGuestData.nationality
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
        Dialog.show(style: .error, title: "Error", message: message, actions: [DialogAction(title: "OK", action: nil)])
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
        self.showImagePicker(sourceType: .photoLibrary)
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
        
        if !self.isEdit && self.passportImage == nil {
            self.showError(message: "Passport is not captured")
            return
        }
        
        Loading.start()
        
        if self.isEdit {
            self.updateGuest()
        } else {
            self.createGuest()
        }
    }
    
    @IBAction func onTapClose(_ sender: Any) {
        self.pop(animationType: .vertical)
    }
    
    private func uploadAllImage(guestId: String, completion: @escaping ((Bool) -> ())) {

        self.uploadImage(guestId: guestId, type: .face1, completion: { resultFace1 in
            self.uploadImage(guestId: guestId, type: .face2, completion: { resultFace2 in
                self.uploadImage(guestId: guestId, type: .face3, completion: { resultFace3 in
                    self.uploadImage(guestId: guestId, type: .passport, completion: { resultPassport in
                        if resultFace1 && resultFace2 && resultFace3 && resultPassport {
                            completion(true)
                        } else {
                            completion(false)
                        }
                    })
                })
            })
        })
    }
    
    private func updateGuest() {
        
        var myGuestData = GuestRequester.shared.query(id: SaveData.shared.guestId)!
        myGuestData.email = self.emailTextField.text ?? ""
        myGuestData.name = self.nameTextField.text ?? ""
        myGuestData.nationality = self.nationalityTextField.text ?? ""
        
        AccountRequester.updateGuest(guestData: myGuestData, completion: { resultAccount in
            if resultAccount {
                self.uploadAllImage(guestId: SaveData.shared.guestId, completion: { resultImage in
                    Loading.stop()
                    
                    if resultImage {
                        Dialog.show(style: .success, title: "Done", message: "Updating is done", actions: [DialogAction(title: "OK", action: nil)])
                    } else {
                        self.showCommunicateError()
                    }
                })
            } else {
                Loading.stop()
                self.showCommunicateError()
            }
        })
    }
    
    private func createGuest() {
        
        let email = self.emailTextField.text ?? ""
        let name = self.nameTextField.text ?? ""
        let nationality = self.nationalityTextField.text ?? ""
        
        AccountRequester.createGuest(email: email, name: name, nationality: nationality, completion: { result, guestId in
            if result, let guestId = guestId {
                self.uploadAllImage(guestId: guestId, completion: { resultImage in
                    if resultImage {
                        self.refetchGuest(guestId: guestId)
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
    
    private func uploadImage(guestId: String, type: ImageType, completion: @escaping ((Bool) -> ())) {
        
        let image: UIImage?
        var params: [String: String] = ["command": "uploadGuestImage"]
        params["guestId"] = guestId
        
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
        case .passport:
            image = self.passportImage
            params["suffix"] = "p"
        }
        
        guard let img = image else {
            completion(true)
            return
        }
        ImageUploader.post(url: Constants.ServerApiUrl, image: img, params: params, completion: { result in
            completion(result)
        })
    }
    
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
                                GuestRequester.shared.fetch(completion: { _ in
                                    Loading.stop()
                                    
                                    let saveData = SaveData.shared
                                    saveData.guestId = guestId
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

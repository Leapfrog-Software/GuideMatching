//
//  BookViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/28.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import Stripe

class BookViewController: UIViewController {
    
    @IBOutlet private weak var headerTitleLabel: UILabel!
    @IBOutlet private weak var faceImageView: UIImageView!
    @IBOutlet private weak var nameLabel: UILabel!
    @IBOutlet private weak var score1ImageView: UIImageView!
    @IBOutlet private weak var score2ImageView: UIImageView!
    @IBOutlet private weak var score3ImageView: UIImageView!
    @IBOutlet private weak var score4ImageView: UIImageView!
    @IBOutlet private weak var score5ImageView: UIImageView!
    @IBOutlet private weak var reviewLabel: UILabel!
    @IBOutlet private weak var categoryLabel: UILabel!
    @IBOutlet private weak var onlineStateView: UIView!
    @IBOutlet private weak var onlineStateLabel: UILabel!
    @IBOutlet private weak var priceLabel: UILabel!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var timeInputView: UIView!
    @IBOutlet private weak var timeInputViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var timeInputStartLabel: UILabel!
    @IBOutlet private weak var timeInputEndButton: UIButton!
    @IBOutlet private weak var timeConfirmView: UIView!
    @IBOutlet private weak var timeConfirmViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var timeConfirmStartLabel: UILabel!
    @IBOutlet private weak var timeConfirmEndLabel: UILabel!
    @IBOutlet private weak var placeInputView: UIView!
    @IBOutlet private weak var placeInputViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var placeInputTextField: UITextField!
    @IBOutlet private weak var placeConfirmView: UIView!
    @IBOutlet private weak var placeConfirmViewHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var placeConfirmTitleLabel: UILabel!
    @IBOutlet private weak var placeConfirmLabel: UILabel!
    @IBOutlet private weak var guideFeeLabel: UILabel!
    @IBOutlet private weak var transactionFeeLabel: UILabel!
    @IBOutlet private weak var totalFeeLabel: UILabel!
    @IBOutlet private weak var notesLabel: UILabel!
    @IBOutlet private weak var nextButton: UIButton!
    
    private var guideData: GuideData!
    private var targetDate: Date!
    private var startTimeIndex: Int!
    private var endTimeIndex: Int?
    private var meetingPlace: String?
    private var selectedEndTimeIndex = 0
    private var selectedCardId: String?
    
    func set(guideData: GuideData, targetDate: Date, startTimeIndex: Int) {
        self.guideData = guideData
        self.targetDate = targetDate
        self.startTimeIndex = startTimeIndex
    }
    
    func set(endTimeIndex: Int, meetingPlace: String) {
        self.endTimeIndex = endTimeIndex
        self.meetingPlace = meetingPlace
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    override func viewDidAppear(_ animated: Bool) {
        super.viewDidAppear(animated)
        
        self.adjustLayout()
    }
    
    private func isInput() -> Bool {
        return self.endTimeIndex == nil || self.meetingPlace == nil
    }
    
    private func initContents() {
        
        self.headerTitleLabel.text = self.isInput() ? "Reservation form" : "Confirmation"
        ImageStorage.shared.fetch(url: Constants.ServerGuideImageRootUrl + self.guideData.id + "-0", imageView: self.faceImageView)
        self.nameLabel.text = self.guideData.name
        
        let estimateDatas = EstimateRequester.shared.dataList.filter { $0.targetId == self.guideData.id && $0.isGuide == true }
        var score = 0
        estimateDatas.forEach {
            score += $0.score
        }
        
        let scoreImages = CommonUtility.createEstimateImages(score)
        self.score1ImageView.image = scoreImages[0]
        self.score2ImageView.image = scoreImages[1]
        self.score3ImageView.image = scoreImages[2]
        self.score4ImageView.image = scoreImages[3]
        self.score5ImageView.image = scoreImages[4]
        self.reviewLabel.text = "(\(estimateDatas.count))"
        
        CommonUtility.setOnLineState(loginDate: self.guideData.loginDate, view: self.onlineStateView, label: self.onlineStateLabel)
        self.priceLabel.text = CommonUtility.digit3Format(value: self.guideData.fee)
        self.dayLabel.text = self.targetDate.toDayMonthYearText()

        if let endTimeIndex = self.endTimeIndex, let meetingPlace = self.meetingPlace {
            self.timeInputView.isHidden = true
            self.timeInputViewHeightConstraint.constant = 0
            self.timeConfirmStartLabel.text = String(format: "%02d", Int(self.startTimeIndex / 2)) + ":" + String(format: "%02d", 30 * Int(self.startTimeIndex % 2))
            self.timeConfirmEndLabel.text = String(format: "%02d", Int(endTimeIndex / 2)) + ":" + String(format: "%02d", 30 * Int(endTimeIndex % 2))
            self.selectedEndTimeIndex = self.startTimeIndex + 1
            self.placeInputView.isHidden = true
            self.placeInputViewHeightConstraint.constant = 0
            self.placeConfirmLabel.text = meetingPlace
        } else {
            self.timeInputStartLabel.text = String(format: "%02d", Int(self.startTimeIndex / 2)) + ":" + String(format: "%02d", 30 * Int(self.startTimeIndex % 2))
            self.timeInputEndButton.setTitle(String(format: "%02d", Int((self.startTimeIndex + 1) / 2)) + ":" + String(format: "%02d", 30 * Int((self.startTimeIndex + 1) % 2)), for: .normal)
            self.timeConfirmView.isHidden = true
            self.timeConfirmViewHeightConstraint.constant = 0
            self.placeConfirmView.isHidden = true
            self.placeConfirmViewHeightConstraint.constant = 0
        }
        
        self.guideFeeLabel.text = CommonUtility.digit3Format(value: self.guideData.fee) + " JPY/30min"
        let transactionFee = self.guideData.fee * 15 / 100
        self.transactionFeeLabel.text = CommonUtility.digit3Format(value: transactionFee) + "JPY/30min"
        self.totalFeeLabel.text = CommonUtility.digit3Format(value: self.guideData.fee + transactionFee) + " JPY/30min"
        
        self.notesLabel.text = self.guideData.notes
        
        let nextButtonTitle = self.isInput() ? "Continue >>" : "Book"
        self.nextButton.setTitle(nextButtonTitle, for: .normal)
    }
    
    private func adjustLayout() {
        
        if !self.isInput() {
            let titleHeight = self.placeConfirmTitleLabel.frame.size.height
            let placeHeight = self.placeConfirmLabel.frame.size.height
            let placeViewHeight = ((titleHeight > placeHeight) ? titleHeight : placeHeight) + 20
            self.placeConfirmViewHeightConstraint.constant = placeViewHeight
        }
    }
    
    @IBAction func didEndEdit(_ sender: Any) {
        self.view.endEditing(true)
    }
    
    @IBAction func onTapEndTime(_ sender: Any) {
        
        var timeOffsets = [Int]()
        for i in (self.startTimeIndex + 1)...48 {
            timeOffsets.append(i)
        }
        let timeStrs = timeOffsets.map { String(format: "%02d", Int($0 / 2)) + ":" + String(format: "%02d", 30 * Int($0 % 2)) }
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        picker.set(title: "Time", dataArray: timeStrs, defaultIndex: self.selectedEndTimeIndex, completion: { [weak self] index in
            self?.selectedEndTimeIndex = index
            let timeStr = String(format: "%02d", Int(index / 2)) + ":" + String(format: "%02d", 30 * Int(index % 2))
            self?.timeInputEndButton.setTitle(timeStr, for: .normal)
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapNext(_ sender: Any) {

        if self.isInput() {
            let meetingPlace = self.placeInputTextField.text ?? ""
            if meetingPlace.count == 0 {
                Dialog.show(style: .error, title: "Error", message: "Input meeting place", actions: [DialogAction(title: "OK", action: nil)])
                return
            }
            let book = self.viewController(storyboard: "Guide", identifier: "BookViewController") as! BookViewController
            book.set(guideData: self.guideData, targetDate: targetDate, startTimeIndex: self.startTimeIndex)
            book.set(endTimeIndex: self.selectedEndTimeIndex, meetingPlace: meetingPlace)
            self.stack(viewController: book, animationType: .horizontal)
            
        } else {
            guard let myGuestData = GuestRequester.shared.query(id: SaveData.shared.guestId) else {
                return
            }
            let customerId = myGuestData.stripeCustomerId
            let customerContext = STPCustomerContext(keyProvider:StripeApiClient(customerId: customerId))
            let paymentMethodsViewController = STPPaymentMethodsViewController(configuration: STPPaymentConfiguration.shared(),
                                                                               theme: STPTheme.default(),
                                                                               customerContext: customerContext,
                                                                               delegate: self)
            self.present(paymentMethodsViewController, animated: true)
        }
    }

    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension BookViewController: STPPaymentMethodsViewControllerDelegate {
    
    func paymentMethodsViewController(_ paymentMethodsViewController: STPPaymentMethodsViewController, didFailToLoadWithError error: Error) {
        
    }
    
    func paymentMethodsViewControllerDidFinish(_ paymentMethodsViewController: STPPaymentMethodsViewController) {
        
         paymentMethodsViewController.dismiss(animated: true, completion: nil)
        
        guard let cardId = self.selectedCardId else {
            return
        }
        
        Loading.start()
        
        let customerId = GuestRequester.shared.query(id: SaveData.shared.guestId)?.stripeCustomerId ?? ""
        let amount = 1000
        let applicationFee = 10
        let destination = self.guideData.stripeAccountId
        StripeManager.charge(customerId: customerId, cardId: cardId, amount: amount, applicationFee: applicationFee, destination: destination, completion: { result in
            Loading.stop()
            
            if result {
                let complete = self.viewController(storyboard: "Guide", identifier: "BookCompleteViewController") as! BookCompleteViewController
                self.stack(viewController: complete, animationType: .horizontal)
            } else {
                Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    func paymentMethodsViewControllerDidCancel(_ paymentMethodsViewController: STPPaymentMethodsViewController) {
        paymentMethodsViewController.dismiss(animated: true, completion: nil)
    }
    
    func paymentMethodsViewController(_ paymentMethodsViewController: STPPaymentMethodsViewController, didSelect paymentMethod: STPPaymentMethod) {
        self.selectedCardId = (paymentMethod as? STPCard)?.cardId
    }
}

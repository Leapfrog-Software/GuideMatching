//
//  GuideDetailTourViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/08/29.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import Stripe

class GuideDetailTourViewController: UIViewController {

    @IBOutlet private weak var tourImageView: UIImageView!
    @IBOutlet private weak var tourImageHeightConstraint: NSLayoutConstraint!
    @IBOutlet private weak var tourTitleLabel: UILabel!
    @IBOutlet private weak var areaLabel: UILabel!
    @IBOutlet private weak var feeLabel: UILabel!
    @IBOutlet private weak var descriptionLabel: UILabel!
    @IBOutlet private weak var highlightsStackView: UIStackView!
    @IBOutlet private weak var dayLabel: UILabel!
    @IBOutlet private weak var startTimeLabel: UILabel!
    @IBOutlet private weak var departurePointLabel: UILabel!
    @IBOutlet private weak var returnDetailLabel: UILabel!
    @IBOutlet private weak var inclusionsLabel: UILabel!
    @IBOutlet private weak var exclusionsLabel: UILabel!
    @IBOutlet private weak var tourFeeLabel: UILabel!
    @IBOutlet private weak var transactionFeeLabel: UILabel!
    @IBOutlet private weak var totalFeeLabel: UILabel!
    
    private var guideId: String!
    private var tourData: GuideTourData!
    private var selectedDayIndex: Int?
    private var selectedCardId: String?
    
    func set(guideId: String, tourData: GuideTourData) {
        self.guideId = guideId
        self.tourData = tourData
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        self.initContents()
    }
    
    private func initContents() {
        
        ImageStorage.shared.fetch(url: Constants.ServerTourImageRootUrl + tourData.id + "-t", imageView: self.tourImageView, defaultImage: UIImage(named: "no_image"), completion: {
            if let image = self.tourImageView.image {
                self.tourImageHeightConstraint.constant = (UIScreen.main.bounds.size.width - 40) * image.size.height / image.size.width
            }
        })
        
        self.tourTitleLabel.text = self.tourData.name
        self.areaLabel.text = self.tourData.area
        self.feeLabel.text = CommonUtility.digit3Format(value: self.tourData.fee) + " JPY"
        self.descriptionLabel.text = self.tourData.description
        
        if !self.tourData.highlights1Title.isEmpty || !self.tourData.highlights1Body.isEmpty {
            self.addHighlights(title: self.tourData.highlights1Title, imageUrl: Constants.ServerTourImageRootUrl + self.tourData.id + "-h1", body: self.tourData.highlights1Body)
        }
        if !self.tourData.highlights2Title.isEmpty || !self.tourData.highlights2Body.isEmpty {
            self.addHighlights(title: self.tourData.highlights2Title, imageUrl: Constants.ServerTourImageRootUrl + self.tourData.id + "-h2", body: self.tourData.highlights2Body)
        }
        if !self.tourData.highlights3Title.isEmpty || !self.tourData.highlights3Body.isEmpty {
            self.addHighlights(title: self.tourData.highlights3Title, imageUrl: Constants.ServerTourImageRootUrl + self.tourData.id + "-h3", body: self.tourData.highlights3Body)
        }
        
        self.dayLabel.text = ""
        self.startTimeLabel.text = CommonUtility.timeOffsetToString(offset: self.tourData.startTime)
        self.departurePointLabel.text = self.tourData.departurePoint
        self.returnDetailLabel.text = self.tourData.returnDetail
        self.inclusionsLabel.text = self.tourData.inclusions
        self.exclusionsLabel.text = self.tourData.exclusions
        
        self.tourFeeLabel.text = CommonUtility.digit3Format(value: self.tourData.fee) + " JPY"
        let transactionFee = CommonUtility.calculateTransactionFee(of: self.tourData.fee)
        self.transactionFeeLabel.text = CommonUtility.digit3Format(value: transactionFee) + " JPY"
        self.totalFeeLabel.text = CommonUtility.digit3Format(value: self.tourData.fee + transactionFee) + " JPY"
    }
    
    private func addHighlights(title: String, imageUrl: String, body: String) {
        
        let highlightsView = UINib(nibName: "GuideDetailTourHighlightsView", bundle: nil).instantiate(withOwner: nil, options: nil).first as! GuideDetailTourHighlightsView
        highlightsView.set(title: title, imageUrl: imageUrl, bodyString: body)
        self.highlightsStackView.addArrangedSubview(highlightsView)
    }
    
    private func postReserve() {
        
        Loading.start()
        
        ReserveRequester.reserve(guestId: SaveData.shared.guestId,
                                 guideId: self.guideId,
                                 fee: self.tourData.fee,
                                 applicationFee: CommonUtility.calculateTransactionFee(of: self.tourData.fee),
                                 meetingPlace: self.tourData.departurePoint,
                                 day: self.tourData.days[self.selectedDayIndex ?? 0],
                                 startTime: self.tourData.startTime,
                                 endTime: self.tourData.endTime,
                                 completion: { result in
            if result {
                self.charge()
            } else {
                Loading.stop()
                Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    private func charge() {

        guard let guideData = GuideRequester.shared.query(id: self.guideId) else {
            return
        }
        
        let customerId = GuestRequester.shared.query(id: SaveData.shared.guestId)?.stripeCustomerId ?? ""
        let cardId = self.selectedCardId ?? ""
        let amount = self.tourData.fee
        let applicationFee = CommonUtility.calculateTransactionFee(of: amount)
        let destination = guideData.stripeAccountId
        StripeManager.charge(customerId: customerId, cardId: cardId, amount: amount, applicationFee: applicationFee, destination: destination, completion: { result in
            Loading.stop()
            
            if result {
                let complete = self.viewController(storyboard: "Guide", identifier: "BookCompleteViewController") as! BookCompleteViewController
                complete.set(guideData: guideData, date: self.tourData.days[self.selectedDayIndex ?? 0], startTimeIndex: self.tourData.startTime, endTimeIndex: self.tourData.endTime, meetingPlace: self.tourData.departurePoint, tourFee: self.tourData.fee)
                self.stack(viewController: complete, animationType: .horizontal)
            } else {
                Dialog.show(style: .error, title: "Error", message: "Failed to communicate", actions: [DialogAction(title: "OK", action: nil)])
            }
        })
    }
    
    @IBAction func onTapDay(_ sender: Any) {
        
        let picker = self.viewController(storyboard: "Common", identifier: "PickerViewController") as! PickerViewController
        let dataArray = self.tourData.days.map { DateFormatter(dateFormat: "M/d(E)").string(from: $0) }
        let defaultIndex = self.selectedDayIndex ?? 0
        picker.set(title: "Day", dataArray: dataArray, defaultIndex: defaultIndex, completion: { [weak self] index in
            self?.selectedDayIndex = index
            self?.dayLabel.text = dataArray[index]
        })
        self.stack(viewController: picker, animationType: .none)
    }
    
    @IBAction func onTapReserve(_ sender: Any) {
        
        if SaveData.shared.guideId.count > 0 {
            Dialog.show(style: .error, title: "エラー", message: "ガイドは予約できません", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        if self.selectedDayIndex == nil {
            Dialog.show(style: .error, title: "Error", message: "Select Day", actions: [DialogAction(title: "OK", action: nil)])
            return
        }
        
        guard let myGuestData = GuestRequester.shared.query(id: SaveData.shared.guestId) else {
            return
        }
        let customerId = myGuestData.stripeCustomerId
        let customerContext = STPCustomerContext(keyProvider:StripeApiClient(customerId: customerId))
        let paymentMethodsViewController = STPPaymentMethodsViewController(configuration: STPPaymentConfiguration.shared(),
                                                                           theme: STPTheme.default(),
                                                                           customerContext: customerContext,
                                                                           delegate: self)
        let navigationController = UINavigationController(rootViewController: paymentMethodsViewController)
        self.present(navigationController, animated: true)
    }
    
    @IBAction func onTapMessage(_ sender: Any) {
        let message = self.viewController(storyboard: "Message", identifier: "MessageDetailViewController") as! MessageDetailViewController
        message.set(targetUserId: self.guideId)
        self.stack(viewController: message, animationType: .horizontal)
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension GuideDetailTourViewController: STPPaymentMethodsViewControllerDelegate {
    
    func paymentMethodsViewController(_ paymentMethodsViewController: STPPaymentMethodsViewController, didFailToLoadWithError error: Error) {
        
    }
    
    func paymentMethodsViewControllerDidFinish(_ paymentMethodsViewController: STPPaymentMethodsViewController) {
        
        paymentMethodsViewController.dismiss(animated: true, completion: nil)
        
        guard let _ = self.selectedCardId else {
            return
        }
        self.postReserve()
    }
    
    func paymentMethodsViewControllerDidCancel(_ paymentMethodsViewController: STPPaymentMethodsViewController) {
        paymentMethodsViewController.dismiss(animated: true, completion: nil)
    }
    
    func paymentMethodsViewController(_ paymentMethodsViewController: STPPaymentMethodsViewController, didSelect paymentMethod: STPPaymentMethod) {
        self.selectedCardId = (paymentMethod as? STPCard)?.cardId
    }
}

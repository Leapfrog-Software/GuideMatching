//
//  AppDelegate.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/26.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit
import Stripe

@UIApplicationMain
class AppDelegate: UIResponder, UIApplicationDelegate {

    var window: UIWindow?

    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplicationLaunchOptionsKey: Any]?) -> Bool {

        STPPaymentConfiguration.shared().publishableKey = Constants.Stripe.Key
        
        return true
    }
    
    func applicationWillEnterForeground(_ application: UIApplication) {
        let saveData = SaveData.shared
        if saveData.guestId.count > 0 || saveData.guideId.count > 0 {
            AccountRequester.login()
        }
    }
}


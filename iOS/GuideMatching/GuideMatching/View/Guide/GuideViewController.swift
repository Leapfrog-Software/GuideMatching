//
//  GuideViewController.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class GuideViewController: UIViewController {

    @IBOutlet private weak var backButton: UIButton!
    
    private var isSearchResult = false
    private var guideDatas = [GuideData]()

    func set(searchResult: [GuideData]) {
        self.isSearchResult = true
        self.guideDatas = searchResult
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        if !isSearchResult {
            self.backButton.isHidden = true
            self.guideDatas = GuideRequester.shared.dataList
        }
    }
    
    @IBAction func onTapBack(_ sender: Any) {
        self.pop(animationType: .horizontal)
    }
}

extension GuideViewController: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return self.guideDatas.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: "GuideTableViewCell", for: indexPath) as! GuideTableViewCell
        cell.configure(guideData: self.guideDatas[indexPath.row])
        return cell
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let detail = self.viewController(storyboard: "Guide", identifier: "GuideDetailViewController") as! GuideDetailViewController
        self.tabbarViewController()?.stack(viewController: detail, animationType: .horizontal)
    }
}

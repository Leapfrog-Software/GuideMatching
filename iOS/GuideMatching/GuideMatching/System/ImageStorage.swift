//
//  ImageStorage.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

private struct ImageStorageData {
    
    let url: String
    let image: UIImage
}

private struct ImageRequestData {
    
    let url: String
    let imageView: UIImageView
}

class ImageStorage {
    
    static let shared = ImageStorage()
    
    private var imageList = [ImageStorageData]()
    private var requestList = [ImageRequestData]()
    
    func fetch(url: String, imageView: UIImageView, defaultImage: UIImage? = nil) {
        
        if let image = (self.imageList.filter { $0.url == url }).first?.image {
            imageView.image = image
            return
        }
        imageView.image = nil
        
        self.cancelRequest(imageView: imageView)
        
        let request = ImageRequestData(url: url, imageView: imageView)
        self.requestList.append(request)
        
        HttpManager.get(url: url) { [weak self] (result, data) in
            
            guard let requestData = (self?.requestList.filter{ $0.url == url })?.first else {
                return
            }
            
            var resultImage: UIImage?
            if result, let data = data, let image = UIImage(data: data) {
                resultImage = image
            } else {
                if let defaultImage = defaultImage {
                    resultImage = defaultImage
                } else {
                    resultImage = UIImage(named: "no_image")
                }
            }
            let storageData = ImageStorageData(url: url, image: resultImage!)
            self?.imageList.append(storageData)
            self?.cancelRequest(imageView: requestData.imageView)
            requestData.imageView.image = resultImage!
        }
    }
    
    func query(url: String) -> UIImage? {
        return (self.imageList.filter { $0.url == url }).first?.image
    }
    
    func cancelRequest(imageView: UIImageView) {
        
        var sameIndexes = [Int]()
        for i in 0..<self.requestList.count {
            if self.requestList[i].imageView == imageView {
                sameIndexes.append(i)
            }
        }
        sameIndexes.forEach{ self.requestList.remove(at: $0) }
    }
    
    func remove(url: String) {
        if let index = self.imageList.index(where: { $0.url == url }) {
            self.imageList.remove(at: index)
        }
    }
}

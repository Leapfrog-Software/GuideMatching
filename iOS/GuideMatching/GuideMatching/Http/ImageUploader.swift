//
//  ImageUploader.swift
//  GuideMatching
//
//  Created by Leapfrog-Software on 2018/06/27.
//  Copyright © 2018年 Leapfrog-Inc. All rights reserved.
//

import UIKit

class ImageUploader {
    
    class func post(url: String, image: UIImage, params: [String: String], completion: @escaping ((Bool) -> ())) {
        
        guard let imageData = UIImagePNGRepresentation(image) else {
            completion(false)
            return
        }
        
        var body = Data()
        
        for (key, value) in params {
            if let data = "--boundary\r\nContent-Disposition: form-data; name=\"\(key)\"\r\n\r\n\(value)\r\n".data(using: .utf8) {
                body.append(data)
            }
        }
        if let data = "--boundary\r\nContent-Disposition: form-data; name=\"image\"; filename=\"image\"\r\nContent-Type: image/png\r\n\r\n".data(using: .utf8) {
            body.append(data)
        }
        body.append(imageData)
        
        if let data = "\r\n\r\n--boundary--\r\n\r\n".data(using: .utf8) {
            body.append(data)
        }
        
        let additionalHeader = ["Content-Type": "multipart/form-data; boundary=boundary"]
        
        HttpManager.request(url: url, method: "POST", body: body, additionalHeader: additionalHeader) { (result, data) in
            if result, let data = data {
                do {
                    if let json = try JSONSerialization.jsonObject(with: data, options: JSONSerialization.ReadingOptions.allowFragments) as? Dictionary<String, Any> {
                        if json["result"] as? String == "0" {
                            completion(true)
                            return
                        }
                    }
                } catch {}
            }
            completion(false)
        }
    }
}

import Foundation

class Base64IvAndCiphertext {
    
    private let base64String: String
    
    init(data: NSData) {
        self.base64String = String(data: data, encoding: DEFAULT_ENCODING)!
    }
    
    var decodedIv: String {
        get {
            let base64Iv = base64String.substringToIndex(base64String.startIndex.advancedBy(44))
            let decodedIv = NSData(base64EncodedString: base64Iv, options: [])!
            return String(data: decodedIv, encoding: DEFAULT_ENCODING)!
        }
    }
    
    var encodedCiphertext: String {
        get {
            return base64String.substringFromIndex(base64String.startIndex.advancedBy(44))        }
    }
}

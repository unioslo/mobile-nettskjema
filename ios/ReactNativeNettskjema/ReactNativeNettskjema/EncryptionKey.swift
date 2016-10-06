import Foundation
import KeychainSwift

class EncryptionKey {
    
    private let keychain = KeychainSwift()
    private let encryptionKeyKey = "encryptionKey"
    
    func getOrCreate() -> String {
        if let key = keychain.get(encryptionKeyKey) {
            return key
        } else {
            let randomKey = NSUUID().UUIDString.stringByReplacingOccurrencesOfString("-", withString: "")
            keychain.set(randomKey, forKey: encryptionKeyKey)
            return randomKey
        }
        
    }
}

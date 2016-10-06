import CryptoSwift
import KeychainSwift


protocol EncryptionMethod {
    func encrypt(plaintext: NSData) throws -> NSData
    func decrypt(encrypted: NSData) throws -> NSData
}

class ChaCha20Encryption: EncryptionMethod {
    
    private let key: String
    
    init(key: String) {
        self.key = key
    }
    
    convenience init() {
        self.init(key: EncryptionKey().getOrCreate())
    }
    
    func encrypt(plaintext: NSData) throws -> NSData {
        let plaintextBytes = plaintext.arrayOfBytes()
        let iv = AES.randomIV(32/2).toHexString()
        let encryptedString = try crypto(iv).encrypt(plaintextBytes).toBase64()!
        let ivBase64 = iv.dataUsingEncoding(DEFAULT_ENCODING)!.base64EncodedStringWithOptions([])
        return (ivBase64 + encryptedString).dataUsingEncoding(DEFAULT_ENCODING, allowLossyConversion: false)!
    }
    
    func decrypt(encryptedBase64: NSData) throws -> NSData {
        let contents = Base64IvAndCiphertext(data: encryptedBase64)
        let decryptedBytes = try contents.encodedCiphertext.decryptBase64(crypto(contents.decodedIv))
        return NSData(bytes: decryptedBytes)
    }
    
    private func crypto(iv: String) -> Cipher {
        return ChaCha20(key: key, iv: iv)!
    }
}

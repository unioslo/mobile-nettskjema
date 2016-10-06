import CryptoSwift
import Foundation

class DecryptionPipe: Pipe {
    
    private let encryptionMethod: EncryptionMethod
    
    init(encryptionMethod: EncryptionMethod) {
        self.encryptionMethod = encryptionMethod
    }
    
    func connect(inputFile: NSURL, outputFile: NSURL) throws {
        let encryptedData = NSData(contentsOfURL: inputFile)!
        let decryptedData = try encryptionMethod.decrypt(encryptedData)
        NSFileManager.defaultManager().createFileAtPath(outputFile.path!, contents: decryptedData, attributes: nil)
    }
}

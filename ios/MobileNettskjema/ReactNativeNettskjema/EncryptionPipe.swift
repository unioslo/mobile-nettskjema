import CryptoSwift
import Foundation

class EncryptionPipe: Pipe {
    
    private let encryptionMethod: EncryptionMethod
    
    init(encryptionMethod: EncryptionMethod) {
        self.encryptionMethod = encryptionMethod
    }
    
    func connect(inputFile: NSURL, outputFile: NSURL) throws {
        let plaintextData = NSData(contentsOfURL: inputFile)!
        let encryptedData = try encryptionMethod.encrypt(plaintextData)
        NSFileManager.defaultManager().createFileAtPath(outputFile.path!, contents: encryptedData, attributes: nil)
    }
}

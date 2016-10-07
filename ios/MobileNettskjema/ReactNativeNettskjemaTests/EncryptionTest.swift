import Foundation
import CryptoSwift
import XCTest
@testable import MobileNettskjema

private let STORAGE_DIRECTORY = try! LibraryCacheStorageDirectory().getOrCreateSubdirectory("encryptionTest")
private let FILES = [
    "original": STORAGE_DIRECTORY.fileNamed("plaintext"),
    "encrypted": STORAGE_DIRECTORY.fileNamed("plaintext.encrypted"),
    "decrypted": STORAGE_DIRECTORY.fileNamed("plaintext.decrypted")
]

class EncryptionTest: XCTestCase {
    let key = String(count: 32, repeatedValue: Character("a"))
    let fileManager = NSFileManager.defaultManager()

    override func setUp() {
        super.setUp()
        fileManager.createFileAtPath(FILES["original"]!.path!, contents: "1test\n2test\n3test".dataUsingEncoding(DEFAULT_ENCODING), attributes: nil)
    }
 
    override func tearDown() {
        super.tearDown()
        for (_, url) in FILES {
            do {
                try fileManager.removeItemAtURL(url)
            } catch { }
        }
    }
    
    func testDecryptedFileIsEqualToOriginal() {
        let method = ChaCha20Encryption(key: key)
        try! EncryptionPipe(encryptionMethod: method).connect(FILES["original"]!, outputFile: FILES["encrypted"]!)
        try! DecryptionPipe(encryptionMethod: method).connect(FILES["encrypted"]!, outputFile: FILES["decrypted"]!)
        
        XCTAssertEqual(NSData(contentsOfURL: FILES["original"]!), NSData(contentsOfURL: FILES["decrypted"]!))
        XCTAssertNotEqual(NSData(contentsOfURL: FILES["original"]!), NSData(contentsOfURL: FILES["encrypted"]!))
    }
    
    func testAppendIvAndRetrieveFromCiphertext() {
        let encryptionMethod = ChaCha20Encryption(key: key)
        try! EncryptionPipe(encryptionMethod: encryptionMethod).connect(FILES["original"]!, outputFile: FILES["encrypted"]!)
        let retrievedIv = Base64IvAndCiphertext(data: NSData(contentsOfURL: FILES["encrypted"]!)!).decodedIv
        XCTAssertEqual(retrievedIv.lengthOfBytesUsingEncoding(DEFAULT_ENCODING), 32)
    }
}

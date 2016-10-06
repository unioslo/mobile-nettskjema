import Foundation
import CryptoSwift
import XCTest
@testable import ReactNativeNettskjema

private let STORAGE_DIRECTORY = DocumentStorageDirectory()
private let ORIGINAL_FILE = STORAGE_DIRECTORY.newFileWithName("plaintext")
private let ENCRYPTED_FILE = STORAGE_DIRECTORY.newFileWithName("plaintext.encrypted")
private let DECRYPTED_FILE = STORAGE_DIRECTORY.newFileWithName("plaintext.decrypted")


class EncryptionTest: XCTestCase {
    let key = String(count: 32, repeatedValue: Character("a"))

    override func setUp() {
        super.setUp()
        let fileManager = NSFileManager.defaultManager()
        fileManager.createFileAtPath(ORIGINAL_FILE.path!, contents: "1test\n2test\n3test".dataUsingEncoding(NSUTF8StringEncoding), attributes: nil)
    }
    
    
    func testDecryptedFileIsEqualToOriginal() {
        let method = ChaCha20Encryption(key: key)
        try! EncryptionPipe(encryptionMethod: method).connect(ORIGINAL_FILE, outputFile: ENCRYPTED_FILE)
        try! DecryptionPipe(encryptionMethod: method).connect(ENCRYPTED_FILE, outputFile: DECRYPTED_FILE)
        
        XCTAssertEqual(NSData(contentsOfURL: ORIGINAL_FILE), NSData(contentsOfURL: DECRYPTED_FILE))
        XCTAssertNotEqual(NSData(contentsOfURL: ORIGINAL_FILE), NSData(contentsOfURL: ENCRYPTED_FILE))
    }
    
    func testAppendIvAndRetrieveFromCiphertext() {
        let encryptionMethod = ChaCha20Encryption(key: key)
        try! EncryptionPipe(encryptionMethod: encryptionMethod).connect(ORIGINAL_FILE, outputFile: ENCRYPTED_FILE)
        let retrievedIv = Base64IvAndCiphertext(data: NSData(contentsOfURL: ENCRYPTED_FILE)!).decodedIv
        XCTAssertEqual(retrievedIv.lengthOfBytesUsingEncoding(NSUTF8StringEncoding), 32)
    }
}

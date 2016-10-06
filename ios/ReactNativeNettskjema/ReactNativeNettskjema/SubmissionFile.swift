import Foundation

class SubmissionFile {
    
    private var file: NSURL
    private let encryptionMethod: EncryptionMethod
    
    init(file: NSURL) {
        self.file = file
        self.encryptionMethod = ChaCha20Encryption()
    }
    
    convenience init(jsonFile: JsonFile) {
        self.init(file: jsonFile.storageFile)
    }
    
    func markAs(submissionFileState: SubmissionFileState, eventSink: EventSink) throws {
        let identifier = submissionFileState.fileExtension
        let newFile = NSURL(fileURLWithPath: (file.path! as NSString).stringByDeletingPathExtension + "." + identifier)
        try NSFileManager.defaultManager().moveItemAtURL(file, toURL: newFile)
        self.file = newFile
        eventSink.put(SubmissionStateChanged(submissionFileState: submissionFileState))
    }
    
    func isMarked(submissionFileState: SubmissionFileState) -> Bool {
        return file.pathExtension! == submissionFileState.fileExtension
    }
    
    func deleteStoredFile() throws {
        try NSFileManager.defaultManager().removeItemAtURL(file)
    }
    
    var contents: String {
        return String(data: NSData(contentsOfURL: file)!, encoding: NSUTF8StringEncoding)!
    }
    
    func encrypt() throws {
        try InplaceFileTransformation(originalFile: file, transformingPipe: EncryptionPipe(encryptionMethod: encryptionMethod)).perform()
    }
    
    func decrypt() throws {
        try InplaceFileTransformation(originalFile: file, transformingPipe: DecryptionPipe(encryptionMethod: encryptionMethod)).perform()
    }
    
}

import Foundation

class SubmissionFile {
    
    private var file: NSURL
    
    init(file: NSURL) {
        self.file = file
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
    
}

import Foundation

private let tempExtension = "queueTemp"

extension NSURL {
    func isTemporary() -> Bool {
        return self.pathExtension! == tempExtension
    }
}

class TemporaryFile {
    private let originalPath: String
    let file: NSURL
    private let fileManager = NSFileManager.defaultManager()
    
    init(file: NSURL) {
        self.originalPath = file.absoluteString!
        self.file = NSURL(fileURLWithPath: originalPath + "." + tempExtension)
    }
    
    func replaceOriginalFile() throws {
        try fileManager.copyItemAtURL(self.file, toURL: NSURL(fileURLWithPath: originalPath))
        try fileManager.removeItemAtURL(self.file)
    }

}

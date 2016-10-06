import Foundation

private let tempExtension = "queueTemp"

extension NSURL {
    func isTemporary() -> Bool {
        return self.pathExtension! == tempExtension
    }
}

class TemporaryFile {
    private let originalFile: NSURL
    let tempFile: NSURL
    private let fileManager = NSFileManager.defaultManager()
    
    init(file: NSURL) {
        self.originalFile = file
        self.tempFile = NSURL(fileURLWithPath: originalFile.path! + "." + tempExtension)
    }
    
    func replaceOriginalFile() throws {
        try fileManager.replaceItemAtURL(originalFile, withItemAtURL: tempFile, backupItemName: nil, options: NSFileManagerItemReplacementOptions.UsingNewMetadataOnly, resultingItemURL: nil)
    }

}

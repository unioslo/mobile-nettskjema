import Foundation

protocol StorageDirectory {
    func newFileWithName(filename: String) -> NSURL
}

class DocumentStorageDirectory: StorageDirectory {
    func newFileWithName(filename: String) -> NSURL {
        let fileManager = NSFileManager()
        let directories: [NSURL] = fileManager.URLsForDirectory(NSSearchPathDirectory.DocumentDirectory, inDomains: NSSearchPathDomainMask.UserDomainMask)
        let directory = directories[0]
        return directory.URLByAppendingPathComponent(filename)!
    }
}

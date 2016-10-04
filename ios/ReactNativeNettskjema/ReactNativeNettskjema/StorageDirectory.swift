import Foundation

protocol StorageDirectory {
    func newFileWithName(filename: String) -> NSURL
    var storedFiles: [NSURL] { get }
}

class DocumentStorageDirectory: StorageDirectory {
    let fileManager = NSFileManager.defaultManager()

    func newFileWithName(filename: String) -> NSURL {
        return directory.URLByAppendingPathComponent(filename)!
    }
    
    private var directory: NSURL {
        get {
            let directories: [NSURL] = fileManager.URLsForDirectory(NSSearchPathDirectory.DocumentDirectory, inDomains: NSSearchPathDomainMask.UserDomainMask)
            return directories[0]
        }
    }
    
    var storedFiles: [NSURL] {
        get {
            return try! fileManager.contentsOfDirectoryAtURL(directory, includingPropertiesForKeys: nil, options: [])
        }
    }

}

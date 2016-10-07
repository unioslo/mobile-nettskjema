import Foundation

extension NSURL {
    func fileNamed(filename: String) -> NSURL {
        return self.URLByAppendingPathComponent(filename)!
    }
}

class LibraryCacheStorageDirectory: StorageDirectory {
    let fileManager = NSFileManager.defaultManager()
    
    func fileNamed(filename: String) throws -> NSURL {
        return try directory().fileNamed(filename)
    }
    
    func getOrCreateSubdirectory(name: String) throws -> NSURL {
        let directoryUrl = rootDirectory.fileNamed(name)
        if (fileManager.fileExistsAtPath(directoryUrl.path!)) {
            return directoryUrl
        } else {
            try fileManager.createDirectoryAtURL(directoryUrl, withIntermediateDirectories: false, attributes: nil)
            return directoryUrl
        }
    }
    
    private var rootDirectory: NSURL {
        get {
            let directories: [NSURL] = fileManager.URLsForDirectory(NSSearchPathDirectory.CachesDirectory, inDomains: NSSearchPathDomainMask.UserDomainMask)
            return directories[0]
        }
    }
    
    private func directory() throws -> NSURL {
        return try getOrCreateSubdirectory("mobileNettskjema")
    }
    
    func storedFiles() throws -> [NSURL] {
        return try fileManager.contentsOfDirectoryAtURL(directory(), includingPropertiesForKeys: nil, options: [])
    }
    
}

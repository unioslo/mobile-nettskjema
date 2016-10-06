import Foundation

class TestFile {
    let file: NSURL
    let kbSize: Int
    let fileManager = NSFileManager.defaultManager()
    
    init(kbSize: Int) throws {
        self.file = try LibraryCacheStorageDirectory().fileNamed(String(kbSize) + "kbFile")
        self.kbSize = kbSize
    }
    
    var randomContent: NSURL {
        get {
            let data = "".stringByPaddingToLength(kbSize * 1024, withString: " ", startingAtIndex: 0).dataUsingEncoding(DEFAULT_ENCODING)
            fileManager.createFileAtPath(self.file.path!, contents: data, attributes: nil)
            return self.file
        }
    }
    
    func delete() {
        try! fileManager.removeItemAtURL(self.file)
    }
    
}

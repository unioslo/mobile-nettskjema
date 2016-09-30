import Foundation

class TestFile {
    let file: NSURL
    let kbSize: Int
    
    init(kbSize: Int) {
        let directory = NSSearchPathForDirectoriesInDomains(NSSearchPathDirectory.DocumentDirectory, NSSearchPathDomainMask.AllDomainsMask,  true).first!
        self.file = NSURL(fileURLWithPath: directory).URLByAppendingPathComponent(String(kbSize) + "kbFile")!
        self.kbSize = kbSize
    }
    
    var randomContent: NSURL {
        get {
            let data = "".stringByPaddingToLength(kbSize * 1024, withString: " ", startingAtIndex: 0).dataUsingEncoding(NSASCIIStringEncoding)
            NSFileManager.defaultManager().createFileAtPath(self.file.path!, contents: data, attributes: nil)
            return self.file
        }
    }
}

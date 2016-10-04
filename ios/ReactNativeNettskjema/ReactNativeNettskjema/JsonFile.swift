import Foundation

class JsonFile {
    let storageFile: NSURL
    
    init(storageFile: NSURL) {
        self.storageFile = storageFile
    }
    
    func store(jsonSerializable: JSONSerializable) throws {
        let jsonData = try NSJSONSerialization.dataWithJSONObject(jsonSerializable.serialized, options: [])
        NSFileManager.defaultManager().createFileAtPath(storageFile.path!, contents: jsonData, attributes: nil)
    }
    
    
}

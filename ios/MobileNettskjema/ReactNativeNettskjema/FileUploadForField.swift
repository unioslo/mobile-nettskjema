import Alamofire
import Foundation

class FileUploadForField: FilledInFormField {
    
    private let identifier: String
    private let filename: String
    private let mimeType: String
    private let fileContents: NSData
    
    init(uploadQuestion: UploadQuestion, file: NSURL, mimeType: String) {
        self.identifier = uploadQuestion.identifier.name
        self.filename = file.lastPathComponent!
        self.fileContents = NSData(contentsOfURL: file)!
        self.mimeType = mimeType
    }
    
    init(serialized: [String: AnyObject]) {
        self.identifier = serialized["identifier"] as! String
        self.filename = serialized["name"] as! String
        self.mimeType = serialized["mimeType"] as! String
        self.fileContents = NSData(base64EncodedString: serialized["base64"] as! String, options: [])!
    }
    
    func addTo(multipartFormData: KeyValueMedia) {
        multipartFormData.writeFile(identifier, value: self.fileContents, filename: filename, mimeType: mimeType)
    }
    
    var serialized: [String : AnyObject] {
        get {
            return [
                "name": filename,
                "identifier": identifier,
                "mimeType": mimeType,
                "base64": fileContents.base64EncodedStringWithOptions([])
            ]
        }
    }
}

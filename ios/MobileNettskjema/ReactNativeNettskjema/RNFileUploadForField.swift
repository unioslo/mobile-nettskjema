import Foundation

class RNFileUploadForField: RNApiBridge {
    typealias T = FileUploadForField
    private let mediaType: String
    private let file: NSURL
    private let questionId: Int
    
    init(field: [String: Any]) {
        self.mediaType = field["mediaType"] as! String
        self.questionId = field["questionId"] as! Int
        self.file = NSURL(fileURLWithPath: field["filepath"] as! String)
    }
    
    var bridged: FileUploadForField {
        get {
            return FileUploadForField(uploadQuestion: UploadQuestion(id: questionId), file: file, mimeType: mediaType)
        }
    }
}

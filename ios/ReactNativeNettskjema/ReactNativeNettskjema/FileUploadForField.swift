import Alamofire
import Foundation

class FileUploadForField: FilledInFormField {
    
    private let identifier: String
    private let file: NSURL
    
    init(uploadQuestion: UploadQuestion, file: NSURL) {
        self.identifier = uploadQuestion.identifier.name
        self.file = file
    }
    
    func addTo(multipartFormData: MultipartFormData) {
        multipartFormData.appendBodyPart(fileURL: self.file, name: self.identifier)
    }
}

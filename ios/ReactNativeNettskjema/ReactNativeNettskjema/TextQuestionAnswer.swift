import Alamofire

class TextQuestionAnswer: FilledInFormField {
    private let identifier: String
    private let answer: String
    
    init(question: TextQuestion, answer: String) {
        self.identifier = question.identifier.name
        self.answer = answer
    }
    
    func addTo(multipartFormData: MultipartFormData) {
        multipartFormData.appendBodyPart(data: answer.dataUsingEncoding(NSASCIIStringEncoding)!, name: identifier)
    }
}

import Alamofire

class TextQuestionAnswer: FilledInFormField {
    private let identifier: String
    private let answer: String
    
    init(question: TextQuestion, answer: String) {
        self.identifier = question.identifier.name
        self.answer = answer
    }
    
    init(serialized: [String: AnyObject]) {
        self.identifier = serialized["identifier"] as! String
        self.answer = serialized["answer"] as! String
    }
    
    func addTo(multipartFormData: KeyValueMedia) {
        multipartFormData.write(identifier, value: answer.dataUsingEncoding(NSASCIIStringEncoding)!)
    }
    
    
    var serialized: [String : AnyObject] {
        get {
            return ["identifier": identifier, "answer": answer]
        }
    }
}



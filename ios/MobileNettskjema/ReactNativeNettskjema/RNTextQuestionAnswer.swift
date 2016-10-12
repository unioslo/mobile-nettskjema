import Foundation

class RNTextQuestionAnswer {
    typealias T = TextQuestionAnswer
    
    private let id: Int
    private let answer: String
    
    init(field: [String: AnyObject]) {
        NSLog("RNTextQuestionAnswer: " + field.debugDescription)
        self.id = field["questionId"]! as! Int
        self.answer = field["answer"]! as! String
    }
    
    var bridged: TextQuestionAnswer {
        get {
            return TextQuestionAnswer(question: TextQuestion(id: id), answer: answer)
        }
    }
}

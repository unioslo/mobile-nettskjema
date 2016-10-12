import Foundation

enum ProgrammerError: ErrorType {
    case InvalidFieldType
}

class NettskjemaRNField {
    private let field: [String: AnyObject]
    
    init(field: [String: AnyObject]) {
        self.field = field
    }
    
    func deserialized() throws -> FilledInFormField {
        NSLog("NettskjemaRNField: " + field.debugDescription)
        switch(field["type"]! as! String) {
        case "upload": return RNFileUploadForField(field: field).bridged
        case "radio": return RNSelectedRadioOption(field: field).bridged
        case "multipleChoice": return RNSelectedMultipleChoiceOption(field: field).bridged
        case "text": return RNTextQuestionAnswer(field: field).bridged
        default : throw ProgrammerError.InvalidFieldType
        }
    }
}

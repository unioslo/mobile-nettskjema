enum ProgrammerError: ErrorType {
    case InvalidFieldType
}

class NettskjemaRNField {
    private let field: [String: Any]
    
    init(field: [String: Any]) {
        self.field = field
    }
    
    func deserialized() throws -> FilledInFormField {
        switch(field["type"]! as! String) {
        case "upload": return RNFileUploadForField(field: field).bridged
        case "radio": return RNSelectedRadioOption(field: field).bridged
        case "multipleChoice": return RNSelectedMultipleChoiceOption(field: field).bridged
        case "text": return RNTextQuestionAnswer(field: field).bridged
        default : throw ProgrammerError.InvalidFieldType
        }
    }
}

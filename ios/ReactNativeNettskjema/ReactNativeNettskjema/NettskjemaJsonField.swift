private class SerializedDoesNotHaveCorrespondingFilledInFormField: ErrorType {
    
}

class NettskjemaJsonField {
    private let field: [String: AnyObject]
    
    init(field: [String: AnyObject]) {
        self.field = field
    }
    
    func deserialized() throws -> FilledInFormField {
        if field["base64"] != nil {
            return FileUploadForField(serialized: field)
        }
        if field["selectedOption"] != nil {
            return SelectedMultiOption(serialized: field)
        }
        if field["answer"] != nil {
            return TextQuestionAnswer(serialized: field)
        }
        throw SerializedDoesNotHaveCorrespondingFilledInFormField()

    }
}

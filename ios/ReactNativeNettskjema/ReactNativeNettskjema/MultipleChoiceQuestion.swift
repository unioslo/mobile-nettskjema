class MultipleChoiceQuestion: MultipleOptionsFormField {
    private let id: Int
    let optionIds: [Int]
    
    init(id: Int, optionIds: [Int]) {
        self.id = id
        self.optionIds = optionIds
    }
    
    var identifier: FieldIdentifier {
        get {
            return NettskjemaFieldIdentifier(nettskjemaName: "answerOptions", id: id)
        }
    }
}

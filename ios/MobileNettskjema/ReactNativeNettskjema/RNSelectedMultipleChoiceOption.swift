class RNSelectedMultipleChoiceOption: RNApiBridge {
    private let questionId: Int
    private let selectedOptionId: Int
    
    init(field: [String: AnyObject]) {
        self.questionId = field["questionId"] as! Int
        self.selectedOptionId = field["selectedOptionId"] as! Int
    }
    
    var bridged: SelectedMultiOption {
        return SelectedMultiOption(
            question: MultipleChoiceQuestion(id: questionId, optionIds: [selectedOptionId]),
            index: 0
        )
    }
}

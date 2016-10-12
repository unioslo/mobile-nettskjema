class RNSelectedRadioOption: RNApiBridge {
    typealias T = SelectedMultiOption
    private let questionId: Int
    private let selectedOptionId: Int
    
    init(field: [String: AnyObject]) {
        self.questionId = field["questionId"] as! Int
        self.selectedOptionId = field["selectedOptionId"] as! Int
    }
    
    var bridged: SelectedMultiOption {
        return SelectedMultiOption(
            question: RadioQuestion(id: questionId, optionIds: [selectedOptionId]),
            index: 0
        )
    }
}

class TestForm {
    static let formId = 75319
    static let form = NettskjemaForm(id: formId)
    static let textQuestionId = 577795
    static let textQuestion = TextQuestion(id: textQuestionId)
    static let radioQuestionId = 578095
    static let radioQuestionSelectedId = 1275519
    static let radioQuestionUnselectedId = 1275520
    static let radioQuestion = RadioQuestion(id: radioQuestionId, optionIds: [radioQuestionSelectedId, radioQuestionUnselectedId])
    static let multipleChoiceQuestionId = 578096
    static let multipleChoiceQuestionOptionIds = [1275521, 1275522, 1275523]
    static let multipleChoiceQuestionSelectedOptionIds = multipleChoiceQuestionOptionIds[0..<2]
    static let multipleChoiceQuestion = MultipleChoiceQuestion(id: multipleChoiceQuestionId, optionIds: multipleChoiceQuestionOptionIds)
    static let uploadField1Id = 577797
    static let uploadField2Id = 577798
    static let uploadField1 = UploadQuestion(id: uploadField1Id)
    static let uploadField2 = UploadQuestion(id: uploadField2Id)
}

import Alamofire

class SelectedMultiOption: FilledInFormField {
    private let identifier: String
    private let selectedOption: String
    
    init(question: MultipleOptionsFormField, index: Int) {
        self.identifier = question.identifier.name
        self.selectedOption = String(question.optionIds[index])
    }
    
    func addTo(multipartFormData: MultipartFormData) {
        multipartFormData.appendBodyPart(data: selectedOption.dataUsingEncoding(NSASCIIStringEncoding)!, name: identifier)
    }
    
}

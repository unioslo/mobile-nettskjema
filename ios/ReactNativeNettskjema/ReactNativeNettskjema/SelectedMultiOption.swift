import Alamofire

class SelectedMultiOption: FilledInFormField {
    private let identifier: String
    private let selectedOption: Int
    
    init(question: MultipleOptionsFormField, index: Int) {
        self.identifier = question.identifier.name
        self.selectedOption = question.optionIds[index]
    }
    
    init(serialized: [String: AnyObject]) {
        self.identifier = serialized["identifier"] as! String
        self.selectedOption = serialized["selectedOption"] as! Int
    }
    
    func addTo(multipartFormData: KeyValueMedia) {
        multipartFormData.write(identifier, value: String(selectedOption).dataUsingEncoding(NSASCIIStringEncoding)!)
    }
    
    var serialized: [String : AnyObject] {
        get {
            return ["identifier": identifier, "selectedOption": selectedOption]
        }
    }
    
}

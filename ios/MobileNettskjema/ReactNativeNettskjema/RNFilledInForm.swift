class RNFilledInForm: RNApiBridge {
    typealias T = FilledInForm
    private let form: Form
    private let filledInFormFields: [FilledInFormField]
    
    
    init(submission: [String: Any]) throws {
        self.form = NettskjemaForm(id: (submission["form"] as! [String: Int])["id"]!)
        self.filledInFormFields = try NettskjemaRNFields(fields: submission["fields"] as! [[String: Any]]).asList
    }
    
    var bridged: FilledInForm {
        get {
            return NettskjemaFilledInForm(form: form, filledInFormFields: filledInFormFields)
        }
    }

}

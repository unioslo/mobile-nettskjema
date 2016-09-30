import Alamofire

class NettskjemaFilledInForm: FilledInForm {
    private let form: Form
    private let filledInFormFields: [FilledInFormField]
    
    init(form: Form, filledInFormFields: [FilledInFormField]) {
        self.form = form
        self.filledInFormFields = filledInFormFields
    }
    
    func postRequest(csrfToken: MultipartRequestField, onComplete: (Manager.MultipartFormDataEncodingResult -> Void)) {
        return Alamofire.upload(
            .POST,
            form.postUrl,
            multipartFormData: {
                csrfToken.addTo($0)
                for filledInFormField in self.filledInFormFields {
                    filledInFormField.addTo($0)
                }
            },
            encodingCompletion: onComplete
        )
    }
}

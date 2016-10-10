import Alamofire

extension MultipartFormData: KeyValueMedia {
    public func write(key: String, value: NSData) {
        self.appendBodyPart(data: value, name: key)
    }
    
    public func writeFile(key: String, value: NSData, filename: String, mimeType: String) {
        self.appendBodyPart(data: value, name: key, fileName: filename, mimeType: mimeType)
    }
}

class NettskjemaFilledInForm: FilledInForm {
    private let form: Form
    private let filledInFormFields: [FilledInFormField]
    
    init(form: Form, filledInFormFields: [FilledInFormField]) {
        self.form = form
        self.filledInFormFields = filledInFormFields
    }
    
    convenience init(json: String) throws {
        let jsonData = try NSJSONSerialization.JSONObjectWithData(
            json.dataUsingEncoding(DEFAULT_ENCODING)!,
            options: [])
        try self.init(fromDictionary: jsonData as! [String: AnyObject])
    }
    
    convenience init(fromDictionary: [String: AnyObject]) throws {
        self.init(form: NettskjemaForm(serialized: fromDictionary["form"] as! [String: AnyObject]),
                  filledInFormFields: try NettskjemaJsonFields(fields: fromDictionary["fields"] as! NSArray).asList)
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
    
    var serialized: [String : AnyObject] {
        get {
            return [
                "form": form.serialized,
                "fields": filledInFormFields.map { field in field.serialized },
            ]
        }
    }
}

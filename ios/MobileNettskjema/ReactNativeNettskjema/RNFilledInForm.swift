import Foundation


class RNFilledInForm: RNApiBridge {
    typealias T = FilledInForm
    private let form: Form
    private let filledInFormFields: [FilledInFormField]
    
    
    init(submission: [String: AnyObject]) throws {
        NSLog("RNFilledInForm: " + submission.debugDescription)
        self.form = NettskjemaForm(id: (submission["form"] as! [String: Int])["id"]!)
        self.filledInFormFields = try NettskjemaRNFields(fields: submission["fields"] as! [[String: AnyObject]]).asList
    }
    
    var bridged: FilledInForm {
        get {
            return NettskjemaFilledInForm(form: form, filledInFormFields: filledInFormFields)
        }
    }

}

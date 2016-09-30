public class NettskjemaFormSubmissionFactory {
    public func newSubmission(formId: Int, filledInFormFields: [FilledInFormField]) -> FormSubmission {
        return newSubmission(
            NettskjemaFilledInForm(
                form: NettskjemaForm(id: formId), filledInFormFields: filledInFormFields))
    }
    
    public func newSubmission(filledInForm: FilledInForm) -> FormSubmission {
        return NettskjemaFormSubmission(
            sessionManager: nil,
            csrfRequestFactory: NettskjemaCsrfRequestFactory(),
            csrfTokenFactory: NettskjemaCsrfTokenFactory(),
            filledInForm: filledInForm)
    }
}

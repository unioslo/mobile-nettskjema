private class NotYetPostedStatus: FormSubmissionStatus {
    let statusCode = FormSubmissionStatusCode.NOT_POSTED
    let description = "Submission not yet posted"
}

private class PostFailedStatus: FormSubmissionStatus {
    let error: String
    
    init(error: String) {
        self.error = error
    }
    
    let statusCode = FormSubmissionStatusCode.POST_FAILED
    var description: String {
        get {
            return "Submission failed with error: " + error
        }
    }
}

private class PostSucceeded: FormSubmissionStatus {
    let statusCode = FormSubmissionStatusCode.POST_SUCCESSFUL
    let description = "Submission successfully posted"
}

class NettskjemaFormSubmission: FormSubmission {
    
    private let csrfRequestFactory: CsrfRequestFactory
    private let csrfTokenFactory: CsrfTokenFactory
    private let filledInForm: FilledInForm
    
    init(sessionManager: String?, csrfRequestFactory: CsrfRequestFactory, csrfTokenFactory: CsrfTokenFactory, filledInForm: FilledInForm) {
        self.csrfRequestFactory = csrfRequestFactory
        self.csrfTokenFactory = csrfTokenFactory
        self.filledInForm = filledInForm
    }
    
    func post(onComplete: (status: FormSubmissionStatus) -> Void ) {
        csrfRequestFactory.newRequest({ response in
            switch response.result {
            case .Success(let data):
                self.filledInForm.postRequest(self.csrfTokenFactory.newCsrfToken(data), onComplete: { encodingResult in
                    switch encodingResult {
                    case .Success(let upload, _, _):
                        upload.responseString { response in
                            let stringResponse = String(response)
                            if stringResponse.containsString("failure") {
                                onComplete(status: PostFailedStatus(error: stringResponse))
                            } else {
                                onComplete(status: PostSucceeded())
                            }
                        }
                    case .Failure(let encodingError):
                        onComplete(status: PostFailedStatus(error: String(encodingError)))
                    }
                })
            case .Failure(let error):
                onComplete(status: PostFailedStatus(error: error.description))
            }
        })
    }
}
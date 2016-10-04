import Foundation

class SubmittedSubmission: SubmissionState {
    private let submissionFile: SubmissionFile
    private let submissionDecision: SubmissionDecision
    
    init(submissionFile: SubmissionFile, submissionDecision: SubmissionDecision) {
        self.submissionFile = submissionFile
        self.submissionDecision = submissionDecision
    }
    
    convenience init(file: NSURL, submissionDecision: SubmissionDecision) {
        self.init(submissionFile: SubmissionFile(file: file), submissionDecision: submissionDecision)
    }
    
    func transformToState(eventSink: EventSink, onComplete: (nextState: SubmissionState) throws -> Void) throws {
        let formSubmission = try NettskjemaFormSubmissionFactory().newSubmission(
            NettskjemaFilledInForm(json: submissionFile.contents))
        formSubmission.post { (status) in
            if (status.statusCode == FormSubmissionStatusCode.POST_SUCCESSFUL) {
                try! self.submissionFile.markAs(SubmissionFileState.SUBMITTED, eventSink: eventSink)
            } else {
                try! self.submissionFile.markAs(SubmissionFileState.SUBMISSION_FAILED, eventSink: eventSink)
            }
            try! onComplete(nextState: self.next)
        }
    }
    
    var next: SubmissionState {
        get {
            return submissionFile.isMarked(SubmissionFileState.SUBMITTED) ?
                DeletedSubmission(submissionFile: submissionFile) :
                EncryptedSubmission(submissionFile: submissionFile, submissionDecision: submissionDecision)
        }
    }
    
    let isEndOfProcessing: Bool = false
    
}

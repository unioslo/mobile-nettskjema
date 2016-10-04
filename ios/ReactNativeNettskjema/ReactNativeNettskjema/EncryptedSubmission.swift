import Foundation

class EncryptedSubmission: SubmissionState {
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
        //TBD
        try submissionFile.markAs(SubmissionFileState.ENCRYPTED, eventSink: eventSink)
        try onComplete(nextState: next)
    }
    
    var next: SubmissionState {
        get {
            return DecryptedSubmission(submissionFile: submissionFile, submissionDecision: submissionDecision)
        }
    }
    
    let isEndOfProcessing: Bool = true

}

import Foundation

class InitialSubmission: SubmissionState {
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
        try submissionFile.markAs(SubmissionFileState.UNENCRYPTED, eventSink: eventSink)
        try onComplete(nextState: next)
    }
    
    let isEndOfProcessing: Bool = false
    let indicatesSemiPermanentStorageOnDevice: Bool = true

    var next: SubmissionState {
        get {
            return submissionDecision.nextSubmissionState(submissionFile)
        }
    }
}

import Foundation

class DeletedSubmission: SubmissionState {
    private let submissionFile: SubmissionFile
    
    init(submissionFile: SubmissionFile) {
        self.submissionFile = submissionFile
    }
    
    convenience init(file: NSURL) {
        self.init(submissionFile: SubmissionFile(file: file))
    }
    
    func transformToState(eventSink: EventSink, onComplete: (nextState: SubmissionState) throws -> Void) throws {
        try submissionFile.deleteStoredFile()
        eventSink.put(SubmissionStateChanged(submissionFileState: SubmissionFileState.DELETED))
        try onComplete(nextState: next)
    }
    
    var next: SubmissionState {
        get {
            return NotASubmission()
        }
    }
    
    let isEndOfProcessing: Bool = true
    let indicatesSemiPermanentStorageOnDevice: Bool = false
    
}
    

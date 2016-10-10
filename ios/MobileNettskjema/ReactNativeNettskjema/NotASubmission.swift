class NotASubmission: SubmissionState {
    func transformToState(eventSink: EventSink, onComplete: (nextState: SubmissionState) throws -> Void) throws {
        
    }
    
    var next: SubmissionState { get { return NotASubmission() } }
    
    let isEndOfProcessing: Bool = true
    let indicatesSemiPermanentStorageOnDevice: Bool = false

}

class Queue {
    func process(submission: SubmissionState, eventSink: EventSink, onFirstProcessingComplete: () -> Void) throws {
        try submission.transformToState(eventSink, onComplete: { nextState in
            if (submission.isEndOfProcessing) {
                onFirstProcessingComplete()
                return
            } else {
                try self.process(nextState, eventSink: eventSink, onFirstProcessingComplete: onFirstProcessingComplete)
            }
        })
    }
}

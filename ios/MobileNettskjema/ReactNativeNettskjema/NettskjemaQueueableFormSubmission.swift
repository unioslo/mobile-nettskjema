import Foundation

class NettskjemaQueueableFormSubmission {
    private let eventSink: EventSink
    private let filledInForm: FilledInForm
    private let storageDirectory: StorageDirectory
    private let queue: Queue
    
    init(eventSink: EventSink, filledInForm: FilledInForm, storageDirectory: StorageDirectory, queue: Queue) {
        self.eventSink = eventSink
        self.filledInForm = filledInForm
        self.storageDirectory = storageDirectory
        self.queue = queue
    }
    
    func submit(onFirstProcessingComplete: () -> Void) throws {
        let jsonFile = try JsonFile(storageFile: storageDirectory.fileNamed(NSUUID().UUIDString))
        try jsonFile.store(filledInForm)
        let submissionFile = SubmissionFile(jsonFile: jsonFile)
        let submission = InitialSubmission(submissionFile: submissionFile, submissionDecision: SubmitIfConnectionIsSatisfactory())
        try queue.process(submission, eventSink: eventSink, onFirstProcessingComplete: onFirstProcessingComplete)
    }
    
}

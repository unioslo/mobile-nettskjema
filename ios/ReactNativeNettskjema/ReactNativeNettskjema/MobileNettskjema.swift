import Foundation

class MobileNettskjema {
    
    private let storageDirectory: StorageDirectory
    private let queue = Queue()
    private let eventSink: EventSink
    
    init(storageDirectory: StorageDirectory, eventSink: EventSink) {
        self.storageDirectory = storageDirectory
        self.eventSink = eventSink
    }
    
    func addToSubmissionQueue(filledInForm: FilledInForm, onFirstProcessingComplete: () -> Void) throws {
        try NettskjemaQueueableFormSubmission(
            eventSink: eventSink,
            filledInForm: filledInForm,
            storageDirectory: storageDirectory,
            queue: queue
        ).submit(onFirstProcessingComplete);
    }
    
    func forceRetryAllSubmissions(onFirstProcessingComplete: () -> Void) throws {
        for url in try storageDirectory.storedFiles() {
            if (url.isTemporary()) {
                try NSFileManager.defaultManager().removeItemAtURL(url)
            }
        }
        for url in try storageDirectory.storedFiles() {
            try queue.process(SubmissionStateFromFile(file: url).withDecision(AlwaysSubmit()).next, eventSink: eventSink, onFirstProcessingComplete: onFirstProcessingComplete)
        }
    }
    
    func setAutoSubmissionsPreference(value: AutoSubmissionSetting) {
        NSUserDefaults.standardUserDefaults().setObject(value.rawValue, forKey: autoSubmissionSettingKey)
    }
    
}

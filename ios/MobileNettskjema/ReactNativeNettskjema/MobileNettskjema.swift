import Foundation

@objc public class MobileNettskjema: NSObject {
    
    private let storageDirectory: StorageDirectory
    private let queue = Queue()
    private let eventSink: EventSink
    
    public init(storageDirectory: StorageDirectory, eventSink: EventSink) {
        self.storageDirectory = storageDirectory
        self.eventSink = eventSink
    }
    
    public func addToSubmissionQueue(filledInForm: FilledInForm, onFirstProcessingComplete: () -> Void) throws {
        try NettskjemaQueueableFormSubmission(
            eventSink: eventSink,
            filledInForm: filledInForm,
            storageDirectory: storageDirectory,
            queue: queue
        ).submit(onFirstProcessingComplete);
    }
    
    @objc public func addToSubmissionQueue(submission: [String: AnyObject], onFirstProcessingComplete: () -> Void) throws {
        try addToSubmissionQueue(RNFilledInForm(submission: submission).bridged, onFirstProcessingComplete: onFirstProcessingComplete)
    }

    
    @objc public func forceRetryAllSubmissions(onFirstProcessingComplete: () -> Void) throws {
        for url in try storageDirectory.storedFiles() {
            if (url.isTemporary()) {
                try NSFileManager.defaultManager().removeItemAtURL(url)
            }
        }
        for url in try storageDirectory.storedFiles() {
            try queue.process(SubmissionStateFromFile(file: url).withDecision(AlwaysSubmit()).next, eventSink: eventSink, onFirstProcessingComplete: onFirstProcessingComplete)
        }
    }
    
    public func setAutoSubmissionsPreference(value: AutoSubmissionSetting) {
        NSUserDefaults.standardUserDefaults().setObject(value.rawValue, forKey: autoSubmissionSettingKey)
    }
    
    @objc public func setAutoSubmissionsPreference(value: String) {
        let setting = AutoSubmissionSetting(rawValue: value)!
        return self.setAutoSubmissionsPreference(setting)
    }
    
    public func submissionStates() throws -> [SubmissionState] {
        return try storageDirectory.storedFiles()
            .map { url in return SubmissionStateFromFile(file: url).withDecision(NeverSubmit()) }
            .filter { submissionState in return submissionState.indicatesSemiPermanentStorageOnDevice }
    }
    
    @objc public func submissionStateStrings() throws -> NSArray {
        var result: [String] = []
        for submissionState in try self.submissionStates() {
            result.append(String(submissionState.dynamicType))
        }
        return result
    }
    
}

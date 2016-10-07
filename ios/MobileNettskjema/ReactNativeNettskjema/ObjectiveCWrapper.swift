import Foundation

@objc
public class MobileNettskjemaObjC: NSObject {
    
    private let storageDirectory: StorageDirectory
    private let queue = Queue()
    private let eventSink: EventSink
    
    @objc public init(storageDirectory: StorageDirectory, eventSink: EventSink) {
        self.storageDirectory = storageDirectory
        self.eventSink = eventSink
    }
    
    @objc public func addToSubmissionQueue(filledInForm: [String: AnyObject], onFirstProcessingComplete: () -> Void) throws {
        /*    try NettskjemaQueueableFormSubmission(
         eventSink: eventSink,
         filledInForm: filledInForm,
         storageDirectory: storageDirectory,
         queue: queue
         ).submit(onFirstProcessingComplete);*/
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
    
    @objc public func setAutoSubmissionsPreference(value: String) {
        let setting = AutoSubmissionSetting(rawValue: value)!
        NSUserDefaults.standardUserDefaults().setObject(setting.rawValue, forKey: autoSubmissionSettingKey)
    }
    
}

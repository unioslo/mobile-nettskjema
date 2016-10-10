import Foundation

@objc
public class MobileNettskjemaObjC: NSObject {
    
    private let mobileNettskjema: MobileNettskjema
    
    @objc public init(storageDirectory: StorageDirectory, eventSink: EventSink) {
        self.mobileNettskjema = MobileNettskjema(storageDirectory: storageDirectory, eventSink: eventSink)
    }
    
    @objc public func addToSubmissionQueue(filledInForm: [String: AnyObject], onFirstProcessingComplete: () -> Void) throws {
        try mobileNettskjema.addToSubmissionQueue(filledInForm, onFirstProcessingComplete: onFirstProcessingComplete)
    }
    
    @objc public func forceRetryAllSubmissions(onFirstProcessingComplete: () -> Void) throws {
        try mobileNettskjema.forceRetryAllSubmissions(onFirstProcessingComplete)
    }
    
    @objc public func setAutoSubmissionsPreference(value: String) {
        let setting = AutoSubmissionSetting(rawValue: value)!
        return mobileNettskjema.setAutoSubmissionsPreference(setting)
    }
    
    @objc public func submissionStates() throws -> NSArray {
        var result: [String] = []
        for submissionState in try mobileNettskjema.submissionStates() {
            result.append(String(submissionState.dynamicType))
        }
        return result
    }
    
}

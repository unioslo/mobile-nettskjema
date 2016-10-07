import Foundation
private let submissionStateChangedKey = "no.uio.mobileapps.mobilenettskjema.submissionStateChanged"

class SubmissionStateChanged: Event {
    private let description: String
    
    init(submissionFileState: SubmissionFileState) {
        self.description = submissionFileState.rawValue
    }
    
    @objc var key: String {
        get {
            return submissionStateChangedKey + "." + description
        }
    }
}

class IOSNotificationCenterEventSink: EventSink {
    @objc func put(event: Event) {
        NSNotificationCenter.defaultCenter().postNotificationName(event.key, object: self)
    }
}

    

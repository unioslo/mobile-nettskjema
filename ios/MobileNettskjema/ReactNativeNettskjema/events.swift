import Foundation
private let submissionStateChangedKey = "no.uio.mobileapps.mobilenettskjema.submissionStateChanged"

class SubmissionStateChanged: Event {
    private let description: String
    
    init(submissionFileState: SubmissionFileState) {
        self.description = submissionFileState.rawValue
    }
    
    var key: String {
        get {
            return submissionStateChangedKey + "." + description
        }
    }
}

class IOSNotificationCenterEventSink: EventSink {
    func put(event: Event) {
        NSNotificationCenter.defaultCenter().postNotificationName(event.key, object: self)
    }
}

    

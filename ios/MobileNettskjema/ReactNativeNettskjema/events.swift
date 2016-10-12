import Foundation
private let submissionStateChangedKey = "no.uio.mobileapps.mobilenettskjema.submissionStateChanged"

class SubmissionStateChanged: Event {
    private let description: String
    
    init(submissionFileState: SubmissionFileState) {
        self.description = submissionFileState.rawValue
    }
    
    @objc var name: String {
        get {
            return "submissionStateChanged"
        }
    }
    
    @objc var data: AnyObject {
        get {
            return description
        }
    }
}

class IOSNotificationCenterEventSink: EventSink {
    @objc func put(event: Event) {
        NSNotificationCenter.defaultCenter().postNotificationName(event.name + "." + (event.data as! String), object: self)
    }
}

    

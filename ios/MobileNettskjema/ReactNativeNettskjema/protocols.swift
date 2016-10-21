import Alamofire
import Foundation

protocol CsrfRequestFactory {
    func newRequest(onComplete: (Response<String, NSError> -> Void)) -> Request
}

protocol CsrfTokenFactory {
    func newCsrfToken(rawToken: String) -> MultipartRequestField
}

@objc public protocol Event {
    var name: String { get }
    var data: AnyObject { get }
}

@objc public protocol EventSink {
    func put(event: Event)
}

protocol FieldIdentifier {
    var name: String { get }
}

public protocol FilledInForm: PostRequest, JSONSerializable {
    func postRequest(csrfToken: MultipartRequestField, onComplete: (Manager.MultipartFormDataEncodingResult -> Void))
}

public protocol FilledInFormField: MultipartRequestField, JSONSerializable {
    
}

protocol Form: JSONSerializable {
    var postUrl: String { get }
}

protocol FormField {
    var identifier: FieldIdentifier { get }
}

public protocol FormSubmission {
    func post(onComplete: (status: FormSubmissionStatus) -> Void)
}

public protocol FormSubmissionStatus {
    var statusCode: FormSubmissionStatusCode { get }
    var description: String { get }
}

public enum FormSubmissionStatusCode {
    case NOT_POSTED
    case POST_SUCCESSFUL
    case POST_FAILED
}

public protocol MultipartRequestField {
    func addTo(media: KeyValueMedia)
}

protocol MultipleOptionsFormField: FormField {
    var optionIds: [Int] { get }
}

public protocol JSONSerializable {
    var serialized: [String: AnyObject] { get }
}

public protocol KeyValueMedia {
    func write(key: String, value: NSData)
    func writeFile(key: String, value: NSData, filename: String, mimeType: String)
}

public protocol PostRequest {
    // TBD
}

protocol RNApiBridge {
    associatedtype T
    var bridged: T { get }
}

@objc public protocol StorageDirectory {
    func fileNamed(filename: String) throws -> NSURL
    func storedFiles() throws -> [NSURL]
}


protocol SubmissionDecision {
    func nextSubmissionState(submissionFile: SubmissionFile) -> SubmissionState
}

enum SubmissionFileState: String {
    case DECRYPTED
    case DELETED
    case UNENCRYPTED
    case SUBMITTED
    case ENCRYPTED
    case SUBMISSION_FAILED
    
    var fileExtension: String {
        get {
            return self.rawValue
        }
    }
}

public protocol SubmissionState {
    func transformToState(eventSink: EventSink, onComplete: (nextState: SubmissionState) throws -> Void) throws
    var next: SubmissionState { get }
    var isEndOfProcessing: Bool { get }
    var indicatesSemiPermanentStorageOnDevice: Bool { get }
}

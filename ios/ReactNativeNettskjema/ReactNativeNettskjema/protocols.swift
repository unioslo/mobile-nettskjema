import Alamofire
import Foundation

protocol CsrfRequestFactory {
    func newRequest(onComplete: (Response<String, NSError> -> Void)) -> Request
}

protocol CsrfTokenFactory {
    func newCsrfToken(rawToken: String) -> MultipartRequestField
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
    func addTo(multipartFormData: MultipartFormData)
}

protocol MultipleOptionsFormField: FormField {
    var optionIds: [Int] { get }
}

public protocol JSONSerializable {
    // TBD
}

public protocol PostRequest {
    // TBD
}

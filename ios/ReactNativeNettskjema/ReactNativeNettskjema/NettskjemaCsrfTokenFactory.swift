import Alamofire

private class CsrfRequestField: MultipartRequestField {
    private let rawToken: String
    
    init(rawToken: String) {
        self.rawToken = rawToken
    }
    
    func addTo(multipartFormData: MultipartFormData) {
        multipartFormData.appendBodyPart(data: rawToken.dataUsingEncoding(NSASCIIStringEncoding)!, name: "NETTSKJEMA_CSRF_PREVENTION")
    }
}

class NettskjemaCsrfTokenFactory: CsrfTokenFactory {
    func newCsrfToken(rawToken: String) -> MultipartRequestField {
        return CsrfRequestField(rawToken: rawToken)
    }
}

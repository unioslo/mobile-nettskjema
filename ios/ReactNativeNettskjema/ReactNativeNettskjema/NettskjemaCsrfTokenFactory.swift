import Alamofire

private class CsrfRequestField: MultipartRequestField {
    private let rawToken: String
    
    init(rawToken: String) {
        self.rawToken = rawToken
    }
    
    func addTo(multipartFormData: KeyValueMedia) {
        multipartFormData.write("NETTSKJEMA_CSRF_PREVENTION", value: rawToken.dataUsingEncoding(NSASCIIStringEncoding)!)
    }
}

class NettskjemaCsrfTokenFactory: CsrfTokenFactory {
    func newCsrfToken(rawToken: String) -> MultipartRequestField {
        return CsrfRequestField(rawToken: rawToken)
    }
}

import Alamofire

class NettskjemaCsrfRequestFactory: CsrfRequestFactory {
    func newRequest(onComplete: (Response<String, NSError> -> Void)) -> Request {
        return Alamofire.request(.GET, "https://nettskjema.uio.no/ping.html").validate().responseString(completionHandler: onComplete)
    }
}

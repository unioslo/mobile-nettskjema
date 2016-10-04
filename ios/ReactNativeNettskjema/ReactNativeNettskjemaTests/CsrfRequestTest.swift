import XCTest
@testable import ReactNativeNettskjema

class CsrfRequestTest: XCTestCase {
    private let csrfRequestFactory = NettskjemaCsrfRequestFactory()
    private let sampleToken = "vg8v81vl1r8s22uj4e0mkq5kp6gk65h3u1n4u84i6v3uc0rf3ghehc2s41hki4p65o71ovtm1r9tik5unn0iid5nelur6oog275b7h1n"
    
    func testCsrfRequestToNettskjemaPing() {
        let expectation = self.expectationWithDescription("Nettskjema should return a CSRF token")
        csrfRequestFactory.newRequest({ response in
            switch response.result {
            case .Success(let data):
                print("Request succeeded with data: \(data)")
                XCTAssert(data.lengthOfBytesUsingEncoding(NSASCIIStringEncoding) == self.sampleToken.lengthOfBytesUsingEncoding(NSASCIIStringEncoding))
                expectation.fulfill()
            case .Failure(let error):
                print("CSRF request failed with error: \(error)")
            }
        })
        waitForExpectationsWithTimeout(5.0, handler: nil)
    }
}

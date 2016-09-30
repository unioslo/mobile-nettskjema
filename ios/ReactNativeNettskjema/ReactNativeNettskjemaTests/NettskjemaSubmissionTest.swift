import XCTest
@testable import ReactNativeNettskjema

class NettskjemaSubmissionTest: XCTestCase {
    
    private let submissionFactory = NettskjemaFormSubmissionFactory();
    
    func testCompleteSubmissionWithFilesIsSuccessful() {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        let testFile1 = TestFile(kbSize: 4)
        let testFile2 = TestFile(kbSize: 8)
        let formSubmission = submissionFactory.newSubmission(
            TestForm.formId,
            filledInFormFields: [
                TextQuestionAnswer(question: TestForm.textQuestion, answer: "iOS test run on " + NSDate().description),
                SelectedMultiOption(question: TestForm.radioQuestion, index: 0),
                SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 0),
                SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 1),
                FileUploadForField(uploadQuestion: TestForm.uploadField1, file: testFile1.randomContent),
                FileUploadForField(uploadQuestion: TestForm.uploadField2, file: testFile2.randomContent),
            ]
        )
        formSubmission.post { formSubmissionStatus in
            expectation.fulfill()
            XCTAssert(formSubmissionStatus.statusCode == FormSubmissionStatusCode.POST_SUCCESSFUL)
        }
        waitForExpectationsWithTimeout(5.0, handler:nil)
    }
    
}

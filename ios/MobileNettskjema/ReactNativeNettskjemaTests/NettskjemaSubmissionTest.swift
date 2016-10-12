import XCTest
@testable import MobileNettskjema

class NettskjemaSubmissionTest: XCTestCase {
    
    private let submissionFactory = NettskjemaFormSubmissionFactory();
    
    func testCompleteSubmissionWithFilesIsSuccessful() {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        let testFile1 = try! TestFile(kbSize: 4)
        let testFile2 = try! TestFile(kbSize: 8)
        let formSubmission = submissionFactory.newSubmission(
            TestForm.formId,
            filledInFormFields: [
                TextQuestionAnswer(question: TestForm.textQuestion, answer: "iOS test run on " + NSDate().description),
                SelectedMultiOption(question: TestForm.radioQuestion, index: 0),
                SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 0),
                SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 1),
                FileUploadForField(uploadQuestion: TestForm.uploadField1, file: testFile1.randomContent, mimeType: "text/txt"),
                FileUploadForField(uploadQuestion: TestForm.uploadField2, file: testFile2.randomContent, mimeType: "text/txt"),
            ]
        )
        formSubmission.post { formSubmissionStatus in
            expectation.fulfill()
            XCTAssert(formSubmissionStatus.statusCode == FormSubmissionStatusCode.POST_SUCCESSFUL)
        }
        waitForExpectationsWithTimeout(5.0, handler:nil)
    }
    
    func testSubmitWithDictionary() {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        let testFile1 = try! TestFile(kbSize: 4)
        let testFile2 = try! TestFile(kbSize: 8)
        let filledInForm = try! RNFilledInForm(submission: [
            "form": [ "id": TestForm.formId ],
            "fields": [
                [
                    "type": "text",
                    "questionId": TestForm.textQuestionId,
                    "answer": "iOS queueing test run on " + NSDate().description + " with serialized input"
                ],
                [
                    "type": "radio",
                    "questionId": TestForm.radioQuestionId,
                    "selectedOptionId": TestForm.radioQuestionSelectedId
                ],
                [
                    "type": "multipleChoice",
                    "questionId": TestForm.multipleChoiceQuestionId,
                    "selectedOptionId": TestForm.multipleChoiceQuestionSelectedOptionIds[0]
                ],
                [
                    "type": "multipleChoice",
                    "questionId": TestForm.multipleChoiceQuestionId,
                    "selectedOptionId": TestForm.multipleChoiceQuestionSelectedOptionIds[1]
                ],
                [
                    "type": "upload",
                    "questionId": TestForm.uploadField1Id,
                    "mediaType": "text/txt",
                    "filepath": testFile1.randomContent.path!,
                ],
                [
                    "type": "upload",
                    "questionId": TestForm.uploadField2Id,
                    "mediaType": "text/txt",
                    "filepath": testFile2.randomContent.path!,
                ]
                ] as [[String: AnyObject]],
            ]
        ).bridged
        let submission = submissionFactory.newSubmission(filledInForm)
        submission.post { formSubmissionStatus in
            expectation.fulfill()
            XCTAssert(formSubmissionStatus.statusCode == FormSubmissionStatusCode.POST_SUCCESSFUL)
        }
        self.waitForExpectationsWithTimeout(5, handler: nil)
        testFile1.delete()
        testFile2.delete()
    }
    
}

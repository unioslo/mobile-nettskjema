import XCTest
@testable import MobileNettskjema

private class TestEventSink: EventSink {
    var emittedEvents: [String] = []

    @objc func put(event: Event) {
        emittedEvents.append(event.name + "." + (event.data as! String))
    }
    
    func clear() {
        emittedEvents = []
    }
}

private func submissionStateChangeEvent(key: String) -> String {
    return "submissionStateChanged." + key
}

class MobileNettskjemaTest: XCTestCase {
    
    private var mobileNettskjema: MobileNettskjema?
    private let eventSink = TestEventSink()
    private let storageDirectory: LibraryCacheStorageDirectory = LibraryCacheStorageDirectory()
    private let fileManager = NSFileManager.defaultManager()
    
    override func setUp() {
        super.setUp()
        self.mobileNettskjema = MobileNettskjema(
            storageDirectory: storageDirectory,
            eventSink: eventSink
        )
        clearStorageDirectory()
        enableUploads()
    }
    
    override func tearDown() {
        super.tearDown()
        eventSink.clear()
    }
    
    private func enableUploads() {
        mobileNettskjema!.setAutoSubmissionsPreference(AutoSubmissionSetting.ALWAYS)
    }
    
    private func clearStorageDirectory() {
        for url in try! storageDirectory.storedFiles() {
            try! fileManager.removeItemAtURL(url)
        }
    }
        
    func testCompleteSubmissionWithFilesSubmitsAndIsDeleted() {
        try! self.submitValidSubmissionWithFiles()
        XCTAssertEqual(eventSink.emittedEvents, [
            submissionStateChangeEvent("UNENCRYPTED"),
            submissionStateChangeEvent("SUBMITTED"),
            submissionStateChangeEvent("DELETED"),
            ]
        )
        XCTAssertEqual(try! storageDirectory.storedFiles().count, 0)
    }
    
    func testIncompleteSubmissionFailsAndEncrypts() {
        try! self.submitInvalidSubmission()
        XCTAssertEqual(eventSink.emittedEvents, [
            submissionStateChangeEvent("UNENCRYPTED"),
            submissionStateChangeEvent("SUBMISSION_FAILED"),
            submissionStateChangeEvent("ENCRYPTED"),
            ]
        )
        XCTAssertEqual(try! storageDirectory.storedFiles().count, 1)
    }
    
    func testRetryFailedUploadsTriesToSubmitAgain() {
        try! self.submitInvalidSubmission()
        try! self.forceRetryExpectingOneUpload()
        XCTAssertEqual(eventSink.emittedEvents, [
            submissionStateChangeEvent("UNENCRYPTED"),
            submissionStateChangeEvent("SUBMISSION_FAILED"),
            submissionStateChangeEvent("ENCRYPTED"),
            submissionStateChangeEvent("DECRYPTED"),
            submissionStateChangeEvent("SUBMISSION_FAILED"),
            submissionStateChangeEvent("ENCRYPTED")
            ]
        )
    }
    
    func testDisablingUploadsCausesNewSubmissionToJustEncrypt() {
        mobileNettskjema!.setAutoSubmissionsPreference(AutoSubmissionSetting.NEVER)
        try! self.submitValidSubmission()
        XCTAssertEqual(eventSink.emittedEvents, [
            submissionStateChangeEvent("UNENCRYPTED"),
            submissionStateChangeEvent("ENCRYPTED"),
            ]
        )
    }
    
    func testForceRetryTriesToSubmitEvenWhenUploadsAreDisabled() {
        mobileNettskjema!.setAutoSubmissionsPreference(AutoSubmissionSetting.NEVER)
        try! self.submitValidSubmission()
        try! self.forceRetryExpectingOneUpload()
        XCTAssertEqual(eventSink.emittedEvents, [
            submissionStateChangeEvent("UNENCRYPTED"),
            submissionStateChangeEvent("ENCRYPTED"),
            submissionStateChangeEvent("DECRYPTED"),
            submissionStateChangeEvent("SUBMITTED"),
            submissionStateChangeEvent("DELETED")
            ]
        )
    }
    
    func testRetryIgnoresIrrelevantFiles() {
        let irrelevantFiles = [try! storageDirectory.fileNamed("test.tull"), try! storageDirectory.fileNamed("irrelevant.junk")]
        for url in irrelevantFiles {
            fileManager.createFileAtPath(url.path!, contents: "test".dataUsingEncoding(DEFAULT_ENCODING), attributes: nil)
        }
        try! mobileNettskjema!.forceRetryAllSubmissions() {}
        XCTAssertEqual(eventSink.emittedEvents.count, 0)
        for url in irrelevantFiles {
            XCTAssertTrue(fileManager.fileExistsAtPath(url.path!))
        }
    }
    
    func testRetryDeletesTemporaryFiles() {
        let tempFiles = [try! storageDirectory.fileNamed("submission1.queueTemp"), try! storageDirectory.fileNamed("submission2.queueTemp")]
        for url in tempFiles {
            fileManager.createFileAtPath(url.path!, contents: "test".dataUsingEncoding(DEFAULT_ENCODING), attributes: nil)
        }
        try! mobileNettskjema!.forceRetryAllSubmissions() {}
        for url in tempFiles {
            XCTAssertFalse(fileManager.fileExistsAtPath(url.path!))
        }
    }

    private func forceRetryExpectingOneUpload() throws {
        let expectation = self.expectationWithDescription("An attempt should be made to re-post to nettskjema")
        try mobileNettskjema!.forceRetryAllSubmissions() { expectation.fulfill() }
        self.waitForExpectationsWithTimeout(5, handler: nil)
    }

    private func submitValidSubmissionWithFiles() throws {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        let testFile1 = try! TestFile(kbSize: 4)
        let testFile2 = try! TestFile(kbSize: 8)
        try mobileNettskjema!.addToSubmissionQueue(
            NettskjemaFilledInForm(
                form: TestForm.form,
                filledInFormFields: [
                    TextQuestionAnswer(question: TestForm.textQuestion, answer: "iOS queueing test run on " + NSDate().description),
                    SelectedMultiOption(question: TestForm.radioQuestion, index: 0),
                    SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 0),
                    SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 1),
                    FileUploadForField(uploadQuestion: TestForm.uploadField1, file: testFile1.randomContent, mimeType: "text/txt"),
                    FileUploadForField(uploadQuestion: TestForm.uploadField2, file: testFile2.randomContent, mimeType: "text/txt"),
                ])
            , onFirstProcessingComplete: { expectation.fulfill() }
        )
        self.waitForExpectationsWithTimeout(5, handler: nil)
        testFile1.delete()
        testFile2.delete()
    }
    
    private func submitValidSubmission() throws {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        try mobileNettskjema!.addToSubmissionQueue(
            NettskjemaFilledInForm(
                form: TestForm.form,
                filledInFormFields: [
                    TextQuestionAnswer(question: TestForm.textQuestion, answer: "iOS queueing test run on " + NSDate().description),
                    SelectedMultiOption(question: TestForm.radioQuestion, index: 0),
                    SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 0),
                    SelectedMultiOption(question: TestForm.multipleChoiceQuestion, index: 1),
                ])
            , onFirstProcessingComplete: { expectation.fulfill() }
        )
        self.waitForExpectationsWithTimeout(5, handler: nil)
    }
    
    private func submitInvalidSubmission() throws {
        let expectation = self.expectationWithDescription("Submission should be posted to nettskjema")
        try mobileNettskjema!.addToSubmissionQueue(
            NettskjemaFilledInForm(
                form: TestForm.form,
                filledInFormFields: [
                    SelectedMultiOption(question: TestForm.radioQuestion, index: 0)
                ])
            , onFirstProcessingComplete: { expectation.fulfill() }
        )
        self.waitForExpectationsWithTimeout(5, handler: nil)
    }
    
}

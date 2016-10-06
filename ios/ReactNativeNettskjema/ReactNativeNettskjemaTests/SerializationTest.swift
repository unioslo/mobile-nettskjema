import XCTest
@testable import ReactNativeNettskjema

private class MockMultipartFormData: KeyValueMedia {
    let media = NSMutableDictionary()
    func write(key: String, value: NSData) {
        media[key] = value
    }
    
    func writeFile(key: String, value: NSData, filename: String, mimeType: String) {
        media[key] = value
        media["filename"] = filename
        media["mimeType"] = mimeType
    }
}

class SerializationTest: XCTestCase {
    func testTextQuestionAnswer() {
        XCTAssert(deserializedEqualsOriginal(
            TextQuestionAnswer(
                question: TextQuestion(id:123),
                answer: "test")))
    }
    
    func testSelectedMultiOption() {
        XCTAssert(deserializedEqualsOriginal(
            SelectedMultiOption(
                question: RadioQuestion(id: 123, optionIds: [234, 345]),
                index: 0)))
    }
    
    func testFileUploadForField() {
        let file = try! LibraryCacheStorageDirectory().fileNamed("uploaded.txt")
        NSFileManager.defaultManager()
            .createFileAtPath(file.path!,
                              contents: "uploaded file contents".dataUsingEncoding(DEFAULT_ENCODING),
                              attributes: nil)
        XCTAssert(deserializedEqualsOriginal(
            FileUploadForField(uploadQuestion: UploadQuestion(id: 123), file: file, mimeType: "text/txt")))
        
    }
    
    func testFilledInForm() {
        let form = NettskjemaFilledInForm(
            form: NettskjemaForm(id: 123),
            filledInFormFields: [
                TextQuestionAnswer(question: TextQuestion(id: 123), answer: "test"),
                SelectedMultiOption(question: RadioQuestion(id: 123, optionIds: [234, 345]), index: 0)
            ]
        )
        let slightlyDifferentForm = NettskjemaFilledInForm(
            form: NettskjemaForm(id: 123),
            filledInFormFields: [
                TextQuestionAnswer(question: TextQuestion(id: 123), answer: "test"),
                SelectedMultiOption(question: RadioQuestion(id: 123, optionIds: [234, 345]), index: 1)
            ]
        )
        let jsonSerializedForm: String = try! String(
            data: NSJSONSerialization.dataWithJSONObject(form.serialized,
                options: NSJSONWritingOptions.PrettyPrinted),
            encoding: DEFAULT_ENCODING)!
        let deserializedForm = try! NettskjemaFilledInForm(json: jsonSerializedForm)
        XCTAssert(equalFilledInForms(deserializedForm, and: form))
        XCTAssert(!equalFilledInForms(deserializedForm, and: slightlyDifferentForm))
    }
    
    private func deserializedEqualsOriginal(field: FilledInFormField) -> Bool {
        let dynamicField = NettskjemaJsonField(field: field.serialized)
        return try! equalFields(field, and: dynamicField.deserialized())
    }
    
    private func equalFields(item: MultipartRequestField, and: MultipartRequestField) -> Bool {
        let firstData = MockMultipartFormData()
        let secondData = MockMultipartFormData()
        item.addTo(firstData)
        and.addTo(secondData)
        return firstData.media == secondData.media
    }
    
    private func equalFilledInForms(item: FilledInForm, and: FilledInForm) -> Bool {
        let firstSerialized = item.serialized
        let secondSerialized = and.serialized
        
        let firstForm = firstSerialized["form"] as! [String: Int]
        let secondForm = secondSerialized["form"] as! [String: Int]
        
        let firstFields = firstSerialized["fields"] as! NSArray
        let secondFields = secondSerialized["fields"] as! NSArray
        
        return equalForms(firstForm, and: secondForm)
            && firstFields.isEqualToArray(secondFields as [AnyObject])
        
    }
    
    private func equalForms(item: [String: Int], and: [String: Int]) -> Bool {
        return item["form"] == and["form"]
    }
    
}

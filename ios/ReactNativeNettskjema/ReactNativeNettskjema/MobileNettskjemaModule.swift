import Foundation

class JSEventSink: EventSink {
    func put(event: Event) {
    
    }
}

class MobileNettskjemaModule: NSObject {
    private let mobileNettskjema: MobileNettskjema
    
    override init() {
        mobileNettskjema = MobileNettskjema(storageDirectory: LibraryCacheStorageDirectory(), eventSink: JSEventSink())
        super.init()
    }
    
    @objc func addToSubmissionQueue(submission: NSDictionary, resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        do {
            try mobileNettskjema.addToSubmissionQueue(submission) { }
            resolve(nil)
        } catch {
            reject(error)
        }
    }
    
    @objc func forceRetryAllSubmissions(resolver resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        do {
            try mobileNettskjema.forceRetryAllSubmissions() {}
        } catch {
            reject(error)
        }
    }
    
    @objc func setAutoSubmissionsPreference(value: String, resolve: RCTPromiseResolveBlock, rejecter reject: RCTPromiseRejectBlock) -> Void {
        mobileNettskjema.setAutoSubmissionsPreference(AutoSubmissionSetting(rawValue: value)!)
        resolve(nil)
    }


}

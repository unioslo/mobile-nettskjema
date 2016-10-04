import Reachability
import Foundation

class SubmitIfConnectionIsSatisfactory: SubmissionDecision {
    func nextSubmissionState(submissionFile: SubmissionFile) -> SubmissionState {
        return uploadConditionIsMet ?
            SubmittedSubmission(submissionFile: submissionFile, submissionDecision: self) :
            EncryptedSubmission(submissionFile: submissionFile, submissionDecision: self)
    }
    
    private var uploadConditionIsMet: Bool {
        get {
            let setting = AutoSubmissionSetting(rawValue: NSUserDefaults.standardUserDefaults().objectForKey(autoSubmissionSettingKey) as! String)
            var onWifi = false
            do {
                let reachability = try Reachability.reachabilityForInternetConnection()
                onWifi = reachability.currentReachabilityStatus == .ReachableViaWiFi
            } catch {
                // TBD: throw or log?
            }
            return setting == AutoSubmissionSetting.ALWAYS ||
                (setting == AutoSubmissionSetting.ONLY_WITH_WIFI && onWifi)
        }
    }
}

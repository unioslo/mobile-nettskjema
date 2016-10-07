import Foundation

extension SubmissionFileState {
    func matches(file: NSURL) -> Bool {
        return file.pathExtension! == self.fileExtension
    }
}

class SubmissionStateFromFile {
    private let file: NSURL
    
    init(file: NSURL) {
        self.file = file
    }
    
    func withDecision(submissionDecision: SubmissionDecision) -> SubmissionState {
        if (SubmissionFileState.DECRYPTED.matches(file)) { return DecryptedSubmission(file: file, submissionDecision: submissionDecision) }
        else if (SubmissionFileState.ENCRYPTED.matches(file)) { return EncryptedSubmission(file: file, submissionDecision: submissionDecision) }
        else if (SubmissionFileState.SUBMISSION_FAILED.matches(file)) { return SubmittedSubmission(file: file, submissionDecision: submissionDecision) }
        else if (SubmissionFileState.SUBMITTED.matches(file)) { return SubmittedSubmission(file: file, submissionDecision: submissionDecision) }
        else if (SubmissionFileState.UNENCRYPTED.matches(file)) { return InitialSubmission(file: file, submissionDecision: submissionDecision) }
        else { return NotASubmission() }
        
    }
}

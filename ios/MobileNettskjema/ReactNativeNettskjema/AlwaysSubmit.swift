class AlwaysSubmit: SubmissionDecision {
    func nextSubmissionState(submissionFile: SubmissionFile) -> SubmissionState {
        return SubmittedSubmission(submissionFile: submissionFile, submissionDecision: self)
    }
}

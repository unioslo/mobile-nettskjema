class NeverSubmit: SubmissionDecision {
    func nextSubmissionState(submissionFile: SubmissionFile) -> SubmissionState {
        return NotASubmission()
    }
}


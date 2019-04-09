import { NativeEventEmitter, NativeModules } from 'react-native'
const { RNNettskjema } = NativeModules
import formSpec from './common/formSpecification'
import getFormInfo from './common/formInfo'
import submissionCreator from './common/submissionCreator'

export async function addToSubmissionQueue(submission) {
  return await RNNettskjema.addToSubmissionQueue(submission)
}

export async function clearTemporaryFiles() {
  return await RNNettskjema.clearTemporaryFiles()
}

export async function forceRetryAllSubmissions() {
  return await RNNettskjema.forceRetryAllSubmissions()
}

export async function setAutoSubmissionsPreference(preference) {
  const validPreferences = ['ALWAYS', 'NEVER', 'ONLY_WITH_WIFI']
  if (validPreferences.includes(preference)) {
    return await RNNettskjema.setAutoSubmissionsPreference(preference)
  } else {
    throw `preference must be one of: ${validPreferences.toString()}`
  }
}

export async function stateOfSubmissions() {
  return await RNNettskjema.stateOfSubmissions()
}

export async function formSpecification(formId) {
  return formSpec(await getFormInfo(formId))
}

export function createSubmission(spec, data) {
  return submissionCreator(spec)(data)
}

export async function addToSubmissionQueueWithMetadata(submission, metadata) {
  return await RNNettskjema.addToSubmissionQueue(submission, metadata)
}

export async function uploadSubmission(submissionId) {
  return await RNNettskjema.retryUploadForFile(submissionId)
}

export const eventEmitter = new NativeEventEmitter(RNNettskjema)

export default {
  addToSubmissionQueue,
  clearTemporaryFiles,
  forceRetryAllSubmissions,
  setAutoSubmissionsPreference,
  stateOfSubmissions,
  formSpecification,
  createSubmission,
  addToSubmissionQueueWithMetadata,
  uploadSubmission,
}

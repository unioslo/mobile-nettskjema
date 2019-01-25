import { NativeEventEmitter, NativeModules } from 'react-native';
const { RNNettskjema } = NativeModules;
import formSpec from './common/formSpecification';
import getFormInfo from './common/formInfo';
import submissionCreator from './common/submissionCreator';

export async function addToSubmissionQueue(submission) {
  return await RNNettskjema.addToSubmissionQueue(submission);
}

export async function addToSubmissionQueueWithMetaData(submission, metaData) {
  return await RNNettskjema.addToSubmissionQueueWithMetaData(
    submission,
    metaData,
  );
}

export async function uploadSubmission(id) {
  /* Todo: Upload a single (encrypted file) */
  return await RNNettskjema.uploadSubmission(id);
}

export async function deleteSubmission(id) {
  /* Todo: Delete a single (encrypted) file */
  return await RNNettskjema.deleteSubmission(id);
}

export async function abortUpload() {
  /* Todo: */
}

export async function getSubmissionsWithMetaData() {
  /* Todo: Get (encrypted) submissions with metadata */
}

export async function clearTemporaryFiles() {
  return await RNNettskjema.clearTemporaryFiles();
}

export async function forceRetryAllSubmissions() {
  return await RNNettskjema.forceRetryAllSubmissions();
}

export async function setAutoSubmissionsPreference(preference) {
  const validPreferences = ['ALWAYS', 'NEVER', 'ONLY_WITH_WIFI'];
  if (validPreferences.includes(preference)) {
    return await RNNettskjema.setAutoSubmissionsPreference(preference);
  } else {
    throw `preference must be one of: ${validPreferences.toString()}`;
  }
}

export async function stateOfSubmissions() {
  return await RNNettskjema.stateOfSubmissions();
}

export async function formSpecification(formId) {
  return formSpec(await getFormInfo(formId));
}

export function createSubmission(spec, data) {
  return submissionCreator(spec)(data);
}

export const eventEmitter = new NativeEventEmitter(RNNettskjema);

export default {
  addToSubmissionQueue,
  addToSubmissionQueueWithMetaData,
  clearTemporaryFiles,
  forceRetryAllSubmissions,
  setAutoSubmissionsPreference,
  stateOfSubmissions,
  formSpecification,
  createSubmission,
  uploadSubmission,
  deleteSubmission,
  abortUpload,
  getSubmissionsWithMetaData,
};

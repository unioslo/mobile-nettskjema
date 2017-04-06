import { NativeModules } from 'react-native'
const { RNNettskjema } = NativeModules

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
  const validPreferences = ["ALWAYS", "NEVER", "ONLY_WITH_WIFI"]
  if (validPreferences.includes(preference)) {
    return await RNNettskjema.setAutoSubmissionsPreference(preference)
  } else {
    throw `preference must be one of: ${validPreferences.toString()}`
  }
}

export async function stateOfSubmissions() {
  return await RNNettskjema.stateOfSubmissions()
}

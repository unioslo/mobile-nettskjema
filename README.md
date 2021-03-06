# Installation with React Native

In the project root folder (not the `ios` or `android` folder), run

* `yarn add git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#3.0.1`
* `react-native link`

## Android specific steps

* Add this inside the `Application` tag in `AndroidManifest.xml`:

```
<!-- Mobile nettskjema -->
<service
    android:name="no.uio.mobileapps.mobilenettskjema.android.deferredsubmission.queueing.QueueService"
    android:enabled="true"
    android:exported="false"
    android:label="Queue Service" />
```

## iOS specific steps

Make sure you have installed [Carthage](https://github.com/Carthage/Carthage).

### Install the MobileNettskjemaIOS support library
* Add the following to the `Cartfile` of your project: `git "ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema-ios.git"`
* Run Carthage (from the ios-folder: `carthage update --platform ios`) and add the installed frameworks to your project as described in the [Carthage documentation](https://github.com/Carthage/Carthage#if-youre-building-for-ios-tvos-or-watchos) (step 3 and onwards)

#### Input og outputfiler
Per carthage dokumentasjon, legg inn dette innholdet i `input.xcfilelist` og `output.xcfilelist`
```
$(SRCROOT)/Carthage/Build/iOS/Alamofire.framework
$(SRCROOT)/Carthage/Build/iOS/CryptoSwift.framework
$(SRCROOT)/Carthage/Build/iOS/KeychainSwift.framework
$(SRCROOT)/Carthage/Build/iOS/MobileNettskjemaIOS.framework
$(SRCROOT)/Carthage/Build/iOS/Reachability.framework
```

```
$(BUILT_PRODUCTS_DIR)/$(FRAMEWORKS_FOLDER_PATH)/Alamofire.framework
$(BUILT_PRODUCTS_DIR)/$(FRAMEWORKS_FOLDER_PATH)/CryptoSwift.framework
$(BUILT_PRODUCTS_DIR)/$(FRAMEWORKS_FOLDER_PATH)/KeychainSwift.framework
$(BUILT_PRODUCTS_DIR)/$(FRAMEWORKS_FOLDER_PATH)/MobileNettskjemaIOS.framework
$(BUILT_PRODUCTS_DIR)/$(FRAMEWORKS_FOLDER_PATH)/Reachability.framework
```

### Embed standard swift libraries
* In Xcode, go to `Build settings` and search for `Always Embed Swift Standard Libraries` and make sure this is set to `Yes`

### Allow connections to Nettskjema
* Add the following inside the `dict` tag of `NSExceptionDomains` in your project's Info.plist file:

```
<key>nettskjema.no</key>
<dict>
  <key>NSExceptionRequiresForwardSecrecy</key>
  <false/>
</dict>
```

## React Native example usage

```
import Nettskjema from 'mobile-nettskjema'


async function deliverTestData() {
  let spec
  let submission

  try {
    spec = await Nettskjema.formSpecification('mytestform')
  } catch(e) {
    console.log(e)
    return
  }
  try {
    submission = Nettskjema.createSubmission(spec, {
      'codebook-value-of-text-field': 'Some text',
      'codebook-value-of-radio-field': 'codebook-value-of-selected-radio',
      'codebook-value-of-multichoice-field': ['codebook-value-of-selected-box-1', 'codebook-value-of-selected-box-2'],
      'codebook-value-of-attachment-field': {
          'filePath': '/path/to/file/on/device',
          'mimeType': 'text/txt',
       }
    })
  } catch(e) {
    // createSubmission will throw if the data object does not correspond to the specification, hopefully with a helpful error message
    console.log(e)
    return
  }

  await Nettskjema.setAutoSubmissionsPreference("ALWAYS") // or "NEVER" or "ONLY_WITH_WIFI"
  await Nettskjema.addToSubmissionQueue(submission)
}

```

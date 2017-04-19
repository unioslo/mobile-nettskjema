# Installation with React Native

In the project root folder (not the `ios` or `android` folder), run

* `yarn add git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#2.2.0`

## iOS specific steps

Make sure you have installed [Carthage](https://github.com/Carthage/Carthage).

### Install the MobileNettskjemaIOS support library
* Add the following to the `Cartfile` of your project: `git "ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema-ios.git"`
* Run Carthage and add the installed frameworks to your project as described in the [Carthage documentation](https://github.com/Carthage/Carthage#if-youre-building-for-ios-tvos-or-watchos)

### Install the React Native module
* `open node_modules/mobile-nettskjema/ios/MobileNettskjema/`. This opens a Finder window.
* Drag `MobileNettskjema.xcodeproj` to the Libraries group in the Xcode project.
* Add `libReactNativeNettskjema.a` to *Linked Frameworks and Libraries* in the Xcode build target settings for your project.

### Allow connections to Nettskjema
* Add the following inside the `dict` tag of `NSExceptionDomains` in your project's Info.plist file:

```
<key>nettskjema.uio.no</key>
<dict>
  <key>NSExceptionRequiresForwardSecrecy</key>
  <false/>
</dict>
```

## React Native example usage

```
import Nettskjema from 'mobile-nettskjema'


async function deliverTestData() {
  try {
    const spec = await Nettskjema.formSpecification('mytestform')
  } catch(e) {
    console.log(e)
    return
  }
  try {
    const submission = Nettskjema.createSubmission(spec, {
      'codebook-value-of-text-field': 'Some text',
      'codebook-value-of-radio-field': 'codebook-value-of-selected-radio',
      'codebook-value-of-multichoice-field': ['codebook-value-of-selected-box-1', 'codebook-value-of-selected-box-2'],
      'codebook-value-of-attachment-field': {
          'filePath': '/path/to/file/on/device',
          'mimeType': 'text/txt',
       }
    }
  } catch(e) {
    // createSubmission will throw if the data object does not correspond to the specification, hopefully with a helpful error message
    console.log(e)
    return
  }

  await Nettskjema.setAutoSubmissionsPreference("ALWAYS") // or "NEVER" or "ONLY_WITH_WIFI"
  await Nettskjema.addToSubmissionQueue(submission)
}

```

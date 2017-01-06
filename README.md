# Installation with React Native

In the project root folder (not the `ios` or `android` folder), run

* `yarn add git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#2.0.0` or
`npm i --save git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#2.0.0`

## iOS specific steps

Make sure you have installed [Carthage](https://github.com/Carthage/Carthage).

### Install the MobileNettskjemaIOS support library
* Add the following to the `Cartfile` of your project: `git "ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema-ios.git"`
* Run Carthage and add the installed frameworks to your project as described in the [Carthage documentation](https://github.com/Carthage/Carthage#if-youre-building-for-ios-tvos-or-watchos)
n

### Install the React Native module
* `open node_modules/mobile-nettskjema/ios/MobileNettskjema/`. This opens a Finder window.
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
import { NativeModules } from 'react-native'
const { RNNettskjema } = NativeModules

async function deliverTestData() {
  const submission = {
    form: { id: 75319 },
    fields: [
      {
        type: "text",
        questionId: 577795,
        answer: 'Hello from React Native',
      },
    ],
  }

  await RNNettskjema.setAutoSubmissionsPreference("ALWAYS") // or "NEVER" or "ONLY_WITH_WIFI"
  await RNNettskjema.addToSubmissionQueue(submission)
}

```

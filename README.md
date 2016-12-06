# Installation with React Native

In the project root folder (not the `ios` or `android` folder), run

* `yarn add git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#1.1.0` or
`npm i --save git+ssh://git@bitbucket.usit.uio.no:7999/mob/mobile-nettskjema.git#1.1.0`

## iOS specific steps

Make sure you have installed [Carthage](https://github.com/Carthage/Carthage).

* `open node_modules/mobile-nettskjema/ios/MobileNettskjema/`. This opens a Finder window.
* Drag the `MobileNettskjema.xcodeproj` file from the Finder window to the *Libraries* group of your Xcode project. Do *not* check "Copy items if needed".
* Add `libReactNativeNettskjema.a` to *Linked Frameworks and Libraries* and `MobileNettskjema.framework` to *Embedded Binaries* in the Xcode build target settings for your project.
* Copy the contents of `Cartfile` in the opened Finder window to the Cartfile of your project. (If you don't already use Carthage, just copy the file to the to the `ios` folder).
* `cd ios; carthage update --platform ios`
* Add the installed frameworks to your project as described in the [Carthage documentation](https://github.com/Carthage/Carthage#if-youre-building-for-ios-tvos-or-watchos)

* Add the following to  *Framework Search Paths* in the Xcode build target settings for your project:
  - `$(PROJECT_DIR)/../node_modules/mobile-nettskjema` (_recursive_)
  -  `$(PROJECT_DIR)/Carthage/Build/iOS` (_non-recursive_)

* Add the following inside the `dict` tag of `NSExceptionDomains` in your project's Info.plist file:

```
<key>nettskjema.uio.no</key>
<dict>
  <key>NSExceptionRequiresForwardSecrecy</key>
  <false/>
</dict>
```

### Troubleshooting

If the app will build, but not archive: Select "MobileNettskjema" as your build target in Xcode and press CMD-B. Then select your app as the build target and archive should (might) work.

## React Native usage

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

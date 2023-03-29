# react-native-app-integrity-checksum

The library supports the VAPT requirement called app integrity. The library can generate a unique checksum based on the files of your react native application source code. It mainly focuses to React Native JS bundle file changes.

## Installation

```sh
npm install react-native-app-integrity-checksum
```

## IOS Guide:

The current implementation of the library (For IOS) is to only detect JS bundle changes and generating a unique hash key when there are changes.

1) Add this line to your project podfile.

your project directory -> ios -> podfile -> under the target

```sh
   pod "AWFileHash"
```
then run,

```sh
   pod install
```

2) Run the below command to generate the bundle file manually. When you are in production, this will create automatically.

```sh
   npx react-native bundle --entry-file='index.js' --bundle-output='./ios/main.jsbundle' --dev=false --platform='ios' --assets-dest='./ios'
```

This will generate the relevant bundle file in your project, ios folder.

3) Open your ios project in XCode (your project directory -> ios -> example.xcworkspace), and go to the **Build** **Phases** and expand the copy bundle resources. click on the + button and add the main.jsbundle as below.

(bundle will be generated usually inside of the ios folder. when adding the file, please tick on _copy if items needed_)

https://images2.imgbox.com/84/1b/1RQiTMWo_o.png

## Android Guide:

The current implementation of the library (For Android) is to detect the JS bundle changes and as well the native source code changes. It also generates a unique hash key when there are changes.

1). First, you have to create an assets folder under the main folder.

your project directory -> android -> app -> src -> main -> assets

https://images2.imgbox.com/52/a3/uA4nRdyK_o.png

If you have this folder already, you are good to go.

2). Run the below command to generate the bundle file manually. When you are in production, this will create automatically.

```sh
   npx react-native bundle --platform android --dev false --entry-file index.js --bundle-output android/app/src/main/assets/index.android.bundle --assets-dest android/app/src/main/res
```
Now you can see the android bundle file inside the previously created assets folder.

## Usage

```js
import { getChecksum } from 'react-native-app-integrity-checksum';

// ...

const result = await getChecksum();
```

## Troubleshoot

* In development mode, when you are doing the source code changes, the previously generated JS bundle will not be changed. You have to generate it manually once you have done the changes. But production mode will automatically generate it when building the application.


* If the library does not return anything, you need to follow the steps previously defined. This happenes when missing the JS bundle file.

* Try with the given example application. If not it works, Please open an Issue.

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

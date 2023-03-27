# react-native-app-integrity-checksum

The library supports the VAPT requirement called app integrity. The library can generate a unique checksum based on the files of your react native application source code. It mainly focuses to React Native JS bundle file changes.

## IOS:

Currently supports only detecting JS bundle changes and generating a unique hash key when there are changes.

## Android:

Detect the JS bundle changes and as well the native source code changes. It also generates a unique hash key when there are changes.

## Installation

```sh
npm install react-native-app-integrity-checksum
```

## Usage

```js
import { multiply } from 'react-native-app-integrity-checksum';

// ...

const result = await multiply(3, 7);
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with [create-react-native-library](https://github.com/callstack/react-native-builder-bob)

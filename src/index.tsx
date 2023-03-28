import { NativeModules, Platform } from 'react-native';

const LINKING_ERROR =
  `The package 'react-native-awesome-library' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

const AppIntegrityChecksum = NativeModules.AppIntegrityChecksum
  ? NativeModules.AppIntegrityChecksum
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function getChecksum(): Promise<string> {
  return new Promise((resolve) => {
    AppIntegrityChecksum.getChecksum((checksum: string) => {
      resolve(checksum);
    });
  });
}

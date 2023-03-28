import * as React from 'react';

import { StyleSheet, View, Text } from 'react-native';
import { getChecksum } from 'react-native-app-integrity-checksum';

export default function App() {
  const [result, setResult] = React.useState<string>('');

  React.useEffect(() => {
    async function setupHash() {
      const res = await getChecksum();
      setResult(res);
    }
    setupHash();
  }, []);

  return (
    <View style={styles.container}>
      <Text>Result: {result}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});

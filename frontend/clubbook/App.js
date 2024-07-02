import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import LogIn from './screens/LogIn';

export default function App() {
  return (
    <View style={styles.container}>
      <View style={{height:35}}></View>
      < LogIn />
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

import { StatusBar } from 'expo-status-bar';
import { StyleSheet, Text, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import LogIn from './screens/LoginScreen';
import AdministratorMainScreen from './screens/mainScreens/AdministratorMainScreen';
import StudentMainScreen from './screens/mainScreens/StudentMainScreen';
import TeacherMainScreen from './screens/mainScreens/TeacherMainScreen';


export default function App() {

  const Stack = createNativeStackNavigator();
  return (
    <View style={styles.container}>
      <View style={{ height: 35 }}></View>
      <NavigationContainer>
        <Stack.Navigator initialRouteName="LogIn">
          <Stack.Screen name="LogIn" component={LogIn} options={{headerShown: false, gestureEnabled: false}}/>
          <Stack.Screen name="AdministratorMainScreen" component={AdministratorMainScreen} options={{headerShown: false, gestureEnabled: false}}/>
          <Stack.Screen name="StudentMainScreen" component={StudentMainScreen} options={{headerShown: false, gestureEnabled: false}}/>
          <Stack.Screen name="TeacherMainScreen" component={TeacherMainScreen} options={{headerShown: false, gestureEnabled: false}}/>
        </Stack.Navigator>
      </NavigationContainer>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
  },
});

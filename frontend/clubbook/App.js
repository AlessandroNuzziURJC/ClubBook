import React, { useState, useEffect } from 'react';
import { StyleSheet, ActivityIndicator, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import LogIn from './screens/LoginScreen';
import AdministratorMainScreen from './screens/mainScreens/AdministratorMainScreen';
import StudentMainScreen from './screens/mainScreens/StudentMainScreen';
import TeacherMainScreen from './screens/mainScreens/TeacherMainScreen';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function App() {
  const [userRole, setUserRole] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const getRole = async () => {
      try {
        const role = await AsyncStorage.getItem('role');
        if (role !== null) {
          switch (role) {
            case 'ADMINISTRATOR':
              setUserRole('AdministratorMainScreen');
              break;
            case 'STUDENT':
              setUserRole('StudentMainScreen');
              break;
            case 'TEACHER':
              setUserRole('TeacherMainScreen');
              break;
            default:
              setUserRole('LogIn');
              break;
          }
        } else {
          setUserRole('LogIn');
        }
      } catch (error) {
        setUserRole('LogIn');
      } finally {
        setIsLoading(false);
      }
    };

    getRole();
  }, []);

  const Stack = createNativeStackNavigator();

  if (isLoading) {
    return (
      <View style={[styles.container, { justifyContent: 'center', alignItems: 'center' }]}>
        <ActivityIndicator size="large" color="#0000ff" />
      </View>
    );
  }

  return (
    <View style={styles.container}>
      <View style={{ height: 35 }}></View>
      <NavigationContainer>
        <Stack.Navigator initialRouteName={userRole}>
          <Stack.Screen name="LogIn" component={LogIn} options={{ headerShown: false, gestureEnabled: false }} />
          <Stack.Screen name="AdministratorMainScreen" component={AdministratorMainScreen} options={{ headerShown: false, gestureEnabled: false }} />
          <Stack.Screen name="StudentMainScreen" component={StudentMainScreen} options={{ headerShown: false, gestureEnabled: false }} />
          <Stack.Screen name="TeacherMainScreen" component={TeacherMainScreen} options={{ headerShown: false, gestureEnabled: false }} />
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


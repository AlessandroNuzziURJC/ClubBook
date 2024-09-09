import React, { useState, useEffect } from 'react';
import * as Notifications from 'expo-notifications';
import * as Device from 'expo-device';
import { StyleSheet, Platform, View } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import LogIn from './screens/LoginScreen';
import AdministratorMainScreen from './screens/mainScreens/AdministratorMainScreen';
import StudentMainScreen from './screens/mainScreens/StudentMainScreen';
import TeacherMainScreen from './screens/mainScreens/TeacherMainScreen';
import AsyncStorage from '@react-native-async-storage/async-storage';

export default function App() {
  const [expoPushToken, setExpoPushToken] = useState('');
  const [notification, setNotification] = useState(false);

  useEffect(() => {
    const initializeNotifications = async () => {
      const token = await registerForPushNotificationsAsync();
      if (token) {
        setExpoPushToken(token);
      }
      await AsyncStorage.setItem('notificationToken', expoPushToken);
    };

    initializeNotifications();

    // Escuchar cuando llega una notificación
    const subscription = Notifications.addNotificationReceivedListener(notification => {
      setNotification(notification);
    });

    return () => subscription.remove();
  }, []);

  const Stack = createNativeStackNavigator();
  return (
    <View style={styles.container}>
      <View style={{ height: 35 }}></View>
      <NavigationContainer>
        <Stack.Navigator initialRouteName="LogIn">
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

// Función para solicitar permisos y obtener el token de Expo
async function registerForPushNotificationsAsync() {
  let token;
  if (Device.isDevice) {
    const { status: existingStatus } = await Permissions.getAsync(Permissions.NOTIFICATIONS);
    let finalStatus = existingStatus;
    if (existingStatus !== 'granted') {
      const { status } = await Permissions.askAsync(Permissions.NOTIFICATIONS);
      finalStatus = status;
    }
    if (finalStatus !== 'granted') {
      alert('¡No se obtuvo permiso para notificaciones!');
      return;
    }
    token = (await Notifications.getExpoPushTokenAsync()).data;
    console.log(token);
  } else {
    alert('Debe usar un dispositivo físico para las notificaciones push');
  }

  if (Platform.OS === 'android') {
    Notifications.setNotificationChannelAsync('default', {
      name: 'default',
      importance: Notifications.AndroidImportance.MAX,
      vibrationPattern: [0, 250, 250, 250],
      lightColor: '#FF231F7C',
    });
  }

  return token;
}

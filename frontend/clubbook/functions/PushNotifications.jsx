import * as Notifications from 'expo-notifications';
import AsyncStorage from '@react-native-async-storage/async-storage';

const PushNotificationConfiguration = {

    pushTokenExists: async () => {
        const storedToken = await AsyncStorage.getItem('expoPushToken');
        if (storedToken) {
            setExpoPushToken(storedToken);
            return true;
        }
        return false;
    },

    getPushToken: async () => {
        if (PushNotificationConfiguration.pushTokenExists()) {
            return await AsyncStorage.getItem('expoPushToken');
        }
        return PushNotificationConfiguration.generatePushtoken();
    },

    setPushToken: async (pushToken) => {
        if (!PushNotificationConfiguration.pushTokenExists()) {
            await AsyncStorage.setItem('expoPushToken', pushToken);
        }
    },

    generatePushtoken: async () => {
        const token = (await Notifications.getExpoPushTokenAsync()).data;
        PushNotificationConfiguration.setPushToken(token);
        return token;
    },

}

export default PushNotificationConfiguration;


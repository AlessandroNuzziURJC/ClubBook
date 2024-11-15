import * as Notifications from 'expo-notifications';
import AsyncStorage from '@react-native-async-storage/async-storage';

/**
 * A configuration module for managing push notifications in an Expo app.
 */
const PushNotificationConfiguration = {

    /**
     * Checks if a push token exists in AsyncStorage.
     *
     * @returns {Promise<boolean>} A promise that resolves to true if a push token exists, otherwise false.
     */
    pushTokenExists: async () => {
        const storedToken = await AsyncStorage.getItem('expoPushToken');
        if (storedToken) {
            setExpoPushToken(storedToken);
            return true;
        }
        return false;
    },

    /**
     * Retrieves the existing push token or generates a new one if it doesn't exist.
     *
     * @returns {Promise<string>} A promise that resolves to the push token.
     */
    getPushToken: async () => {
        if (PushNotificationConfiguration.pushTokenExists()) {
            return await AsyncStorage.getItem('expoPushToken');
        }
        return PushNotificationConfiguration.generatePushtoken();
    },

    /**
     * Sets the push token in AsyncStorage.
     *
     * @param {string} pushToken - The push token to be stored.
     * @returns {Promise<void>} A promise that resolves when the push token is set.
     */
    setPushToken: async (pushToken) => {
        if (!PushNotificationConfiguration.pushTokenExists()) {
            await AsyncStorage.setItem('expoPushToken', pushToken);
        }
    },

    /**
     * Generates a new push token using Expo Notifications and sets it in AsyncStorage.
     *
     * @returns {Promise<string>} A promise that resolves to the generated push token.
     */
    generatePushtoken: async () => {
        const token = (await Notifications.getExpoPushTokenAsync()).data;
        PushNotificationConfiguration.setPushToken(token);
        return token;
    },

}

export default PushNotificationConfiguration;


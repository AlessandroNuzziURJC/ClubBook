import React, { useEffect } from "react";
import { BackHandler, Alert } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import ProfileScreen from "../profileScreens/ProfileScreen";
import ProfileEdit from '../profileScreens/ProfileEditScreen';
import { Ionicons } from "@expo/vector-icons";
import NotificationsScreen from "../notificationsScreens/NotificationScreen";
import StudentHomeScreen from "../homeScreens/StudentHomeScreen";
import CalendarScreen from "../eventsScreens/CalendarScreen";
import EventListScreen from "../eventsScreens/EventListScreen";
import EventInfoScreen from "../eventsScreens/EventInfoScreen";

const HomeStack = createNativeStackNavigator();

/**
 * HomeStack Navigator for the student app, contains screens related to events and the home screen.
 * @returns {JSX.Element} The stack navigator for the Home screen stack.
 */
const HomeStackScreen = () => {
    return (
        <HomeStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="Home">
            <HomeStack.Screen name="Home" component={StudentHomeScreen} />
            <HomeStack.Screen name="Calendar" component={CalendarScreen} initialParams={{ editAndDelete: false }}/>
            <HomeStack.Screen name="EventList" component={EventListScreen} initialParams={{ editAndDelete: false, fetchFutureEvents: true }}/>
            <HomeStack.Screen name="PastEventsList" component={EventListScreen} initialParams={{ editAndDelete: false, fetchFutureEvents: false }}/>
            <HomeStack.Screen name="EventInfoScreen" component={EventInfoScreen} initialParams={{ admin: false, teacher: false }}/>
        </HomeStack.Navigator>
    );
};

const NotificationsStack = createNativeStackNavigator();

/**
 * NotificationsStack Navigator for handling notifications in the student app.
 * @returns {JSX.Element} The stack navigator for the Notifications screen.
 */
const NotificationsStackNavigator = () => {
    return (
        <NotificationsStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="Notifications">
            <NotificationsStack.Screen name="Notifications" component={NotificationsScreen} />
        </NotificationsStack.Navigator>
    );
}

const ProfileStack = createNativeStackNavigator();

/**
 * ProfileStack Navigator for managing user profile and profile editing screens.
 * @returns {JSX.Element} The stack navigator for the Profile screen stack.
 */
const ProfileStackNavigator = () => {
    return (
        <ProfileStack.Navigator screenOptions={{
            headerShown: false
        }}>
            <ProfileStack.Screen name="Profile" component={ProfileScreen} />
            <ProfileStack.Screen name="ProfileEdit" component={ProfileEdit} />
        </ProfileStack.Navigator>
    );
};

/**
 * Main tab navigator component for the student app. 
 * Includes Home, Notifications, and Profile tabs.
 * @returns {JSX.Element} The main tab navigator for the student app.
 */
const StudentMainScreen = () => {
    const Tab = createBottomTabNavigator();

    useEffect(() => {
        const backAction = () => {
            Alert.alert("Salir", "¿Estás seguro que quieres salir de la aplicación?", [
                {
                    text: "Cancelar",
                    onPress: () => null,
                    style: "cancel"
                },
                { text: "Salir", onPress: () => BackHandler.exitApp() }
            ]);
            return true;
        };

        const backHandler = BackHandler.addEventListener(
            "hardwareBackPress",
            backAction
        );

        return () => backHandler.remove();
    }, []);

    return (
        <Tab.Navigator screenOptions={{
            headerShown: false,
            style: {
                height: '9%',
                position: 'absolute',
                bottom: 0,
                elevation: 0
            },
            tabBarActiveTintColor: '#1162BF',
        }}>
            <Tab.Screen name="Inicio" component={HomeStackScreen} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="home" color={color} size={size} />
                ),
            }} />
            <Tab.Screen name="Notificaciones" component={NotificationsStackNavigator}
                options={{
                    tabBarIcon: ({ color, size }) => (
                        <Ionicons name="notifications-outline" color={color} size={size} />
                    ),
                }} />
            <Tab.Screen name="Perfil" component={ProfileStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="person" color={color} size={size} />
                ),
            }} />
        </Tab.Navigator>
    );
};

export default StudentMainScreen;

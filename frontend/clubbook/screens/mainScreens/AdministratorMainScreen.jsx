import React from "react";
import { View, Text } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Profile from '../../components/Profile';
import ProfileEdit from '../profileScreens/ProfileEditScreen';
import { Ionicons } from "@expo/vector-icons";

const HomeStackScreen = () => {
    return (
        <View>
            <Text>Este es el home administrador</Text>
        </View>
    );
};

const SettingsStackScreen = () => {
    return (
        <View>
            <Text>Este es el settings administrador</Text>
        </View>
    );
}

const ProfileStack = createNativeStackNavigator();

const ProfileStackNavigator = () => {
    return (
        <ProfileStack.Navigator screenOptions={{
            headerShown: false}}>
            <ProfileStack.Screen name="Profile" component={Profile} />
            <ProfileStack.Screen name="ProfileEdit" component={ProfileEdit} />
        </ProfileStack.Navigator>
    );
};

const AdministratorMainScreen = () => {
    const Tab = createBottomTabNavigator();
    return (
        <Tab.Navigator screenOptions={{ headerShown: false }}>
            <Tab.Screen name="HomeMenu" component={HomeStackScreen} />
            <Tab.Screen name="SettingsMenu" component={SettingsStackScreen} />
            <Tab.Screen name="Perfil" component={ProfileStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="person" color={color} size={size} />
                ),
            }} />
        </Tab.Navigator>
    );
};

export default AdministratorMainScreen;
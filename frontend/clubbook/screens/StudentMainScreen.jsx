import React from "react";
import { View, Text } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import Profile from '../components/Profile'

const HomeStackScreen = () => {
    return (
        <View>
            <Text>Este es el home student</Text>
        </View>
    );
};

const SettingsStackScreen = () => {
    return (
        <View>
            <Text>Este es el settings student</Text>
        </View>
    );
}

const StudentMainScreen = () => {
    const Tab = createBottomTabNavigator();
    return (
        <Tab.Navigator screenOptions={{ headerShown: false }}>
            <Tab.Screen name="HomeMenu" component={HomeStackScreen} />
            <Tab.Screen name="SettingsMenu" component={SettingsStackScreen} />
            <Tab.Screen name="Perfil" component={Profile} />
        </Tab.Navigator>
    );
};

export default StudentMainScreen;
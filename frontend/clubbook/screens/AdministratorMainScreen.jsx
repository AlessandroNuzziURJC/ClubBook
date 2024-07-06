import React from "react";
import { View, Text } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import Profile from '../components/Profile';
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

const AdministratorMainScreen = () => {
    const Tab = createBottomTabNavigator();
    return (
        <Tab.Navigator screenOptions={{ headerShown: false }}>
            <Tab.Screen name="HomeMenu" component={HomeStackScreen} />
            <Tab.Screen name="SettingsMenu" component={SettingsStackScreen} />
            <Tab.Screen name="Perfil" component={Profile} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="person" color={color} size={size} />
                ),
            }} />
        </Tab.Navigator>
    );
};

export default AdministratorMainScreen;
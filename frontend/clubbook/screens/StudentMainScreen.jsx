import React from "react";
import { View, Text } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import Profile from '../components/Profile';
import { Ionicons } from "@expo/vector-icons";

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

export default StudentMainScreen;
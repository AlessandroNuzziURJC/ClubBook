import React, { useEffect } from "react";
import { View, Text, BackHandler, Alert } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import Profile from '../../components/Profile';
import ProfileEdit from '../profileScreens/ProfileEditScreen';
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

const ProfileStack = createNativeStackNavigator();

const ProfileStackNavigator = () => {
    return (
        <ProfileStack.Navigator screenOptions={{
            headerShown: false
            }}>
            <ProfileStack.Screen name="Profile" component={Profile} />
            <ProfileStack.Screen name="ProfileEdit" component={ProfileEdit} />
        </ProfileStack.Navigator>
    );
};

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

export default StudentMainScreen;

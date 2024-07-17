import React from "react";
import { View, Text } from "react-native";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import ProfileScreen from '../profileScreens/ProfileScreen';
import ProfileEdit from '../profileScreens/ProfileEditScreen';
import UsersScreen from '../studentsListScreens/UsersScreen';
import { Ionicons } from "@expo/vector-icons";
import UserInfoScreen from '../studentsListScreens/UserInfoScreen';
import UserListSelector from "../userListSelector/userListSelector";
import SearchUser from "../../components/SearchUser";

const HomeStackScreen = () => {
    return (
        <View>
            <Text>Este es el home administrador</Text>
        </View>
    );
};

const UsersStack = createNativeStackNavigator();

const UsersStackNavigator = () => {
    return (
        <UsersStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="UsersLists">
            <UsersStack.Screen name="UsersLists" component={UserListSelector} />
            <UsersStack.Screen name="UsersScreen" component={UsersScreen} />
            <UsersStack.Screen name="UserProfile" component={UserInfoScreen}/>
            <UsersStack.Screen name="Searcher" component={SearchUser}/>
        </UsersStack.Navigator>
    );
}

const ProfileStack = createNativeStackNavigator();

const ProfileStackNavigator = () => {
    return (
        <ProfileStack.Navigator screenOptions={{
            headerShown: false}}>
            <ProfileStack.Screen name="Profile" component={ProfileScreen} />
            <ProfileStack.Screen name="ProfileEdit" component={ProfileEdit} />
        </ProfileStack.Navigator>
    );
};

const AdministratorMainScreen = () => {
    const Tab = createBottomTabNavigator();
    return (
        <Tab.Navigator screenOptions={{ headerShown: false }}>
            <Tab.Screen name="HomeMenu" component={HomeStackScreen} />
            <Tab.Screen name="Usuarios" component={UsersStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="people" color={color} size={size} />
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

export default AdministratorMainScreen;
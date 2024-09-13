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
import ClassGroupListSelector from "../classGroupScreens/ClassGroupListSelectorScreen";
import NewClassGroup from "../classGroupScreens/NewClassGroupScreen";
import ClassGroupInfo from "../classGroupScreens/ClassGroupInfoScreen";
import EditClassGroup from "../classGroupScreens/EditClassGroupScreen";
import ClassGroupAddStudent from "../classGroupScreens/ClassGroupAddStudentsScreen";
import AdministratorHomeScreen from "../homeScreens/AdministratorHomeScreen";
import AttendanceControlSelector from "../attendanceScreens/AttendanceControlSelectorScreen";
import AttendanceData from "../attendanceScreens/AttendanceData";

const ClassStack = createNativeStackNavigator();

const ClassGroupNavigator = () => {
    return (
        <ClassStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="ClassLists">
            <ClassStack.Screen name="ClassGroupLists" component={ClassGroupListSelector} initialParams={{ editAndDelete: true }}/>
            <ClassStack.Screen name="NewClassGroup" component={NewClassGroup} />
            <ClassStack.Screen name="ClassGroupInfo" component={ClassGroupInfo} />
            <ClassStack.Screen name="UserProfile" component={UserInfoScreen}/>
            <ClassStack.Screen name="EditClassGroup" component={EditClassGroup}/>
            <ClassStack.Screen name="ClassGroupAddStudent" component={ClassGroupAddStudent}/>
        </ClassStack.Navigator>
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

const HomeStack = createNativeStackNavigator();

const HomeStackNavigator = () => {
    return (
        <HomeStack.Navigator screenOptions={{headerShown: false}} initialRouteName="Home">
            <HomeStack.Screen name="Home" component={AdministratorHomeScreen} />
            <HomeStack.Screen name="AttendanceControlSelector" component={AttendanceControlSelector} initialParams={{ checkList: false }}/>
            <HomeStack.Screen name="AttendanceData" component={AttendanceData} />
        </HomeStack.Navigator>
    );
};

const AdministratorMainScreen = () => {
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
            <Tab.Screen name="HomeMenu" component={HomeStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="home" color={color} size={size} />
                )
            }} />
            <Tab.Screen name="Notificaciones" component={HomeStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="notifications-outline" color={color} size={size} />
                ),
            }} />
            <Tab.Screen name="Clases" component={ClassGroupNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="barbell-outline" color={color} size={size} />
                ),
            }} />
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
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
import ClassGroupListSelector from "../classGroupScreens/ClassGroupListSelector";
import NewClassGroup from "../classGroupScreens/NewClassGroup";
import ClassGroupInfo from "../classGroupScreens/ClassGroupInfo";
import EditClassGroup from "../classGroupScreens/EditClassGroup";
import ClassGroupAddStudent from "../classGroupScreens/ClassGroupAddStudents";

const HomeStackScreen = () => {
    return (
        <View>
            <Text>Este es el home administrador</Text>
        </View>
    );
};

const ClassStack = createNativeStackNavigator();

const ClassGroupScreen = () => {
    return (
        <ClassStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="ClassLists">
            <ClassStack.Screen name="ClassGroupLists" component={ClassGroupListSelector} />
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
            <Tab.Screen name="HomeMenu" component={HomeStackScreen} />
            <Tab.Screen name="Clases" component={ClassGroupScreen} options={{
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
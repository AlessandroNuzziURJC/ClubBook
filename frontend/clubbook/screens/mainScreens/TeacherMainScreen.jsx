import React from "react";
import { createBottomTabNavigator } from "@react-navigation/bottom-tabs";
import { createNativeStackNavigator } from '@react-navigation/native-stack';
import ProfileScreen from '../profileScreens/ProfileScreen';
import ProfileEdit from '../profileScreens/ProfileEditScreen';
import UsersScreen from '../studentsListScreens/UsersScreen';
import { Ionicons } from "@expo/vector-icons";
import UserInfoScreen from '../studentsListScreens/UserInfoScreen';
import SearchUser from "../../components/SearchUser";
import ClassGroupListSelector from "../classGroupScreens/ClassGroupListSelectorScreen";
import ClassGroupInfo from "../classGroupScreens/ClassGroupInfoScreen";
import TeacherHomeScreen from "../homeScreens/TeacherHomeScreen";
import AttendanceControlSelector from "../attendanceScreens/AttendanceControlSelectorScreen"
import AttendanceData from "../attendanceScreens/AttendanceData";
import AttendanceCheckList from "../attendanceScreens/AttendanceCheckList";
import CalendarScreen from "../eventsScreens/CalendarScreen";
import EventListScreen from "../eventsScreens/EventListScreen";
import EventInfoScreen from "../eventsScreens/EventInfoScreen";
import AttendanceEventListScreen from "../eventsScreens/AttendanceEventListScreen";
import NotificationsScreen from "../notificationsScreens/NotificationScreen";
import NotebookMainScreen from "../notebookScreens/NotebookMainScreen";
import ClassGroupAgendaScreen from "../notebookScreens/ClassGroupAgendaScreen";
import VirtualAssistantConfigFormScreen from "../notebookScreens/VirtualAssistantConfigFormScreen";
import AgendaScreen from "../notebookScreens/AgendaScreen";


const HomeStack = createNativeStackNavigator();

/**
 * Navigator stack for home-related screens.
 * Includes screens for teacher home, attendance control, calendar, and event details.
 * @returns {React.Component} HomeStackNavigator component.
 */
const HomeStackNavigator = () => {
    return (
        <HomeStack.Navigator screenOptions={{headerShown: false}} initialRouteName="Home">
            <HomeStack.Screen name="Home" component={TeacherHomeScreen} />
            <HomeStack.Screen name="AttendanceControlSelector" component={AttendanceControlSelector} initialParams={{ checkList: true }}/>
            <HomeStack.Screen name="AttendanceData" component={AttendanceData} />
            <HomeStack.Screen name='AttendanceCheckList' component={AttendanceCheckList} />
            <HomeStack.Screen name="Calendar" component={CalendarScreen} initialParams={{ editAndDelete: false }}/>
            <HomeStack.Screen name="EventList" component={EventListScreen} initialParams={{ editAndDelete: false, fetchFutureEvents: true }}/>
            <HomeStack.Screen name="PastEventsList" component={EventListScreen} initialParams={{ editAndDelete: false, fetchFutureEvents: false }}/>
            <HomeStack.Screen name="EventInfoScreen" component={EventInfoScreen} initialParams={{ admin: false, teacher: true }}/>
            <HomeStack.Screen name="AttendanceEvent" component={AttendanceEventListScreen} />
            <HomeStack.Screen name="NotebookMainScreen" component={NotebookMainScreen} />
            <HomeStack.Screen name="ClassGroupAgendaScreen" component={ClassGroupAgendaScreen} />
            <HomeStack.Screen name="VirtualAssistantConfigFormScreen" component={VirtualAssistantConfigFormScreen} />
            <HomeStack.Screen name={"AgendaScreen"} component={AgendaScreen} />
        </HomeStack.Navigator>
    );
};

const NotificationsStack = createNativeStackNavigator();

/**
 * Navigator stack for notifications.
 * Provides navigation for viewing notifications.
 * @returns {React.Component} NotificationsStackNavigator component.
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

const ClassStack = createNativeStackNavigator();

/**
 * Navigator stack for class group-related screens.
 * Includes screens for class group lists, details, and user profile.
 * @returns {React.Component} ClassGroupNavigator component.
 */
const ClassGroupNavigator = () => {
    return (
        <ClassStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="ClassLists">
            <ClassStack.Screen name="ClassGroupLists" component={ClassGroupListSelector} initialParams={{ editAndDelete: false }} />
            <ClassStack.Screen name="ClassGroupInfo" component={ClassGroupInfo} />
            <ClassStack.Screen name="UserProfile" component={UserInfoScreen} />
        </ClassStack.Navigator>
    );
};

const UsersStack = createNativeStackNavigator();

/**
 * Navigator stack for user-related screens.
 * Provides navigation for user list, profile, and search functionality.
 * @returns {React.Component} UsersStackNavigator component.
 */
const UsersStackNavigator = () => {
    return (
        <UsersStack.Navigator screenOptions={{
            headerShown: false
        }} initialRouteName="UsersScreen">
            <UsersStack.Screen name="UsersScreen" component={UsersScreen} initialParams={{ key: 'student' }} />
            <UsersStack.Screen name="UserProfile" component={UserInfoScreen} />
            <UsersStack.Screen name="Searcher" component={SearchUser} />
        </UsersStack.Navigator>
    );
}


const ProfileStack = createNativeStackNavigator();

/**
 * Navigator stack for profile-related screens.
 * Provides navigation for viewing and editing user profile.
 * @returns {React.Component} ProfileStackNavigator component.
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
 * Main screen for teacher role, providing bottom tab navigation.
 * Includes tabs for home, notifications, class groups, users, and profile.
 * @returns {React.Component} TeacherMainScreen component.
 */
const TeacherMainScreen = () => {
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
            <Tab.Screen name="Inicio" component={HomeStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="home" color={color} size={size} />
                ),
            }} />
            <Tab.Screen name="Notificaciones" component={NotificationsStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="notifications-outline" color={color} size={size} />
                ),
            }} />
            <Tab.Screen name="Clases" component={ClassGroupNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="barbell-outline" color={color} size={size} />
                ),
            }} />
            <Tab.Screen name="Alumnos" component={UsersStackNavigator} options={{
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

export default TeacherMainScreen;


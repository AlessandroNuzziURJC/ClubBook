import React from "react";
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
import SeasonControlScreen from "../seasonScreens/SeasonControlScreen";
import CalendarScreen from "../eventsScreens/CalendarScreen";
import EventListScreen from "../eventsScreens/EventListScreen";
import EventInfoScreen from "../eventsScreens/EventInfoScreen";
import NewEventFormScreen from "../eventsScreens/NewEventFormScreen";
import EditEventFormScreen from "../eventsScreens/EditEventFormScreen";
import AttendanceEventListScreen from "../eventsScreens/AttendanceEventListScreen";
import NotificationsScreen from "../notificationsScreens/NotificationScreen";
import ModifyClassGroupStudent from "../classGroupScreens/ModifyClassGroupStudentsScreen";
import ClassGroupDeleteStudentScreen from "../classGroupScreens/ClassGroupDeleteStudentScreen";
import UserManagementScreen from "../administratorUserManagement/UserManagementScreen";
import NewUserFormScreen from "../administratorUserManagement/NewUserFormScreen";
import AdministratorListScreen from "../administratorUserManagement/AdministratorListScreen";
import DeleteUsersListScreen from "../administratorUserManagement/DeleteUsersListScreen";

const HomeStack = createNativeStackNavigator();

/**
 * Home stack navigator for the administrator's main screen.
 * Contains screens for season control, attendance, events, and user management.
 * @component
 * @returns {JSX.Element} The HomeStackNavigator component.
 */
const HomeStackNavigator = () => {
    return (
        <HomeStack.Navigator screenOptions={{headerShown: false}} initialRouteName="Home">
            <HomeStack.Screen name="Home" component={AdministratorHomeScreen} />
            <HomeStack.Screen name="Season" component={SeasonControlScreen} />
            <HomeStack.Screen name="AttendanceControlSelector" component={AttendanceControlSelector} initialParams={{ checkList: false }}/>
            <HomeStack.Screen name="AttendanceData" component={AttendanceData} />
            <HomeStack.Screen name="Calendar" component={CalendarScreen} initialParams={{ editAndDelete: true }}/>
            <HomeStack.Screen name="NewEvent" component={NewEventFormScreen} />
            <HomeStack.Screen name="EditEvent" component={EditEventFormScreen}/>
            <HomeStack.Screen name="EventList" component={EventListScreen} initialParams={{ editAndDelete: true, fetchFutureEvents: true }}/>
            <HomeStack.Screen name="PastEventsList" component={EventListScreen} initialParams={{ editAndDelete: false, fetchFutureEvents: false }}/>
            <HomeStack.Screen name="EventInfoScreen" component={EventInfoScreen} initialParams={{ admin: true, teacher: false }}/>
            <HomeStack.Screen name="AttendanceEvent" component={AttendanceEventListScreen} />
            <HomeStack.Screen name="UserManagementScreen" component={UserManagementScreen} />
            <HomeStack.Screen name="NewUserFormScreen" component={NewUserFormScreen} />
            <HomeStack.Screen name="AdministratorListScreen" component={AdministratorListScreen} />
            <HomeStack.Screen name="DeleteUsersListScreen" component={DeleteUsersListScreen} />
        </HomeStack.Navigator>
    );
};

const NotificationsStack = createNativeStackNavigator();

/**
 * Notifications stack navigator for handling notification-related screens.
 * @component
 * @returns {JSX.Element} The NotificationsStackNavigator component.
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
 * Class group stack navigator for managing class group screens, such as adding students and editing groups.
 * @component
 * @returns {JSX.Element} The ClassGroupNavigator component.
 */
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
            <ClassStack.Screen name="ModifyClassGroupStudent" component={ModifyClassGroupStudent} />
            <ClassStack.Screen name="ClassGroupAddStudent" component={ClassGroupAddStudent}/>
            <ClassStack.Screen name="ClassGroupDeleteStudent" component={ClassGroupDeleteStudentScreen} />
        </ClassStack.Navigator>
    );
};

const UsersStack = createNativeStackNavigator();

/**
 * Users stack navigator for user-related screens, including user lists and profile views.
 * @component
 * @returns {JSX.Element} The UsersStackNavigator component.
 */
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

/**
 * Profile stack navigator for profile-related screens, including profile viewing and editing.
 * @component
 * @returns {JSX.Element} The ProfileStackNavigator component.
 */
const ProfileStackNavigator = () => {
    return (
        <ProfileStack.Navigator screenOptions={{
            headerShown: false}}>
            <ProfileStack.Screen name="Profile" component={ProfileScreen} />
            <ProfileStack.Screen name="ProfileEdit" component={ProfileEdit} />
        </ProfileStack.Navigator>
    );
};

/**
 * Main screen for the administrator, containing a bottom tab navigator
 * with tabs for home, notifications, class groups, users, and profile.
 * @component
 * @returns {JSX.Element} The AdministratorMainScreen component.
 */
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
            <Tab.Screen name="Inicio" component={HomeStackNavigator} options={{
                tabBarIcon: ({ color, size }) => (
                    <Ionicons name="home" color={color} size={size} />
                )
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
import React, { useState, useCallback, useEffect } from "react";
import { View, Text, StyleSheet, SectionList, RefreshControl } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import Notification from "./NotificationComponent";
import ServerRequest from "../../serverRequests/ServerRequests";

/**
 * A functional component that displays a list of notifications categorized by date.
 *
 * @component
 * @returns {JSX.Element} The rendered notifications screen.
 */
const NotificationsScreen = () => {
    const [notifications, setNotifications] = useState([]);
    const [refreshing, setRefreshing] = useState(false);

    /**
     * Formats a date into a readable string.
     *
     * @param {string} date - The date to format.
     * @returns {string} The formatted date string.
     */
    const formatDate = (date) => {
        const dateValue = new Date(date);
        return dateValue.toDateString(); 
    };

    /**
     * Categorizes notifications into today's and previous notifications.
     *
     * @param {Array} notifications - The list of notifications to categorize.
     * @returns {Array} An array of categorized notifications for rendering.
     */
    const categorizeNotifications = (notifications) => {
        const today = new Date().toDateString();

        const todayNotifications = notifications.filter(notification => formatDate(notification.createdAt) === today);
        const previousNotifications = notifications.filter(notification => formatDate(notification.createdAt) !== today);

        return [
            { title: "Hoy", data: todayNotifications },
            { title: "Anteriores", data: previousNotifications }
        ];
    };

    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    /**
     * Fetches notifications from the server and updates the state.
     *
     * @returns {Promise<void>}
     */
    const getFromServer = async () => {
        const response = await ServerRequest.getNotificationsByUserId();
        if (response.ok) {
            const responseData = await response.json();
            setNotifications(responseData);
        } else {
            Alert.alert('Error al cargar los datos del servidor.');
        }
    }

    /**
     * Handles the refresh control for the notifications list.
     *
     * @returns {Promise<void>}
     */
    const handleRefresh = useCallback(async () => {
        setRefreshing(true);
        setTimeout(() => {
            setRefreshing(false);
        }, 2000);
        getFromServer();
    }, []);

    useEffect(() => {
        getFromServer();
    }, []);

    /**
     * Renders a notification item.
     *
     * @param {Object} param0 - The object containing the item.
     * @param {Object} param0.item - The notification data to render.
     * @returns {JSX.Element} The rendered notification component.
     */
    const renderItem = ({ item }) => (
        <Notification data={item} />
    );

    /**
     * Renders the section header for the notifications list.
     *
     * @param {Object} param0 - The object containing the section.
     * @param {Object} param0.section - The section data.
     * @returns {JSX.Element} The rendered section header.
     */
    const renderSectionHeader = ({ section: { title } }) => (
        <View style={styles.sectionHeaderContainer}>
            <View style={styles.sectionHeaderLine} />
            <Text style={styles.sectionHeaderText}>{title}</Text>
            <View style={styles.sectionHeaderLine} />
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Notificaciones</Text>
            </View>
            <SectionList
                sections={categorizeNotifications(notifications)}
                renderItem={renderItem}
                renderSectionHeader={renderSectionHeader}
                keyExtractor={(item) => item.id.toString()}
                contentContainerStyle={styles.listContent}
                refreshControl={
                    <RefreshControl
                        refreshing={refreshing}
                        onRefresh={handleRefresh}
                        colors={['#1162BF']} 
                        tintColor='#1162BF' 
                    />
                }
            />
        </View>
    );
}

export default NotificationsScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    sectionHeaderContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 10,
        backgroundColor: 'white'
    },
    sectionHeaderLine: {
        flex: 1,
        height: 1,
        backgroundColor: '#1162BF',
    },
    sectionHeaderText: {
        color: '#1162BF',
        paddingHorizontal: 10,
        backgroundColor: '#fff',
    },
    listContent: {
        paddingBottom: 20,
    },
});

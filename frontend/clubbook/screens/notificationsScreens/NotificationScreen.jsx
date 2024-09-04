import React, { useState, useCallback } from "react";
import { View, Text, StyleSheet, SectionList, RefreshControl } from "react-native";
import Notification from "./NotificationComponent";

// Mock notification data
const initialNotifications = [
    { id: 1, title: "Falta de asistencia", timestamp: "2024-09-04T10:00:00Z", content: "El alumno Pedro Pérez Arnautovich no ha asistido a la clase de MMA el día 18 de agosto de 2024." },
    { id: 2, title: "Título", timestamp: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 3, title: "Título", timestamp: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " },
    { id: 4, title: "Título", timestamp: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 5, title: "Título", timestamp: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " },
    { id: 6, title: "Título", timestamp: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 7, title: "Título", timestamp: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " }
];

const NotificationsScreen = () => {
    const [notifications, setNotifications] = useState(initialNotifications);
    const [refreshing, setRefreshing] = useState(false);

    // Function to format date and time
    const formatDate = (timestamp) => {
        const date = new Date(timestamp);
        return date.toDateString(); // Returns the date in a readable format
    };

    // Function to categorize notifications
    const categorizeNotifications = (notifications) => {
        const today = new Date().toDateString();

        const todayNotifications = notifications.filter(notification => formatDate(notification.timestamp) === today);
        const previousNotifications = notifications.filter(notification => formatDate(notification.timestamp) !== today);

        return [
            { title: "Hoy", data: todayNotifications },
            { title: "Anteriores", data: previousNotifications }
        ];
    };

    const handleRefresh = useCallback(async () => {
        setRefreshing(true);
        // Simulate a network request or data fetching
        setTimeout(() => {
            // Here you would normally fetch new data
            // For now, we'll just reset the state to simulate a refresh
            setNotifications(initialNotifications);
            setRefreshing(false);
        }, 2000); // Simulate a network delay
    }, []);

    const renderItem = ({ item }) => (
        <Notification data={item} />
    );

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
                        colors={['#1162BF']} // Color del spinner de carga
                        tintColor='#1162BF' // Color del spinner en iOS
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
        backgroundColor: '#fff', // Make background white to overlay line
    },
    listContent: {
        paddingBottom: 20,
    },
});

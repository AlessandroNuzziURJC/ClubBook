import React, { useState, useCallback, useEffect } from "react";
import { View, Text, StyleSheet, SectionList, RefreshControl } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import Notification from "./NotificationComponent";
import ServerRequest from "../../serverRequests/ServerRequests";

// Mock notification data
/*const initialNotifications = [
    { id: 1, title: "Falta de asistencia", date: "2024-09-04T10:00:00Z", content: "El alumno Pedro Pérez Arnautovich no ha asistido a la clase de MMA el día 18 de agosto de 2024." },
    { id: 2, title: "Título", date: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 3, title: "Título", date: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " },
    { id: 4, title: "Título", date: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 5, title: "Título", date: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " },
    { id: 6, title: "Título", date: "2024-09-03T10:00:00Z", content: "Lorem Ipsum " },
    { id: 7, title: "Título", date: "2024-09-02T10:00:00Z", content: "Lorem Ipsum " }
];*/

const NotificationsScreen = () => {
    const [notifications, setNotifications] = useState([]);
    const [refreshing, setRefreshing] = useState(false);

    // Function to format date and time
    const formatDate = (date) => {
        const dateValue = new Date(date);
        return dateValue.toDateString(); // Returns the date in a readable format
    };

    // Function to categorize notifications
    const categorizeNotifications = (notifications) => {
        const today = new Date().toDateString();

        const todayNotifications = notifications.filter(notification => formatDate(notification.date) === today);
        const previousNotifications = notifications.filter(notification => formatDate(notification.date) !== today);

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

    const getFromServer = async () => {
        const response = await ServerRequest.getNotificationsByUserId();
        if (response.ok) {
            const responseData = await response.json();
            setNotifications(responseData);
        } else {
            Alert.alert('Error al cargar los datos del servidor.');
        }
    }

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

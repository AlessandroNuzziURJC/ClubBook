import React, { useState } from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import Functions from "../../functions/Functions";

/**
 * A functional component that displays a notification.
 *
 * @component
 * @param {Object} props - The component props.
 * @param {Object} props.data - The notification data.
 * @param {string} props.data.title - The title of the notification.
 * @param {string} props.data.content - The content of the notification.
 * @param {string} props.data.createdAt - The timestamp when the notification was created.
 * @param {string} props.data.date - The date to be converted and displayed.
 * @returns {JSX.Element} The rendered notification component.
 */
const Notification = ({ data }) => {
    const [isExpanded, setIsExpanded] = useState(false);

    /**
     * Calculates the number of days remaining until the notification can be deleted.
     *
     * @param {string} timestamp - The timestamp of when the notification was created.
     * @returns {number} The number of days remaining until the notification can be deleted.
     */
    const calculateTimeRemaining = (timestamp) => {
        const now = new Date();
        const notificationDate = new Date(timestamp.split('T')[0]);
        const deleteDate = new Date(notificationDate.getTime() + 7 * 24 * 60 * 60 * 1000);
        const daysRemaining = Math.max(0, Math.ceil((deleteDate - now) / (1000 * 60 * 60 * 24)));
        return daysRemaining;
    };

    const timeRemaining = calculateTimeRemaining(data.createdAt);

    return (
        <View style={styles.notificationContainer}>
            <View style={styles.contentContainer}>
                <View style={styles.header}>
                    <Text style={styles.notificationTitle}>{data.title}</Text>
                    <View style={styles.clockContainer}>
                        <Ionicons name="time" color={'#C8102E'} size={20} />
                        <Text style={styles.timeRemainingText}>{timeRemaining} días</Text>
                    </View>
                </View>
                <Text
                    style={[styles.notificationContent, { maxHeight: isExpanded ? 'none' : 50 }]}
                    numberOfLines={isExpanded ? undefined : 2}
                >
                    {data.content}
                </Text>



                <Text style={styles.notificationTimestamp}>{Functions.convertDateEngToSpa(data.date)}</Text>
                {data.content.length > 80 &&
                <TouchableOpacity onPress={() => setIsExpanded(!isExpanded)} style={styles.centerButton}>

                    <Text style={styles.toggleText}>
                        {isExpanded ? 'Ver menos' : 'Ver más'}
                    </Text>
                </TouchableOpacity>}
            </View>
        </View>
    );
}

export default Notification;

const styles = StyleSheet.create({
    notificationContainer: {
        flexDirection: 'column',
        backgroundColor: '#f5f5f5',
        borderRadius: 8,
        marginVertical: 5,
        padding: 15,
        borderLeftWidth: 8,
        borderLeftColor: '#1162BF',
        borderTopWidth: 8,
        borderBottomWidth: 8,
        borderTopColor: '#f5f5f5',
        borderBottomColor: '#f5f5f5'
    },
    contentContainer: {
        flex: 1,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    notificationTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#333',
        marginBottom: 5,
    },
    notificationContent: {
        fontSize: 16,
        color: '#555',
        marginBottom: 10,
        overflow: 'hidden',
    },
    notificationTimestamp: {
        fontSize: 14,
        color: '#888',
    },
    clockContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        backgroundColor: 'transparent',
    },
    timeRemainingText: {
        fontSize: 14,
        color: '#C8102E',
        marginLeft: 5,
    },
    centerButton: {
        marginTop: 5,
        alignItems: 'center'
    },
    toggleText: {
        color: '#1162BF',
        marginTop: 5,
        fontSize: 14,
        fontWeight: 'bold',
    },
});

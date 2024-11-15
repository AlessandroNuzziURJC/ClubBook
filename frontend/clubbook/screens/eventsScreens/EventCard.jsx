import React, { useState } from "react";
import { useNavigation } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity, Alert } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import Functions from "../../functions/Functions";
import ServerRequests from "../../serverRequests/ServerRequests";
import Toast from "../../components/Toast";

/**
 * EventCard component displays an event with options to edit, delete, or view more details.
 * Includes confirmation for deletion and displays toast notifications for user actions.
 * 
 * @component
 * @param {Object} props - Component props.
 * @param {boolean} props.editAndDelete - Flag to enable edit and delete options.
 * @param {Object} props.data - Event data to display.
 * @param {function} [props.onCloseModal] - Function to close the modal, if applicable.
 * @param {function} [props.updateScreenWhenDelete] - Callback to update the screen after deletion.
 * @returns {JSX.Element} The rendered EventCard component.
 */
const EventCard = ({ editAndDelete, data, onCloseModal, updateScreenWhenDelete }) => {
    const navigation = useNavigation();

    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    /**
     * Displays a temporary toast notification.
     */
    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    /**
     * Navigates to the EditEvent screen with the event data.
     * 
     * @param {Object} data - Event data to edit.
     */
    const handleEdit = (data) => {
        navigation.navigate("EditEvent", { data });
    };

    /**
     * Deletes the event by calling the server, then shows a toast and updates the screen if applicable.
     */
    const handleDelete = async () => {
        const response = await ServerRequests.deleteEvent(data.id);
        const result = await response.json();
        setIsToastVisible(true);
        setToastMessage(result.message);
        showToast();
        if (editAndDelete) {
            updateScreenWhenDelete()
        }
    };

    /**
     * Shows a confirmation alert before deleting the event.
     */
    const confirmDelete = () => {
        Alert.alert(
            "Confirmar eliminación",
            "¿Estás seguro de que deseas eliminar este evento?",
            [
                {
                    text: "Cancelar",
                    onPress: () => null,
                    style: "cancel",
                },
                {
                    text: "Eliminar",
                    onPress: handleDelete,
                    style: "destructive",
                },
            ],
            { cancelable: false }
        );
    };

    /**
    * Navigates to the EventInfoScreen with the event data, closing the modal if provided.
    * 
    * @param {Object} data - Event data to view.
    */
    const handleSeeMore = (data) => {
        navigation.navigate("EventInfoScreen", { event: data });
        if (onCloseModal) {
            onCloseModal();
        }
    };

    /**
     * Determines the background color based on the event type ID.
     * 
     * @param {number} eventTypeId - ID of the event type.
     * @returns {string} Background color for the event type.
     */
    const getBackgroundColor = (eventTypeId) => {
        switch (eventTypeId) {
            case 1:
                return "#ffedbd";
            case 2:
                return "#cffcd0";
            case 3:
                return "#eed1ff";
            default:
                return "#f8f9fa";
        }
    };

    return (
        <View style={[styles.eventContainer, { backgroundColor: getBackgroundColor(data.type.eventTypeId) }]}>
            <View style={styles.header}>
                <Text style={styles.title}>{data.title.length > 20
                    ? data.title.substring(0, 20) + '...'
                    : data.title
                }</Text>
                {editAndDelete &&
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity onPress={() => handleEdit(data)} style={styles.iconButton}>
                            <Ionicons name="pencil-outline" size={20} color="#1162BF" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={confirmDelete} style={styles.iconButton}>
                            <Ionicons name="trash-outline" size={20} color="red" />
                        </TouchableOpacity>
                    </View>
                }
            </View>
            <Text style={styles.eventType}>{Functions.translateEventTypes(data.type.name)}</Text>
            <View style={styles.info}>
                <Text style={styles.date}>{Functions.convertDateEngToSpa(data.date)}</Text>
                <TouchableOpacity onPress={() => handleSeeMore(data)} style={styles.iconButton}>
                    <Text style={styles.seeMore}>Ver más</Text>
                </TouchableOpacity>
            </View>
            <Toast
                visible={isToastVisible}
                message={toastMessage}
                onClose={() => setIsToastVisible(false)}
            />
        </View>
    );
};

export default EventCard;

const styles = StyleSheet.create({
    eventContainer: {
        flexDirection: "column",
        padding: 15,
        marginBottom: 10,
        borderRadius: 10,
    },
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    iconButton: {
        marginLeft: 20
    },
    title: {
        fontSize: 18,
        fontWeight: "bold",
        color: "#333",
        marginBottom: 5,
    },
    eventType: {
        fontSize: 14,
        color: "#666",
        marginBottom: 10,
    },
    info: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    date: {
        fontSize: 14,
        color: "#666",
    },
    seeMore: {
        fontWeight: 'bold',
        color: "#1162BF"
    }
});

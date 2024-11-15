import React, { useState } from "react";
import { View, StyleSheet } from "react-native";
import { useRoute } from "@react-navigation/native";
import EventForm from "./EventForm";
import ServerRequests from "../../serverRequests/ServerRequests";

/**
 * EditEventFormScreen component allows editing of an event.
 * Receives event data from navigation parameters and renders
 * an EventForm component with the edit mode enabled.
 * 
 * @component
 * @returns {JSX.Element} Rendered EditEventFormScreen component.
 */
const EditEventFormScreen = () => {
    const route = useRoute();
    const [event, setEvent] = useState(route.params.data);

    return (
        <View style={styles.container}>
            <EventForm edit={true} eventReceived={event} saveFunction={ServerRequests.editEvent} />
        </View>
    )
}

export default EditEventFormScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
    }
})
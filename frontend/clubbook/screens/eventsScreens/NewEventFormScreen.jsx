import React from "react";
import { View, StyleSheet } from "react-native";
import EventForm from "./EventForm";
import NewEventDto from "../../dto/NewEventDto";
import ServerRequests from "../../serverRequests/ServerRequests";

const NewEventFormScreen = () => {

    return (
        <View style={styles.container}>
            <EventForm edit={false} eventReceived={new NewEventDto()} saveFunction={ServerRequests.postNewEvent} />
        </View>
    )
}

export default NewEventFormScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
    }
})
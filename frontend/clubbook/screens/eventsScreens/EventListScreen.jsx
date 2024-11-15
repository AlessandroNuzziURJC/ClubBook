import React, { useState, useCallback } from "react";
import { useFocusEffect, useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, RefreshControl, Alert } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import EventCard from "./EventCard";
import ServerRequests from "../../serverRequests/ServerRequests";

/**
 * Screen component to display a list of events.
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const EventListScreen = () => {
    const [events, setEvents] = useState([]);
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete, fetchFutureEvents } = route.params;
    const [refreshing, setRefreshing] = useState(false);
    const [message, setMessage] = useState("");
    const [showMessage, setShowMessage] = useState(false);

    /**
     * Determines which event fetch function to use based on route parameters.
     * @type {Function}
     */
    const fetchEvents = fetchFutureEvents
        ? ServerRequests.getAllFutureEvents
        : ServerRequests.getAllPastEvents;

    /**
     * Page title based on event type (upcoming or past).
     * @type {string}
     */
    const title = fetchFutureEvents
        ? "Próximos eventos"
        : "Eventos pasados";

    /**
     * Fetches events from the server and updates the event list.
     * Displays an error message if the request fails.
     * @async
     */
    const getFromServer = async () => {
        const response = await fetchEvents();
        const result = await response.json();
        if (response.ok) {
            setEvents(result.data);
        } else {
            if (result.message) {
                setShowMessage(true);
                setMessage(result.message);
            } else {
                Alert.alert("Error en la comunicación con el servidor");
            }
        }
    }

    /**
     * Refreshes the event list by re-fetching data from the server.
     */
    const refreshData = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

    
    useFocusEffect(
        useCallback(() => {
            setEvents([]);
            refreshData();
        }, [])
    );

    /**
     * Renders each event item in the list.
     * @param {Object} item - The event item data.
     * @returns {JSX.Element} Event card component.
     */
    const renderItem = ({ item }) => {
        return (
            <EventCard editAndDelete={editAndDelete} data={item} updateScreenWhenDelete={refreshData} />
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{title}</Text>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => navigation.navigate("NewEvent")}>
                        <Ionicons name="add-outline" size={30} color='#1162BF' />
                    </TouchableOpacity>
                }
            </View>
            {editAndDelete &&
                <TouchableOpacity onPress={() => navigation.navigate("PastEventsList")} style={styles.pastEventsButton}>
                    <Text style={styles.pastEvents}>Ver eventos pasados</Text>
                </TouchableOpacity>
            }
            {!showMessage ?
                <FlatList
                    data={events}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    refreshControl={
                        <RefreshControl
                            refreshing={refreshing}
                            onRefresh={refreshData}
                        />
                    }
                    style={styles.alignTop} />
                : <Text style={styles.noEvents}>{message}</Text>
            }
        </View>
    );
}

export default EventListScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    alignTop: {
        marginTop: 10,
    },
    pastEventsButton: {
        backgroundColor: "#1162BF",
        padding: 15,
        borderRadius: 10,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 10,
    },
    pastEvents: {
        color: "#fff",
        fontSize: 16,
        fontWeight: "bold",
    },
    noEvents: {
        color: 'darkgray',
        alignSelf: 'center',
        marginTop: 20
    }
})
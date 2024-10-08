import React, { useState, useEffect, useCallback } from "react";
import { useFocusEffect, useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, RefreshControl, Alert } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import EventCard from "./EventCard";
import ServerRequests from "../../serverRequests/ServerRequests";

const EventListScreen = () => {
    const [events, setEvents] = useState([]);
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete, fetchFutureEvents } = route.params;
    const [refreshing, setRefreshing] = useState(false);

    const fetchEvents = fetchFutureEvents
        ? ServerRequests.getAllFutureEvents
        : ServerRequests.getAllPastEvents;

    const title = fetchFutureEvents
        ? "Próximos eventos"
        : "Eventos pasados";

    const getFromServer = async () => {
        const response = await fetchEvents();
        if (response.ok) {
            const result = await response.json();
            setEvents(result.data);
        } else {
            Alert.alert("Error en la comunicación con el servidor");
        }
    }

    useEffect(() => {
        getFromServer();
    }, []);

    const refreshData = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    const renderItem = ({ item }) => {
        return (
            <EventCard editAndDelete={editAndDelete} data={item} />
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
    }
})
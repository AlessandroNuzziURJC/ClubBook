import React, { useState, useEffect } from "react";
import { useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, RefreshControl } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import EventCard from "./EventCard";

const EventList = () => {
    const [events, setEvents] = useState([
        {
            "id": 1,
            "title": "Campeonato del Madrid 2024",
            "type": "Competición",
            "date": "09/11/2024"
        }, {
            "id": 2,
            "title": "Exhibición 2025",
            "type": "Competición",
            "date": "09/11/2024"
        }, {
            "id": 3,
            "title": "Campeonato del Mundo Kata 2024",
            "type": "Competición",
            "date": "09/11/2024"
        }, {
            "id": 4,
            "title": "Campeonato del Mundo Kata 2024",
            "type": "Competición",
            "date": "09/11/2024"
        }, {
            "id": 5,
            "title": "Campeonato del Mundo Kata 2024",
            "type": "Competición",
            "date": "09/11/2024"
        }
    ]);
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete } = route.params;
    const [refreshing, setRefreshing] = useState(false);

    const refreshData = () => {
        setRefreshing(true);
        setTimeout(() => {
            //getFromServer();
            setRefreshing(false);
        }, 500);
    };

    const renderItem = ({ item }) => {
        return (
            <EventCard editAndDelete={true} data={ item } />
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Lista de eventos</Text>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => navigation.navigate("NewEvent")}>
                        <Ionicons name="add-outline" size={30} color='#1162BF' />
                    </TouchableOpacity>
                }
            </View>
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

export default EventList;

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
    }
})
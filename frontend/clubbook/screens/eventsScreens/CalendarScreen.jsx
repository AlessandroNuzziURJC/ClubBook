import Calendar from "./Calendar";
import React, { useState, useEffect } from "react";
import { useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import EventCard from "./EventCard";

const nextEvent = {
    "title": "Campeonato del Mundo Kata 2024",
    "type": "Competición",
    "date": "09/11/2024"
};

const CalendarScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete } = route.params;

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Calendario</Text>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => navigation.navigate("NewEvent")}>
                        <Ionicons name="add-outline" size={30} color='#1162BF' />
                    </TouchableOpacity>
                }
            </View>
            <TouchableOpacity style={styles.futureEventsButton} onPress={() => navigation.navigate("EventList")}>
                <Text style={styles.futureEvents}>Ver todos los eventos futuros</Text>
            </TouchableOpacity>
            <Calendar />
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Próximo evento</Text>
            </View>
            
            <EventCard editAndDelete={false} data={nextEvent}/>
        </View>
    )
}

export default CalendarScreen;

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
    futureEventsButton: {
        backgroundColor: "#1162BF", 
        padding: 15,
        borderRadius: 10, 
        alignItems: "center", 
        justifyContent: "center",
        marginVertical: 10,
    },
    futureEvents: {
        color: "#fff",
        fontSize: 16,
        fontWeight: "bold",
    },
});
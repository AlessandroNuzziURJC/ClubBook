import React, { useState, useEffect } from "react";
import { useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { Ionicons } from "@expo/vector-icons";

const EventCard = ({ editAndDelete, data }) => {

    const handleEdit = () => {
        console.log("Hola :)");
    }

    const handleDelete = () => {
        console.log("Hola :)");
    }

    const handleSeeMore = () => {
        console.log("Hola :)");
    }

    return (
        <View style={styles.eventContainer}>
            <View style={styles.header}>
                <Text style={styles.title}>{data.title.length > 20
                    ? data.title.substring(0, 20) + '...'
                    : data.title
                }</Text>
                {editAndDelete &&
                    <View style={styles.buttonContainer}>
                        <TouchableOpacity onPress={handleEdit} style={styles.iconButton}>
                            <Ionicons name="pencil-outline" size={20} color="#1162BF" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={handleDelete} style={styles.iconButton}>
                            <Ionicons name="trash-outline" size={20} color="red" />
                        </TouchableOpacity>
                    </View>
                }
            </View>
            <Text style={styles.eventType}>{data.type}</Text>
            <View style={styles.info}>
                <Text style={styles.date}>{data.date}</Text>
                <TouchableOpacity onPress={handleSeeMore} style={styles.iconButton}>
                    <Text style={styles.seeMore}>Ver más</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

export default EventCard;

const styles = StyleSheet.create({
    eventContainer: {
        flexDirection: "column",
        backgroundColor: "#f8f9fa",
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
})
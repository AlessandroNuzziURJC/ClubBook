import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation } from '@react-navigation/native';

/**
 * Teacher Home Screen component that provides navigation options for teachers.
 * Allows access to attendance control, notebook (agenda), and events.
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const TeacherHomeScreen = () => {
    const navigation = useNavigation();

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Inicio</Text>
            </View>
            <View style={styles.content}>
                <TouchableOpacity style={styles.element} onPress={() => navigation.navigate('AttendanceControlSelector')}>
                    <Ionicons name="checkmark-circle-outline" size={25} color="#1162BF" style={styles.icon} />
                    <Text style={styles.elementText}>Control de asistencia</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.element} onPress={() => navigation.navigate('NotebookMainScreen')}>
                    <Ionicons name="book" size={25} color="#1162BF" style={styles.icon} />
                    <Text style={styles.elementText}>Agenda</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.element} onPress={() => navigation.navigate('Calendar')}>
                    <Ionicons name="calendar" size={25} color="#1162BF" style={styles.icon} />
                    <Text style={styles.elementText}>Eventos</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

export default TeacherHomeScreen;

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
    content: {
        marginTop: 20
    },
    element: {
        flexDirection: 'row',
        padding: 10,
        borderColor: '#1162BF',
        borderWidth: 1,
        borderRadius: 10,
        marginTop: 10,
        marginBottom: 10,
        alignItems: 'center',
    },
    elementText: {
        color: '#1162BF',
        fontWeight: '600',
        fontSize: 14
    },
    icon: {
        marginRight: 10,
    }
})
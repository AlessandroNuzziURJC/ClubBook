import React from "react";
import { View, Text, StyleSheet } from "react-native";
import Ionicons from 'react-native-vector-icons/Ionicons';

const AttendanceEventStatusComponent = ({ data }) => {

    const backgroundTeacher = { backgroundColor: '#ddeeff' }; 
    const backgroundStudent = { backgroundColor: '#fff3dd' }; 

    let statusIcon;
    if (data.status === null) {
        statusIcon = <Ionicons name="help-circle-outline" size={20} color="#555" />; 
    } else if (data.status === true) {
        statusIcon = <Ionicons name="checkmark-circle" size={20} color="green" />; 
    } else {
        statusIcon = <Ionicons name="close-circle" size={20} color="red" />; 
    }

    return (
        <View style={[styles.container, data.user.role.name === 'TEACHER' ? backgroundTeacher : backgroundStudent]}>
            <Text style={styles.userName}>
                {data.user.firstName} {data.user.lastName}
            </Text>
            <View style={styles.iconContainer}>
                {statusIcon} 
            </View>
        </View>
    );
}

export default AttendanceEventStatusComponent;

const styles = StyleSheet.create({
    container: {
        borderRadius: 10,
        padding: 20,
        marginBottom: 10,
        flexDirection: 'row',
        alignItems: 'center', // Alinear ícono y texto
    },
    userName: {
        fontSize: 14,
        fontWeight: 'bold',
        color: '#1162BF', // Color de texto moderno
        flex: 1, // Permite que el texto use el espacio disponible
    },
    iconContainer: {
        marginLeft: 10, // Espacio entre el texto y el ícono
    },
});
import React from "react";
import { View, StyleSheet, TouchableOpacity, Text } from "react-native";
import { useNavigation } from '@react-navigation/native';
import ServerRequest from "../../serverRequests/ServerRequests";

const UserListSelector = () => {
    const navigation = useNavigation();

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Listas disponibles</Text>
            </View>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('UsersScreen', { key: 'teacher' })}>
                <Text style={styles.buttonText}>Lista de profesores</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('UsersScreen', { key: 'student' })}>
                <Text style={styles.buttonText}>Lista de alumnos</Text>
            </TouchableOpacity>
        </View>
    )
}

export default UserListSelector;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    button: {
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: '#1162BF',
        borderRadius: 5,
        padding: 20,
        marginBottom: 20
    },
    buttonText: {
        fontSize: 18,
        fontWeight: '500',
        color: '#1162BF'
    }
});
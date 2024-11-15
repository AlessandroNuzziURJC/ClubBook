import React from "react";
import { View, StyleSheet, TouchableOpacity, Text } from "react-native";
import { useRoute, useNavigation } from '@react-navigation/native';

/**
 * ModifyClassGroupStudent component allows users to modify students in a class group.
 * It displays the class group name and provides options to add or remove students.
 *
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const ModifyClassGroupStudent = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{item.name}</Text>
            </View>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('ClassGroupAddStudent', { item })}>
                <Text style={styles.buttonText}>Añadir alumnos</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.button} onPress={() => navigation.navigate('ClassGroupDeleteStudent', { item })}>
                <Text style={styles.buttonText}>Quitar alumnos</Text>
            </TouchableOpacity>
        </View>
    )
}

export default ModifyClassGroupStudent;

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
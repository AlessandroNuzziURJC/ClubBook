import React, { useState } from "react";
import { View, Text, StyleSheet, Alert } from "react-native";
import { useNavigation } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroupForm from "../../components/ClassGroupForm";

/**
 * NewClass component allows users to create a new class group.
 * It renders a form for inputting class group details and handles the submission.
 *
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const NewClass = () => {
    const [classGroup, setClassGroup] = useState(null);
    const navigation = useNavigation();

    /**
     * Saves the new class group data to the server.
     *
     * @param {Object} classGroupOutput - The data of the class group to be saved.
     * @returns {Promise<void>} 
     */
    const saveData = async (classGroupOutput) => {
        if (!classGroupOutput.validate()) {
            Alert.alert('No se ha rellenado correctamente.');
            return;
        }

        try {
            const response = await ServerRequests.createClassGroup(classGroupOutput);

            if (!response.ok) {
                Alert.alert('Error de comunicación con el servidor.');
            } else {
                const updatedClassGroup = await response.json();
                navigation.navigate('ClassGroupLists', { classGroup: updatedClassGroup.data });
            }
        } catch (error) {
            Alert.alert('Ocurrió un error inesperado. Por favor, inténtalo de nuevo.');
        }
    };

    /**
     * Handles the creation of a new class group.
     *
     * @param {Object} newClassGroup - The new class group data.
     */
    const createClassGroup = (newClassGroup) => {
        setClassGroup(newClassGroup);
        saveData(newClassGroup);
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Nueva clase</Text>
            </View>
            <ClassGroupForm sendClassGroupBack={createClassGroup} />
        </View>
    );
};

export default NewClass;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    }
});

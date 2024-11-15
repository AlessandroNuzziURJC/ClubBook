import React, { useState } from "react";
import { View, StyleSheet, Text, Alert } from "react-native";
import { useRoute } from '@react-navigation/native';
import { useNavigation } from "@react-navigation/native";
import ClassGroupForm from "../../components/ClassGroupForm";
import ServerRequests from "../../serverRequests/ServerRequests";

/**
 * EditClassGroup component allows users to edit an existing class group.
 * It retrieves the class group data from the route parameters and provides
 * a form for editing the class details.
 *
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const EditClassGroup = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(item);
    const [editedClassGroup, setEditedClassGroup] = useState(null);

    /**
     * Saves the edited class group data to the server.
     *
     * @async
     * @param {Object} classGroupOutput - The output data from the class group form.
     * @returns {Promise<void>}
     */
    const saveData = async (classGroupOutput) => {
        if (!classGroupOutput.validate()) {
            Alert.alert('No se ha rellenado correctamente.');
            return;
        }

        try {
            const response = await ServerRequests.modifyClassGroup(classGroupOutput);

            if (!response.ok) {
                Alert.alert('Error de comunicación con el servidor.');
            } else {
                const updatedClassGroup = await response.json();
                navigation.navigate('ClassGroupLists', { classGroup: updatedClassGroup });
            }
        } catch (error) {
            Alert.alert('Ocurrió un error inesperado. Por favor, inténtalo de nuevo.');
            console.log(error);
        }
    };

    /**
     * Updates the class group with the new data and saves it.
     *
     * @async
     * @param {Object} classGroupOutput - The updated class group data.
     * @returns {Promise<void>}
     */
    const updateClassGroup = async (classGroupOutput) => {
        setEditedClassGroup(classGroupOutput);

        await saveData(classGroupOutput);
    };


    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Editar clase</Text>
            </View>
            <ClassGroupForm classGroup={classGroup} sendClassGroupBack={updateClassGroup} />

        </View>
    )
};

export default EditClassGroup;

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
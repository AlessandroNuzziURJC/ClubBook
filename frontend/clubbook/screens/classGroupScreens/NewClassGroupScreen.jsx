import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, ScrollView, Alert, TouchableOpacity } from "react-native";
import { useNavigation } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroupForm from "../../components/ClassGroupForm";

const NewClass = () => {
    const [classGroup, setClassGroup] = useState(null);
    const navigation = useNavigation();

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
                navigation.navigate('ClassGroupLists', { classGroup: updatedClassGroup });
            }
        } catch (error) {
            Alert.alert('Ocurrió un error inesperado. Por favor, inténtalo de nuevo.');
        }
    };

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

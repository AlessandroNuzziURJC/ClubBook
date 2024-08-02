import React, { useState, useEffect } from "react";
import { View, ScrollView, StyleSheet, Text, Alert } from "react-native";
import { useRoute } from '@react-navigation/native';
import { useNavigation } from "@react-navigation/native";
import ClassGroupForm from "../../components/ClassGroupForm";
import ServerRequests from "../../serverRequests/ServerRequests";

const EditClassGroup = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(item);
    const [editedClassGroup, setEditedClassGroup] = useState(null);
    
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
                navigation.navigate('ClassGroupLists', { classGroup: updatedClassGroup});
            }
        } catch (error) {
            Alert.alert('Ocurrió un error inesperado. Por favor, inténtalo de nuevo.');
            console.log(error);
        }
    };
    
    const updateClassGroup = async (classGroupOutput) => {
        setEditedClassGroup(classGroupOutput);

        await saveData(classGroupOutput);
    };
    

    return (
        <ScrollView style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>Editar clase</Text>
                </View>
            </View>
            <ClassGroupForm classGroup={classGroup} sendClassGroupBack={updateClassGroup}/>
            
        </ScrollView>
    )
};

export default EditClassGroup;

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
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        marginTop: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    }
});
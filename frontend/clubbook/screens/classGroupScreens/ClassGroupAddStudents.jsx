import React, { useState, useRef, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Alert, Animated, ScrollView } from "react-native";
import { useRoute, useNavigation } from '@react-navigation/native';
import UserCheckboxList from "../../components/UserCheckboxList";
import ServerRequests from "../../serverRequests/ServerRequests";

const ClassGroupAddStudents = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(item);
    const [totalStudents, setTotalStudents] = useState([]);

    const handleSelectStudent = (id) => {
        setTotalStudents(prev =>
            prev.map(student => {
                if (student.id === id) {
                    return { ...student, selected: !student.selected }
                } else {
                    return student
                }

            }));
    };

    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getAllStudentsWithoutClassGroup(); //Sustituir por función buena

            if (response.ok) {
                const result = await response.json();
                setTotalStudents(result);
            } else {
                Alert.alert('Error de comunicación con el servidor.');
            }
        } catch (error) {
            Alert.alert('Error de comunicación con el servidor.');
            console.log(error);
        }
    }

    const handleSave = async () => {
        const newStudents = totalStudents.filter(item => item.selected);
    
        if (!newStudents.length) {
            Alert.alert('No hay alumnos seleccionados.');
            return;
        }
    
        try {
            const updatedClassGroup = { ...classGroup, students: [...classGroup.students, ...newStudents] };
            const response = await ServerRequests.postNewStudentsInClassGroup(classGroup.id, newStudents.map(item => item.id));
    
            if (response.ok) {
                setClassGroup(updatedClassGroup);
                navigation.navigate('ClassGroupLists', { classGroup: updatedClassGroup });
            } else {
                Alert.alert('Error al guardar los cambios');
            }
        } catch (error) {
            Alert.alert('Error', 'Ha ocurrido un error al guardar los cambios.');
        }
    }
    

    useEffect(() => {
        getFromServer();
    }, [])

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>Añadir alumno: </Text>
                    <Text style={styles.classGroupName}>{classGroup.name}</Text>
                </View>
            </View>
            <View style={styles.content}>
                <UserCheckboxList users={totalStudents} usersError={false} handleSelectUser={handleSelectStudent} />
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity style={styles.cancelButton} onPress={() => navigation.goBack()}>
                    <Text style={styles.buttonText}>Cancelar</Text>
                </TouchableOpacity>
                <TouchableOpacity style={styles.saveButton} onPress={handleSave}>
                    <Text style={styles.buttonText}>Guardar</Text>
                </TouchableOpacity>
            </View>
        </View>
    );
}

export default ClassGroupAddStudents;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        justifyContent: 'flex-start',
        paddingTop: 20,
        marginBottom: 20,
    },
    subheader: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'flex-start',
        marginTop: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    classGroupName: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    content: {
        flex: 1,
    },
    saveButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center'
    },
    cancelButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center'
    },
    buttonText: {
        color: '#1162BF',
        fontWeight: 'bold',
        fontSize: 16
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 40
    }
})
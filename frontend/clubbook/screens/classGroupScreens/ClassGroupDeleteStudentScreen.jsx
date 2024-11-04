import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, Alert, ScrollView } from "react-native";
import { useRoute, useNavigation } from '@react-navigation/native';
import UserCheckboxList from "../../components/UserCheckboxList";
import ServerRequests from "../../serverRequests/ServerRequests";
import FormFooter from "../../components/FormFooter";

/**
 * Screen component for deleting students from a class group.
 * 
 * This component allows users to select students to remove from a specific class group.
 * It fetches the list of students currently in the class group and provides a checkbox
 * list for selecting which students to remove.
 *
 * @returns {JSX.Element} The rendered component.
 */
const ClassGroupDeleteStudentScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(item);
    const [totalStudents, setTotalStudents] = useState([]);

    /**
     * Handles the selection and deselection of students in the checkbox list.
     * 
     * @param {number} id - The ID of the student to select or deselect.
     */
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

    /**
     * Saves the changes made by removing selected students from the class group.
     * 
     * It sends a request to the server to remove the selected students from the class group
     * and updates the local state accordingly.
     */
    const handleSave = async () => {
        const newStudents = totalStudents.filter(item => item.selected);

        if (!newStudents.length) {
            Alert.alert('No hay alumnos seleccionados.');
            return;
        }

        try {
            const updatedClassGroup = { ...classGroup, students: [...classGroup.students, ...newStudents] };
            const response = await ServerRequests.removeStudentsInClassGroup(classGroup.id, newStudents.map(item => item.id));

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
        setTotalStudents(classGroup.students);
    }, [])

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Quitar alumnos</Text>
            </View>
            <ScrollView style={styles.content}>
                <UserCheckboxList users={totalStudents} usersError={false} handleSelectUser={handleSelectStudent} />
            </ScrollView>
            <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: handleSave, text: 'Guardar' }} />
        </View>
    );
}

export default ClassGroupDeleteStudentScreen;


const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'flex-start',
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20,
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
        paddingLeft: 20,
        paddingRight: 20,
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
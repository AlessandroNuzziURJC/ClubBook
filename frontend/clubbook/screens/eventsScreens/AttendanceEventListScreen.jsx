import React, { useState, useCallback } from "react";
import { useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, SectionList, RefreshControl, Alert, TouchableOpacity } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import AttendanceEventStatusComponent from "./AttendanceEventStatusComponent";
import ServerRequest from "../../serverRequests/ServerRequests";
import * as FileSystem from 'expo-file-system';
import * as Sharing from 'expo-sharing';

/**
 * AttendanceEventListScreen component displays a list of attendance records for an event.
 * It fetches data for both teachers and students, allows refreshing the list,
 * and provides functionality to download attendance as a PDF.
 *
 * @component
 * @returns {JSX.Element} The rendered component.
 */
const AttendanceEventListScreen = () => {
    const route = useRoute();
    const { eventId } = route.params;
    const categories = ['Asistencia de profesores', 'Asistencia de alumnos'];
    const [users, setUsers] = useState({ students: [], teachers: [] });
    const [refreshing, setRefreshing] = useState(false);

    /**
     * Handles the refresh action to reload attendance data from the server.
     *
     * @returns {Promise<void>}
     */
    const handleRefresh = useCallback(async () => {
        setRefreshing(true);
        await getFromServer();
        setRefreshing(false);
    }, []);

    /**
     * Fetches attendance data for students and teachers from the server.
     *
     * @returns {Promise<void>}
     */
    const getFromServer = async () => {
        const usersData = { students: [], teachers: [] };

        const responseStudents = await ServerRequest.getEventStudentsAttendance(eventId);
        if (responseStudents.ok) {
            const responseStudentsData = await responseStudents.json();
            usersData.students = responseStudentsData.data;
        } else {
            Alert.alert('Error en la comunicación al cargar los alumnos.');
        }

        const responseTeachers = await ServerRequest.getEventTeachersAttendance(eventId);
        if (responseTeachers.ok) {
            const responseTeachersData = await responseTeachers.json();
            usersData.teachers = responseTeachersData.data;
        } else {
            Alert.alert('Error en la comunicación al cargar los profesores.');
        }

        setUsers(usersData);
    }

    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    /**
     * Categorizes users into sections for the SectionList.
     *
     * @param {Object} users - The users object containing students and teachers.
     * @returns {Array} The categorized users for rendering.
     */
    const categorizeUsers = (users) => {
        return [
            { title: 'Asistencia de profesores', data: users.teachers || [] },
            { title: 'Asistencia de alumnos', data: users.students || [] }
        ];
    };

    /**
     * Renders an individual item in the SectionList.
     *
     * @param {Object} item - The item to render.
     * @returns {JSX.Element} The rendered AttendanceEventStatusComponent.
     */
    const renderItem = ({ item }) => (
        <AttendanceEventStatusComponent data={item} />
    );

    /**
     * Renders the section header for the SectionList.
     *
     * @param {Object} section - The section to render.
     * @returns {JSX.Element} The rendered section header.
     */
    const renderSectionHeader = ({ section: { title } }) => (
        <View style={styles.sectionHeaderContainer}>
            <View style={styles.sectionHeaderLine} />
            <Text style={styles.sectionHeaderText}>{title}</Text>
            <View style={styles.sectionHeaderLine} />
        </View>
    );

    /**
     * Handles the download of the attendance report as a PDF.
     *
     * @returns {Promise<void>}
     */
    const handleDownload = async () => {
        try {
            const response = await ServerRequest.downloadPdfEventAttendance(eventId);
            const blobData = await response.blob();
            
            const path = `${FileSystem.documentDirectory}attendance_${eventId}.pdf`;
            const reader = new FileReader();
    
            reader.onloadend = async () => {
                const base64Data = reader.result.split(',')[1];
                await FileSystem.writeAsStringAsync(path, base64Data, {
                    encoding: FileSystem.EncodingType.Base64,
                });
    
                if (await Sharing.isAvailableAsync()) {
                    await Sharing.shareAsync(path);
                } else {
                    console.log('Compartir no disponible');
                }
            };
            reader.readAsDataURL(blobData);
    
        } catch (error) {
            console.error('Error al descargar o abrir el PDF:', error);
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Asistencia</Text>
                <TouchableOpacity style={styles.button} onPress={handleDownload}>
                    <Text style={styles.buttonText}>Descargar PDF</Text>
                </TouchableOpacity>
            </View>
            <SectionList
                sections={categorizeUsers(users)}
                renderItem={renderItem}
                renderSectionHeader={renderSectionHeader}
                keyExtractor={(item) => item.id.toString()}
                contentContainerStyle={styles.listContent}
                refreshControl={
                    <RefreshControl
                        refreshing={refreshing}
                        onRefresh={handleRefresh}
                        colors={['#1162BF']}
                        tintColor='#1162BF'
                    />
                }
            />
        </View>
    )

}

export default AttendanceEventListScreen;

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
    sectionHeaderContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 10,
        backgroundColor: 'white'
    },
    sectionHeaderLine: {
        flex: 1,
        height: 1,
        backgroundColor: '#1162BF',
    },
    sectionHeaderText: {
        color: '#1162BF',
        paddingHorizontal: 10,
        backgroundColor: '#fff',
    },
    listContent: {
        paddingBottom: 20,
    },
    button: {
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 5,
        marginTop: 20,
        borderColor: '#1162BF',
        alignItems: 'center',
        borderWidth: 1
    },
    buttonText: {
        color: '#1162BF',
        fontSize: 16,
        fontWeight: '700'
    },
});

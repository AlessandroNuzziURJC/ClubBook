import React, { useState, useCallback } from "react";
import { useRoute } from "@react-navigation/native";
import { View, Text, StyleSheet, SectionList, RefreshControl, Alert } from "react-native";
import { useFocusEffect } from "@react-navigation/native";
import AttendanceEventStatusComponent from "./AttendanceEventStatusComponent";
import ServerRequest from "../../serverRequests/ServerRequests";

const AttendanceEventListScreen = () => {
    const route = useRoute();
    const { eventId } = route.params;
    const categories = ['Asistencia de profesores', 'Asistencia de alumnos'];
    const [users, setUsers] = useState({ students: [], teachers: [] });
    const [refreshing, setRefreshing] = useState(false);

    const handleRefresh = useCallback(async () => {
        setRefreshing(true);
        await getFromServer();
        setRefreshing(false);
    }, []);

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

    const categorizeUsers = (users) => {
        return [
            { title: 'Asistencia de profesores', data: users.teachers || [] },
            { title: 'Asistencia de alumnos', data: users.students || [] }
        ];
    };

    const renderItem = ({ item }) => (
        <AttendanceEventStatusComponent data={item} />
    );

    const renderSectionHeader = ({ section: { title } }) => (
        <View style={styles.sectionHeaderContainer}>
            <View style={styles.sectionHeaderLine} />
            <Text style={styles.sectionHeaderText}>{title}</Text>
            <View style={styles.sectionHeaderLine} />
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Asistencia</Text>
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
    }
});

import React, { useState, useEffect, useCallback } from "react";
import { View, Text, StyleSheet, FlatList, TouchableOpacity, Alert } from "react-native";
import { useNavigation } from '@react-navigation/native';
import ServerRequests from "../serverRequests/ServerRequests";
import ClassGroup from "../entities/ClassGroup";

const AttendanceControlSelector = () => {
    const [classes, setClasses] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();

    useEffect(() => {
        getFromServer();
    }, []);

    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getClassGroups();

            if (!response.ok) {
                Alert.alert('Error en la comunicación con el servidor');
                return;
            }

            const data = await response.json();
            setClasses(data.map(item => new ClassGroup.parseFromJSON(item)));
        } catch (error) {
            console.log('Error: ', error);
            Alert.alert('Error en la comunicación con el servidor.');
        } finally {
            setRefreshing(false);
        }
    };

    const onRefresh = useCallback(() => {
        setRefreshing(true);
        getFromServer();
    }, []);

    const renderItem = ({ item }) => (
        <View style={styles.classItem}>
            <View style={styles.className}>
                <Text style={styles.classNameText}>{item.name}</Text>
            </View>

            <View style={styles.iconContainer}>
                <TouchableOpacity onPress={() => navigation.navigate('AttendanceData', { item })} style={styles.iconButton}>
                    <Text style={styles.iconButtonText}>Consultar</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => navigation.navigate('AttendanceCheckList', { item })} style={styles.iconButton}>
                    <Text style={styles.viewMoreText}>Pasar lista</Text>
                </TouchableOpacity>
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Control de asistencia</Text>
            </View>
            <FlatList
                data={classes}
                renderItem={renderItem}
                keyExtractor={(item) => item.id.toString()}
                style={styles.alignTop}
                onRefresh={onRefresh}
                refreshing={refreshing}
            />
        </View>
    );
}

export default AttendanceControlSelector;

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
    alignTop: {
        marginTop: 10,
    },
    classItem: {
        backgroundColor: '#ddeeff',
        padding: 20,
        marginVertical: 8,
        borderRadius: 10,
    },
    className: {
        justifyContent: 'left'
    },
    classNameText: {
        fontSize: 20,
        fontWeight: 'bold',
    },
    iconContainer: {
        flexDirection: 'row',
        justifyContent: 'space-around',
        paddingTop: 10,
        marginTop: 10,
        borderTopWidth: 1,
        borderColor: '#1162BF'
    },
    iconButton: {
        flexDirection: 'row',
        alignItems: 'center',
        marginLeft: 10,
    },
    viewMoreText: {
        color: '#1162BF',
    },
    iconButtonText: {
        color: '#1162BF',
    },
});

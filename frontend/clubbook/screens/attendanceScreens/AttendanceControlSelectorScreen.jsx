import React, { useState, useEffect, useCallback } from "react";
import { View, Text, StyleSheet, FlatList, TouchableOpacity, Alert } from "react-native";
import { useNavigation, useRoute } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroup from "../../entities/ClassGroup";

/**
 * AttendanceControlSelector component for displaying and managing attendance records for classes.
 *
 * This component fetches class groups from the server and allows navigation to attendance data or checklist views.
 * It displays a list of classes and provides options to view attendance data or pass a checklist.
 *
 * @component
 */
const AttendanceControlSelector = () => {
    const [classes, setClasses] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();
    const route = useRoute();
    const { checkList } = route.params;

    useEffect(() => {
        getFromServer();
    }, []);

    const [emptyMessage, setEmptyMessage] = useState('');
    
    /**
     * Fetches class groups from the server.
     *
     * Handles various response statuses and updates state accordingly.
     * Displays alerts for errors and manages the empty message state.
     */
    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getClassGroups();
            
            if (response.ok) {
                const responseData = await response.json();
                setClasses(responseData.data.map(item => new ClassGroup.parseFromJSON(item)));
            } else if (response.status === 400) {
                const responseData = await response.json();
                setClasses([]);
                setEmptyMessage('No hay clases disponibles.\n' + responseData.message);
                return;
            } else {
                setClasses([]);
                Alert.alert('Error en la comunicación con el servidor.');
                setEmptyMessage('No hay clases disponibles.\n');
                return;
            }
        } catch (error) {
            console.log('Error: ', error);
            Alert.alert('Error en la comunicación con el servidor.');
            setEmptyMessage('No hay clases disponibles.\n');
        } finally {
            setRefreshing(false);
        }
    };

    /**
     * Refreshes the class groups by re-fetching from the server.
     *
     * Triggered when the user pulls down to refresh the FlatList.
     */
    const onRefresh = useCallback(() => {
        setRefreshing(true);
        getFromServer();
    }, []);

    /**
     * Renders a single item in the FlatList.
     *
     * @param {Object} item - The class group item to render.
     * @returns {JSX.Element} The rendered class group item.
     */
    const renderItem = ({ item }) => (
        <View style={styles.classItem}>
            <View style={styles.className}>
                <Text style={styles.classNameText}>{item.name}</Text>
            </View>

            <View style={styles.iconContainer}>
                <TouchableOpacity onPress={() => navigation.navigate('AttendanceData', { item })} style={styles.iconButton}>
                    <Text style={styles.iconButtonText}>Consultar</Text>
                </TouchableOpacity>
                {checkList &&
                    <TouchableOpacity onPress={() => navigation.navigate('AttendanceCheckList', { item })} style={styles.iconButton}>
                        <Text style={styles.viewMoreText}>Pasar lista</Text>
                    </TouchableOpacity>
                }
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Control de asistencia</Text>
            </View>
            {classes.length > 0 ? (
                <FlatList
                    data={classes}
                    renderItem={renderItem}
                    keyExtractor={(item) => item.id.toString()}
                    style={styles.alignTop}
                    onRefresh={onRefresh}
                    refreshing={refreshing}
                />
            ) : (
                <View style={styles.emptyContainer}>
                    <Text style={styles.emptyMessage}>{emptyMessage}</Text>
                </View>
            )}
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
    emptyContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    emptyMessage: {
        fontSize: 18,
        textAlign: 'center',
        color: '#888',
    },
});

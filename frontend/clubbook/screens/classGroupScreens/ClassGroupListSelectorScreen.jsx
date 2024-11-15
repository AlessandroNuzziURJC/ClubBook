import React, { useState, useEffect, useCallback } from "react";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert, RefreshControl } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation, useRoute } from "@react-navigation/native";
import { useFocusEffect } from '@react-navigation/native';
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroup from "../../entities/ClassGroup";
import Schedule from "../../entities/Schedule";

/**
 * ClassListSelector component for displaying and managing a list of class groups.
 * 
 * This component allows users to view, edit, delete, and add class groups. 
 * It fetches the class groups from the server and handles the state of the 
 * list, including refreshing and displaying messages when there are no classes available.
 */
const ClassListSelector = () => {
    const [classes, setClasses] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete } = route.params;

    const [emptyMessage, setEmptyMessage] = useState('');

    useEffect(() => {
        getFromServer();
    }, []);

    useFocusEffect(
        useCallback(() => {
            setEmptyMessage('');
            getFromServer();
        }, [])
    );

    useEffect(() => {
        if (route.params?.classGroup) {
            if (classes.some(cls => cls.id === route.params.classGroup.id)) {
                updateClassInList(route.params.classGroup);
            } else {
                setClasses([...classes, route.params.classGroup]);
            }
        }
    }, [route.params?.classGroup]);

    /**
     * Navigates to the EditClassGroup screen with the selected item.
     * 
     * @param {Object} item - The selected class group to edit.
     */
    const handleEdit = (item) => {
        navigation.navigate('EditClassGroup', {
            item
        });
    };

    /**
    * Navigates to the ClassGroupInfo screen to view more details about the selected item.
    * 
    * @param {Object} item - The selected class group to view.
    */
    const handleViewMore = (item) => {
        navigation.navigate('ClassGroupInfo', {
            item,
            editAndDelete
        });
    };

    /**
     * Displays an alert to confirm deletion of the selected class group.
     * If confirmed, it calls the deleteClassGroup function from ServerRequests.
     * 
     * @param {Object} item - The selected class group to delete.
     */
    const handleDelete = (item) => {
        Alert.alert(
            "Confirmar eliminación",
            `¿Estás seguro de que deseas eliminar la clase "${item.name}"?`,
            [
                {
                    text: "Cancelar",
                    style: "cancel"
                },
                {
                    text: "Eliminar",
                    onPress: async () => {
                        try {
                            const response = await ServerRequests.deleteClassGroup(item.id);
                            if (!response.ok) {
                                Alert.alert('Error en la comunicación con el servidor 1.');
                                refreshClasses();
                                return;
                            }
                            refreshClasses();
                        } catch (error) {
                            console.log('Error: ', error);
                            Alert.alert('Error en la comunicación con el servidor.');
                        }
                    },
                    style: "destructive"
                }
            ]
        );
    };

    /**
     * Refreshes the class groups by fetching them from the server.
     * Sets the refreshing state to show the refresh control.
     */
    const refreshClasses = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

    /**
     * Fetches class groups from the server and updates the state.
     * Displays an empty message if no classes are found.
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
        }
    };

    /**
     * Updates a class group in the list with the provided updated class.
     * 
     * @param {Object} updatedClass - The class group object with updated data.
     */
    const updateClassInList = (updatedClass) => {
        setClasses(classes.map(cls => (cls.id === updatedClass.id ? updatedClass : cls)));
    };

    /**
     * Renders a single class group item in the list.
     * 
     * @param {Object} param0 - Contains the item to be rendered.
     * @returns {JSX.Element} - The rendered class group item.
     */
    const renderItem = ({ item }) => (
        <View style={styles.classItem}>
            <View style={styles.classDetails}>
                <Text style={styles.className}>{item.name.substring(0, 12)}</Text>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => handleDelete(item)} style={styles.iconButton}>
                        <Ionicons name="trash-outline" size={20} color="red" />
                    </TouchableOpacity>
                }
            </View>

            <Text style={[styles.infoClass, { marginTop: 10 }]}>Estudiantes: {item.students.length}</Text>
            <View style={styles.schedules}>
                {item.schedules.map(day => (
                    <Text style={styles.infoClass} key={day.id}>{Schedule.reverseTranslate(day.weekDay) + ':'
                        + day.init.substring(0, 5)}</Text>
                ))}
            </View>

            <View style={styles.iconContainer}>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => handleEdit(item)} style={styles.iconButton}>
                        <Text style={styles.iconButtonText}>Editar</Text>
                        <Ionicons name="pencil-outline" size={20} color="#1162BF" />
                    </TouchableOpacity>
                }
                <TouchableOpacity onPress={() => handleViewMore(item.id)} style={styles.iconButton}>
                    <Text style={styles.viewMoreText}>Ver más</Text>
                </TouchableOpacity>
            </View>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Lista de clases</Text>
                {editAndDelete &&
                    <TouchableOpacity onPress={() => navigation.navigate('NewClassGroup')}>
                        <Ionicons name="add-outline" size={30} color='#1162BF' />
                    </TouchableOpacity>
                }
            </View>
            {classes.length > 0 ? (
                <View style={styles.content}>
                    <FlatList
                        data={classes}
                        renderItem={renderItem}
                        keyExtractor={(item) => item.id.toString()}
                        refreshControl={
                            <RefreshControl
                                refreshing={refreshing}
                                onRefresh={refreshClasses}
                            />
                        }
                        style={styles.alignTop}
                    />
                </View>
            ) : (
                <View style={styles.emptyContainer}>
                    <Text style={styles.emptyMessage}>{emptyMessage}</Text>
                </View>
            )}
        </View>
    );
};

export default ClassListSelector;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    iconButton: {
        flexDirection: 'row',
        alignItems: 'center',
        marginLeft: 10,
    },
    iconButtonText: {
        marginRight: 5,
        fontSize: 16,
        color: '#1162BF',
    },
    iconButtonTextDelete: {
        color: 'red',
    },
    content: {
        flex: 1,
    },
    alignTop: {
        marginTop: 10,
    },
    classItem: {
        backgroundColor: '#ddeeff',
        padding: 20,
        marginVertical: 8,
        borderRadius: 10,
        marginLeft: 20,
        marginRight: 20,
    },
    classDetails: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
    },
    iconContainer: {
        flexDirection: 'row',
        justifyContent: 'space-around',
        paddingTop: 10,
        marginTop: 10,
        borderTopWidth: 1,
        borderColor: '#1162BF'
    },
    className: {
        fontSize: 20,
        fontWeight: 'bold',
    },
    classSchedule: {
        fontSize: 16,
        color: '#555',
    },
    infoClass: {
        fontSize: 14,
        color: '#777',
    },
    schedules: {
        marginTop: 10
    },
    viewMoreButton: {
        marginTop: 10,
        alignItems: 'flex-end',
    },
    viewMoreText: {
        color: '#1162BF',
        fontSize: 16,
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

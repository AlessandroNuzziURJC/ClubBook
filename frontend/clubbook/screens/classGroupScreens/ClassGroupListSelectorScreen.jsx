import React, { useState, useEffect, useCallback } from "react";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert, RefreshControl } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation, useRoute } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroup from "../../entities/ClassGroup";
import Schedule from "../../entities/Schedule";

const ClassListSelector = () => {
    const [classes, setClasses] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();
    const route = useRoute();
    const { editAndDelete } = route.params;

    useEffect(() => {
        getFromServer();
    }, []);

    useEffect(() => {
        if (route.params?.classGroup) {
            if (classes.some(cls => cls.id === route.params.classGroup.id)) {
                updateClassInList(route.params.classGroup);
            } else {
                setClasses([...classes, route.params.classGroup]);
            }
        }
    }, [route.params?.classGroup]);

    const handleEdit = (item) => {
        navigation.navigate('EditClassGroup', {
            item
        });
    };

    const handleViewMore = (item) => {
        navigation.navigate('ClassGroupInfo', {
            item,
            editAndDelete
        });
    };

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

    const refreshClasses = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

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
        }
    };

    const updateClassInList = (updatedClass) => {
        setClasses(classes.map(cls => (cls.id === updatedClass.id ? updatedClass : cls)));
    };

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
                <TouchableOpacity onPress={() => handleViewMore(item)} style={styles.iconButton}>
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
});

import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert, RefreshControl } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation, useFocusEffect } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";
import ClassGroup from "../../entities/ClassGroup";

const ClassListSelector = () => {
    const [classes, setClasses] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();

    useEffect(() => {
        getFromServer();
    }, []);

    useFocusEffect(
        React.useCallback(() => {
            refreshClasses();
        }, [])
    );

    const handleEdit = (item) => {
        console.log('Editar', item.name);
    };

    const handleDelete =  (item) => {
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
                            console.log(item)
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

    const handleViewMore = (item) => {
        console.log('Ver más', item.name);
    };

    const refreshClasses = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 2000);
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

    const renderItem = ({ item }) => (
        <View style={styles.classItem}>
            <View style={styles.classDetails}>
                <Text style={styles.className}>{item.name}</Text>
                <View style={styles.iconContainer}>
                    <TouchableOpacity onPress={() => handleEdit(item)} style={styles.iconButton}>
                        <Ionicons name="pencil-outline" size={20} color="#1162BF" />
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => handleDelete(item)} style={styles.iconButton}>
                        <Ionicons name="trash-outline" size={20} color="red" />
                    </TouchableOpacity>
                </View>
            </View>
            <Text style={styles.classSchedule}>{item.schedule}</Text>
            <Text style={styles.classStudentCount}>Estudiantes: {item.students.length}</Text>
            <TouchableOpacity onPress={() => handleViewMore(item)} style={styles.viewMoreButton}>
                <Text style={styles.viewMoreText}>Ver más</Text>
            </TouchableOpacity>
        </View>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>Lista de clases</Text>
                    <TouchableOpacity onPress={() => navigation.navigate('NewClass')} style={styles.iconButtonAdd}>
                        <Ionicons name="add-outline" size={30} color='#1162BF' />
                    </TouchableOpacity>
                </View>
            </View>
            <View style={styles.content}>
                <FlatList
                    data={classes}
                    renderItem={renderItem}
                    
                    refreshControl={
                        <RefreshControl
                            refreshing={refreshing}
                            onRefresh={refreshClasses}
                        />
                    }
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
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20,
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
    },
    iconButtonAdd: {

    },
    iconButton: {
        padding: 5,
    },
    content: {
        flex: 1,
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
    },
    className: {
        fontSize: 20,
        fontWeight: 'bold',
    },
    classSchedule: {
        fontSize: 16,
        color: '#555',
    },
    classStudentCount: {
        fontSize: 14,
        color: '#777',
        marginTop: 5,
    },
    viewMoreButton: {
        marginTop: 10,
        alignItems: 'flex-end',
    },
    viewMoreText: {
        color: '#1162BF',
        fontSize: 16,
        textDecorationLine: 'underline',
    },
});

import React, { useState } from "react";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, Alert, RefreshControl } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import { useNavigation } from "@react-navigation/native";

const initialClasses = [
    { id: '1', name: 'Costa 1', schedule: 'Lunes y Miércoles 10:00-11:30', students: ['Alice', 'Bob', 'Charlie'] },
    { id: '2', name: 'Costa 2', schedule: 'Martes y Jueves 09:00-10:30', students: ['David', 'Eve', 'Frank'] },
    { id: '3', name: 'Quevedo 1', schedule: 'Lunes y Miércoles 12:00-13:30', students: ['Grace', 'Heidi', 'Ivan'] },
    { id: '4', name: 'Quevedo 2', schedule: 'Martes y Jueves 11:00-12:30', students: ['Jack', 'Kathy', 'Liam'] },
    { id: '5', name: 'Quevedo 3', schedule: 'Viernes 10:00-12:00', students: ['Mona', 'Nancy', 'Oscar'] },
];

const ClassListSelector = () => {
    const [classes, setClasses] = useState(initialClasses);
    const [refreshing, setRefreshing] = useState(false);
    const navigation = useNavigation();

    const handleEdit = (item) => {
        console.log('Editar', item.name);
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
                    onPress: () => console.log('Eliminar', item.name),
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
            setClasses(initialClasses); 
            setRefreshing(false);
        }, 2000);
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
                    keyExtractor={item => item.id}
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

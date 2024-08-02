import React, { useState, useRef, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Alert, Animated, ScrollView } from "react-native";
import { useRoute, useNavigation } from '@react-navigation/native';
import UsersFlatListNotPaged from "../../components/UsersFlatListNotPaged";
import Schedule from "../../entities/Schedule";
import { Ionicons } from "@expo/vector-icons";
import ServerRequests from "../../serverRequests/ServerRequests";

const ClassInfo = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(item);
    const [isContentExpanded, setIsContentExpanded] = useState(false); // Inicialmente cerrado
    const animatedHeight = useRef(new Animated.Value(0)).current;

    // Function to toggle content expansion
    const toggleContent = () => {
        const toValue = isContentExpanded ? 0 : 1;
        Animated.timing(animatedHeight, {
            toValue,
            duration: 300,
            useNativeDriver: false,
        }).start();
        setIsContentExpanded(!isContentExpanded);
    };

    // Update the height of the animated view based on isContentExpanded state
    useEffect(() => {
        Animated.timing(animatedHeight, {
            toValue: isContentExpanded ? 1 : 0,
            duration: 300,
            useNativeDriver: false,
        }).start();
    }, [isContentExpanded]);

    // Calculate the height of the content based on its content size
    const contentHeight = animatedHeight.interpolate({
        inputRange: [0, 1],
        outputRange: [180, Math.min(40 + 40 * (classGroup.teachers.length + classGroup.schedules.length + 2), 600)]
    });

    // Handle edit action
    const handleEdit = (item) => {
        navigation.navigate('EditClassGroup', { item });
    };

    // Handle delete action
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
                                Alert.alert('Error en la comunicación con el servidor.');
                                return;
                            }
                            navigation.goBack();
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

    // Handle add student action
    const handleAddStudent = (item) => {
        navigation.navigate('ClassGroupAddStudent', { item });
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>{classGroup.name.substring(0, 18)}</Text>
                    <View style={styles.iconButtonsContainer}>
                        <TouchableOpacity onPress={() => handleEdit(classGroup)} style={styles.iconButton}>

                            <Ionicons name="pencil-outline" size={20} color="#1162BF" />
                        </TouchableOpacity>
                        <TouchableOpacity onPress={() => handleDelete(classGroup)} style={styles.iconButton}>

                            <Ionicons name="trash-outline" size={20} color="red" />
                        </TouchableOpacity>
                    </View>
                </View>
            </View>
            <Animated.View style={[styles.content, { height: contentHeight }]}>
                <View style={styles.generalInfo}>
                    <View style={styles.toggleContainer}>
                        <Text style={styles.toggleTitle}>Información general</Text>
                        <TouchableOpacity onPress={toggleContent} style={styles.toggleButton}>
                            <Ionicons name={isContentExpanded ? "chevron-up-outline" : "chevron-down-outline"} size={20} color="#1162BF" />
                        </TouchableOpacity>
                    </View>
                    <ScrollView>
                        <View style={styles.row}>
                            <Text style={styles.label}>Dirección:</Text>
                            <Text style={styles.text}>{classGroup.address}</Text>
                        </View>
                        {classGroup.teachers.map((item, index) => (
                            <View key={index} style={styles.row}>
                                <Text style={styles.label}>Profesor:</Text>
                                <Text style={styles.text}>{item.firstName} {item.lastName}</Text>
                            </View>
                        ))}
                        <View style={styles.row}>
                            <Text style={styles.label}>Horario:</Text>
                            {classGroup.schedules.length > 0 && (
                                <Text style={styles.text}>{Schedule.reverseTranslate(classGroup.schedules[0].weekDay)}: {classGroup.schedules[0].init.substring(0, 5)} - {new Schedule(classGroup.schedules[0]).calculateEndTime()}</Text>
                            )}
                        </View>
                        {classGroup.schedules.slice(1).map((item, index) => {
                            const scheduleItem = new Schedule(item);
                            return (
                                <View key={index} style={styles.row}>
                                    <Text style={styles.text}>{Schedule.reverseTranslate(scheduleItem.weekDay)}: {scheduleItem.init.substring(0, 5)} - {scheduleItem.calculateEndTime()}</Text>
                                </View>
                            )
                        })}
                    </ScrollView>
                </View>
            </Animated.View>
            <View style={styles.studentsListContainer}>
                <View style={styles.studentsListHeader}>
                    <Text style={styles.labelStudentsList}>Lista de alumnos</Text>
                    <TouchableOpacity onPress={() => handleAddStudent(classGroup)} style={styles.addButton}>
                        <Text style={styles.addButtonText}>Añadir alumnos</Text>
                        <Ionicons name="add-circle-outline" size={20} color="#1162BF" />
                    </TouchableOpacity>
                </View>
                <UsersFlatListNotPaged users={classGroup.students} />
            </View>
        </View>
    );
};

export default ClassInfo;

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
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginTop: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    iconButtonsContainer: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    iconButton: {
        flexDirection: 'row',
        alignItems: 'center',
        marginLeft: 10,
        padding: 5,
    },
    iconButtonTextEdit: {
        marginRight: 5,
        fontSize: 16,
        color: '#1162BF'
    },
    iconButtonTextDelete: {
        marginRight: 5,
        fontSize: 16,
        color: 'red'
    },
    content: {
        overflow: 'hidden',
        paddingBottom: 20,
        borderRadius:10
    },
    generalInfo: {
        backgroundColor: '#ddeeff',
        borderRadius: 10,
        paddingTop: 20,
        paddingRight: 20,
        paddingLeft: 20,
        paddingBottom: 20
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 20
    },
    label: {
        fontWeight: '500',
        fontSize: 16,
        marginRight: 10,
        color: '#1162BF'
    },
    text: {
        flex: 1,
        textAlign: 'right',
        fontSize: 16,
    },
    toggleContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        marginBottom: 20
    },
    toggleTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        color: '#1162BF',
    },
    toggleButton: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    studentsListContainer: {
        flex: 1,
        marginTop: 20,
    },
    studentsListHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: 20,
    },
    labelStudentsList: {
        fontWeight: 'bold',
        fontSize: 16,
        marginRight: 10,
    },
    addButton: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    addButtonText: {
        fontSize: 16,
        color: '#1162BF',
        marginRight: 5,
    }
});

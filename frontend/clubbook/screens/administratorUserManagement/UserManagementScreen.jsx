import React, { useState } from "react";
import { View, StyleSheet, TouchableOpacity, Text, ScrollView, Modal, Alert, Linking, ActivityIndicator } from "react-native";
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from '@expo/vector-icons';
import * as DocumentPicker from 'expo-document-picker';
import * as FileSystem from 'expo-file-system';
import Papa from 'papaparse';
import ServerRequest from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";
import NewUserDto from "../../dto/RegisterUserDto";

const UserManagementScreen = () => {
    const navigation = useNavigation();
    const [isModalVisible, setIsModalVisible] = useState(false);
    const [role, setRole] = useState(null);
    const [isResultModalVisible, setIsResultModalVisible] = useState(false);
    const [registeredUsers, setRegisteredUsers] = useState([]);
    const [isLoading, setIsLoading] = useState(false);

    const link = {
        teacher: 'https://forms.gle/Nwt46KJjedRc9mpK9',
        student: 'https://forms.gle/c1cSETNn1U19fW99A'
    };

    const openModal = (roleName) => {
        setRole(roleName); // Guardar el rol
        setIsModalVisible(true);
    };

    const closeModal = () => {
        setIsModalVisible(false);
    };

    const openLink = async () => {
        const supported = await Linking.canOpenURL(link[role]);
        if (supported) {
            await Linking.openURL(link[role]);
        } else {
            Alert.alert("No se puede abrir el enlace");
        }
    };

    const addSingleUser = () => {
        if (role) {
            closeModal();
            navigation.navigate('NewUserFormScreen', { role });
        }
    };

    const addCsvFile = async () => {
        try {
            const res = await DocumentPicker.getDocumentAsync({
                type: 'text/csv',
            });

            if (!res.canceled && res.assets.length > 0) {
                setIsLoading(true);
                const file = res.assets[0];
                const fileUri = file.uri;

                const cacheUri = `${FileSystem.cacheDirectory}${file.name}`;
                await FileSystem.copyAsync({
                    from: fileUri,
                    to: cacheUri,
                });

                const fileContent = await FileSystem.readAsStringAsync(cacheUri);

                Papa.parse(fileContent, {
                    header: true,
                    complete: async (results) => {
                        const listUsers = results.data.map(row => Object.values(row));
                        const userResults = [];

                        for (const item of listUsers) {
                            try {
                                const newUser = new NewUserDto(item[2], item[3], item[1], item[4], Functions.convertCSVDate(item[5]), role, item[6], item[7], item[8] === 'Sí');
                                const response = await ServerRequest.signUpUser(newUser);
                                const result = await response.json();

                                if (response.ok) {
                                    userResults.push({ name: item[2] + ' ' + item[3], status: 'success' });
                                } else {
                                    userResults.push({ name: item[2] + ' ' + item[3], status: 'error' });
                                }
                            } catch (error) {
                                userResults.push({ name: item[2] + ' ' + item[3], status: 'error' });
                            }
                        }

                        setRegisteredUsers(userResults);
                        setIsLoading(false);
                        closeModal();
                        setIsResultModalVisible(true);
                    },
                    error: (error) => {
                        closeModal();
                        Alert.alert("Error", "Error al procesar el archivo CSV");
                    }
                });
            } else {
                closeModal();
            }
        } catch (err) {
            Alert.alert("Error", "No se pudo abrir el archivo");
            closeModal();
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Gestionar usuarios</Text>
            </View>
            <ScrollView bounces={false}>
                <View style={styles.header}>
                    <Text style={styles.pageSubtitle}>Añadir nuevos usuarios</Text>
                </View>
                <View style={styles.content}>
                    <TouchableOpacity onPress={() => navigation.navigate('NewUserFormScreen', { role: 'administrator' })} style={[styles.button, styles.add]}>
                        <Ionicons name="add" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Administrador</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => openModal('teacher')} style={[styles.button, styles.add]}>
                        <Ionicons name="add" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Profesor</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => openModal('student')} style={[styles.button, styles.add]}>
                        <Ionicons name="add" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Alumno</Text>
                    </TouchableOpacity>
                </View>
                <View style={styles.header}>
                    <Text style={styles.pageSubtitle}>Eliminar usuarios</Text>
                </View>
                <View style={styles.content}>
                    <TouchableOpacity onPress={() => navigation.navigate('AdministratorListScreen')} style={[styles.button, styles.delete]}>
                        <Ionicons name="trash-outline" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Administrador</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => navigation.navigate('DeleteUsersListScreen', { role: 'teacher' })} style={[styles.button, styles.delete]}>
                        <Ionicons name="trash-outline" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Profesor</Text>
                    </TouchableOpacity>
                    <TouchableOpacity onPress={() => navigation.navigate('DeleteUsersListScreen', { role: 'student' })} style={[styles.button, styles.delete]}>
                        <Ionicons name="trash-outline" size={20} color="#fff" style={styles.icon} />
                        <Text style={styles.buttonText}>Alumno</Text>
                    </TouchableOpacity>
                </View>
            </ScrollView>
            <Modal
                visible={isModalVisible}
                animationType="slide"
                transparent={true}
                onRequestClose={closeModal}
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        {isLoading ? (
                            <View style={styles.loadingContainer}>
                                <ActivityIndicator size="large" color="#1162BF" style={styles.loadingIcon} />
                                <Text style={styles.loadingText}>Procesando archivo...</Text>
                            </View>
                        ) : (
                            <View>
                                <Text style={styles.modalTitle}>Añadir usuario</Text>
                                <View style={styles.linkContainer}>
                                    <Ionicons name="link" size={20} color="#1162BF" style={styles.linkIcon} />
                                    <TouchableOpacity onPress={openLink} style={styles.linkTextContainer}>
                                        <Text style={styles.linkText}>{link[role]}</Text>
                                    </TouchableOpacity>
                                </View>
                                <View style={styles.buttonsContainer}>
                                    <TouchableOpacity onPress={addSingleUser} style={styles.squareButton}>
                                        <Ionicons name="add" size={30} color="#1162BF" style={styles.squareIcon} />
                                        <Text style={styles.squareButtonText}>Añadir usuario</Text>
                                    </TouchableOpacity>
                                    <TouchableOpacity onPress={addCsvFile} style={styles.squareButton}>
                                        <Ionicons name="attach" size={30} color="#1162BF" style={styles.squareIcon} />
                                        <Text style={styles.squareButtonText}>CSV</Text>
                                    </TouchableOpacity>
                                </View>
                                <TouchableOpacity onPress={closeModal} style={styles.closeButton}>
                                    <Text style={styles.closeButtonText}>Cerrar</Text>
                                </TouchableOpacity>
                            </View>
                        )}
                    </View>
                </View>
            </Modal>

            <Modal
                visible={isResultModalVisible}
                animationType="slide"
                transparent={true}
                onRequestClose={() => setIsResultModalVisible(false)}
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Resultados del registro</Text>
                        <ScrollView style={styles.resultList}>
                            {registeredUsers.map((user, index) => (
                                <View
                                    key={index}
                                    style={[
                                        styles.resultItem,
                                        user.status === 'success'
                                            ? styles.successItem
                                            : styles.errorItem,
                                    ]}
                                >
                                    <Ionicons
                                        name={user.status === 'success' ? 'checkmark-circle' : 'alert-circle'}
                                        size={20}
                                        color={user.status === 'success' ? '#28a745' : '#dc3545'}
                                        style={styles.resultIcon}
                                    />
                                    <Text style={styles.resultText}>{user.name}</Text>
                                </View>
                            ))}
                        </ScrollView>
                        <TouchableOpacity onPress={() => setIsResultModalVisible(false)} style={styles.closeButton}>
                            <Text style={styles.closeButtonText}>Cerrar</Text>
                        </TouchableOpacity>
                    </View>
                </View>
            </Modal>
        </View>
    );
};

export default UserManagementScreen;

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
    pageSubtitle: {
        fontSize: 18,
        color: '#696969',
        fontWeight: 'bold',
    },
    content: {
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        flex: 1,
    },
    button: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        borderRadius: 10,
        paddingVertical: 15,
        paddingHorizontal: 40,
        marginVertical: 10,
        width: '80%',
        backgroundColor: '#28a745', // Verde
        elevation: 2, // Sombra
    },
    buttonText: {
        color: '#fff', // Texto blanco
        fontWeight: 'bold',
        fontSize: 16,
        flex: 1,
        textAlign: 'center',
    },
    icon: {
        position: 'absolute',
        left: 20,
    },
    add: {
        backgroundColor: '#28a745',
    },
    delete: {
        backgroundColor: '#dc3545', // Rojo
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        maxHeight: '80%',
        width: '80%',
        backgroundColor: '#fff',
        borderRadius: 10,
        padding: 20,
        alignItems: 'center',
        shadowColor: '#000',
        shadowOffset: {
            width: 0,
            height: 2,
        },
        shadowOpacity: 0.25,
        shadowRadius: 4,
        elevation: 5,
    },
    modalTitle: {
        fontSize: 20,
        fontWeight: 'bold',
        marginBottom: 20,
    },
    linkContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        width: '100%',
        marginBottom: 20,
        overflow: 'hidden', // Evita que el enlace desborde
    },
    linkTextContainer: {
        flex: 1, // Permitir que el texto ocupe el espacio restante
        marginLeft: 10, // Espacio entre el ícono y el texto
    },
    linkText: {
        fontSize: 16,
        color: '#1162BF',
        flexWrap: 'wrap',
    },
    linkIcon: {
        color: '#1162BF',
    },
    buttonsContainer: {
        flexDirection: 'row',
        width: '100%',
        marginBottom: 20,
        justifyContent: 'space-between'
    },
    squareButton: {
        width: '48%',
        backgroundColor: '#e0e0e0',
        borderRadius: 5,
        paddingVertical: 15,
        alignItems: 'center',
        flexDirection: 'column',
        justifyContent: 'center',
        height: 100
    },
    squareButtonText: {
        color: '#1162BF',
        fontSize: 16,
        padding: 5,
        marginTop: 10
    },
    squareIcon: {
    },
    closeButton: {
        marginTop: 20,
        paddingVertical: 10,
        paddingHorizontal: 20,
        backgroundColor: '#1162BF',
        borderRadius: 5,
        alignItems: 'center'
    },
    closeButtonText: {
        color: 'white',
        fontSize: 16,
    },
    resultList: {
        width: '100%',
        marginVertical: 20,
    },
    resultItem: {
        flexDirection: 'row',
        alignItems: 'center',
        padding: 10,
        marginBottom: 10,
        borderRadius: 5,
    },
    successItem: {
        backgroundColor: '#d4edda',
    },
    errorItem: {
        backgroundColor: '#f8d7da',
    },
    resultIcon: {
        marginRight: 10,
    },
    resultText: {
        fontSize: 16,
        color: '#333',
    },
    loadingContainer: {
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
    },
    loadingIcon: {
        marginBottom: 20,
        animation: 'spin 2s linear infinite'
    },
    loadingText: {
        fontSize: 16,
        color: '#1162BF',
    },
});

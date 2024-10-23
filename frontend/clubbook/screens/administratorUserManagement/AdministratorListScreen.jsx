import React, { useState, useCallback } from "react";
import { useFocusEffect } from "@react-navigation/native";
import { View, Text, StyleSheet, TouchableOpacity, FlatList, RefreshControl, Alert, Image, TextInput } from "react-native";
import { Ionicons } from "@expo/vector-icons";
import ServerRequests from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";

const AdministratorListScreen = () => {
    const [administrators, setAdministrators] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const [userImages, setUserImages] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [filteredAdministrators, setFilteredAdministrators] = useState([]);

    const getFromServer = async () => {
        const response = await ServerRequests.getAllAdministrators();
        const result = await response.json();
        if (response.ok) {
            setAdministrators(result.data);
            setFilteredAdministrators(result.data);
            setUserImages(Array(result.data.length).fill(require('../../assets/loading.gif')));
            result.data.forEach((user, index) => {
                getProfilePicture(user.id, index);
            });
        } else {
            Alert.alert('Error en la comunicación con el servidor.');
        }
    };

    const getProfilePicture = async (id, index) => {
        const response = await ServerRequests.getUserPhoto(id);
        if (response.ok) {
            const blob = await response.blob();
            const image = await Functions.blobToBase64(blob);
            if (image != null) {
                setUserImages((prevImages) => {
                    const newImages = [...prevImages];
                    newImages[index] = { uri: image };
                    return newImages;
                });
            }
        } else {
            setUserImages((prevImages) => {
                const newImages = [...prevImages];
                newImages[index] = require('../../assets/error.png');
                return newImages;
            });
        }
    };

    const refreshData = () => {
        setRefreshing(true);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

    const confirmDelete = (adminId) => {
        Alert.alert(
            "Confirmación",
            "¿Estás seguro de que deseas eliminar a este administrador?",
            [
                { text: "Cancelar", style: "cancel" },
                { text: "Eliminar", onPress: () => handleDelete(adminId), style: "destructive" }
            ]
        );
    };

    const handleDelete = async (adminId) => {
        const response = await ServerRequests.deleteUser(adminId);
        const result = await response.json();
        if (response.ok & result.data) {
            refreshData();
        } else {
            Alert.alert("Error al eliminar el usuario.");
        }
    };

    useFocusEffect(
        useCallback(() => {
            setUserImages([]);
            setAdministrators([]);
            refreshData();
        }, [])
    );

    const handleSearch = (text) => {
        setSearchTerm(text);
        const filtered = administrators.filter(admin =>
            admin.firstName.toLowerCase().includes(text.toLowerCase()) ||
            admin.lastName.toLowerCase().includes(text.toLowerCase())
        );
        setFilteredAdministrators(filtered);
    };

    const renderItem = ({ item, index }) => {
        return (
            <View style={styles.card}>
                <View style={styles.row}>
                    <Image
                        source={userImages[index]}
                        style={styles.image}
                    />
                    <Text style={styles.adminName}>{item.firstName + ' ' + item.lastName}</Text>
                    <TouchableOpacity onPress={() => confirmDelete(item.id)} style={styles.icon}>
                        <Ionicons name="trash-outline" size={20} color="red" />
                    </TouchableOpacity>
                </View>
            </View>
        );
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Eliminar administradores</Text>
            </View>
            <TextInput
                style={styles.searchBar}
                placeholder="Buscar administrador por nombre..."
                value={searchTerm}
                onChangeText={handleSearch}
            />
            <FlatList
                data={filteredAdministrators} // Usamos la lista filtrada
                renderItem={renderItem}
                keyExtractor={(item) => item.id.toString()}
                refreshControl={<RefreshControl refreshing={refreshing} onRefresh={refreshData} />}
                style={styles.alignTop}
            />
        </View>
    );
};

export default AdministratorListScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'flex-end',
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    searchBar: {
        borderColor: '#ccc',
        borderWidth: 1,
        borderRadius: 8,
        padding: 10,
        marginBottom: 20,
        fontSize: 16,
    },
    alignTop: {
        marginTop: 10,
    },
    adminName: {
        fontSize: 16,
        fontWeight: 'bold',
        flex: 1,
    },
    card: {
        backgroundColor: '#ddeeff',
        borderRadius: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 10,
        paddingRight: 20,
        marginBottom: 10,
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 50,
    },
    icon: {
        alignSelf: 'center',
        marginLeft: 10
    },
    image: {
        width: 50,
        height: 50,
        borderRadius: 100,
        marginRight: 20,
        alignSelf: 'center',
    }
});

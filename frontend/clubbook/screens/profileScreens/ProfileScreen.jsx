import React, { useState, useEffect } from "react";
import { View, Alert, StyleSheet } from "react-native";
import Profile from "../../components/Profile";
import AsyncStorage from '@react-native-async-storage/async-storage';
import Functions from "../../functions/Functions";
import ServerRequest from "../../serverRequests/ServerRequests";

const ProfileScreen = () => {
    const [refreshing, setRefreshing] = useState(false);
    const [profilePicture, setProfilePicture] = useState(null);
    const [user, setUser] = useState({
        email: '',
        id: '',
        firstName: '',
        lastName: '',
        phoneNumber: '',
        birthday: '',
        address: '',
        idCard: '',
        partner: ''
    });

    const updateScreenData = (result) => {
        const userData = {
            email: result.email,
            id: result.id.toString(),
            firstName: result.firstName,
            lastName: result.lastName,
            phoneNumber: result.phoneNumber,
            birthday: result.birthday,
            address: result.address,
            idCard: result.idCard,
            partner: result.partner
        };
        setUser(userData);
    };

    const saveInAsyncStorage = async (result) => {
        await AsyncStorage.setItem('id', result.id.toString());
        await AsyncStorage.setItem('firstName', result.firstName);
        await AsyncStorage.setItem('lastName', result.lastName);
        await AsyncStorage.setItem('phoneNumber', result.phoneNumber);
        await AsyncStorage.setItem('birthday', result.birthday);
        await AsyncStorage.setItem('address', result.address);
        await AsyncStorage.setItem('idCard', result.idCard);
        await AsyncStorage.setItem('partner', result.partner.toString());
    };

    const getFromServer = async () => {
        try {
            const data = await ServerRequest.getTokenAndId();

            const response = await ServerRequest.getUserData(data);

            if (response.ok) {
                const result = await response.json();
                updateScreenData(result);
                saveInAsyncStorage(result);
            } else {
                Alert.alert('Error', 'Error al cargar los datos.');
            }

            const responseImage = await ServerRequest.getUserPhoto(data.id);

            if (responseImage.ok) {
                const blob = await responseImage.blob();
                const base64 = await Functions.blobToBase64(blob);
                setProfilePicture({ uri: base64});
            } else {
                setProfilePicture(require('../../assets/error.png'));
                Alert.alert('Error', 'Error al cargar la imagen del perfil');
            }
        } catch (error) {
            Alert.alert('Ha habido un error en la comunicación: ', error.message);
        }
    };

    const onRefresh = () => {
        setRefreshing(true);
        setTimeout(() => {
            setRefreshing(false);
        }, 2000);
        getFromServer();
    };

    useEffect(() => {
        getFromServer();
    }, []);

    return (
        <View style={styles.container}>
            <Profile 
                editButton={true}
                refreshing={refreshing}
                onRefresh={onRefresh}
                profilePicture={profilePicture}
                user={user}
            />
        </View>
    );
};

export default ProfileScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    },
});

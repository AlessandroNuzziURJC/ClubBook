import React, { useState, useEffect, useCallback } from "react";
import { View, Alert, StyleSheet } from "react-native";
import Profile from "../../components/Profile";
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useNavigation, useFocusEffect } from '@react-navigation/native';
import Functions from "../../functions/Functions";
import ServerRequest from "../../serverRequests/ServerRequests";

/**
 * ProfileScreen component displays the user's profile information.
 * It allows the user to refresh their profile data and navigate to the profile edit screen.
 *
 * @returns {JSX.Element} The ProfileScreen component.
 */
const ProfileScreen = () => {
    const navigation = useNavigation();
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

    /**
     * Updates the user state with data fetched from the server.
     * 
     * @param {Object} result - The user data retrieved from the server.
     */
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

    /**
     * Saves user data into AsyncStorage for persistence.
     * 
     * @param {Object} result - The user data to be saved.
     */
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

    /**
     * Fetches user data and profile picture from the server.
     * Handles errors and updates the screen state accordingly.
     */
    const getFromServer = async () => {
        try {
            const response = await ServerRequest.getUserData();
            if (response.ok) {
                const result = await response.json();
                updateScreenData(result.data);
                saveInAsyncStorage(result.data);
            } else {
                Alert.alert('Error', 'Error al cargar los datos.');
            }

            const data = await ServerRequest.getTokenAndId();
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

    /**
     * Refreshes the profile data when called.
     */
    const onRefresh = () => {
        setRefreshing(true);
        setTimeout(() => {
            setRefreshing(false);
        }, 500);
        getFromServer();
    };

    
    useEffect(() => {
        getFromServer();
    }, []);


    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    /**
     * Navigates to the ProfileEdit screen with the user data as parameters.
     */
    const handleEditProfile = () => {
        navigation.navigate('ProfileEdit', { 'user': user });
    };

    return (
        <View style={styles.container}>
            <Profile 
                editButton={true}
                onEditPress={handleEditProfile}
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

import React, { useState, useEffect } from "react";
import { View, StyleSheet, Alert } from "react-native";
import Profile from "../../components/Profile";
import { useRoute } from '@react-navigation/native';
import ServerRequest from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";

/**
 * ProfileScreen component displays the user's profile information and allows
 * for refreshing the profile picture.
 *
 * @component
 * @returns {JSX.Element} The ProfileScreen component.
 */
const ProfileScreen = () => {
    const [refreshing, setRefreshing] = useState(false);
    const [profilePicture, setProfilePicture] = useState(null);
    const route = useRoute();
    const [userdata, setUser] = useState(route.params.item);

    /**
     * Fetches the user's profile picture from the server and updates the state.
     * If there's an error while fetching, it displays an alert message.
     *
     * @async
     * @function getFromServer
     * @returns {Promise<void>} A promise that resolves when the fetch operation is complete.
     */
    const getFromServer = async () => {
        try {

            const responseImage = await ServerRequest.getUserPhoto(userdata.id);

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
     * Handles the refresh action, setting the refreshing state to true 
     * and calling getFromServer to update the profile picture.
     * 
     * @function onRefresh
     * @returns {void}
     */
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
                editButton={false}
                onEditPress={null}
                refreshing={refreshing}
                onRefresh={onRefresh}
                profilePicture={profilePicture}
                user={userdata} />
        </View>
    );
}

export default ProfileScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
    }
});
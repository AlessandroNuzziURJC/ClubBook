import React from 'react';
import { TouchableOpacity, View, Text, StyleSheet, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import ServerRequest from '../serverRequests/ServerRequests';

/**
 * CloseSessionButton Component.
 * 
 * Displays a button that allows the user to log out of the application. 
 * When pressed, it shows a confirmation alert. If confirmed, it logs the user out 
 * and navigates back to the login screen.
 * 
 * @component
 * @param {Object} props - Component properties.
 * @param {boolean} props.visible - Determines if the button should be visible.
 * @returns {JSX.Element|null} - A logout button or `null` if `visible` is `false`.
 */
const CloseSessionButton = ({ visible }) => {
    const navigation = useNavigation();

    if (!visible) return null;

    /**
     * Displays a confirmation alert for logging out.
     * If the user confirms, it sends a logout request to the server 
     * and navigates to the login screen.
     */
    const logout = async () => {
        Alert.alert(
            'Cerrar sesión',
            '¿Estás seguro que quieres cerrar sesión?',
            [
                {
                    text: 'Cancelar',
                    style: 'cancel'
                },
                {
                    text: 'Cerrar sesión',
                    onPress: async () => {
                        await ServerRequest.requestLogout();
                        navigation.navigate('LogIn');
                    }
                }
            ],
            { cancelable: false }
        );
    }

    return (
        <View style={styles.logoutContainer} visible={visible}>
            <TouchableOpacity onPress={logout}>
                <Text style={styles.logout}>Cerrar sesión</Text>
            </TouchableOpacity>
        </View>
    );
};

const styles = StyleSheet.create({
    logoutContainer: {
        alignItems: 'center',
        marginTop: 30,
        marginBottom: 40
    },
    logout: {
        color: 'red',
        fontSize: 16,
        fontWeight: 'bold'
    }
});

export default CloseSessionButton;

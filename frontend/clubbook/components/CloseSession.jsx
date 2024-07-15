import React from 'react';
import { TouchableOpacity, View, Text, StyleSheet, Alert } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import ServerRequest from '../serverRequests/ServerRequests';

const CloseSessionButton = ({ visible }) => {
    const navigation = useNavigation();

    if (!visible) return null;

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

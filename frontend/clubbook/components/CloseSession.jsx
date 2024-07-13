import React from 'react';
import { TouchableOpacity, View, Text, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';

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
                        await requestLogout();
                        navigation.navigate('LogIn');
                    }
                }
            ],
            { cancelable: false }
        );
    }

    const requestLogout = async () => {
        const data = {
            token: await AsyncStorage.getItem("userToken"),
            id: await AsyncStorage.getItem("id")
        }
        await fetch(`${Configuration.API_URL}/auth/logout`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${data.token}`,
            }
        });
        await AsyncStorage.removeItem('id');
        await AsyncStorage.removeItem('userToken');
        await AsyncStorage.removeItem('firstName');
        await AsyncStorage.removeItem('lastName');
        await AsyncStorage.removeItem('phoneNumber');
        await AsyncStorage.removeItem('birthday');
        await AsyncStorage.removeItem('address');
        await AsyncStorage.removeItem('idCard');
        await AsyncStorage.removeItem('partner');
    };

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

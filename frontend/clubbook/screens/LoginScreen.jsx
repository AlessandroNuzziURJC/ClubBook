import React, { useState } from 'react';
import { View, Text, TextInput, StyleSheet, Button, Image, Alert } from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useNavigation } from '@react-navigation/native';
import ServerRequests from '../serverRequests/ServerRequests';

import Toast from '../components/Toast';

export default function LogIn() {
    const [email, onChangeEmail] = React.useState('');
    const [password, onChangePassword] = React.useState('');
    const [isSubmitting, setIsSubmitting] = React.useState(false);
    const [isValidEmail, setIsValidEmail] = React.useState(true);
    const [showNotificationAlert, setShowNotificationAlert] = useState(false);
    const navigation = useNavigation();

    const emailRegex = /\S+@\S+\.\S+/;

    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    const handleSubmit = async () => {
        if (!emailRegex.test(email)) {
            setIsValidEmail(false);
            return;
        }

        setIsValidEmail(true);
        setIsSubmitting(true);

        try {
            const response = await ServerRequests.logIn(email, password);

            if (response.ok) {
                const result = await response.json();
                await AsyncStorage.setItem('userToken', result.data.token);
                await AsyncStorage.setItem('email', email);
                await AsyncStorage.setItem('userPassword', password);
                await AsyncStorage.setItem('id', result.data.user.id.toString());
                await AsyncStorage.setItem('firstName', result.data.user.firstName);
                await AsyncStorage.setItem('lastName', result.data.user.lastName);
                await AsyncStorage.setItem('phoneNumber', result.data.user.phoneNumber);
                await AsyncStorage.setItem('birthday', result.data.user.birthday);
                await AsyncStorage.setItem('role', result.data.user.role.name);

                const role = result.data.user.role.name;
                setToastMessage(result.message);
                showToast();

                
                //Generar token unico unica vez y almacenarlo en AsynStorage
                //Contrastar si el usuario consta de ese token en la BBDD
                //ServerRequests.checkNotificationToken(user, token);
                //Lo que devuelva guardarlo en showNotificationAlert
                setShowNotificationAlert(true);

                // Crear ventanita que pregunte si desea recibir notificaciones de este usuario en este dispositivo si no está registrado ya en la BBDD

                switch (role) {
                    case 'ADMINISTRATOR':
                        navigation.navigate('AdministratorMainScreen');
                        break;
                    case 'STUDENT':
                        navigation.navigate('StudentMainScreen');
                        break;
                    case 'TEACHER':
                        navigation.navigate('TeacherMainScreen');
                        break;
                    default:
                        break;
                }
            }

            if (response.status === 401) {
                Alert.alert('Error', 'El usuario o contraseña introducidos no son correctos.');
            }

            if (response.status === 403) {
                Alert.alert('Error', 'No estás autorizado para hacer esta operación.');
            }

            if (response.status === 500) {
                Alert.alert('Error', 'Error interno del servidor.');
            }

        } catch (error) {
            Alert.alert('Error', 'Se ha producido un error');
            console.error('Error:', error);
        } finally {
            setIsSubmitting(false);
        }
    };

    const sendNotificationTokenToServer = () => {
        /* Enviar token y usuario al servidor */
        console.log("Notificaciones activadas.")
    }

    return (
        <KeyboardAwareScrollView style={styles.container}>
            <View>
                <Text style={styles.welcome}>¡Bienvenido!</Text>
                <Image source={require('./images/ClubBook_logo.png')}
                    style={styles.logo}
                />
                <Text style={styles.name}>ClubBook</Text>
            </View>
            <View style={styles.containerForm}>
                <Text style={styles.label}>Correo electrónico</Text>
                <TextInput
                    style={[styles.input, !isValidEmail && styles.inputError]}
                    onChangeText={onChangeEmail}
                    value={email}
                    placeholder="Introduce tu correo electrónico"
                    keyboardType="email-address"
                />
                <Text style={styles.label}>Contraseña</Text>
                <TextInput
                    style={styles.input}
                    onChangeText={onChangePassword}
                    value={password}
                    placeholder="Introduce tu contraseña"
                    secureTextEntry
                />
                <Text style={styles.passwordForgotten}>Haz clic aqui para reestablecer la contraseña</Text>
                <View>
                    <Button
                        title="Iniciar sesión"
                        color="#1162BF"
                        accessibilityLabel="Inicio de sesión"
                        onPress={handleSubmit}
                        disabled={isSubmitting}
                    />
                </View>
                <Toast
                    visible={isToastVisible}
                    message={toastMessage}
                    onClose={() => setIsToastVisible(false)}
                />
            </View>
            {showNotificationAlert &&
                Alert.alert(
                    "Activar notificaciones",
                    "¿Deseas recibir notificaciones de este usuario en este dispositivo?",
                    [
                        {
                            text: "No",
                            onPress: () => null,
                            style: "cancel"
                        },
                        { text: "Sí", onPress: () => sendNotificationTokenToServer() }
                    ],
                    { cancelable: false }
                )}
        </KeyboardAwareScrollView>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff'
    },
    welcome: {
        marginTop: 80,
        fontSize: 32,
        fontWeight: 'bold',
        alignSelf: 'center'
    },
    name: {
        fontSize: 32,
        fontWeight: 'bold',
        alignSelf: 'center',
        marginBottom: 40
    },
    logo: {
        marginTop: 60,
        marginBottom: 10,
        height: 200,
        width: 200,
        alignSelf: 'center'
    },
    containerForm: {
        flex: 1,
        backgroundColor: '#fff',
        padding: 20,
        justifyContent: 'center',
    },
    label: {
        marginBottom: 8,
        fontSize: 16,
        fontWeight: 'bold',
    },
    input: {
        height: 40,
        borderWidth: 1,
        borderColor: '#ccc',
        padding: 10,
        marginBottom: 20,
        borderRadius: 5,
        width: '100%',
    },
    passwordForgotten: {
        fontSize: 12,
        marginTop: -10,
        alignSelf: 'center',
        marginBottom: 20
    },
    inputError: {
        borderColor: 'red',
    }
});

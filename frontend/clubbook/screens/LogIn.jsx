import React from 'react';
import { View, Text, TextInput, StyleSheet, Button, Image, Alert } from 'react-native';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';
import Configuration from './config/Configuration'
import AsyncStorage from '@react-native-async-storage/async-storage';
import { useNavigation } from '@react-navigation/native';


export default function LogIn() {
    const [email, onChangeEmail] = React.useState('');
    const [password, onChangePassword] = React.useState('');
    const [isSubmitting, setIsSubmitting] = React.useState(false);
    const [isValidEmail, setIsValidEmail] = React.useState(true);
    const navigation = useNavigation();

    const emailRegex = /\S+@\S+\.\S+/;

    const handleSubmit = async () => {
        if (!emailRegex.test(email)) {
            setIsValidEmail(false);
            return ;
        }

        setIsValidEmail(true);
        setIsSubmitting(true);

        try {
            const response = await fetch(`${Configuration.API_URL}/auth/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ email, password }),
            });

            if (response.ok) {
                const result = await response.json();
                await AsyncStorage.setItem('userToken', result.token);
                await AsyncStorage.setItem('email', email);
                await AsyncStorage.setItem('userPassword', password);
                await AsyncStorage.setItem('id', result.user.id.toString());
                await AsyncStorage.setItem('firstName', result.user.firstName);
                await AsyncStorage.setItem('lastName', result.user.lastName);
                await AsyncStorage.setItem('phoneNumber', result.user.phoneNumber);
                await AsyncStorage.setItem('birthday', result.user.birthday);

                const role = result.user.role.name;

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

                // Do something with the result if needed
                //Alert.alert('Información', 'El usuario es: ' + await AsyncStorage.getItem('userName'));
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
            </View>
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

import React, { useState } from "react";
import { View, Text, StyleSheet, TextInput, TouchableOpacity, Alert } from "react-native";
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { useNavigation } from "@react-navigation/native";
import { useRoute } from '@react-navigation/native';
import Checkbox from 'expo-checkbox';
import Functions from "../../functions/Functions";
import Toast from '../../components/Toast';
import FormFooter from "../../components/FormFooter";
import NewUserDto from "../../dto/RegisterUserDto";
import ServerRequest from "../../serverRequests/ServerRequests";

/**
 * NewUserFormScreen is a React component for creating a new user.
 * It handles user input for different roles (administrator, teacher, student) 
 * and validates the input before sending it to the server.
 */
const NewUserFormScreen = () => {
    const navigation = useNavigation();
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [idCard, setIdCard] = useState('');
    const [birthday, setBirthday] = useState(null);
    const [isDatePickerVisible, setDatePickerVisibility] = useState(false);
    const [isPartner, setIsPartner] = useState(false);
    const route = useRoute();
    const { role } = route.params;

    const [firstNameError, setFirstNameError] = useState(false);
    const [lastNameError, setLastNameError] = useState(false);
    const [emailError, setEmailError] = useState(false);
    const [phoneNumberError, setPhoneError] = useState(false);
    const [idCardError, setIdCardError] = useState(false);
    const [addressError, setAddressError] = useState(false);
    const [birthdayError, setBirthdayError] = useState(false);

    /**
     * Returns the title based on the user role.
     * @returns {string} The title for the form.
     */
    const getTitle = () => {
        switch (role) {
            case 'administrator':
                return 'Nuevo administrador';
            case 'teacher':
                return 'Nuevo profesor';
            case 'student':
                return 'Nuevo alumno';
            default:
                return 'Nuevo usuario';
        }
    };

    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    /**
     * Shows a toast message for feedback.
     */
    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    /**
     * Checks if the user is over 18 based on the given date.
     * @param {Date} date - The date to check.
     * @returns {boolean} True if over 18, false otherwise.
     */
    const isOver18 = (date) => {
        const today = new Date();
        const birthDate = new Date(date);
        const age = today.getFullYear() - birthDate.getFullYear();
        const monthDifference = today.getMonth() - birthDate.getMonth();

        if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
            return age - 1;
        }

        return age >= 18;
    };

    /**
     * Handles the date confirmation from the date picker.
     * @param {Date} date - The selected date.
     */
    const handleConfirm = (date) => {
        const today = new Date();

        if (date > today) {
            Alert.alert("Error", "La fecha de nacimiento no puede ser en el futuro.");
            setDatePickerVisibility(false);
            return;
        }

        if (role === 'student') {
            setBirthday(Functions.convertDateEngToSpa(date));
        } else {
            if (isOver18(date)) {
                setBirthday(Functions.convertDateEngToSpa(date));
            } else {
                Alert.alert("Error", "Debe ser mayor de 18 años para ser administrador.");
            }
        }
        setDatePickerVisibility(false);
    };

    /**
     * Hides the date picker modal.
     */
    const hideDatePicker = () => {
        setDatePickerVisibility(false);
    };

    /**
     * Validates the form data before submission.
     * @returns {boolean} True if all fields are valid, false otherwise.
     */
    const validateFormData = () => {
        let valid = true;

        const nameRegEx = /^[a-zA-ZñÑáéíóúÁÉÍÓÚ ]+$/;
        if (!nameRegEx.test(firstName)) {
            setFirstNameError(true);
            valid = false;
        } else {
            setFirstNameError(false);
        }

        if (!nameRegEx.test(lastName)) {
            setLastNameError(true);
            valid = false;
        } else {
            setLastNameError(false);
        }

        const emailRegEx = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegEx.test(email)) {
            setEmailError(true);
            valid = false;
        } else {
            setEmailError(false);
        }

        const phoneRegEx = /^[0-9]{9}$/;
        if (!phoneRegEx.test(phoneNumber)) {
            setPhoneError(true);
            valid = false;
        } else {
            setPhoneError(false);
        }

        const idCardRegEx = /^[0-9A-Z]+$/;
        if (!idCardRegEx.test(idCard)) {
            setIdCardError(true);
            valid = false;
        } else {
            setIdCardError(false);
        }

        if (!address) {
            setAddressError(true);
            valid = false;
        } else {
            setAddressError(false);
        }

        if (!birthday) {
            setBirthdayError(true);
            valid = false;
        } else {
            setBirthdayError(false);
        }

        return valid;
    };

    /**
     * Handles the save action for creating a new user.
     */
    const handleSave = async () => {
        if (validateFormData()) {
            const newUser = new NewUserDto(firstName, lastName, email, phoneNumber, Functions.convertDateSpaToEng(birthday), role, address, idCard, isPartner);
            const response = await ServerRequest.signUpUser(newUser);
            const result = await response.json();
            if (result.message) {
                setToastMessage(result.message);
            } else {
                setToastMessage('Error en el registro');
            }
            showToast();
            if (response.ok) {
                navigation.goBack();
            }
        } else {
            Alert.alert("Error", "Rellena el formulario correctamente.");
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                {
                    <Text style={styles.pageTitle}>{getTitle()}</Text>
                }
            </View>
            <KeyboardAwareScrollView contentContainerStyle={styles.scrollViewContent} bounces={false}>
                <View style={styles.separator}>
                    <Text style={styles.label}>Nombre:</Text>
                    <TextInput
                        style={[styles.input, firstNameError && styles.errorInput]}
                        placeholder="Nombre"
                        value={firstName}
                        onChangeText={setFirstName}
                    />
                    <Text style={styles.label}>Apellidos:</Text>
                    <TextInput
                        style={[styles.input, lastNameError && styles.errorInput]}
                        placeholder="Apellidos"
                        value={lastName}
                        onChangeText={setLastName}
                    />
                    <Text style={styles.label}>Correo electrónico:</Text>
                    <TextInput
                        style={[styles.input, emailError && styles.errorInput]}
                        placeholder="Correo electrónico"
                        keyboardType="email-address"
                        value={email}
                        onChangeText={setEmail}
                    />
                    <Text style={styles.label}>Número de teléfono:</Text>
                    <TextInput
                        style={[styles.input, phoneNumberError && styles.errorInput]}
                        placeholder="Número de teléfono"
                        keyboardType="phone-pad"
                        value={phoneNumber}
                        onChangeText={setPhoneNumber}
                    />
                    <Text style={styles.label}>Dirección:</Text>
                    <TextInput
                        style={[styles.input, addressError && styles.errorInput]}
                        placeholder="Dirección"
                        value={address}
                        onChangeText={setAddress}
                    />
                    <Text style={styles.label}>DNI/NIE:</Text>
                    <TextInput
                        style={[styles.input, idCardError && styles.errorInput]}
                        placeholder="DNI/NIE"
                        value={idCard}
                        onChangeText={setIdCard}
                    />
                </View>
                <View style={styles.separator}>
                    <View style={styles.checkboxContainer}>
                        <Text style={styles.label}>Socio:</Text>
                        <Checkbox
                            value={isPartner}
                            onValueChange={setIsPartner}
                            style={styles.checkbox}
                        />
                    </View>
                    <View style={styles.checkboxContainer}>
                        <Text style={[styles.label, { paddingTop: 10 }]}>Fecha de nacimiento:</Text>
                        <TouchableOpacity onPress={() => setDatePickerVisibility(true)} style={[styles.timeButton, birthdayError && styles.errorInput]}>
                            <Text style={[styles.timeButtonText, birthdayError && styles.textError]}>{birthday || "Fecha nac."}</Text>
                        </TouchableOpacity>
                    </View>
                </View>

            </KeyboardAwareScrollView>
            <View>
                <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: handleSave, text: 'Guardar' }} />
            </View>
            <DateTimePickerModal
                isVisible={isDatePickerVisible}
                mode="date"
                onConfirm={handleConfirm}
                onCancel={hideDatePicker}
            />
            <Toast
                visible={isToastVisible}
                message={toastMessage}
                onClose={() => setIsToastVisible(false)}
            />
        </View>
    );
};

export default NewUserFormScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    scrollViewContent: {
        paddingLeft: 20,
        paddingRight: 20
    },
    label: {
        fontWeight: '500',
        fontSize: 18,
        marginBottom: 10,
    },
    input: {
        backgroundColor: 'white',
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        marginBottom: 20,
        borderWidth: 1,
        borderColor: 'gray',
    },
    errorInput: {
        borderColor: 'red',
        borderWidth: 1,
        backgroundColor: 'white'
    },
    textError: {
        color: 'red'
    },
    timeButton: {
        backgroundColor: '#1162BF',
        height: 40,
        borderRadius: 10,
        padding: 10,
        fontSize: 16,
        alignItems: 'center',
        justifyContent: 'center',
    },
    timeButtonText: {
        fontWeight: 'bold',
        color: 'white',
    },
    checkboxContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        alignContent: 'center',
        verticalAlign: 'center',
    },
    checkbox: {
        marginLeft: 10,
        marginBottom: 10,
    },
    separator: {
        backgroundColor: '#f0eded',
        borderRadius: 10,
        paddingLeft: 10,
        paddingRight: 10,
        paddingTop: 20,
        paddingBottom: 20,
        marginBottom: 20,
    }
});

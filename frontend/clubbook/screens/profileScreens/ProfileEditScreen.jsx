import React, { useState, useEffect } from "react";
import { ScrollView, View, Text, StyleSheet, Image, TextInput, Alert, TouchableOpacity } from "react-native";
import Configuration from '../../config/Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { useNavigation } from '@react-navigation/native';
import { useRoute } from '@react-navigation/native';
import FormFooter from "../../components/FormFooter";

const EditProfile = () => {
    const [profilePicture, setProfilePicture] = useState(null);
    const navigation = useNavigation();
    const route = useRoute();
    const { user: initialUser } = route.params;
    const [user, setUser] = useState(initialUser);

    const blobToBase64 = (blob) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onerror = reject;
            reader.onload = () => {
                resolve(reader.result);
            };
            reader.readAsDataURL(blob);
        });
    };

    const checkNotEmpty = () => {
        return Object.values(user).every(value => String(value).trim() !== '');
    }

    const dataValid = () => {
        const phoneRegex = /^[0-9]+$/;
        const dniNieRegex = /^[0-9A-Z]+$/;
        const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
        const nameRegex = /^[a-zA-Z\s]+$/;

        if (!phoneRegex.test(user.phoneNumber)) {
            Alert.alert("Formato de número de teléfono inválido. Debe tener 10 dígitos.");
            return false;
        }
        if (!dniNieRegex.test(user.idCard)) {
            Alert.alert("Formato de DNI/NIE inválido.");
            return false;
        }
        if (!dateRegex.test(user.birthday)) {
            Alert.alert("Formato de fecha de nacimiento inválido. Debe ser YYYY-MM-DD.");
            return false;
        }
        if (!nameRegex.test(user.firstName) || user.firstName.trim() === '') {
            Alert.alert("Formato de nombre inválido. Solo debe contener letras y espacios.");
            return false;
        }
        if (!nameRegex.test(user.lastName) || user.lastName.trim() === '') {
            Alert.alert("Formato de apellidos inválido. Solo debe contener letras y espacios.");
            return false;
        }

        return true;
    }

    const sendUpdates = async () => {
        const empty = checkNotEmpty();
        const valid = dataValid();
        if (!empty) {
            Alert.alert("No dejes campos vacíos en el formulario.");
            return;
        }

        if (!valid) {
            return;
        }

        const data = {
            token: await AsyncStorage.getItem("userToken"),
            id: await AsyncStorage.getItem("id")
        }
        await fetch(`${Configuration.API_URL}/${data.id}/updateUser`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${data.token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(user),
        });
        saveInAsyncStorage(user);
        navigation.goBack();

    }

    const getUserPhoto = async (data) => {
        return await fetch(`${Configuration.API_URL}/${data.id}/profilePicture`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${data.token}`,
            }
        });
    }

    const updateScreenData = async (result) => {
        const userData = {
            email: result.email,
            id: result.id.toString(),
            firstName: result.firstName,
            lastName: result.lastName,
            phoneNumber: result.phoneNumber,
            birthday: result.birthday,
            address: result.address,
            idCard: result.idCard,
            partner: result.partner.toString()
        };
        setUser(userData);
    }

    const saveInAsyncStorage = async (result) => {
        await AsyncStorage.setItem('firstName', result.firstName);
        await AsyncStorage.setItem('lastName', result.lastName);
        await AsyncStorage.setItem('phoneNumber', result.phoneNumber);
        await AsyncStorage.setItem('birthday', result.birthday);
        await AsyncStorage.setItem('address', result.address);
        await AsyncStorage.setItem('idCard', result.idCard);
        await AsyncStorage.setItem('partner', result.partner);
    }

    const getOldData = async () => {
        try {
            const data = {
                token: await AsyncStorage.getItem("userToken"),
                id: await AsyncStorage.getItem("id")
            }

            const userData = {
                email: await AsyncStorage.getItem('email'),
                id: await AsyncStorage.getItem('id'),
                firstName: await AsyncStorage.getItem('firstName'),
                lastName: await AsyncStorage.getItem('lastName'),
                phoneNumber: await AsyncStorage.getItem('phoneNumber'),
                birthday: await AsyncStorage.getItem('birthday'),
                address: await AsyncStorage.getItem('address'),
                idCard: await AsyncStorage.getItem('idCard'),
                partner: Boolean(await AsyncStorage.getItem('partner'))
            }

            updateScreenData(userData);

            const responseImage = await getUserPhoto(data);

            if (responseImage.ok) {
                const blob = await responseImage.blob();
                const base64 = await blobToBase64(blob);
                setProfilePicture(base64);
            } else {
                setProfilePicture(require('../../assets/error.png'));
                Alert.alert('Error', 'Error al cargar la imagen del perfil');
            }
        } catch (error) {
            Alert.alert('Ha habido un error en la comunicación: ', error);
        }
    };

    useEffect(() => {
        getOldData();
    }, []);

    const handleInputChange = (field, value) => {
        setUser({
            ...user,
            [field]: value
        });
    }

    const [selectedDate, setSelectedDate] = useState(null);
    const [datePickerVisible, setDatePickerVisible] = useState(false);

    const showDatePicker = () => {
        setDatePickerVisible(true);
    };

    const hideDatePicker = () => {
        setDatePickerVisible(false);
    };

    const handleConfirm = (date) => {
        setSelectedDate(date);
        hideDatePicker();
        var month = date.getMonth() + 1;
        if (month.toString().length == 1) {
            month = '0' + month;
        }

        var day = date.getDate();
        if (day.toString().length == 1) {
            day = '0' + day;
        }
        const dateValid = date.getFullYear() + '-' + month + '-' + day;
        handleInputChange("birthday", dateValid);
    };


    return (
        <View style={styles.container}>
            <ScrollView contentContainerStyle={styles.scrollViewContent}>
                <View style={styles.contentContainer}>
                    <View style={styles.header}>
                        <View style={styles.columnContainer}>
                            <View style={styles.subheader}>
                                <Text style={styles.pageTitle}>Editar perfil</Text>
                            </View>
                            {user.partner && (
                                <View style={styles.partnerContainer}>
                                    <Text style={styles.partner}>Socio</Text>
                                </View>
                            )}
                        </View>
                        <Image
                            source={profilePicture ? { uri: profilePicture } : require('../../assets/loading.gif')}
                            style={styles.image}
                        />
                    </View>
                    <View style={styles.infoContainer}>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Nombre:</Text>
                            <View style={styles.dataContainer}>
                                <TextInput
                                    style={styles.data}
                                    value={user.firstName}
                                    onChangeText={(text) => {
                                        handleInputChange('firstName', text);
                                    }}
                                    placeholder={'Nombre'}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Apellidos:</Text>
                            <View style={styles.dataContainer}>
                                <TextInput
                                    style={styles.data}
                                    value={user.lastName}
                                    onChangeText={(text) => {
                                        handleInputChange('lastName', text);
                                    }}
                                    placeholder={'Apellidos'}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>DNI/NIE:</Text>
                            <View style={styles.dataContainer}>
                                <TextInput
                                    style={styles.data}
                                    value={user.idCard}
                                    onChangeText={(text) => {
                                        handleInputChange('idCard', text);
                                    }}
                                    placeholder={'DNI/NIE'}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Dirección:</Text>
                            <View style={styles.dataContainer}>
                                <TextInput
                                    style={styles.data}
                                    value={user.address}
                                    onChangeText={(text) => {
                                        handleInputChange('address', text);
                                    }}
                                    placeholder={'Dirección'}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Número de teléfono:</Text>
                            <View style={styles.dataContainer}>
                                <TextInput
                                    style={styles.data}
                                    value={user.phoneNumber}
                                    onChangeText={(text) => {
                                        handleInputChange('phoneNumber', text);
                                    }}
                                    placeholder={'Número de teléfono'}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Email:</Text>
                            <View style={styles.dataContainerNotMod}>
                                <Text style={styles.data}>{user.email}</Text>
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Fecha de nacimiento:</Text>
                            <View style={styles.dataContainer}>
                                <TouchableOpacity onPress={showDatePicker}>
                                    <Text>{user.birthday}</Text>
                                </TouchableOpacity>
                                <DateTimePickerModal
                                    value={selectedDate}
                                    isVisible={datePickerVisible}
                                    mode="date"
                                    onConfirm={handleConfirm}
                                    onCancel={hideDatePicker}
                                />
                            </View>
                        </View>
                        <View style={styles.labelDataContainer}>
                            <Text style={styles.label}>Número de licencia:</Text>
                            <View style={styles.dataContainerNotMod}>
                                <Text style={styles.data}>4R-123123123</Text>
                            </View>
                        </View>
                    </View>
                </View>
            </ScrollView>
            <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: sendUpdates, text: 'Guardar' }} />
        </View>
    );
}

export default EditProfile;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    scrollViewContent: {
        paddingLeft: 20,
        paddingRight: 20
    },
    icon: {
        width: 20,
        height: 20
    },
    image: {
        width: 124,
        height: 124,
        justifyContent: 'flex-end',
        borderRadius: 100
    },
    header: {
        width: '100%',
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20
    },
    columnContainer: {
        flexDirection: 'column',
        flex: 1,
        paddingRight: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    editContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingBottom: 3,
    },
    edit: {
        alignSelf: 'flex-end',
        textDecorationLine: 'underline',
        fontWeight: '300',
        color: '#1162BF'
    },
    partnerContainer: {
        justifyContent: 'center',
        marginBottom: 0 // Modificación: Eliminado el marginBottom
    },
    partner: {
        fontSize: 18,
        fontWeight: '600'
    },
    infoContainer: {
        marginBottom: 0 // Modificación: Eliminado el marginBottom
    },
    labelDataContainer: {
        marginBottom: 15
    },
    label: {
        fontWeight: '500',
        fontSize: 18,
        marginBottom: 10
    },
    dataContainer: {
        backgroundColor: 'lightgray',
        padding: 10,
        borderRadius: 10,
    },
    data: {
        fontSize: 16,
    },
});

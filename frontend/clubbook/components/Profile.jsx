import React, { useState, useEffect } from "react";
import { ScrollView, View, Text, StyleSheet, Image, TouchableOpacity, Alert, RefreshControl } from "react-native";
import Configuration from '../screens/config/Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

const Profile = () => {
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

    const getUserFromAsyncStorage = async () => {
        try {
            const userToken = await AsyncStorage.getItem("userToken");
            const userId = await AsyncStorage.getItem("id");
            const response = await fetch(`${Configuration.API_URL}/me/${userId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${userToken}`,
                }
            });

            if (response.ok) {
                const result = await response.json();
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
                await AsyncStorage.setItem('id', result.id.toString());
                await AsyncStorage.setItem('firstName', result.firstName);
                await AsyncStorage.setItem('lastName', result.lastName);
                await AsyncStorage.setItem('phoneNumber', result.phoneNumber);
                await AsyncStorage.setItem('birthday', result.birthday);
                await AsyncStorage.setItem('address', result.address);
                await AsyncStorage.setItem('idCard', result.idCard);
            } else {
                Alert.alert('Error', 'Ha habido un error en la comunicación.');
            }
        } catch (error) {
            Alert.alert('Ha habido un error en la comunicación: ', error);
        }
    };

    const getUserProfile = async () => {
        try {
            const userToken = await AsyncStorage.getItem("userToken");
            const userId = await AsyncStorage.getItem("id");
            const response = await fetch(`${Configuration.API_URL}/${userId}/profilePicture`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${userToken}`,
                }
            });

            if (response.ok) {
                const blob = await response.blob();
                setProfilePicture(blob);
            } else {
                setProfilePicture(require('../assets/error.png'));
                Alert.alert('Error', 'Error al cargar la imagen del perfil');
            }
        } catch (error) {
            setProfilePicture(require('../assets/error.png'));
            Alert.alert('Error', 'Ha ocurrido un error al intentar cargar la imagen del perfil');
        }
    }


    const onRefresh = React.useCallback(() => {
        setRefreshing(true);
        setTimeout(() => {
            setRefreshing(false);
        }, 2000);
        getUserFromAsyncStorage();
    }, []);

    useEffect(() => {
        getUserFromAsyncStorage();
        getUserProfile();
    }, []);

    return (
        <ScrollView style={styles.container} refreshControl={
            <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }>
            <View>
                <View style={styles.header}>
                    <View style={styles.columnContainer}>
                        <View style={styles.subheader}>
                            <Text style={styles.pageTitle}>Perfil</Text>
                            <TouchableOpacity style={styles.editContainer}>
                                <Image
                                    source={require('../assets/edit_icon.png')}
                                    style={styles.icon}
                                />
                                <Text style={styles.edit}>Editar usuario</Text>
                            </TouchableOpacity>
                        </View>
                        {user.partner && (
                            <View style={styles.partnerContainer}>
                                <Text style={styles.partner}>Socio</Text>
                            </View>
                        )}
                    </View>
                    <Image
                        source={profilePicture ? { uri: URL.createObjectURL(profilePicture) } : require('../assets/loading.gif')}
                        style={styles.image}
                    />
                </View>
                <View style={styles.infoContainer}>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Nombre:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{`${user.firstName} ${user.lastName}`}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>DNI/NIE:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.idCard}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Dirección:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.address}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de teléfono:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.phoneNumber}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Email:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.email}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Fecha de nacimiento:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.birthday}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de licencia:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>4R-123123123</Text>
                        </View>
                    </View>
                </View>
            </View>
        </ScrollView>
    );
}

export default Profile;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        padding: 20
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
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        marginBottom: 10,
        marginTop: 20
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
    },
    partner: {
        fontSize: 18,
        fontWeight: '600'
    },
    infoContainer: {
        marginBottom: 20
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
        borderRadius: 10
    },
    data: {
        fontSize: 16,
    }
});

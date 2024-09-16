import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Modal, TextInput, Alert } from "react-native";
import AsyncStorage from '@react-native-async-storage/async-storage';
import ServerRequests from "../../serverRequests/ServerRequests";

const SeasonControlScreen = () => {
    const [seasonStatus, setSeasonStatus] = useState(false);
    const [startDate, setStartDate] = useState(null);
    const [showConfirm, setShowConfirm] = useState(false);
    const [confirmAction, setConfirmAction] = useState(null);
    const [email, setEmail] = useState('');
    const [error, setError] = useState('');

    const handleStart = () => {
        setConfirmAction('start');
        setShowConfirm(true);
        
    };

    const handleEnd = () => {
        setConfirmAction('end');
        setShowConfirm(true);
        
    };

    const getCurrentDate = () => {
        const today = new Date();
        const year = today.getFullYear();
        const month = String(today.getMonth() + 1).padStart(2, '0'); // Los meses en JavaScript son 0-indexados
        const day = String(today.getDate()).padStart(2, '0');
        return `${year}-${month}-${day}`;
    };

    const handleConfirm = async () => {
        const correctEmail = await AsyncStorage.getItem('email');
        if (email === correctEmail) {
            if (confirmAction === 'start') {
                setSeasonStatus(true);
                setStartDate(getCurrentDate());
                ServerRequests.seasonStart();
            } else if (confirmAction === 'end') {
                if (seasonStatus) {
                    setSeasonStatus(false);
                    ServerRequests.seasonFinish();
                }
            }
            setShowConfirm(false);
            setEmail('');
            setError('');
        } else {
            setError('Correo electrónico incorrecto.');
        }
    };

    const getFromServer = async () => {
        try {
            const response = await ServerRequests.seasonStarted();
            console.log(response);
            if (response.ok) {
                const data = await response.json();
                console.log(data);
                if (data.active) {
                    setSeasonStatus(true);
                    setStartDate(data.init);
                } else {
                    setSeasonStatus(false);
                    setStartDate(null);
                }
            } else if (response.status = 404 ){
                setSeasonStatus(false);
                setStartDate(null);

            }
        } catch (error) {
            console.log('Error:', error);
            Alert.alert("Error en el servidor.");
        }
    }

    useEffect(() => {
        getFromServer();
    }, []);

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Gestión de temporada</Text>
            </View>
            <View style={styles.infoSection}>
                <Text style={styles.description}>
                    Gestiona el inicio y la finalización de una temporada fácilmente. Si la temporada no está en curso, gran parte de la funcionalidad del sistema estará inaccesible para los usuarios profesores y alumnos.
                </Text>
            </View>
            <View>
                <TouchableOpacity
                    style={[styles.startButton, seasonStatus && styles.disabledButton]}
                    onPress={handleStart}
                    disabled={seasonStatus}
                >
                    <Text style={[styles.buttonText, styles.start]}>Iniciar temporada</Text>
                    {seasonStatus && startDate && (
                        <Text style={[styles.buttonSubText, styles.start]}>Temporada iniciada el: {startDate}</Text>
                    )}
                </TouchableOpacity>
                <TouchableOpacity
                    style={[styles.endButton, !seasonStatus && styles.disabledButton]}
                    onPress={handleEnd}
                    disabled={!seasonStatus}
                >
                    <Text style={[styles.buttonText, styles.end]}>Finalizar temporada</Text>
                </TouchableOpacity>
            </View>
            <Modal
                transparent={true}
                visible={showConfirm}
                animationType="slide"
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <Text style={styles.modalTitle}>Confirmación requerida</Text>
                        <TextInput
                            style={styles.input}
                            placeholder="Introduce tu correo electrónico"
                            value={email}
                            onChangeText={setEmail}
                        />
                        {error ? <Text style={styles.errorText}>{error}</Text> : null}
                        <View style={styles.buttonContainer}>
                            <TouchableOpacity style={styles.button} onPress={() => { setShowConfirm(false); setEmail(''); setError(''); }}>
                                <Text style={styles.buttonText}>Cancelar</Text>
                            </TouchableOpacity>
                            <TouchableOpacity style={styles.button} onPress={handleConfirm}>
                                <Text style={styles.buttonText}>Confirmar</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </View>
            </Modal>
        </View>
    );
}

export default SeasonControlScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#f5f5f5',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
        color: '#333',
    },
    infoSection: {
        marginBottom: 20,
    },
    description: {
        fontSize: 16,
        color: '#555',
        marginBottom: 10,
    },
    startButton: {
        borderColor: '#28a745',
        borderWidth: 1,
        paddingVertical: 12,
        paddingHorizontal: 24,
        borderRadius: 10,
        marginVertical: 10,
        alignItems: 'center',
        elevation: 3,
    },
    endButton: {
        borderColor: '#dc3545',
        borderWidth: 1,
        paddingVertical: 12,
        paddingHorizontal: 24,
        borderRadius: 10,
        marginVertical: 10,
        alignItems: 'center',
    },
    disabledButton: {
        backgroundColor: '#e0e0e0',
        borderColor: '#b0b0b0',
    },
    buttonText: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#1162BF',
    },
    buttonSubText: {
        fontSize: 14,
        fontWeight: 'bold',
        marginTop: 5,
    },
    start: {
        color: '#28a745',
    },
    end: {
        color: '#dc3545',
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        width: '80%',
        backgroundColor: '#fff',
        paddingLeft: 20,
        paddingRight: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
        marginBottom: 20,
        marginTop: 20
    },
    input: {
        width: '100%',
        borderWidth: 1,
        borderColor: 'grey',
        borderRadius: 5,
        padding: 10,
        marginBottom: 20,
    },
    errorText: {
        color: 'red',
        marginBottom: 10,
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
    },
    button: {
        flex: 1,
        paddingVertical: 12,
        borderRadius: 10,
        marginHorizontal: 5,
        alignItems: 'center',
        marginBottom: 10
    }
});

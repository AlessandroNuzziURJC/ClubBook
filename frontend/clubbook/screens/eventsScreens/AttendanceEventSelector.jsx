import React, { useState, useEffect } from "react";
import { View, Text, TouchableOpacity, StyleSheet, Alert } from "react-native";
import Toast from "../../components/Toast";
import ServerRequests from "../../serverRequests/ServerRequests";

const AttendanceEventSelector = ({ dataEvent }) => {
    const [attendanceEventId, setAttendanceEventId] = useState();
    const [attendance, setAttendance] = useState(null);
    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');

    const getFromServer = async () => {
        const response = await ServerRequests.getEventAttendanceByUserId(dataEvent.id);
        const result = await response.json();

        if (response.ok) {
            setAttendance(result.data.status);
            setAttendanceEventId(result.data.id);
        } else {
            Alert.alert("Error en la petición al cargar estado de asistencia");
        }
    }

    useEffect(() => {
        getFromServer();
    }, []);

    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    const handleAttendanceSelection = async (selection) => {
        const data = await ServerRequests.getTokenAndId();
        const output = {
            eventAttendanceId: attendanceEventId,
            eventId: dataEvent.id,
            userId: data.id,
            status: selection
        };

        const response = await ServerRequests.updateAttendance(output);
        const result = await response.json();
        
        setIsToastVisible(true);
        setToastMessage(result.message);
        showToast();
    };

    const handleSelection = (selection) => {
        Alert.alert(
            "Confirmar asistencia",
            `¿Estás seguro de cambiar tu asistencia a "${selection === true ? 'Sí' : selection === false ? 'No' : '?'}"?`,
            [
                {
                    text: "Cancelar",
                    style: "cancel",
                },
                {
                    text: "Confirmar",
                    onPress: () => {
                        setAttendance(selection);
                        handleAttendanceSelection(selection);
                    },
                },
            ]
        );
    };

    return (
        <View style={styles.container}>
            <Text style={styles.question}>Confirmar asistencia:</Text>
            <View style={styles.buttonContainer}>
                <TouchableOpacity
                    style={[
                        styles.button,
                        attendance === true && styles.selectedButton,
                    ]}
                    onPress={() => handleSelection(true)}
                >
                    <Text style={styles.buttonText}>Sí</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={[
                        styles.button,
                        attendance === null && styles.selectedButton,
                    ]}
                    onPress={() => handleSelection(null)}
                >
                    <Text style={styles.buttonText}>?</Text>
                </TouchableOpacity>
                <TouchableOpacity
                    style={[
                        styles.button,
                        attendance === false && styles.selectedButton,
                    ]}
                    onPress={() => handleSelection(false)}
                >
                    <Text style={styles.buttonText}>No</Text>
                </TouchableOpacity>
            </View>
            <Toast
                visible={isToastVisible}
                message={toastMessage}
                onClose={() => setIsToastVisible(false)}
            />
        </View>
    );
};

export default AttendanceEventSelector;

const styles = StyleSheet.create({
    container: {
        padding: 10,
        alignItems: "center",
    },
    question: {
        fontSize: 16,
        fontWeight: "bold",
        marginBottom: 15,
        color: "#333",
    },
    buttonContainer: {
        flexDirection: "row",
        justifyContent: "center", 
        width: "100%",
    },
    button: {
        backgroundColor: "#ccc",
        padding: 12,
        width: "28%",
        alignItems: "center",
    },
    selectedButton: {
        backgroundColor: "#1162BF",
    },
    buttonText: {
        color: "#fff",
        fontSize: 14,
        fontWeight: "bold",
        textAlign: "center",
        alignSelf: 'center'
    },
});

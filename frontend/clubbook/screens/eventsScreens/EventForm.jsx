import React, { useState, useEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import { View, Text, StyleSheet, TextInput, TouchableOpacity, Modal, FlatList, Alert } from "react-native";
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import FormFooter from "../../components/FormFooter";
import NewEventDto from "../../dto/NewEventDto";
import ServerRequests from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";
import Toast from "../../components/Toast";

const EventForm = ({ edit }) => {
    const navigation = useNavigation();
    const [event, setEvent] = useState(new NewEventDto());
    const [nameError, setNameError] = useState(false);
    const [isDatePickerVisible, setDatePickerVisibility] = useState(false);
    const [isTypePickerVisible, setTypePickerVisibility] = useState(false);
    const [selectedType, setSelectedType] = useState('');
    const [eventTypes, setEventTypes] = useState([]);
    const [initYear, setInitYear] = useState();
    const [endYear, setEndYear] = useState();
    const [errors, setErrors] = useState({
        title: false,
        address: false,
        date: false,
        type: false,
        birthYearStart: false,
        birthYearEnd: false,
    });
    
    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    const validateFields = () => {
        let isValid = true;
        const newErrors = { title: false, address: false, date: false, type: false, birthYearStart: false, birthYearEnd: false };

        // Validar el nombre
        if (!event.title) {
            newErrors.title = true;
            isValid = false;
        }

        // Validar la dirección
        if (!event.address) {
            newErrors.address = true;
            isValid = false;
        }

        // Validar la fecha
        if (!event.date || event.date <= new Date()) {
            newErrors.date = true;
            isValid = false;
        }

        // Validar tipo de evento
        if (!event.type) {
            newErrors.type = true;
            isValid = false;
        }

        if (!initYear) {
            newErrors.birthYearStart = true;
            isValid = false;
        }

        if (!endYear) {
            newErrors.birthYearEnd = true;
            isValid = false;
        }

        // Validar años de nacimiento
        if (initYear >= endYear) {
            newErrors.birthYearStart = true;
            newErrors.birthYearEnd = true;
            isValid = false;
        }

        setErrors(newErrors);
        return isValid;
    };

    const handleSave = async () => {
        if (validateFields()) {
            //console.log("Evento guardado:", event);
            const response = await ServerRequests.postNewEvent(event);
            const result = await response.json();
            const message = result.message;
            setToastMessage(message);
            showToast();
            setTimeout(() => {
                navigation.goBack();
            }, 1000);
        } else {
            Alert.alert("Error al rellenar el formulario. Revise los datos.");
        }
    };

    const showDatePicker = () => {
        setDatePickerVisibility(true);
    };

    const hideDatePicker = () => {
        setDatePickerVisibility(false);
    };

    const handleConfirm = (date) => {
        setEvent({ ...event, date: date });
        hideDatePicker();
    };

    const showTypePicker = () => {
        setTypePickerVisibility(true);
    };

    const hideTypePicker = () => {
        setTypePickerVisibility(false);
    };

    const handleTypeSelect = (type) => {
        setSelectedType(Functions.translateEventTypes(type.name));
        setEvent({ ...event, type: type.eventTypeId });
        hideTypePicker();
    };

    const getFromServer = async () => {
        const response = await ServerRequests.getEventTypes();
        if (response.ok) {
            const eventTypesJSON = await response.json();
            setEventTypes(eventTypesJSON);
        } else {
            Alert.alert('Error en al comunicación con el servidor');
            console.log('Error en la peticion de tipos de eventos.');
        }
    }

    useEffect(() => {
        getFromServer();
    }, []);

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{edit ? "Editar evento" : "Nuevo evento"}</Text>
            </View>
            <KeyboardAwareScrollView style={styles.content}
                extraScrollHeight={-100}
                enableOnAndroid={true}
                keyboardShouldPersistTaps="handled"
            >
                <View style={styles.inputMargin}>
                    <Text style={styles.label}>Nombre:</Text>
                    <TextInput
                        style={[styles.input, errors.title && styles.errorInput]}
                        placeholder="Nombre del evento"
                        value={event.title}
                        onChangeText={(text) => setEvent({ ...event, title: text })}
                    />
                </View>

                <View style={styles.inputMargin}>
                    <Text style={styles.label}>Dirección:</Text>
                    <TextInput
                        style={[styles.input, errors.address && styles.errorInput]}
                        placeholder="Dirección del evento"
                        value={event.address}
                        onChangeText={(text) => setEvent({ ...event, address: text })}
                    />
                </View>


                <View style={styles.inputMargin}>
                    <Text style={styles.label}>Fecha:</Text>
                    <TouchableOpacity onPress={showDatePicker} style={[styles.input, errors.date && styles.errorInput]}>
                        <Text>{event.date ? event.date.toLocaleDateString() : "Seleccionar fecha"}</Text>
                    </TouchableOpacity>
                    <DateTimePickerModal
                        isVisible={isDatePickerVisible}
                        mode="date"
                        onConfirm={handleConfirm}
                        onCancel={hideDatePicker}
                    />
                </View>

                <View style={styles.inputMargin}>
                    <Text style={styles.label}>Tipo de evento:</Text>
                    <TouchableOpacity onPress={showTypePicker} style={[styles.input, errors.type && styles.errorInput]}>
                        <Text>{selectedType ? selectedType : "Seleccionar tipo de evento"}</Text>
                    </TouchableOpacity>
                </View>

                {/* Modal para seleccionar tipo de evento */}
                <Modal visible={isTypePickerVisible} transparent={true} animationType="slide">
                    <View style={styles.modalContainer}>
                        <View style={styles.modalContent}>
                            <Text style={styles.modalTitle}>Seleccionar qué tipo de evento es:</Text>
                            <FlatList
                                data={eventTypes}
                                keyExtractor={(item) => item.eventTypeId}
                                renderItem={({ item }) => (
                                    <TouchableOpacity onPress={() => handleTypeSelect(item)}>
                                        <Text style={styles.modalItem}>{Functions.translateEventTypes(item.name)}</Text>
                                    </TouchableOpacity>
                                )}
                            />
                            <TouchableOpacity onPress={hideTypePicker} style={styles.modalCancelButton}>
                                <Text style={styles.modalCancel}>Cancelar</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                </Modal>

                <Text style={styles.label}>Información adicional:</Text>
                <TextInput
                    style={styles.multilineInput}
                    placeholder="Información adicional"
                    multiline={true}
                    numberOfLines={4}
                    value={event.additionalInfo}
                    onChangeText={(text) => setEvent({ ...event, additionalInfo: text })}
                />

                <Text style={styles.label}>Años de nacimiento límite para inscripciones (incluidos):</Text>

                <View style={styles.row}>
                    <TextInput
                        style={[styles.input, styles.smallInput, errors.birthYearStart && styles.errorInput]}
                        placeholder="Año inicio (menor)"
                        keyboardType="numeric"
                        value={initYear ? initYear.toString() : ''}
                        onChangeText={(text) => {
                            const year = parseInt(text);
                            setInitYear(year);
                            if (!isNaN(year) && text.length === 4) {
                                setEvent(prevEvent => ({
                                    ...prevEvent,
                                    birthYearStart: new Date(Date.UTC(year, 0, 1))
                                }));
                            }
                        }}
                    />
                    <Text style={styles.dash}>:</Text>
                    <TextInput
                        style={[styles.input, styles.smallInput, errors.birthYearEnd && styles.errorInput]}
                        placeholder="Año fin (mayor)"
                        keyboardType="numeric"
                        value={endYear ? endYear.toString() : ''}
                        onChangeText={(text) => {
                            const year = parseInt(text);
                            setEndYear(year);
                            if (!isNaN(year) && text.length === 4) {
                                setEvent(prevEvent => ({
                                    ...prevEvent,
                                    birthYearEnd: new Date(Date.UTC(year, 11, 31))
                                }));
                            }
                        }}
                    />
                </View>

            </KeyboardAwareScrollView>
            <View style={styles.footerContainer}>
                <FormFooter cancel={{ function: navigation.goBack, text: "Cancelar" }} save={{ function: handleSave, text: "Guardar" }} />
            </View>
            <Toast
                    visible={isToastVisible}
                    message={toastMessage}
                    onClose={() => setIsToastVisible(false)}
                />
        </View>
    );
};

export default EventForm;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        paddingTop: 20,
        backgroundColor: "white",
    },
    header: {
        justifyContent: "space-between",
        paddingTop: 20,
        marginBottom: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: "bold",
        color: "#333",
    },
    content: {
        paddingLeft: 20,
        paddingRight: 20,
    },
    label: {
        fontWeight: "500",
        fontSize: 18,
        marginBottom: 10,
    },
    inputMargin: {
        marginBottom: 20,
    },
    input: {
        backgroundColor: "white",
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        borderWidth: 1,
        borderColor: "gray",
        justifyContent: "center",
    },
    smallInput: {
        flex: 1,
    },
    dash: {
        fontSize: 20,
        marginRight: 10,
        marginLeft: 10
    },
    multilineInput: {
        backgroundColor: "white",
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        marginBottom: 20,
        borderWidth: 1,
        borderColor: "gray",
        textAlignVertical: "top",
        height: 200,
    },
    row: {
        flexDirection: "row",
        alignItems: "center",
        marginBottom: 20,
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        backgroundColor: 'rgba(0,0,0,0.5)',
    },
    modalContent: {
        backgroundColor: 'white',
        marginHorizontal: 20,
        padding: 20,
        borderRadius: 10,
    },
    modalTitle: {
        fontSize: 16,
        fontWeight: 'bold',
        marginBottom: 20
    },
    modalItem: {
        paddingVertical: 10,
        fontSize: 16,
        textAlign: 'center',
        color: "#1162BF"
    },
    modalCancelButton: {
        width: '100%',
        backgroundColor: '#f8d7da',
        borderRadius: 10,
        marginTop: 10,
    },
    modalCancel: {
        paddingVertical: 10,
        fontSize: 18,
        textAlign: 'center',
        color: 'red',
        fontWeight: 'bold'
    },
    errorInput: {
        borderColor: 'red'
    }
});

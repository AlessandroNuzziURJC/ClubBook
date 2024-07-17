import React, { useState } from "react";
import { View, Text, StyleSheet, TextInput, TouchableOpacity, ScrollView } from "react-native";
import Checkbox from 'expo-checkbox';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { useNavigation } from "@react-navigation/native";

const NewClass = () => {
    const navigation = useNavigation();

    const [professors, setProfessors] = useState([
        { id: 1, name: 'Profesor 1', selected: false },
        { id: 2, name: 'Profesor 2', selected: false },
        { id: 3, name: 'Profesor 3', selected: false },
    ]);

    const [schedule, setSchedule] = useState([
        { day: 'Lunes', selected: false, startTime: '', endTime: '' },
        { day: 'Martes', selected: false, startTime: '', endTime: '' },
        { day: 'Miércoles', selected: false, startTime: '', endTime: '' },
        { day: 'Jueves', selected: false, startTime: '', endTime: '' },
        { day: 'Viernes', selected: false, startTime: '', endTime: '' },
        { day: 'Sábado', selected: false, startTime: '', endTime: '' },
        { day: 'Domingo', selected: false, startTime: '', endTime: '' },
    ]);

    const [isStartTimePickerVisible, setStartTimePickerVisible] = useState(false);
    const [isEndTimePickerVisible, setEndTimePickerVisible] = useState(false);
    const [selectedDayIndex, setSelectedDayIndex] = useState(null);
    const [timeType, setTimeType] = useState('startTime');

    const handleSelectProfessor = (id) => {
        setProfessors(prevProfessors =>
            prevProfessors.map(professor =>
                professor.id === id
                    ? { ...professor, selected: !professor.selected }
                    : professor
            )
        );
    };

    const handleScheduleChange = (index, field, value) => {
        const updatedSchedule = [...schedule];
        updatedSchedule[index][field] = value;
        setSchedule(updatedSchedule);
    };

    const showTimePicker = (index, type) => {
        setSelectedDayIndex(index);
        setTimeType(type);
        if (type === 'startTime') {
            setStartTimePickerVisible(true);
        } else {
            setEndTimePickerVisible(true);
        }
    };

    const hideTimePicker = () => {
        setStartTimePickerVisible(false);
        setEndTimePickerVisible(false);
    };

    const handleConfirm = (time) => {
        const formattedTime = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        if (timeType === 'startTime') {
            handleScheduleChange(selectedDayIndex, 'startTime', formattedTime);
        } else {
            handleScheduleChange(selectedDayIndex, 'endTime', formattedTime);
        }
        hideTimePicker();
    };

    return (
        <ScrollView style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>Nueva clase</Text>
                </View>
            </View>
            <View style={styles.content}>
                <Text style={styles.label}>Nombre:</Text>
                <TextInput style={styles.input} placeholder="Nombre de la clase" />
                <Text style={styles.label}>Dirección:</Text>
                <TextInput style={styles.input} placeholder="Dirección" />
                <Text style={styles.label}>Profesor/es:</Text>
                <View style={styles.teacherlist}>
                    {professors.map(item => (
                        <View key={item.id} style={styles.scheduleContainer}>
                            <View style={styles.checkboxContainer}>
                                <Text style={styles.teacherName}>{item.name}</Text>
                                <Checkbox
                                    value={item.selected}
                                    onValueChange={() => handleSelectProfessor(item.id)}
                                    style={styles.checkbox}
                                />
                            </View>
                        </View>
                    ))}
                </View>
                <Text style={styles.label}>Horario:</Text>
                {schedule.map((item, index) => (
                    <View key={index} style={styles.scheduleContainer}>
                        <View style={styles.checkboxContainer}>
                            <Text style={styles.teacherName}>{item.day}</Text>
                            <Checkbox
                                value={item.selected}
                                onValueChange={() => {
                                    handleScheduleChange(index, 'selected', !item.selected);
                                }}
                                style={styles.checkbox}
                            />
                        </View>
                        {item.selected && (
                            <View style={styles.timeInputContainer}>
                                <TouchableOpacity
                                    style={styles.timeButton}
                                    onPress={() => showTimePicker(index, 'startTime')}
                                >
                                    <Text style={styles.timeButtonText}>{item.startTime || 'Inicio'}</Text>
                                </TouchableOpacity>
                                <TouchableOpacity
                                    style={styles.timeButton}
                                    onPress={() => showTimePicker(index, 'endTime')}
                                >
                                    <Text style={styles.timeButtonText}>{item.endTime || 'Fin'}</Text>
                                </TouchableOpacity>
                            </View>
                        )}
                    </View>
                ))}
                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                    <TouchableOpacity style={styles.cancelButton} onPress={() => navigation.goBack()}>
                        <Text style={styles.buttonText}>Cancelar</Text>
                    </TouchableOpacity>
                    <TouchableOpacity style={styles.saveButton}>
                        <Text style={styles.buttonText}>Guardar</Text>
                    </TouchableOpacity>
                </View>
            </View>
            <DateTimePickerModal
                isVisible={isStartTimePickerVisible}
                mode="time"
                onConfirm={handleConfirm}
                onCancel={hideTimePicker}
            />
            <DateTimePickerModal
                isVisible={isEndTimePickerVisible}
                mode="time"
                onConfirm={handleConfirm}
                onCancel={hideTimePicker}
            />
        </ScrollView>
    );
};


export default NewClass;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20,
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20,
    },
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        marginTop: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    content: {
        flex: 1,
        paddingBottom: 40
    },
    label: {
        fontWeight: '500',
        fontSize: 18,
        marginBottom: 10
    },
    input: {
        backgroundColor: 'white',
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        marginBottom: 20,
        borderWidth: 1,
        borderColor: 'gray'
    },
    saveButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center'
    },
    cancelButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center'
    },
    buttonText: {
        color: '#1162BF',
        fontWeight: 'bold',
        fontSize: 16
    },
    checkboxContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 40,
    },
    teacherlist: {
        marginBottom: 20
    },
    teacherName: {
        fontSize: 16,
    },
    checkbox: {
    },
    scheduleContainer: {
        backgroundColor: '#ddeeff',
        marginBottom: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 20,
        paddingRight: 20,
        borderRadius: 5
    },
    timeInputContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    timeInput: {
        backgroundColor: 'lightgray',
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        width: '48%',
    },
    timeButton: {
        marginTop: 10,
        backgroundColor: 'white',
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        width: '45%',
        alignItems: 'center',
        justifyContent: 'center',
    },
    timeButtonText: {
        fontWeight: 'bold',
        color: '#1162BF'
    }
});

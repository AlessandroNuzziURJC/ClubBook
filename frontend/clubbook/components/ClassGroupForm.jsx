import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TextInput, TouchableOpacity, Alert, ScrollView } from "react-native";
import Schedule from "../entities/Schedule";
import Checkbox from 'expo-checkbox';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import ServerRequests from "../serverRequests/ServerRequests";
import { useNavigation } from "@react-navigation/native";
import ClassGroup from "../entities/ClassGroup";
import UserCheckboxList from "./UserCheckboxList";
import FormFooter from "./FormFooter";

const ClassGroupForm = ({ classGroup, sendClassGroupBack }) => {
    const navigation = useNavigation();

    const [name, setName] = useState('');
    const [nameError, setNameError] = useState(false);
    const [address, setAddress] = useState('');
    const [addressError, setAddressError] = useState(false);
    const [teachers, setTeachers] = useState([]);
    const [teachersError, setTeachersError] = useState(false);
    const [schedule, setSchedule] = useState([]);
    const [scheduleError, setScheduleError] = useState([false, false, false, false, false, false, false]);

    const [isStartTimePickerVisible, setStartTimePickerVisible] = useState(false);
    const [selectedDayIndex, setSelectedDayIndex] = useState(null);

    useEffect(() => {
        const fetchData = async () => {
            const teachersFromServer = await getTeachers();
            const allSchedules = getSchedules();
            if (classGroup) {
                setName(classGroup.name);
                setAddress(classGroup.address);

                const set = new Set(classGroup.teachers.map(item => item.id));
                for (const teacher of teachersFromServer) {
                    teacher.selected = set.has(teacher.id);
                }

                const scheduleMap = new Map(classGroup.schedules.map(item => [item.weekDay, item]));
                for (const scheduleItem of allSchedules) {
                    if (scheduleMap.has(Schedule.translate(scheduleItem.weekDay))) {
                        const value = scheduleMap.get(Schedule.translate(scheduleItem.weekDay));
                        scheduleItem.selected = true;
                        scheduleItem.init = value.init.substring(0, 5);
                        scheduleItem.duration = value.duration;
                        scheduleItem.id = value.id;
                    }
                }
            }

            setTeachers(teachersFromServer);
            setSchedule(allSchedules);
        };

        fetchData();
    }, []);

    const getTeachers = async () => {
        const response = await ServerRequests.getAllTeachers();
        if (!response.ok) {
            Alert.alert('Problemas con la comunicación con el servidor.');
            return [];
        }

        const result = await response.json();
        return result;
    };

    const getSchedules = () => {
        return [
            { weekDay: 'Lunes', selected: false, init: '', duration: '' },
            { weekDay: 'Martes', selected: false, init: '', duration: '' },
            { weekDay: 'Miércoles', selected: false, init: '', duration: '' },
            { weekDay: 'Jueves', selected: false, init: '', duration: '' },
            { weekDay: 'Viernes', selected: false, init: '', duration: '' },
            { weekDay: 'Sábado', selected: false, init: '', duration: '' },
            { weekDay: 'Domingo', selected: false, init: '', duration: '' },
        ];
    };

    const handleSelectProfessor = (id) => {
        setTeachers(prevTeachers =>
            prevTeachers.map(teacher =>
                teacher.id === id
                    ? { ...teacher, selected: !teacher.selected }
                    : teacher
            )
        );
    };

    const handleScheduleChange = (index, field, value) => {
        const updatedSchedule = [...schedule];
        updatedSchedule[index][field] = value;
        setSchedule(updatedSchedule);
    };

    const showTimePicker = (index) => {
        setSelectedDayIndex(index);
        setStartTimePickerVisible(true);
    };

    const hideTimePicker = () => {
        setStartTimePickerVisible(false);
    };

    const handleConfirm = (time) => {
        const formattedTime = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
        if (selectedDayIndex !== null) {
            handleScheduleChange(selectedDayIndex, 'init', formattedTime);
        }
        hideTimePicker();
    };

    const handleSave = () => {
        const selectedTeachers = teachers.filter(teacher => teacher.selected)
            .map(item => item.id);

        const selectedSchedule = schedule.filter(weekDay => weekDay.selected)
            .map(item => new Schedule(item));

        if (!validForm()) {
            Alert.alert('No se ha rellenado correctamente el formulario.');
            return;
        }

        const newClassGroup = classGroup
            ? new ClassGroup(name, classGroup.id, address, selectedTeachers, selectedSchedule, classGroup.students)
            : new ClassGroup(name, null, address, selectedTeachers, selectedSchedule, []);

        sendClassGroupBack(newClassGroup);
    };

    const validForm = () => {
        let valid = true;
        setNameError(false);
        setAddressError(false);
        setTeachersError(false);
        setScheduleError([false, false, false, false, false, false, false]);

        if (name.trim() === '') {
            setNameError(true);
            valid = false;
        }
        if (address.trim() === '') {
            setAddressError(true);
            valid = false;
        }
        if (teachers.filter(item => item.selected).length === 0) {
            setTeachersError(true);
            valid = false;
        }

        if (schedule.filter(item => item.selected).length === 0) {
            setScheduleError([true, true, true, true, true, true, true]);
            return false;
        }

        const newScheduleError = schedule.map(item => {
            if (item.selected && (item.init === '' || item.duration === '')) {
                valid = false;
                return true;
            }
            return false;
        });

        setScheduleError(newScheduleError);

        return valid;
    };

    return (
        <View style={styles.container}>
            <ScrollView contentContainerStyle={styles.scrollViewContent}>
                <Text style={styles.label}>Nombre:</Text>
                <TextInput
                    style={[styles.input, nameError && styles.errorInput]}
                    placeholder="Nombre de la clase"
                    value={name}
                    onChangeText={setName}
                />
                <Text style={styles.label}>Dirección:</Text>
                <TextInput
                    style={[styles.input, addressError && styles.errorInput]}
                    placeholder="Dirección"
                    value={address}
                    onChangeText={setAddress}
                />
                <Text style={styles.label}>Profesor/es:</Text>
                <UserCheckboxList users={teachers} usersError={teachersError} handleSelectUser={handleSelectProfessor} />
                <Text style={styles.label}>Horario:</Text>
                {schedule.map((item, index) => (
                    <View key={index} style={[styles.scheduleContainer, scheduleError[index] && styles.errorBackground]}>
                        <View style={styles.checkboxContainer}>
                            <Text style={styles.teacherName}>{item.weekDay}</Text>
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
                                    onPress={() => showTimePicker(index)}
                                >
                                    <Text style={styles.timeButtonText}>{item.init || 'Inicio'}</Text>
                                </TouchableOpacity>
                                <TextInput
                                    style={[styles.durationInput, scheduleError[index] && styles.errorInput]}
                                    placeholder="Duración (min)"
                                    keyboardType="numeric"
                                    value={item.duration.toString()}
                                    onChangeText={(text) => handleScheduleChange(index, 'duration', text)}
                                />
                            </View>
                        )}
                    </View>
                ))}
            </ScrollView>
            <View style={styles.footerContainer}>
                <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: handleSave, text: 'Guardar' }} />
            </View>
            <DateTimePickerModal
                isVisible={isStartTimePickerVisible}
                mode="time"
                onConfirm={handleConfirm}
                onCancel={hideTimePicker}
            />
        </View>
    );
};

export default ClassGroupForm;

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    scrollViewContent: {
        paddingLeft: 20,
        paddingRight: 20,
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
    },
    checkboxContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 40,
    },
    teacherName: {
        fontSize: 16,
    },
    checkbox: {},
    scheduleContainer: {
        backgroundColor: '#ddeeff',
        marginBottom: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 20,
        paddingRight: 20,
        borderRadius: 5,
    },
    errorBackground: {
        borderWidth: 2,
        borderColor: 'red',
    },
    timeInputContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
    },
    durationInput: {
        backgroundColor: 'white',
        height: 40,
        borderRadius: 5,
        padding: 10,
        fontSize: 16,
        width: '45%',
        borderWidth: 1,
        borderColor: 'gray',
        marginTop: 10,
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
        color: '#1162BF',
    },
});

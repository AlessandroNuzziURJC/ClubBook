import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, ScrollView, TouchableOpacity, Alert } from "react-native";
import AttendanceSelector from "../../components/AttendanceSelector";
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { useNavigation } from '@react-navigation/native';
import FormFooter from "../../components/FormFooter";
import { useRoute } from '@react-navigation/native';
import AttendanceDto from "../../dto/AttendanceDto";
import ServerRequests from "../../serverRequests/ServerRequests";
import Toast from "../../components/Toast";

const AttendanceCheckList = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [classGroup, setClassGroup] = useState(null);
    const [students, setStudents] = useState([]);

    const [emptyMessage, setEmptyMessage] = useState('');

    const [isToastVisible, setIsToastVisible] = useState(false);
    const [toastMessage, setToastMessage] = useState('');
    const showToast = () => {
        setIsToastVisible(true);
        setTimeout(() => {
            setIsToastVisible(false);
        }, 1000);
    };

    useEffect(() => {
        getFromServer();
    }, []);

    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getClassGroup(item.id);
            if (response.ok) {
                const responseData = await response.json();
                setClassGroup(responseData.data);
                setStudents(responseData.data.students.map((student) => { student.selected = null; return student; }));
            } else if (response.status === 400) {
                const responseData = await response.json();
                setEmptyMessage('No hay datos disponibles.\n' + responseData.message);
                return;
            } else {
                Alert.alert('Error en la comunicación con el servidor.');
                setEmptyMessage('No hay clases disponibles.\n');
                return;
            }
        } catch (error) {
            console.log('Error: ', error);
            Alert.alert('Error en la comunicación con el servidor.');
        }
    }

    const handleSelectStudent = (id, selectedStatus) => {
        setStudents(prevStudents =>
            prevStudents.map(student =>
                student.id === id
                    ? { ...student, selected: selectedStatus }
                    : student
            )
        );
    };

    const getFormattedDateToday = () => {
        const today = new Date();
        const day = String(today.getDate()).padStart(2, '0');
        const month = String(today.getMonth() + 1).padStart(2, '0');
        const year = today.getFullYear();

        const formattedDate = `${day}/${month}/${year}`;
        return formattedDate;
    }

    const [selectedDate, setSelectedDate] = useState(getFormattedDateToday());
    const [datePickerVisible, setDatePickerVisible] = useState(false);

    const showDatePicker = () => {
        setDatePickerVisible(true);
    };

    const hideDatePicker = () => {
        setDatePickerVisible(false);
    };

    const handlerWrongDate = () => {
        Alert.alert('La fecha no puede ser posterior al día actual.');
    }

    const handleConfirm = (date) => {
        const today = new Date();
        if (date.getFullYear() > today.getFullYear()) {
            handlerWrongDate();
            return;
        } else if (date.getFullYear() === today.getFullYear()) {
            if (date.getMonth() > today.getMonth()) {
                handlerWrongDate();
                return;
            } else if (date.getMonth() === today.getMonth()) {
                if (date.getDate() > today.getDate()) {
                    handlerWrongDate();
                    return;
                }
            }
        }
        hideDatePicker();
        var month = date.getMonth() + 1;
        if (month.toString().length == 1) {
            month = '0' + month;
        }

        var day = date.getDate();
        if (day.toString().length == 1) {
            day = '0' + day;
        }
        const dateFormatted = day + '/' + month + '/' + date.getFullYear();
        setSelectedDate(dateFormatted);
    };

    const convertDateFormat = (dateStr) => {
        const [day, month, year] = dateStr.split('/');
        const formattedDate = `${year}-${month}-${day}`;
        return formattedDate;
    }

    const validateAndSave = async () => {
        if (students.filter(item => item.selected === null).length > 0) {
            Alert.alert('Hay usuarios sin especificar su asistencia.');
            return;
        }

        const date = convertDateFormat(selectedDate); //Cambiar formato
        const attendanceDto = new AttendanceDto(date, classGroup.id, students.filter(item => item.selected).map(item => item.id), students.filter(item => !item.selected).map(item => item.id));
        if (!attendanceDto.validate(classGroup)) {
            Alert.alert('La selección introducida es incorrecta.');
            return;
        }
        const response = await ServerRequests.saveAttendance(attendanceDto);

        if (!response.ok) {
            Alert.alert('Ha habido un error en el servidor.');

        } else {
            const result = await response.json();
            const message = result.message;
            setToastMessage(message);
            showToast();
            setTimeout(() => {
                navigation.goBack();
            }, 1000);
        }

        return;
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Pasar lista</Text>
            </View>
            {classGroup !== null ? (
                <View style={styles.content}>
                    <View style={styles.dateContainer}>
                        <Text style={styles.bold}>Seleccionar fecha:</Text>
                        <TouchableOpacity style={[styles.dataContainer, styles.bold]} onPress={showDatePicker}>
                            <Text>{selectedDate}</Text>
                        </TouchableOpacity>
                        <DateTimePickerModal
                            value={selectedDate}
                            isVisible={datePickerVisible}
                            mode="date"
                            onConfirm={handleConfirm}
                            onCancel={hideDatePicker}
                        />
                    </View>

                    <View style={styles.classGroupContainer}>
                        <Text style={[styles.classGroupTitle, styles.bold]}>Clase 1</Text>
                    </View>

                    <View style={styles.scrollViewContainer}>
                        <ScrollView style={styles.checkboxList}>
                            <AttendanceSelector users={students} usersError={null} handleSelectUser={handleSelectStudent} />
                        </ScrollView>
                    </View>

                    <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: validateAndSave, text: 'Finalizar' }} />
                </View>
            ) : (
                <View style={styles.emptyContainer}>
                    <Text style={styles.emptyMessage}>{emptyMessage}</Text>
                </View>
            )}
            <Toast
                    visible={isToastVisible}
                    message={toastMessage}
                    onClose={() => setIsToastVisible(false)}
                />
        </View>
    );
};

export default AttendanceCheckList;

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
        paddingRight: 20,
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    content: {
        flex: 1,
    },
    bold: {
        fontWeight: 'bold',
        marginBottom: 10,
    },
    dateContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        marginBottom: 20,
        alignItems: 'center',
        paddingLeft: 20,
        paddingRight: 20,
    },
    classGroupContainer: {
        alignItems: 'center',
        justifyContent: 'center',
    },
    classGroupTitle: {
        fontSize: 18,
    },
    dataContainer: {
        borderWidth: 1,
        borderColor: '#1162BF',
        borderRadius: 10,
        padding: 10,
    },
    scrollViewContainer: {
        flex: 1,
    },
    checkboxList: {
        flexGrow: 1,
        paddingLeft: 20,
        paddingRight: 20,
    },
    emptyContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    emptyMessage: {
        fontSize: 18,
        textAlign: 'center',
        color: '#888', // Gris claro para el mensaje
    },
});
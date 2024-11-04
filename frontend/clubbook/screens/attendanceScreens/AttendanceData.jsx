import React, { useState, useRef, useEffect } from "react";
import { View, Text, StyleSheet, ScrollView, Alert, TouchableOpacity } from "react-native";
import DropDownPicker from 'react-native-dropdown-picker';
import { useRoute } from '@react-navigation/native';
import ServerRequests from "../../serverRequests/ServerRequests";
import * as FileSystem from 'expo-file-system';
import * as Sharing from 'expo-sharing';

/**
 * AttendanceData component displays attendance records for a selected class group.
 * It allows users to select a month, view attendance data, and download it as a PDF.
 * 
 * @returns {JSX.Element} The rendered component.
 */
const AttendanceData = () => {

    const route = useRoute();
    const { item } = route.params;
    const [dates, setDates] = useState([]);
    const [classGroup, setClassGroup] = useState(item);
    const [students, setStudents] = useState([]);
    const [emptyMessage, setEmptyMessage] = useState('');

    /**
     * Retrieves the current month value.
     * 
     * @returns {number} The current month as a number (1-12).
     */
    const getActualMonthValue = () => {
        const actualDate = new Date();
        const month = actualDate.getMonth();
        return month + 1;
    }

    const [monthsOpen, setMonthsOpen] = useState(false);
    const [monthsValue, setMonthsValue] = useState(getActualMonthValue());
    const [monthsItems, setMonthsItems] = useState([
        { label: 'Enero', value: 1 },
        { label: 'Febrero', value: 2 },
        { label: 'Marzo', value: 3 },
        { label: 'Abril', value: 4 },
        { label: 'Mayo', value: 5 },
        { label: 'Junio', value: 6 },
        { label: 'Julio', value: 7 },
        { label: 'Agosto', value: 8 },
        { label: 'Septiembre', value: 9 },
        { label: 'Octubre', value: 10 },
        { label: 'Noviembre', value: 11 },
        { label: 'Diciembre', value: 12 },
    ]);

    /**
     * Truncates a student's name if it exceeds a specified length.
     * 
     * @param {string} name - The name to be truncated.
     * @returns {string} The adapted name.
     */
    const adaptNameView = (name) => {
        if (name.length >= 18)
            return name.substring(0, 16) + '...'
        return name;
    }

    useEffect(() => {
        getFromServer();
    }, [monthsValue]);

    /**
     * Fetches attendance data from the server based on the selected month and class group.
     * Handles server responses and updates state accordingly.
     */
    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getAttendances(monthsValue, classGroup.id);
            if (response.ok) {
                const responseData = await response.json();
                setStudents(responseData.data.usersList);
                setDates(responseData.data.datesList);
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
    };

    /**
     * Handles the PDF download process, saving it to the device and sharing it if possible.
     */
    const handleDownload = async () => {
        try {
            const response = await ServerRequests.downloadPdf(classGroup.id);
            const blobData = await response.blob();
            
            const path = `${FileSystem.documentDirectory}attendance_${classGroup.id}.pdf`;
            const reader = new FileReader();
    
            reader.onloadend = async () => {
                const base64Data = reader.result.split(',')[1];
                await FileSystem.writeAsStringAsync(path, base64Data, {
                    encoding: FileSystem.EncodingType.Base64,
                });
    
                if (await Sharing.isAvailableAsync()) {
                    await Sharing.shareAsync(path);
                } else {
                    console.log('Compartir no disponible');
                }
            };
            reader.readAsDataURL(blobData);
    
        } catch (error) {
            console.error('Error al descargar o abrir el PDF:', error);
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Consultar asistencia</Text>
            </View>
            {students.length !== 0 ? (
                <View style={styles.content}>
                    <View style={{ flexDirection: 'row', justifyContent: 'space-between', zIndex: 1 }}>
                        <DropDownPicker
                            open={monthsOpen}
                            value={monthsValue}
                            items={monthsItems}
                            setOpen={setMonthsOpen}
                            setValue={setMonthsValue}
                            setItems={setMonthsItems}
                            placeholder="Selecciona el mes"
                            theme="LIGHT"
                            multiple={false}
                            onChangeValue={value => setMonthsValue(value)}
                        />
                    </View>
                    <TouchableOpacity style={styles.button} onPress={handleDownload}>
                        <Text style={styles.buttonText}>Descargar PDF</Text>
                    </TouchableOpacity>
                    <ScrollView
                        style={styles.table}
                        contentContainerStyle={styles.tableContent}
                        bounces={false}
                    >
                        <View style={styles.tableRow}>
                            <View style={styles.tableNameColumn}>
                                <Text style={[styles.tableCell, styles.headerCell]}>Nombre</Text>
                                {students.map((student) => (
                                    <Text key={student.id} style={[styles.tableCell, styles.nameCell]}>{adaptNameView(student.firstName + ' ' + student.lastName)}</Text>
                                ))}
                            </View>
                            <ScrollView
                                horizontal
                                bounces={false}
                            >
                                <View>
                                    <View style={styles.dateRow}>
                                        {dates.map((date, index) => (
                                            <Text key={index} style={[styles.tableCell, styles.headerCell]}>{date.substring(8, 10)}</Text>
                                        ))}
                                    </View>
                                    {students.map((student, studentIndex) => (
                                        <View key={student.id} style={[
                                            styles.tableRow,
                                            (studentIndex % 2 === 0 ? styles.evenRow : styles.oddRow)
                                        ]}>
                                            {student.attendanceList.map((value, index) => {
                                                if (value === null) {
                                                    return (<Text key={`${student.id}-no-data-${index}`} style={styles.tableCell}>_</Text>);
                                                }
                                                if (!value)
                                                    return (<Text key={`${student.id}-no-data-${index}`} style={[styles.tableCell, styles.notAttended]}>F</Text>);
                                                else
                                                    return (<Text key={`${student.id}-no-data-${index}`} style={[styles.tableCell, styles.attended]}>✓</Text>);
                                            })}
                                        </View>
                                    ))}
                                </View>
                            </ScrollView>
                        </View>
                    </ScrollView>
                </View>
            ) : (
                <View style={styles.emptyContainer}>
                    <Text style={styles.emptyMessage}>{emptyMessage}</Text>
                </View>
            )}
        </View>
    );
}

export default AttendanceData;

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
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold',
    },
    content: {
        marginTop: 20,
    },
    dropdown: {
        width: 100,
        marginBottom: 20,
    },
    table: {
        marginTop: 20,
        flexGrow: 1,
        borderRadius: 10,
        marginBottom: 200
    },
    tableContent: {
        flexGrow: 1,
    },
    tableRow: {
        flexDirection: 'row',
    },
    tableNameColumn: {
        flexDirection: 'column',
        borderRightWidth: 3,
        borderRightColor: 'white'
    },
    dateRow: {
        flexDirection: 'row',
    },
    tableCell: {
        padding: 10,
        textAlign: 'center',
        minWidth: 40,
    },
    headerCell: {
        backgroundColor: '#1162BF',
        color: 'white',
        fontWeight: '500',
    },
    nameCell: {
        backgroundColor: '#1162BF',
        width: 150,
        color: 'white',
    },
    evenRow: {
        backgroundColor: '#c1deff',
    },
    oddRow: {
        backgroundColor: '#FFFFFF',
    },
    button: {
        backgroundColor: 'white',
        padding: 15,
        borderRadius: 5,
        marginTop: 20,
        borderColor: '#1162BF',
        alignItems: 'center',
        borderWidth: 1
    },
    buttonText: {
        color: '#1162BF',
        fontSize: 16,
        fontWeight: '700'
    },
    notAttended: {
        fontWeight: 'bold',
        backgroundColor: '#9c0000',
        color: 'white'
    },
    attended: {
        color: '#009c17',
        fontWeight: 'bold'
    },
    emptyContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
    },
    emptyMessage: {
        fontSize: 18,
        textAlign: 'center',
        color: '#888',
    },
});

import React, { useState, useRef, useEffect } from "react";
import { View, Text, StyleSheet, ScrollView, Alert, TouchableOpacity } from "react-native";
import DropDownPicker from 'react-native-dropdown-picker';
import { useRoute } from '@react-navigation/native';
import { useNavigation } from "@react-navigation/native";
import ServerRequests from "../../serverRequests/ServerRequests";

const AttendanceData = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { item } = route.params;
    const [dates, setDates] = useState([]);
    const [classGroup, setClassGroup] = useState(item);
    const [students, setStudents] = useState([]);

    const getActualMonthValue = () => {
        const actualDate = new Date();
        const month = actualDate.getMonth();
        return month + 1;
    }

    const getActualYearValue = () => {
        const actualDate = new Date();
        const year = actualDate.getFullYear();
        return year;
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

    const [yearsOpen, setYearsOpen] = useState(false);
    const [yearsValue, setYearsValue] = useState(getActualYearValue);
    const [yearsItems, setYearsItems] = useState([]);

    const adaptNameView = (name) => {
        if (name.length > 18)
            return name.substring(0, 16) + '...'
        return name;
    }

    useEffect(() => {
        getFromServer();
    }, [monthsValue, yearsValue]);

    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getAttendances(yearsValue, monthsValue, classGroup.id);

            if (!response.ok) {
                Alert.alert('Error en la comunicación con el servidor');
                return;
            }

            const data = await response.json();
            setStudents(data.usersList);
            setDates(data.datesList);

            const responseYears = await ServerRequests.getYears(classGroup.id);
            if (!responseYears.ok) {
                Alert.alert('Error en la comunicación con el servidor');
                return;
            }
            const dataYears = await responseYears.json();
            setYearsItems(dataYears);
        } catch (error) {
            console.log('Error: ', error);
            Alert.alert('Error en la comunicación con el servidor.');
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Consultar asistencia</Text>
            </View>
            <View style={styles.content}>
                <View style={{ flexDirection: 'row', justifyContent: 'space-between', zIndex: 1 }}>
                    <DropDownPicker
                        open={yearsOpen}
                        value={yearsValue}
                        items={yearsItems}
                        setOpen={setYearsOpen}
                        setValue={setYearsValue}
                        setItems={setYearsItems}
                        placeholder="Selecciona el año"
                        theme="LIGHT"
                        multiple={false}
                        containerStyle={{ width: '48%' }}
                        onChangeValue={value => setYearsValue(value)}
                    />
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
                        containerStyle={{ width: '48%' }}
                        onChangeValue={value => setMonthsValue(value)}
                    />
                </View>
                <TouchableOpacity style={styles.button} onPress={null}>
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
        minWidth: 40, // Min width for cells
    },
    headerCell: {
        backgroundColor: '#1162BF',
        color: 'white',
        fontWeight: '500',
    },
    nameCell: {
        backgroundColor: '#1162BF',
        width: 150, // Fixed width for the name column
        color: 'white',
    },
    evenRow: {
        backgroundColor: '#c1deff', // Color for even rows
    },
    oddRow: {
        backgroundColor: '#FFFFFF', // Color for odd rows
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
    }
});

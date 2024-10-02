import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Modal, FlatList } from "react-native";
import Functions from "../../functions/Functions";
import EventCard from "./EventCard"; 
import { Ionicons } from "@expo/vector-icons";

const Calendar = () => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [calendarTable, setCalendarTable] = useState([]);
    const [selectedDay, setSelectedDay] = useState(null);
    const [isModalVisible, setModalVisible] = useState(false);
    const [events, setEvents] = useState([]);
    const weekDays = ['L', 'M', 'X', 'J', 'V', 'S', 'D'];

    useEffect(() => {
        generateCalendar();
    }, [currentDate]);

    const generateCalendar = () => {
        const year = currentDate.getFullYear();
        const month = currentDate.getMonth();

        const firstDay = new Date(year, month, 1);
        const startDay = firstDay.getDay() === 0 ? 6 : firstDay.getDay() - 1;

        const totalDays = new Date(year, month + 1, 0).getDate();
        const weeks = [];

        let week = Array(7).fill(null);
        for (let day = 1; day <= totalDays; day++) {
            const currentWeekDay = (startDay + day - 1) % 7;
            week[currentWeekDay] = day;

            if (currentWeekDay === 6 || day === totalDays) {
                weeks.push(week);
                week = Array(7).fill(null);
            }
        }

        setCalendarTable(weeks);
    };

    const changeMonth = (direction) => {
        const newDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + direction, 1);
        setCurrentDate(newDate);
    };

    const showEvents = (day) => {
        const mockEvents = [
            { id: '1', title: 'Evento A', type: "Prueba", description: 'Descripción A', date: day },
            { id: '2', title: 'Evento B', type: "Prueba", description: 'Descripción B', date: day }
        ];

        setSelectedDay(day);
        setEvents(mockEvents);
        setModalVisible(true);
    };

    const closeModal = () => {
        setModalVisible(false);
        setSelectedDay(null);
    };

    const renderEventCard = ({ item }) => <EventCard editAndDelete={false} data={item} />;

    const monthName = currentDate.toLocaleString("default", { month: "long" });
    const year = currentDate.getFullYear();
    const today = new Date().getDate();

    return (
        <View style={styles.container}>
            <Text style={styles.header}>
                {Functions.translateEngToSpaMonth(monthName)} {year}
            </Text>
            <View style={styles.calendar}>
                <View style={styles.week}>
                    {weekDays.map((dayName) => (
                        <Text style={styles.dayName} key={dayName}>{dayName}</Text>
                    ))}
                </View>

                {calendarTable.map((week, index) => (
                    <View key={index} style={styles.week}>
                        {week.map((day, dayIndex) => (
                            <TouchableOpacity
                                key={dayIndex}
                                onPress={() => day && showEvents(day)}
                                style={styles.dayButton}
                            >
                                <Text
                                    style={[
                                        styles.day,
                                        day === today && currentDate.getMonth() === new Date().getMonth() && currentDate.getFullYear() === new Date().getFullYear()
                                            ? styles.currentDay
                                            : null
                                    ]}
                                >
                                    {day !== null ? day : ""}
                                </Text>
                            </TouchableOpacity>
                        ))}
                    </View>
                ))}
            </View>
            <View style={styles.buttonContainer}>
                <TouchableOpacity onPress={() => changeMonth(-1)} style={styles.button}>
                    <Text style={styles.buttonText}>Anterior</Text>
                </TouchableOpacity>
                <TouchableOpacity onPress={() => changeMonth(1)} style={styles.button}>
                    <Text style={styles.buttonText}>Siguiente</Text>
                </TouchableOpacity>
            </View>

            {/* Modal para mostrar eventos */}
            <Modal
                visible={isModalVisible}
                transparent={true}
                animationType="slide"
            >
                <View style={styles.modalContainer}>
                    <View style={styles.modalContent}>
                        <View style={styles.modalHeader}>
                            <Text style={styles.modalTitle}>
                                {`Eventos para el día ${selectedDay}`}
                            </Text>
                            <TouchableOpacity onPress={closeModal}>
                                <Ionicons name="close-outline" size={25} color='#1162BF' />
                            </TouchableOpacity>
                        </View>

                        {events.length > 0 ? (
                            <FlatList
                                data={events}
                                renderItem={renderEventCard}
                                keyExtractor={(item) => item.id}
                                style={styles.list}
                            />
                        ) : (
                            <View style={styles.noEventsContainer}>
                                <Text style={styles.noEventsText}>No hay eventos para este día</Text>
                            </View>
                        )}
                    </View>
                </View>
            </Modal>
        </View>
    );
};

const styles = StyleSheet.create({
    container: {
        padding: 20,
        backgroundColor: "#fff",
        borderRadius: 10,
        backgroundColor: '#f2f2f2'
    },
    header: {
        fontSize: 24,
        fontWeight: "bold",
        textAlign: "center",
        marginBottom: 10,
    },
    buttonContainer: {
        flexDirection: "row",
        justifyContent: "space-between",
        marginTop: 20
    },
    button: {
        padding: 10,
        borderRadius: 5,
        width: '48%',
        alignItems: "center",
    },
    buttonText: {
        color: "#1162BF",
        fontWeight: "bold",
    },
    calendar: {
        flexDirection: "column",
    },
    week: {
        flexDirection: "row",
        justifyContent: "space-between",
    },
    dayButton: {
        width: "14%",
        justifyContent: "center",  // Centrar verticalmente
        alignItems: "center",      // Centrar horizontalmente
    },
    day: {
        textAlign: "center",
        padding: 10,
        fontSize: 13
    },
    currentDay: {
        backgroundColor: '#ddeeff',
        borderRadius: 1,
    },
    dayName: {
        color: "#1162BF",
        width: "14%",
        textAlign: "center",
        padding: 10,
        fontWeight: 'bold'
    },
    modalContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: 'rgba(0, 0, 0, 0.5)',
    },
    modalContent: {
        backgroundColor: 'white',
        width: '90%',  // Deja margen de 20 en los laterales
        height: '80%', // Deja margen de 20 en la parte superior e inferior
        padding: 20,
        borderRadius: 10,
        alignItems: 'center',
    },
    modalHeader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
        marginBottom: 20,
    },
    modalTitle: {
        fontSize: 18,
        fontWeight: 'bold',
    },
    list: {
        width: '100%',
    },
    noEventsContainer: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center', 
    },
    noEventsText: {
        fontSize: 16,
        color: 'gray',
    },
});

export default Calendar;

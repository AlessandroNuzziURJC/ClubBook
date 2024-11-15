import React, { useState, useEffect } from "react";
import { View, Text, StyleSheet, TouchableOpacity, Modal, FlatList } from "react-native";
import { useFocusEffect, } from "@react-navigation/native";
import Functions from "../../functions/Functions";
import EventCard from "./EventCard";
import { Ionicons } from "@expo/vector-icons";
import ServerRequests from "../../serverRequests/ServerRequests";

/**
 * Calendar component that displays a monthly calendar and a modal to show events on selected days.
 * @component
 */
const Calendar = () => {
    const [currentDate, setCurrentDate] = useState(new Date());
    const [calendarTable, setCalendarTable] = useState([]);
    const [selectedDay, setSelectedDay] = useState(null);
    const [isModalVisible, setModalVisible] = useState(false);
    const [events, setEvents] = useState({});
    const weekDays = ['L', 'M', 'X', 'J', 'V', 'S', 'D'];

    /**
     * Fetches events from the server for the current month and year.
     * @async
     */
    const getFromServer = async () => {
        try {
            const response = await ServerRequests.getMonthEvents(currentDate.getMonth() + 1, currentDate.getFullYear());
            const result = await response.json();
            if (response.ok) {
                setEvents(result.data);
            }
        } catch (error) {
            console.error("Error al obtener los eventos del servidor", error);
        }
    };

    useFocusEffect(
        React.useCallback(() => {
            const getData = async () => {
                await getFromServer();
            };

            getData();

            return () => {

            };
        }, [])
    );

    useEffect(() => {
        const fetchEventsAndGenerateCalendar = async () => {
            generateCalendar();
            await getFromServer();
        };

        fetchEventsAndGenerateCalendar();
    }, [currentDate]);

    /**
    * Generates the calendar layout based on the current date, accounting for days and weeks.
    */
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

    /**
     * Changes the displayed month in the calendar.
     * @param {number} direction - The direction to change the month, either -1 for previous or 1 for next month.
     */
    const changeMonth = (direction) => {
        setEvents({});
        const newDate = new Date(currentDate.getFullYear(), currentDate.getMonth() + direction, 1);
        setCurrentDate(newDate);
    };

    /**
     * Displays the modal with events for a selected day.
     * @param {number} day - The selected day to display events for.
     */
    const showEvents = (day) => {
        setSelectedDay(day);
        setModalVisible(true);
    };

    /**
     * Closes the event modal and clears the selected day.
     */
    const closeModal = () => {
        setModalVisible(false);
        setSelectedDay(null);
    };

    /**
     * Renders each event card in the modal.
     * @param {Object} item - The event item data to be rendered.
     */
    const renderEventCard = ({ item }) => (
        <EventCard editAndDelete={false} data={item} onCloseModal={closeModal} />
    );

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
                        {week.map((day, dayIndex) => {
                            const hasEvents = events[day]?.length > 0;
                            return (
                                <TouchableOpacity
                                    key={dayIndex}
                                    onPress={() => day && showEvents(day)}
                                    style={[
                                        styles.dayButton,
                                        day === today && currentDate.getMonth() === new Date().getMonth() && currentDate.getFullYear() === new Date().getFullYear()
                                            ? styles.currentDayContainer
                                            : null,
                                        hasEvents ? styles.eventDay : null  // Aplicar fondo rojo si hay eventos
                                    ]}
                                >
                                    <Text
                                        style={[
                                            styles.day,
                                            day === today && currentDate.getMonth() === new Date().getMonth() && currentDate.getFullYear() === new Date().getFullYear()
                                                ? styles.currentDay
                                                : null,
                                            hasEvents ? styles.eventDayText : null
                                        ]}
                                    >
                                        {day !== null ? day : ""}
                                    </Text>
                                </TouchableOpacity>
                            );
                        })}
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

                        {events[selectedDay]?.length > 0 ? (
                            <FlatList
                                data={events[selectedDay]}
                                renderItem={renderEventCard}
                                keyExtractor={(item) => item.id.toString()}
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
        justifyContent: "center",
        alignItems: "center",
    },
    day: {
        textAlign: "center",
        padding: 10,
        fontSize: 13
    },
    currentDayContainer: {
        borderRadius: 40,
        borderWidth: 1,
        borderColor: "#0f3b69"
    },
    currentDay: {
        fontWeight: 'bold',
        color: "#0f3b69",
        textDecorationLine: 'underline',
    },
    dayName: {
        color: "#1162BF",
        width: "14%",
        textAlign: "center",
        padding: 10,
        fontWeight: 'bold'
    },
    eventDay: {
        backgroundColor: "#ffc459",
        borderRadius: 40,
    },
    eventDayText: {
        color: '#634000',
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
        width: '90%',
        height: '80%',
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

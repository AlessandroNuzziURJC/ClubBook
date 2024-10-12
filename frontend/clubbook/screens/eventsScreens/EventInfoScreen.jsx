import React, { useState } from "react";
import { useNavigation, useRoute } from "@react-navigation/native";
import { View, Text, TouchableOpacity, StyleSheet, ScrollView } from "react-native";
import Functions from "../../functions/Functions";
import AttendanceEventSelector from "./AttendanceEventSelector";

const EventInfoScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const [event, setEvent] = useState(route.params.event);
    const admin = route.params.admin;
    const teacher = route.params.teacher;

    const calculateTimeRemaining = (timestamp) => {
        const now = new Date();
        const notificationDate = new Date(timestamp);
        const daysRemaining = Math.ceil((notificationDate - now) / (1000 * 60 * 60 * 24));
        return daysRemaining;
    };

    const isDeadline = () => {
        return calculateTimeRemaining(event.deadline) < 0;
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{event.title}</Text>
            </View>
            <View style={styles.content}>


                <View style={[styles.infoSection, styles.attendanceContainer]}>
                    {admin &&
                        <TouchableOpacity style={styles.attendanceControlButton} onPress={() => navigation.navigate("AttendanceEvent", { eventId: event.id })}>
                            <Text style={styles.attendanceControl}>Ver confirmación de asistencia</Text>
                        </TouchableOpacity>
                    }
                    {teacher &&
                        <View>
                            <TouchableOpacity style={styles.attendanceControlButton} onPress={() => navigation.navigate("AttendanceEvent", { eventId: event.id })}>
                                <Text style={styles.attendanceControl}>Ver confirmación de asistencia</Text>
                            </TouchableOpacity>
                            {isDeadline()
                                ? <View>
                                    <Text style={styles.deadline}>El periodo de inscripción finalizó</Text>
                                    <AttendanceEventSelector dataEvent={event} blocked={true}/>
                                </View>
                                : <View>
                                    <Text style={styles.deadline}>¡Quedan {calculateTimeRemaining(event.deadline)} días para inscribirse!</Text>
                                    <AttendanceEventSelector dataEvent={event} blocked={false}/>
                                </View>
                            }
                        </View>
                    }
                    {!admin && !teacher &&
                        <View>
                            {isDeadline()
                                ? <View>
                                    <Text style={styles.deadline}>El periodo de inscripción finalizó</Text>
                                    <AttendanceEventSelector dataEvent={event} blocked={true}/>
                                </View>
                                : <View>
                                    <Text style={styles.deadline}>¡Quedan {calculateTimeRemaining(event.deadline)} días para inscribirse!</Text>
                                    <AttendanceEventSelector dataEvent={event} blocked={false}/>
                                </View>
                            }
                        </View>
                    }
                </View>
                <Text style={styles.infoHeader}>Información general</Text>
                <ScrollView>


                    <View style={styles.infoSection}>
                        <Text style={styles.infoLabel}>Dirección:</Text>
                        <Text style={styles.infoText}>{event.address}</Text>
                    </View>

                    <View style={styles.infoSection}>
                        <Text style={styles.infoLabel}>Edad límite:</Text>
                        <Text style={styles.infoText}>Nacidos entre {event.birthYearStart.substring(0, 4)} y {event.birthYearEnd.substring(0, 4)} (incluidos)</Text>
                    </View>

                    <View style={styles.infoSection}>
                        <Text style={styles.infoLabel}>Fecha:</Text>
                        <Text style={styles.infoText}>{Functions.convertDateEngToSpa(event.date)}</Text>
                    </View>

                    <View style={styles.infoSection}>
                        <Text style={styles.infoLabel}>Tipo de evento:</Text>
                        <Text style={styles.infoText}>{Functions.translateEventTypes(event.type.name)}</Text>
                    </View>

                    <View style={styles.infoSection}>
                        <Text style={styles.infoLabel}>Información adicional</Text>
                        <Text style={styles.infoText}>{event.additionalInfo}</Text>
                    </View>
                </ScrollView>

            </View>
        </View >
    );
}

export default EventInfoScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        flexDirection: 'row',
        alignItems: 'flex-end',
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
        paddingVertical: 10,
    },
    infoHeader: {
        fontSize: 22,
        fontWeight: '600',
        color: '#333',
        marginBottom: 15,
        paddingLeft: 20,
        paddingRight: 20,
        marginTop: 30,
    },
    infoSection: {

        borderTopWidth: 1,
        borderTopColor: '#e0e0e0',
        paddingTop: 10,
        paddingLeft: 20,
        paddingRight: 20,
    },
    infoLabel: {
        fontSize: 16,
        fontWeight: 'bold',
        color: '#666',
    },
    infoText: {
        fontSize: 16,
        color: '#333',
        marginTop: 10,
        marginBottom: 15,
    },
    attendanceControlButton: {
        backgroundColor: "#1162BF",
        padding: 15,
        borderRadius: 10,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 10,
    },
    attendanceControl: {
        color: "#fff",
        fontSize: 16,
        fontWeight: "bold",
    },
    attendanceContainer: {
        backgroundColor: '#ddeeff',
        paddingBottom: 10
    },
    deadline: {
        fontWeight: 'bold',
        color: 'red',
        textAlign: 'center'
    }
});

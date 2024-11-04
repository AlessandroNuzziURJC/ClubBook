import React, { useState, useCallback } from "react";
import { View, StyleSheet, TouchableOpacity, Text, ScrollView, Alert } from "react-native";
import { useNavigation, useRoute, useFocusEffect } from '@react-navigation/native';
import { Ionicons } from "@expo/vector-icons";
import ServerRequest from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";

/**
 * ClassGroupAgendaScreen component renders the agenda and class group information for a specific class notebook.
 * Displays virtual assistant settings, today's agenda entry, and provides options to view all entries.
 */
const ClassGroupAgendaScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { notebookBasicInfo } = route.params;
    const [isConfigMissing, setIsConfigMissing] = useState(false);
    const [notebook, setNotebook] = useState(null);
    const [season, setSeason] = useState(true);
    const [entry, setEntry] = useState(null);

    /**
     * Fetches notebook and entry information from the server.
     * Sets the notebook and entry data, and checks if the configuration is missing.
     */
    const getFromServer = async () => {
        const response = await ServerRequest.getNotebookById(notebookBasicInfo.notebookId);
        const result = await response.json();

        if (response.ok) {
            setNotebook(result.data);
            setIsConfigMissing(result.data.sport === null || result.data.level === null);
            const entryResponse = await ServerRequest.getTodayEntryByNotebookId(notebookBasicInfo.notebookId);
            const entryResult = await entryResponse.json();

            if (entryResponse. ok) {
                setEntry(entryResult.data);
            } else {
                Alert.alert("Error en la comunicación con el servidor.");
            }

        } else {
            if (response.status === 400) {
                setSeason(false);
                setMessage(result.message);
            } else {
                Alert.alert("Error en la comunicación con el servidor.");
            }
        }
    };

    /**
     * Navigates to the Agenda screen, passing the notebook as a parameter.
     */
    const openAgenda = () => {
        navigation.navigate('AgendaScreen', { notebook: notebook });
    };

    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{notebookBasicInfo.classGroupName}</Text>
            </View>
            <View style={styles.content}>
                {season ? (
                    <View style={styles.innerContent}>
                        <TouchableOpacity
                            style={styles.configButton}
                            onPress={() => navigation.navigate('VirtualAssistantConfigFormScreen', { notebook: notebook })}
                        >
                            <Ionicons name="settings-outline" size={20} color="#1162BF" style={styles.icon} />
                            <Text style={styles.config}>Asistente Virtual</Text>
                            {isConfigMissing ?
                                <View style={styles.alertBadge}>
                                    <Text style={styles.alertText}>!</Text>
                                </View>
                                :
                                <></>
                            }
                        </TouchableOpacity>
                        <View style={styles.agendaContainer}>
                            <Text style={styles.title}>Agenda</Text>
                            {entry ? (
                                <View>
                                    <Text style={styles.date}>{Functions.convertDateEngToSpa(entry.date)}</Text>
                                    <ScrollView bounces={false}>
                                        <View style={styles.entryContainer}>
                                            <View style={styles.phaseContainer}>
                                                <Text style={styles.phaseTitle}>Calentamiento</Text>
                                                {entry.warmUpExercises.map((task, index) => (
                                                    <Text key={index} style={styles.taskText}>• {task}</Text>
                                                ))}
                                            </View>
                                            <View style={styles.phaseContainer}>
                                                <Text style={styles.phaseTitle}>Fase Específica</Text>
                                                {entry.specificExercises.map((task, index) => (
                                                    <Text key={index} style={styles.taskText}>• {task}</Text>
                                                ))}
                                            </View>
                                            <View style={styles.phaseContainer}>
                                                <Text style={styles.phaseTitle}>Fase Final</Text>
                                                {entry.finalExercises.map((task, index) => (
                                                    <Text key={index} style={styles.taskText}>• {task}</Text>
                                                ))}
                                            </View>
                                        </View>
                                    </ScrollView>
                                </View>
                            ) : (
                                <Text style={styles.noPlan}>No hay nada planeado para hoy</Text>
                            )}
                        </View>
                        <View style={styles.buttonContainer}>
                            <TouchableOpacity style={styles.button} onPress={openAgenda}>
                                <Text style={styles.buttonText}>Ver todas las entradas</Text>
                            </TouchableOpacity>
                        </View>
                    </View>
                ) : (
                    <Text style={styles.seasonNotStarted}>{message}</Text>
                )}
            </View>
        </View>
    );
};

export default ClassGroupAgendaScreen;

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
        overflow: 'hidden',
        paddingBottom: 20,
        borderRadius: 10,
        paddingLeft: 20,
        paddingRight: 20,
    },
    innerContent: {
        flex: 1,
    },
    configButton: {
        flexDirection: 'row',
        backgroundColor: '#fff',
        padding: 15,
        borderRadius: 10,
        alignItems: "center",
        justifyContent: "center",
        marginVertical: 10,
        borderColor: "#1162BF",
        borderWidth: 1
    },
    config: {
        color: "#1162BF",
        fontSize: 16,
        fontWeight: "bold",
        marginLeft: 10
    },
    alertBadge: {
        position: 'absolute',
        top: -10,
        right: -10,
        backgroundColor: 'red',
        borderRadius: 10,
        width: 20,
        height: 20,
        justifyContent: 'center',
        alignItems: 'center',
    },
    alertText: {
        color: '#fff',
        fontWeight: 'bold',
    },
    agendaContainer: {
        flex: 1,
        backgroundColor: "#f2f2f2",
        borderRadius: 10,
        padding: 15,
        marginVertical: 10,
    },
    title: {
        fontSize: 20,
        fontWeight: "bold",
        color: "#1162BF",
    },
    date: {
        fontSize: 14,
        color: "#666",
        marginTop: 5,
    },
    noPlan: {
        fontSize: 16,
        color: "#333",
        marginTop: 10,
        justifyContent: 'center',
        textAlign: 'center',
        marginTop: '48%'
    },
    info: {
        fontSize: 16,
        color: "#333",
        marginTop: 10,
    },
    buttonContainer: {
        flexDirection: 'row',
        justifyContent: 'flex-end',
        alignItems: 'flex-end',
        marginTop: 15,
    },
    button: {
        backgroundColor: '#f2f2f2',
        borderRadius: 10,
        padding: 10,
        flex: 1,
        alignItems: 'center',
        justifyContent: 'center'
    },
    buttonText: {
        color: '#1162BF',
        fontWeight: 'bold',
        textAlign: 'center',
        textAlignVertical: 'center'
    },
    seasonNotStarted: {
        color: 'darkgray',
        alignSelf: 'center',
        marginTop: 20
    },
    entryContainer: {
        padding: 15,
        borderTopWidth: 1,
        borderColor: '#1162BF'
    },
    phaseContainer: {
        marginTop: 10,
    },
    phaseTitle: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1162BF',
        marginBottom: 10,
        marginTop: 10
    }
});

import React, { useState, useEffect, useCallback } from 'react';
import { View, Text, TextInput, StyleSheet, FlatList, TouchableOpacity, Alert, ScrollView, Image } from 'react-native';
import { useNavigation, useRoute, useFocusEffect } from '@react-navigation/native';
import DateTimePickerModal from 'react-native-modal-datetime-picker';
import { Ionicons } from '@expo/vector-icons';
import FormFooter from '../../components/FormFooter';
import Functions from '../../functions/Functions';
import ServerRequest from '../../serverRequests/ServerRequests';
import { KeyboardAwareScrollView } from 'react-native-keyboard-aware-scroll-view';

/**
 * AgendaScreen component represents the screen to manage and display agenda entries.
 * Users can add, edit, delete, and view entries for a specific date, divided into
 * warm-up, specific phase, and final phase tasks.
 * 
 * @returns {JSX.Element} The rendered AgendaScreen component.
 */
const AgendaScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { notebook } = route.params;
    const [entries, setEntries] = useState([]);
    const [newEntryView, setNewEntryView] = useState(false);
    const [editEntryView, setEditEntryView] = useState(false);
    const [page, setPage] = useState(null);
    const [isDatePickerVisible, setDatePickerVisibility] = useState(false);
    const [date, setDate] = useState(null);
    const [id, setId] = useState(null);
    const [datesSet, setDatesSet] = useState({});
    const [loading, setLoading] = useState(false);
    const [hasMore, setHasMore] = useState(true);

    const [warmUpTasks, setWarmUpTasks] = useState([]);
    const [specificPhaseTasks, setSpecificPhaseTasks] = useState([]);
    const [finalPhaseTasks, setFinalPhaseTasks] = useState([]);

    const [taskTextWarmUp, setTaskTextWarmUp] = useState('');
    const [taskTextSpecificPhase, setTaskTextSpecificPhase] = useState('');
    const [taskTextFinalPhase, setTaskTextFinalPhase] = useState('');

    /**
    * Checks if the given date is after today.
    * 
    * @param {Date} dateValue - The date to check.
    * @returns {boolean} True if the date is today or after, false otherwise.
    */
    const dateAfterToday = (dateValue) => {
        const dateUsed = new Date(dateValue);
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        dateUsed.setHours(0, 0, 0, 0);

        return dateUsed >= today;
    }

    /**
     * Shows the date picker modal.
     */
    const showDatePicker = () => {
        setDatePickerVisibility(true);
    };

    /**
     * Hides the date picker modal.
     */
    const hideDatePicker = () => {
        setDatePickerVisibility(false);
    };

    /**
     * Handles the confirmation of the date selection in the date picker.
     * 
     * @param {Date} dateMod - The selected date.
     */
    const handleConfirm = (dateMod) => {
        const today = new Date();
        today.setHours(0, 0, 0, 0);
        dateMod.setHours(0, 0, 0, 0);

        if (dateMod < today) {
            Alert.alert("No se puede seleccionar una fecha anterior a hoy.");
            return;
        }

        setDate(Functions.convertDateEngToSpa(dateMod));
        hideDatePicker();
    };

    /**
     * Deletes a specified entry from the server and updates the entry list.
     * 
     * @param {Object} item - The entry to delete.
     */
    const handleDelete = async (item) => {
        const response = await ServerRequest.deleteEntry(item.id);
        if (response.ok) {
            setHasMore(true);
            setPage(-1);
            setEntries([]);
            setPage(0);
        } else {
            Alert.alert("Error al eliminar la entrada.");
        }
    }

    /**
     * Prepares an entry for editing by setting it in the edit mode.
     * 
     * @param {Object} item - The entry to edit.
     */
    const handleEdit = async (item) => {
        setId(item.id);
        setDate(item.date);
        setWarmUpTasks(item.warmUpExercises);
        setSpecificPhaseTasks(item.specificExercises);
        setFinalPhaseTasks(item.finalExercises);
        setEditEntryView(true);
    }

    /**
     * Saves the edited entry to the server.
     */
    const handleSaveEdit = async () => {
        if (validate()) {
            const editEntry = {
                id: id,
                date: date,
                warmUpExercises: warmUpTasks,
                specificExercises: specificPhaseTasks,
                finalExercises: finalPhaseTasks,
            }
            const response = await ServerRequest.editNotebookEntry(editEntry);
            if (response.ok) {
                setHasMore(true);
                setPage(-1);
                setId(null);
                setEntries([]);
                setWarmUpTasks([]);
                setSpecificPhaseTasks([]);
                setFinalPhaseTasks([]);
                setEditEntryView(false);
                setPage(0);
            }
        }
    }

    /**
     * Fetches notebook entries from the server based on the page number.
     * 
     * @param {number} pageNumber - The page number to fetch.
     */
    const fetchEntries = async (pageNumber) => {
        const response = await ServerRequest.getNotebookEntries(notebook.id, pageNumber);
        const result = await response.json();

        if (response.ok) {
            if (result.data.last) {
                setHasMore(false);
            }
            setEntries((prevEntries) => [...prevEntries, ...result.data.content]);
        } else {
            console.log("Error");
            Alert.alert("Error en la petición al servidor");
            return;
        }

        const responseDatesSet = await ServerRequest.getInvalidDates(notebook.id);
        const resultDatesSet = await responseDatesSet.json();
        if (responseDatesSet.ok) {
            setDatesSet(new Set(resultDatesSet));
        } else {
            console.log("Error");
            Alert.alert("Error en la petición al servidor");
        }
    };

    useEffect(() => {
        if (page !== null && page >= 0) {
            fetchEntries(page);
        }
    }, [page]);

    useFocusEffect(
        useCallback(() => {
            setEntries([]);
            setHasMore(true);
            setPage(0);
            return () => {
                setPage(-1);
            };
        }, [])
    );

    /**
     * Validates the form fields for adding or editing an entry.
     * 
     * @returns {boolean} True if all fields are valid, false otherwise.
     */
    const validate = () => {
        if (!date) {
            Alert.alert("Por favor, selecciona una fecha.");
            return false;
        }
        if (warmUpTasks.length === 0 || specificPhaseTasks.length === 0 || finalPhaseTasks.length === 0) {
            Alert.alert("Por favor, agrega al menos un ejercicio en todas las fases.");
            return false;
        }
        return true;
    }

    /**
     * Saves a new entry to the server if valid and date is available.
     */
    const handleSave = async () => {
        const formIsValid = validate();
        if (formIsValid & !datesSet.has(Functions.convertDateSpaToEng(date))) {
            const newEntry = {
                date: Functions.convertDateSpaToEng(date),
                warmUpExercises: warmUpTasks,
                specificExercises: specificPhaseTasks,
                finalExercises: finalPhaseTasks,
            }
            const response = await ServerRequest.addNotebookEntry(newEntry, notebook.id);
            if (response.ok) {
                setHasMore(true);
                setPage(-1);
                setEntries([]);
                setWarmUpTasks([]);
                setSpecificPhaseTasks([]);
                setFinalPhaseTasks([]);
                setNewEntryView(false);
                setPage(0);
            }
        }

        if (datesSet.has(Functions.convertDateSpaToEng(date))) {
            Alert.alert("Ya existe una entrada para el día especificado.");
        }
    };

    /**
     * Adds a task to the specified task list.
     * 
     * @param {string} text - The task text to add.
     * @param {Function} setTasks - State setter function for the task list.
     * @param {Function} restoreInput - Function to reset the input field.
     */
    const handleAddTask = (text, setTasks, restoreInput) => {
        if (text.trim()) {
            setTasks((prevTasks) => [...prevTasks, text]);
            restoreInput('');
        }
    };

    /**
     * Removes a task from the specified task list.
     * 
     * @param {number} index - The index of the task to remove.
     * @param {Function} setTasks - State setter function for the task list.
     */
    const handleRemoveTask = (index, setTasks) => {
        setTasks((prevTasks) => prevTasks.filter((_, i) => i !== index));
    };

    /**
     * Loads more entries when the user scrolls to the end of the list.
     */
    const loadMoreEntries = () => {
        if (hasMore) {
            setPage((prevPage) => prevPage + 1);
        }
    };

    /**
     * Generates a new entry for the specified date by calling the server.
     */
    const generateEntry = async () => {
        if (date === null) {
            Alert.alert("Indica la fecha para la que deseas generar la clase");
            return;
        }

        if (datesSet.has(Functions.convertDateSpaToEng(date))) {
            Alert.alert("Ya hay una entrada para ese día");
            return;
        }
        setLoading(true);
        const response = await ServerRequest.generateEntry(notebook.id, Functions.convertDateSpaToEng(date));
        const result = await response.json();

        setLoading(false);

        if (response.ok) {
            setWarmUpTasks(result.data.warmUpExercises);
            setSpecificPhaseTasks(result.data.specificExercises);
            setFinalPhaseTasks(result.data.finalExercises);
        } else {
            Alert.alert("Error en la generación. Pruebe más tarde.");
        }
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Agenda</Text>
            </View>
            <View style={styles.content}>
                {!newEntryView & !editEntryView ?
                    (entries.length !== 0 ? (
                        <FlatList
                            data={entries}
                            inverted
                            renderItem={({ item }) => (
                                <View>
                                    <View style={styles.entryHeader}>
                                        <Text style={styles.date}>{Functions.convertDateEngToSpa(item.date)}</Text>
                                        <View style={{ flexDirection: 'row' }}>
                                            {dateAfterToday(item.date) && (
                                                <TouchableOpacity style={styles.edit} onPress={() => handleEdit(item)}>
                                                    <Ionicons name={"pencil-outline"} color={"white"} size={20} />
                                                </TouchableOpacity>
                                            )}
                                            <TouchableOpacity style={styles.delete} onPress={() => handleDelete(item)}>
                                                <Ionicons name={"trash-outline"} color={"white"} size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    </View>
                                    <View style={styles.entryContainer}>
                                        <View style={styles.phaseContainer}>
                                            <Text style={styles.phaseTitle}>Calentamiento</Text>
                                            {item.warmUpExercises.map((task, index) => (
                                                <Text key={index} style={styles.taskText}>• {task}</Text>
                                            ))}
                                        </View>
                                        <View style={styles.phaseContainer}>
                                            <Text style={styles.phaseTitle}>Fase Específica</Text>
                                            {item.specificExercises.map((task, index) => (
                                                <Text key={index} style={styles.taskText}>• {task}</Text>
                                            ))}
                                        </View>
                                        <View style={styles.phaseContainer}>
                                            <Text style={styles.phaseTitle}>Fase Final</Text>
                                            {item.finalExercises.map((task, index) => (
                                                <Text key={index} style={styles.taskText}>• {task}</Text>
                                            ))}
                                        </View>
                                    </View>
                                </View>
                            )}
                            keyExtractor={(item) => item.id.toString()}
                            onEndReached={loadMoreEntries}
                            onEndReachedThreshold={0.1}
                            style={styles.flatList}
                        />
                    ) :
                        <View style={styles.noEntries}>
                            <Text style={styles.noEntriesText}>No hay entradas en la agenda</Text>
                        </View>
                    ) : (
                        null
                    )}

                {newEntryView & !editEntryView ? (
                    <KeyboardAwareScrollView>
                        <View style={styles.newEntryContainer}>
                            <Text style={styles.newEntryTitle}>Nueva entrada</Text>
                            {!loading ?
                                <TouchableOpacity style={styles.gptButton} onPress={generateEntry}>
                                    <Text style={styles.gptTextButton}>Generar clase</Text>
                                    <Image source={require('../../assets/logo-gpt.png')} style={styles.gptIconButton} />
                                </TouchableOpacity>
                                :
                                <View style={styles.gptButton}>
                                    <Text style={styles.gptTextButton}>Generar clase</Text>
                                    <Image source={require('../../assets/loading.gif')} style={styles.gptIconButton} />
                                </View>
                            }
                            <Text style={styles.phaseTitle}>Fecha</Text>
                            <TouchableOpacity onPress={showDatePicker}>
                                <View>
                                    <View style={[styles.buttonDateSelector, { flexDirection: 'row', marginBottom: 10, justifyContent: 'space-between' }]}>
                                        <Text style={styles.dateSelector}>{date}</Text>
                                        <Ionicons name={"calendar"} color={'#1162BF'} size={15} />
                                    </View>
                                </View>
                                <DateTimePickerModal
                                    isVisible={isDatePickerVisible}
                                    mode="date"
                                    onConfirm={handleConfirm}
                                    onCancel={hideDatePicker}
                                />
                            </TouchableOpacity>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Calentamiento</Text>
                                    <Text style={styles.numberExercises}>{warmUpTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {warmUpTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setWarmUpTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextWarmUp}
                                        onChangeText={setTaskTextWarmUp}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextWarmUp, setWarmUpTasks, setTaskTextWarmUp)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>

                            </View>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Fase Específica</Text>
                                    <Text style={styles.numberExercises}>{specificPhaseTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {specificPhaseTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setSpecificPhaseTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextSpecificPhase}
                                        onChangeText={setTaskTextSpecificPhase}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextSpecificPhase, setSpecificPhaseTasks, setTaskTextSpecificPhase)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>
                            </View>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Fase Final</Text>
                                    <Text style={styles.numberExercises}>{finalPhaseTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {finalPhaseTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setFinalPhaseTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextFinalPhase}
                                        onChangeText={setTaskTextFinalPhase}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextFinalPhase, setFinalPhaseTasks, setTaskTextFinalPhase)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                        <View style={styles.footerContainer}>
                            <FormFooter cancel={{
                                function: () => {
                                    setEditEntryView(false);
                                    setDate(null);
                                    setWarmUpTasks([]);
                                    setSpecificPhaseTasks([]);
                                    setFinalPhaseTasks([]);
                                    setTaskTextWarmUp('');
                                    setTaskTextSpecificPhase('');
                                    setTaskTextFinalPhase('');
                                    setNewEntryView(false)
                                }, text: "Cancelar"
                            }} save={{ function: handleSave, text: "Guardar" }} />
                        </View>
                    </KeyboardAwareScrollView>
                ) : (!editEntryView ? (
                    <View style={{ backgroundColor: 'lightgray' }}>
                        <TouchableOpacity style={styles.button} onPress={() => setNewEntryView(true)}>
                            <Text style={styles.buttonText}>Nueva entrada</Text>
                            <Ionicons name="add" size={24} color="white" />
                        </TouchableOpacity>
                    </View>
                ) : (
                    <KeyboardAwareScrollView>
                        <View style={styles.newEntryContainer}>
                            <Text style={styles.newEntryTitle}>Editar entrada</Text>
                            <Text style={styles.phaseTitle}>Fecha</Text>
                            <View>
                                <View style={[styles.buttonDateSelector, { flexDirection: 'row', marginBottom: 10, justifyContent: 'space-between' }]}>
                                    <Text style={styles.dateSelector}>{Functions.convertDateEngToSpa(date)}</Text>
                                    <Ionicons name={"calendar"} color={'#1162BF'} size={15} />
                                </View>
                            </View>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Calentamiento</Text>
                                    <Text style={styles.numberExercises}>{warmUpTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {warmUpTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setWarmUpTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextWarmUp}
                                        onChangeText={setTaskTextWarmUp}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextWarmUp, setWarmUpTasks, setTaskTextWarmUp)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>

                            </View>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Fase Específica</Text>
                                    <Text style={styles.numberExercises}>{specificPhaseTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {specificPhaseTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setSpecificPhaseTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextSpecificPhase}
                                        onChangeText={setTaskTextSpecificPhase}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextSpecificPhase, setSpecificPhaseTasks, setTaskTextSpecificPhase)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>
                            </View>

                            <View style={styles.phaseContainer}>
                                <View style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <Text style={styles.phaseTitle}>Fase Final</Text>
                                    <Text style={styles.numberExercises}>{finalPhaseTasks.length} ejercicios</Text>
                                </View>
                                <ScrollView style={styles.addNewTaskScrollView}>
                                    {finalPhaseTasks.map((task, index) => (
                                        <View key={index} style={styles.addNewTask}>
                                            <Text style={styles.taskText}>• {task}</Text>
                                            <TouchableOpacity onPress={() => handleRemoveTask(index, setFinalPhaseTasks)}>
                                                <Ionicons name="remove-outline" color="red" size={20} />
                                            </TouchableOpacity>
                                        </View>
                                    ))}
                                </ScrollView>
                                <View style={styles.addNewTask}>
                                    <TextInput
                                        style={styles.input}
                                        placeholder="Añadir ejercicio"
                                        value={taskTextFinalPhase}
                                        onChangeText={setTaskTextFinalPhase}
                                    />
                                    <TouchableOpacity onPress={() => handleAddTask(taskTextFinalPhase, setFinalPhaseTasks, setTaskTextFinalPhase)}>
                                        <Ionicons name="add-outline" color="green" size={20} />
                                    </TouchableOpacity>
                                </View>
                            </View>
                        </View>
                        <View style={styles.footerContainer}>
                            <FormFooter cancel={{
                                function: () => {
                                    setEditEntryView(false);
                                    setDate(null);
                                    setWarmUpTasks([]);
                                    setSpecificPhaseTasks([]);
                                    setFinalPhaseTasks([]);
                                    setTaskTextWarmUp('');
                                    setTaskTextSpecificPhase('');
                                    setTaskTextFinalPhase('');
                                }, text: "Cancelar"
                            }} save={{ function: handleSaveEdit, text: "Guardar" }} />
                        </View>
                    </KeyboardAwareScrollView>
                )
                )}
            </View>
        </View >
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
    },
    header: {
        paddingTop: 20,
        paddingBottom: 10,
        paddingHorizontal: 20,
    },
    pageTitle: {
        fontSize: 26,
        fontWeight: 'bold',
        color: '#333',
    },
    content: {
        flex: 1,
        backgroundColor: '#fff',
    },
    noEntries: {
        flex: 1,
        justifyContent: 'center'
    },
    noEntriesText: {
        textAlignVertical: 'center',
        textAlign: 'center',
        color: 'gray'
    },
    flatList: {
        borderTopWidth: 1,
        borderBottomWidth: 1,
        borderColor: 'lightgray'
    },
    entryContainer: {
        padding: 15,
        borderTopWidth: 1,
        borderColor: '#1162BF'
    },
    newEntryContainer: {
        padding: 15,
        borderTopWidth: 1,
        borderColor: '#1162BF',
        backgroundColor: '#ddeeff',
    },
    entryHeader: {
        backgroundColor: '#1162BF',
        paddingHorizontal: 20,
        flexDirection: 'row',
        justifyContent: 'space-between'
    },
    date: {
        paddingTop: 10,
        fontSize: 18,
        fontWeight: '600',
        color: 'white',
        marginBottom: 10,
    },
    edit: {
        marginVertical: 10,
        marginRight: 20
    },
    delete: {
        marginVertical: 10
    },
    newEntryTitle: {
        paddingTop: 10,
        fontSize: 18,
        fontWeight: '600',
        color: '#666',
        marginBottom: 10,
        textAlign: 'center'
    },
    buttonDateSelector: {
        backgroundColor: 'white',
        borderRadius: 10,
        padding: 10
    },
    dateSelector: {
        color: '#1162BF',
        fontSize: 14,
        fontWeight: '600',
    },
    phaseContainer: {
        marginTop: 10,
        borderTopWidth: 1
    },
    phaseTitle: {
        fontSize: 16,
        fontWeight: '600',
        color: '#1162BF',
        marginBottom: 10,
        marginTop: 10
    },
    taskText: {
        fontSize: 14,
        color: '#333',
        marginLeft: 10,
        marginVertical: 2,
        width: '90%',
        marginBottom: 10
    },
    inputContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingHorizontal: 20,
        paddingVertical: 10,
        backgroundColor: '#fff',
        borderTopWidth: 1,
        borderColor: '#ddd',
    },
    input: {
        height: 30,
        borderColor: '#ddd',
        borderWidth: 1,
        borderRadius: 8,
        paddingHorizontal: 12,
        fontSize: 14,
        backgroundColor: '#fff',
        width: '90%',
        marginRight: 10
    },
    buttonText: {
        color: 'white',
        fontWeight: 'bold'
    },
    button: {
        flexDirection: 'row',
        backgroundColor: '#1162BF',
        padding: 10,
        borderRadius: 8,
        margin: 20,
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    addNewTask: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        alignItems: 'center',
    },

    numberExercises: {
        textAlignVertical: 'center',
        fontSize: 16,
        fontWeight: '600',
        color: 'black',
        marginBottom: 10,
        marginTop: 10
    },
    gptButton: {
        paddingVertical: 10,
        paddingHorizontal: 20,
        backgroundColor: 'black',
        borderRadius: 100,
        alignItems: 'center',
        marginHorizontal: 100,
        flexDirection: 'row',
        justifyContent: 'space-between'
    },
    gptTextButton: {
        color: '#fff',
        fontSize: 14
    },
    gptIconButton: {
        width: 20,
        height: 20,
        marginLeft: 10
    }
});

export default AgendaScreen;

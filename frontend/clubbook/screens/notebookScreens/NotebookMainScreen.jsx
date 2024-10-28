import React, { useCallback, useState } from "react";
import { View, StyleSheet, TouchableOpacity, Text, Alert, FlatList } from "react-native";
import { useFocusEffect, useNavigation } from '@react-navigation/native';
import ServerRequest from "../../serverRequests/ServerRequests";


const NotebookMainScreen = () => {
    const navigation = useNavigation();
    const [season, setSeason] = useState(true);
    const [message, setMessage] = useState('');
    const [notebooks, setNotebooks] = useState([]);

    const getFromServer = async () => {
        const response = await ServerRequest.getNotebooks();
        const result = await response.json();

        if (response.ok) {
            setNotebooks(result.data);
        } else {
            if (response.status === 400) {
                setSeason(false);
                setMessage(result.message)
            } else {
                Alert.alert("Error en la comunicación con el servidor.");
            }
        }
    }

    useFocusEffect(
        useCallback(() => {
            getFromServer();
        }, [])
    );

    const renderClassgroup = ({ item }) => (
        <TouchableOpacity style={styles.button} onPress={() => navigation.navigate("ClassGroupAgendaScreen", { notebookBasicInfo: item})}>
            <Text style={styles.buttonText}>{item.classGroupName}</Text>
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Agendas disponibles</Text>
            </View>
            <View style={styles.content}>
                {season ?
                    <FlatList
                    data={notebooks}
                    renderItem={renderClassgroup}
                    keyExtractor={(item) => item.notebookId.toString()} 
                    contentContainerStyle={styles.content}
                />
                    :
                    <Text style={styles.seasonNotStarted}>{message}</Text>
                }
            </View>
        </View>
    )
}

export default NotebookMainScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        paddingTop: 20,
        paddingLeft: 20,
        paddingRight: 20
    },
    header: {
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    button: {
        width: '100%',
        justifyContent: 'center',
        alignItems: 'center',
        borderWidth: 1,
        borderColor: '#1162BF',
        borderRadius: 5,
        padding: 20,
        marginBottom: 20
    },
    buttonText: {
        fontSize: 18,
        fontWeight: '500',
        color: '#1162BF'
    },
    content: {
        marginTop: 20
    },
    seasonNotStarted: {
        color: 'darkgray',
        alignSelf: 'center',
        marginTop: 20
    }
});
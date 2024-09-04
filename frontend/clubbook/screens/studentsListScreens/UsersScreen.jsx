import React, { useState, useEffect } from "react";
import { ScrollView, View, Text, StyleSheet, Alert, TouchableOpacity } from "react-native";
import UsersFlatList from '../../components/UsersFlatListPage';
import ServerRequest from "../../serverRequests/ServerRequests";
import { useNavigation } from '@react-navigation/native';
import { useRoute } from '@react-navigation/native';

const UsersScreen = () => {
    const [userList, setUserList] = useState([]);
    const [page, setPage] = useState(0);
    const [maxUsers, setMaxUsers] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const route = useRoute();
    const { key } = route.params;
    const navigation = useNavigation();

    const serverFunctionMap = {
        student: ServerRequest.getStudentsPage,
        teacher: ServerRequest.getTeachersPage
    };

    const getUsers = async () => {
        const data = await ServerRequest.getTokenAndId();
        try {
            const response = await serverFunctionMap[key](data, page);

            if (!response.ok) {
                Alert.alert('Error', 'Error al cargar los datos.');
                return;
            }

            const result = await response.json();
            setMaxUsers(result.totalElements);
            setUserList(prevUsers => [...prevUsers, ...result.content]);
            setHasMore(!result.last);
        } catch (error) {
            Alert.alert('Error', 'Error al cargar los datos.');
        }
    };

    useEffect(() => {
        getUsers();
    }, [page])

    const loadMoreUsers = () => {
        if (hasMore) {
            setPage(prevPage => prevPage + 1);
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{key === 'student' ? 'Alumnos' : 'Profesores'}</Text>
                <View style={styles.subheader}>
                    <TouchableOpacity style={styles.searchbarcontainer} onPress={() => navigation.navigate('Searcher', { key })}>
                        <Text style={styles.searchbar}>Buscar</Text>
                    </TouchableOpacity>
                </View>
            </View>
            <View>
                <Text style={styles.filter}>Filtros:</Text>
                <ScrollView horizontal style={styles.horizontalScrollView} contentContainerStyle={styles.horizontalContentContainer}>
                    {['Clase 1', 'Clase 2', 'Clase 3', 'Clase 4'].map((filter, index) => (
                        <TouchableOpacity key={index} style={styles.filterbox}>
                            <Text style={styles.filtertext}>{filter}</Text>
                        </TouchableOpacity>
                    ))}
                </ScrollView>
            </View>

            <UsersFlatList users={userList} maxUsers={maxUsers} loadMoreUsers={loadMoreUsers} />
        </View>
    );
};

export default UsersScreen;

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
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        marginTop: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    searchbarcontainer: {
        width: '100%'
    },
    searchbar: {
        height: 40,
        borderWidth: 1,
        borderColor: '#ccc',
        padding: 10,
        borderRadius: 5,
        width: '100%',
        backgroundColor: '#fff',
        color: 'gray'
    },
    horizontalScrollView: {
        height: 40,
        marginBottom: 20
    },
    filter: {
        color: '#1162BF',
        fontSize: 16,
        paddingBottom: 10
    },
    filterbox: {
        backgroundColor: '#1162BF',
        borderRadius: 5,
        marginRight: 20,
        padding: 10
    },
    filtertext: {
        color: 'white',
        fontWeight: 'medium'
    },
});

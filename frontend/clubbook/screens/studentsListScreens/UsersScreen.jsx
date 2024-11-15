import React, { useState, useEffect, useCallback } from "react";
import { View, Text, StyleSheet, Alert, TouchableOpacity } from "react-native";
import { useNavigation } from '@react-navigation/native';
import { useRoute } from '@react-navigation/native';
import { useFocusEffect } from '@react-navigation/native';
import UsersFlatList from '../../components/UsersFlatListPage';
import ServerRequest from "../../serverRequests/ServerRequests";

/**
 * UsersScreen component that displays a list of users (students or teachers)
 * and allows for pagination and searching.
 *
 * @component
 * @returns {JSX.Element} The UsersScreen component.
 */
const UsersScreen = () => {
    const [userList, setUserList] = useState([]);
    const [page, setPage] = useState(0);
    const [maxUsers, setMaxUsers] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const route = useRoute();
    const { key } = route.params;
    const navigation = useNavigation();

    const [emptyMessage, setEmptyMessage] = useState('');

    // Mapping user type to corresponding server request function
    const serverFunctionMap = {
        student: ServerRequest.getStudentsPage,
        teacher: ServerRequest.getTeachersPage
    };

    /**
     * Fetches users from the server based on the current page and user type.
     * Updates the user list and handles empty responses and errors.
     *
     * @async
     * @function getUsers
     * @returns {Promise<void>} A promise that resolves when the fetch operation is complete.
     */
    const getUsers = async () => {
        try {
            const response = await serverFunctionMap[key](page);

            if (response.ok) {
                const responseData = await response.json();
                setMaxUsers(responseData.data.totalElements);
                setUserList(prevUsers => [...prevUsers, ...responseData.data.content]);
                setHasMore(!responseData.data.last);
            } else if (response.status === 400) {
                const responseData = await response.json();
                setEmptyMessage('No hay usuarios disponibles.\n' + responseData.message);
                return;
            } else {
                setEmptyMessage('No hay usuarios disponibles.\n');
                Alert.alert('Error', 'Error al cargar los datos.');
                return;
            }

        } catch (error) {
            Alert.alert('Error', 'Error al cargar los datos.');
        }
    };

    /**
     * Resets the user list and pagination state when the screen is focused.
     *
     * @function useFocusEffect
     * @returns {void}
     */
    useFocusEffect(
        useCallback(() => {
            setUserList([]);
            setMaxUsers(0);
            setPage(0);
            setEmptyMessage('');
            return () => {
                setPage(-1);
        };
        }, [])
    );

    useEffect(() => {
        if (page >= 0) {
            getUsers();
        }
    }, [page]);

    /**
     * Loads more users by incrementing the page state if more users are available.
     *
     * @function loadMoreUsers
     * @returns {void}
     */
    const loadMoreUsers = () => {
        if (hasMore) {
            setPage(prevPage => prevPage + 1);
        }
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{key === 'student' ? 'Alumnos' : 'Profesores'}</Text>
                {userList.length !== 0 &&
                    <View style={styles.subheader}>
                        <TouchableOpacity style={styles.searchbarcontainer} onPress={() => navigation.navigate('Searcher', { key })}>
                            <Text style={styles.searchbar}>Buscar</Text>
                        </TouchableOpacity>
                    </View>
                }
            </View>

            {userList.length !== 0 ? (
                <UsersFlatList users={userList} maxUsers={maxUsers} loadMoreUsers={loadMoreUsers} />
            ) : (
                <View style={styles.emptyContainer}>
                    <Text style={styles.emptyMessage}>{emptyMessage}</Text>
                </View>
            )}
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

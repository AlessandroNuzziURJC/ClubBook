import React, { useState, useEffect } from "react";
import { ScrollView, View, Text, StyleSheet, TextInput, Alert, TouchableOpacity } from "react-native";
import UsersFlatList from '../../components/UsersFlatList';
import ServerRequest from "../../serverRequests/ServerRequests";
import SearchUser from '../../components/SearchUser';
import { useRoute } from '@react-navigation/native';

const UsersScreen = () => {
    const [userList, setUserList] = useState([]);
    const [page, setPage] = useState(0);
    const [hasMore, setHasMore] = useState(true);
    const [maxUsers, setMaxUsers] = useState(0);
    const [isSearching, setIsSearching] = useState(false);
    const route = useRoute();
    const { key } = route.params;

    const serverFunctionMap = {
        student: ServerRequest.getStudentsPage,
        teacher: ServerRequest.getTeachersPage
    }

    const getUsers = async () => {
        if (!hasMore || isSearching) return;

        const data = await ServerRequest.getTokenAndId();

        try {
            const response = await serverFunctionMap[key](data, page);

            if (!response.ok) {
                Alert.alert('Error', 'Error al cargar los datos.');
                setHasMore(false);
                return;
            }

            const result = await response.json();
            setMaxUsers(result.totalElements);
            setUserList(prevUsers => [...prevUsers, ...result.content]);
            setHasMore(!result.content.last);
        } catch (error) {
            Alert.alert('Error', 'Error al cargar los datos.');
        }
    };

    useEffect(() => {
        getUsers();
    }, [page, isSearching]);

    const loadMoreUsers = () => {
        if (hasMore) {
            setPage(prevPage => prevPage + 1);
        }
    };

    const handleSearchResults = (data) => {
        setIsSearching(true);
        setUserList(data);
    };

    const handleSearchClear = () => {
        setIsSearching(false);
        setUserList([]);
        setPage(0);
        getUsers();
    };

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <View style={styles.subheader}>
                    <Text style={styles.pageTitle}>{key === 'student' ? 'Alumnos' : 'Profesores'}</Text>
                </View>
                <View style={styles.subheader}>
                    <SearchUser
                        usersSearch={handleSearchResults}
                        onSearchClear={handleSearchClear}
                        userType={key}
                    />
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
    icon: {
        width: 20,
        height: 20
    },
    image: {
        width: 70,
        height: 70,
        borderRadius: 100,
        marginRight: 20,
        alignSelf: 'center',
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
    horizontalScrollView: {
        height: 40,
        marginBottom: 20
    },
    horizontalContentContainer: {
        alignItems: 'center'
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
    content: {
        flex: 1
    },
    column: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    profileinfo: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%'
    },
    rows: {
        borderTopWidth: 1,
        flex: 1,
        paddingTop: 10
    },
    contact: {
        flexDirection: 'row',
        alignItems: 'center',
        marginTop: 5
    },
    name: {
        fontWeight: 'bold',
        fontSize: 16
    },
    phonenumber: {
        fontSize: 14,
        color: 'gray'
    },
    marginionicon: {
        marginRight: 5
    },
    seemore: {
        alignSelf: 'flex-end',
        color: '#1162BF',
        fontWeight: 'bold',
        marginTop: 15,
        marginBottom: 10
    }
});

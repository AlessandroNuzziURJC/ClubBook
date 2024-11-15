import React, { useState, useEffect } from "react";
import { useNavigation } from "@react-navigation/native";
import { useRoute } from '@react-navigation/native';
import { View, Text, StyleSheet, FlatList, RefreshControl, Alert, Image, TextInput } from "react-native";
import Checkbox from 'expo-checkbox';
import ServerRequests from "../../serverRequests/ServerRequests";
import Functions from "../../functions/Functions";
import ServerRequest from "../../serverRequests/ServerRequests";
import FormFooter from "../../components/FormFooter";

/**
 * DeleteUsersListScreen component allows administrators to delete users (students, teachers, or administrators) from the app.
 * It fetches user data from the server, displays a list of users with checkboxes for selection, and allows for user deletion.
 */
const DeleteUsersListScreen = () => {
    const navigation = useNavigation();
    const route = useRoute();
    const { role } = route.params;

    const [users, setUsers] = useState([]);
    const [refreshing, setRefreshing] = useState(false);
    const [userImages, setUserImages] = useState([]);
    const [searchTerm, setSearchTerm] = useState("");
    const [checkedUsers, setCheckedUsers] = useState([]);
    const [filteredUsers, setFilteredUsers] = useState([]);
    const [userImagesFiltered, setUserImagesFiltered] = useState([]);
    const [currentPage, setCurrentPage] = useState(-1);
    const [hasMore, setHasMore] = useState(true);
    const [debounceTimeout, setDebounceTimeout] = useState(null);

    /**
     * Gets the title of the screen based on the user role.
     * @returns {string} Title for the screen.
     */
    const getTitle = () => {
        switch (role) {
            case 'administrator':
                return 'Eliminar administradores';
            case 'teacher':
                return 'Eliminar profesores';
            case 'student':
                return 'Eliminar alumnos';
            default:
                return 'Eliminar usuarios';
        }
    };

    /**
     * Fetches user data from the server based on the user role and current page.
     */
    const getFromServer = async () => {
        const selectFunction = {
            student: ServerRequest.getStudentsPage,
            teacher: ServerRequest.getTeachersPage,
        }
        const response = await selectFunction[role](currentPage);
        const result = await response.json();
        setUsers((prevUsers) => [...prevUsers, ...result.data.content]);
        setFilteredUsers((prevUsers) => [...prevUsers, ...result.data.content]);

        if (result.data.content.length < 10) {
            setHasMore(false);
        }

        result.data.content.forEach((user, index) => {
            getProfilePicture(user.id, index + currentPage*10);
        });
    }

    /**
     * Fetches the profile picture for a specific user.
     * @param {number} id - The user's ID.
     * @param {number} index - The index of the user in the user list.
     */
    const getProfilePicture = async (id, index) => {
        const response = await ServerRequests.getUserPhoto(id);
        if (response.ok) {
            const blob = await response.blob();
            const image = await Functions.blobToBase64(blob);
            if (image != null) {
                setUserImages((prevImages) => {
                    const newImages = [...prevImages];
                    newImages[index] = { uri: image };
                    return newImages;
                });
                setUserImagesFiltered((prevImages) => {
                    const newImages = [...prevImages];
                    newImages[index] = { uri: image };
                    return newImages;
                });
            }
        } else {
            setUserImages((prevImages) => {
                const newImages = [...prevImages];
                newImages[index] = require('../../assets/error.png');
                return newImages;
            });
            setUserImagesFiltered((prevImages) => {
                const newImages = [...prevImages];
                newImages[index] = require('../../assets/error.png');
                return newImages;
            });
        }
    };

    /**
     * Refreshes the user data by resetting the state and fetching data from the server.
     */
    const refreshData = () => {
        setRefreshing(true);
        setCurrentPage(-1);
        setUsers([]);
        setFilteredUsers([]);
        setUserImages([]);
        setUserImagesFiltered([]);
        setCheckedUsers([]);
        setCurrentPage(0);
        setTimeout(() => {
            getFromServer();
            setRefreshing(false);
        }, 500);
    };

    useEffect(() => {
        if (currentPage >= 0) {
            getFromServer();
        }
    }, [currentPage]);

    /**
     * Loads more users when the user reaches the end of the list.
     */
    const loadMoreUsers = () => {
        if (hasMore) {
            setCurrentPage(prevPage => prevPage + 1);
        }
    };

    useEffect(() => {
        if (debounceTimeout) {
            clearTimeout(debounceTimeout);
        }
        const timeout = setTimeout(() => {
            handleSearch();
        }, 500);

        setDebounceTimeout(timeout);

        return () => clearTimeout(timeout);
    }, [searchTerm]);

    /**
     * Handles the search functionality for users.
     * It fetches users based on the search term.
     */
    const handleSearch = async () => {
        if (searchTerm.trim() === '') {
            setFilteredUsers([]);
            setUserImagesFiltered([]);
            setCurrentPage(-1);
            setCurrentPage(0);
        } else {            
            const serverFunctionMap = {
                student: ServerRequest.getStudentsSearchPage,
                teacher: ServerRequest.getTeachersSearchPage
            };

            const response = await serverFunctionMap[role](searchTerm);
            const result = await response.json();
            setFilteredUsers(result.data);
            setUserImagesFiltered([]);
            if (result.data) {
                result.data.forEach((user, index) => {
                    getProfilePicture(user.id, index);
                });
            }
        }
    };

    /**
     * Handles the saving of deleted users.
     * It sends delete requests for each selected user.
     */
    const handleSave = () => {
        checkedUsers.forEach(async (item) => {
            const response = await ServerRequest.deleteUser(item.id);
            const result = await response.json();

            if (response.ok && !result.data) {
                Alert.alert(item.firstName + ' ' + item.lastName, 
                    result.message.replace('<rol>', Functions.translateRole(role)));
            }
        });

        setTimeout(() => {
            setUserImages([]);
            setFilteredUsers([]);
            setUserImagesFiltered([]);
            setUsers([]);
            setCheckedUsers([]);
            setCurrentPage(-1);
            setCurrentPage(0);
        }, 1000);
        
    }

    /**
     * Handles the selection of users with checkboxes.
     * @param {object} item - The user item that is checked or unchecked.
     */
    const handleCheckBox = (item) => {
        setCheckedUsers((prevChecked) => {
            if (prevChecked.find(checkedUser => checkedUser.id === item.id)) {
                return prevChecked.filter(value => value.id !== item.id);
            } else {
                return [...prevChecked, item];
            }
        });
    };

    /**
     * Renders a single user item in the FlatList.
     * @param {object} param - The object containing item and index.
     * @returns {JSX.Element} The rendered user item.
     */
    const renderItem = ({ item, index }) => {
        return (
            <View style={styles.card}>
                <View style={styles.row}>
                    <Image
                        source={userImagesFiltered[index]}
                        style={styles.image}
                    />
                    <Text style={styles.userName}>{item.firstName + ' ' + item.lastName}</Text>
                    <Checkbox
                        value={checkedUsers.some(checkedUser => checkedUser.id === item.id)}
                        onValueChange={() => handleCheckBox(item)}
                    />
                </View>
            </View>
        );
    }

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>{getTitle()}</Text>
            </View>
            <TextInput
                style={styles.searchBar}
                placeholder="Buscar por nombre..."
                value={searchTerm}
                onChangeText={setSearchTerm}
            />
            <FlatList
                data={filteredUsers}
                renderItem={renderItem}
                keyExtractor={(item) => item.id.toString()}
                onEndReached={!searchTerm ? loadMoreUsers : null}
                onEndReachedThreshold={0.5}
                refreshControl={<RefreshControl refreshing={refreshing} onRefresh={refreshData} />}
                style={styles.alignTop}
            />
            <View>
                <FormFooter cancel={{ function: navigation.goBack, text: 'Cancelar' }} save={{ function: handleSave, text: 'Eliminar' }} />
            </View>
        </View>
    )
}

export default DeleteUsersListScreen;

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
    searchBar: {
        borderColor: '#ccc',
        borderWidth: 1,
        borderRadius: 8,
        padding: 10,
        marginBottom: 20,
        fontSize: 16,
    },
    card: {
        backgroundColor: '#ddeeff',
        borderRadius: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 10,
        paddingRight: 20,
        marginBottom: 10,
    },
    row: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 50,
    },
    icon: {
        alignSelf: 'center',
        marginLeft: 10
    },
    image: {
        width: 50,
        height: 50,
        borderRadius: 100,
        marginRight: 20,
        alignSelf: 'center',
    },
    userName: {
        fontSize: 16,
        fontWeight: 'bold',
        flex: 1,
    }
})

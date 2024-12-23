import React, { useEffect, useRef, useState } from 'react';
import { Animated, TextInput, View, StyleSheet, Text } from 'react-native';
import ServerRequest from '../serverRequests/ServerRequests';
import UsersFlatListNotPaged from './UsersFlatListNotPaged';
import { useRoute } from '@react-navigation/native';

/**
 * SearchUser component allows users to search for either students or teachers based on input text.
 * It performs a debounced server-side search query and displays matching results in a flat list.
 * 
 * @component
 * @returns {JSX.Element} The rendered component for searching users.
 */
const SearchUser = () => {
    const [text, setSearchText] = useState('');
    const [debounceTimeout, setDebounceTimeout] = useState(null);
    const [users, setUsers] = useState([]);
    const inputRef = useRef(null);
    const route = useRoute();
    const { key } = route.params;
    const fadeAnim = useRef(new Animated.Value(0)).current;

    /**
     * Triggers the fade-in animation and focuses the search input field on component mount.
     */
    useEffect(() => {
        fadeIn();
        inputRef.current.focus();
    }, []);

    /**
     * Debounces the search input and triggers the search function after a delay.
     */
    useEffect(() => {
        if (debounceTimeout) {
            clearTimeout(debounceTimeout);
        }
        setDebounceTimeout(setTimeout(handleSearch, 500));
    }, [text]);

    /**
     * Executes a search request to the server based on the input text and user type.
     * Updates the users state with the fetched results.
     * 
     * @async
     */
    const handleSearch = async () => {
        const serverFunctionMap = {
            student: ServerRequest.getStudentsSearchPage,
            teacher: ServerRequest.getTeachersSearchPage
        };
        let textAux = text.trim();
        if (textAux !== '') {
            const response = await serverFunctionMap[key](textAux);
            const result = await response.json();
            setUsers(result.data);
        } else {
            setUsers([]);
        }
    };

    /**
     * Triggers a fade-in animation effect for the component.
     */
    const fadeIn = () => {
        Animated.timing(fadeAnim, {
            toValue: 1,
            duration: 500,
            useNativeDriver: true,
        }).start();
    };

    return (
        <Animated.View
            style={[
                styles.container,
                {
                    opacity: fadeAnim,
                },
            ]}>
            <View style={styles.header}>
                <Text style={styles.pageTitle}>Buscar</Text>
                <View style={styles.subheader}>
                    <TextInput
                        ref={inputRef}
                        style={styles.searchbar}
                        placeholder={'Introduce el nombre'}
                        onChangeText={setSearchText}
                        value={text}
                    />
                </View>
            </View>
            <UsersFlatListNotPaged users={users} />
        </Animated.View>
    );
};

export default SearchUser;

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
    searchbar: {
        height: 40,
        borderWidth: 1,
        borderColor: '#ccc',
        padding: 10,
        borderRadius: 5,
        width: '100%',
        backgroundColor: '#fff',
    },
});

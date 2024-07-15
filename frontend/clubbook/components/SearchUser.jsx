import React, { useState, useEffect } from 'react';
import { TextInput, View, StyleSheet } from 'react-native';
import ServerRequest from '../serverRequests/ServerRequests';

const SearchUser = ({ usersSearch, onSearchClear, key }) => {
    const [text, setSearchText] = useState('');
    const [debounceTimeout, setDebounceTimeout] = useState(null);

    const serverFunctionMap = {
        student: ServerRequest.getStudentsSearchPage,
        teacher: ServerRequest.getTeachersSearchPage
    }

    useEffect(() => {
        if (debounceTimeout) {
            clearTimeout(debounceTimeout);
        }

        const timeoutId = setTimeout(() => {
            if (text.trim() !== '') {
                handleSearch(text);
            } else {
                onSearchClear();
            }
        }, 500);

        setDebounceTimeout(timeoutId);

        return () => {
            clearTimeout(timeoutId);
        };
    }, [text]);

    const handleSearch = async (searchText) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await serverFunctionMap[key](data, searchText);
        const result = await response.json();
        usersSearch(result);
    };

    return (
        <View style={styles.container}>
            <TextInput
                style={styles.searchbar}
                placeholder={'Introduce el nombre'}
                onChangeText={setSearchText}
                value={text}
            />
        </View>
    );
};

export default SearchUser;

const styles = StyleSheet.create({
    container: {
        flex: 1,
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

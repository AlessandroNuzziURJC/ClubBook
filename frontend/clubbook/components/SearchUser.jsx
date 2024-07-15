import React, { useState, useEffect } from 'react';
import { TextInput, View, StyleSheet } from 'react-native';
import ServerRequests from '../serverRequests/ServerRequests';

const SearchUser = ({ usersSearch, onSearchClear }) => {
    const [text, setSearchText] = useState('');
    const [debounceTimeout, setDebounceTimeout] = useState(null);

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
        const data = await ServerRequests.getTokenAndId();
        const response = await ServerRequests.getStudentsSearchPage(data, searchText);
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

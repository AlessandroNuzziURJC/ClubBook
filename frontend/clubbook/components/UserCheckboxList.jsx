import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import Checkbox from 'expo-checkbox';

/**
 * UserCheckboxList component displays a list of users with checkboxes to select them.
 *
 * @param {Array} users - An array of user objects, each containing an id, firstName, lastName, and selected state.
 * @param {boolean} usersError - Indicates if there is an error related to users (for styling purposes).
 * @param {function} handleSelectUser - Callback function to handle user selection, receiving the user id as an argument.
 * @returns {JSX.Element} The rendered user checkbox list.
 */
const UserCheckboxList = ({ users, usersError, handleSelectUser }) => {
    return (
        <View style={styles.userlist}>
            {users.map(item => (
                <View key={item.id} style={[styles.scheduleContainer, usersError && styles.errorBackground]}>
                    <View style={styles.checkboxContainer}>
                        <Text style={styles.userName}>{item.firstName} {item.lastName}</Text>
                        <Checkbox
                            value={item.selected}
                            onValueChange={() => handleSelectUser(item.id)}
                            style={styles.checkbox}
                        />
                    </View>
                </View>
            ))}
        </View>
    );
};

export default UserCheckboxList;

const styles = StyleSheet.create({
    userlist: {
        marginBottom: 20
    },
    scheduleContainer: {
        backgroundColor: '#ddeeff',
        marginBottom: 10,
        paddingTop: 10,
        paddingBottom: 10,
        paddingLeft: 20,
        paddingRight: 20,
        borderRadius: 5
    },
    checkboxContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        height: 40,
    },
    userName: {
        fontSize: 16,
    },
    checkbox: {},
    errorBackground: {
        borderWidth: 2,
        borderColor: 'red'
    },
});

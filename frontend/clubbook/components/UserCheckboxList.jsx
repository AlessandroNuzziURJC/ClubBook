import React from 'react';
import { View, Text, StyleSheet } from 'react-native';
import Checkbox from 'expo-checkbox';

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

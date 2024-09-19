import React from 'react';
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';
import { Ionicons } from "@expo/vector-icons";

const AttendanceSelector = ({ users, usersError, handleSelectUser }) => {

    const handleCheck = (user) => {
        handleSelectUser(user.id, true);  // Notifica al componente padre que el usuario está presente
    };

    const handleCross = (user) => {
        handleSelectUser(user.id, false);  // Notifica al componente padre que el usuario está ausente
    };

    return (
        <View style={styles.userlist}>
            {users.map(item => {
                const backgroundColor = item.selected === null
                    ? '#ddeeff'
                    : item.selected
                        ? '#d4edda'
                        : '#f8d7da';

                return (
                    <View key={item.id} style={[styles.scheduleContainer, { backgroundColor }, usersError && styles.errorBackground]}>
                        <TouchableOpacity style={[styles.divisor, styles.textAlignLeft]} onPress={() => handleCross(item)}>
                            <Ionicons name="close" color={'red'} size={30} />
                        </TouchableOpacity>
                        <View style={[styles.divisorMid, styles.textAlignCenter]}>
                            <View style={styles.nameContainer}>
                                {item.selected === false && (
                                    <Ionicons name="close-circle" color="red" size={20} style={styles.statusIcon} />
                                )}
                                <Text style={styles.userName}>
                                    {item.firstName} {item.lastName}
                                </Text>
                                {item.selected === true && (
                                    <Ionicons name="checkmark-circle" color="green" size={20} style={styles.statusIcon} />
                                )}
                            </View>
                        </View>
                        <TouchableOpacity style={[styles.divisor, styles.textAlignRight]} onPress={() => handleCheck(item)}>
                            <Ionicons name="checkmark" color={'green'} size={30} />
                        </TouchableOpacity>
                    </View>
                );
            })}
        </View>
    );
};

export default AttendanceSelector;

const styles = StyleSheet.create({
    scheduleContainer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingBottom: 10,
        alignItems: 'center',
        padding: 10,
        borderRadius: 10,
        marginBottom: 10,
    },
    divisor: {
        width: '10%',
    },
    divisorMid: {
        width: '80%',
    },
    textAlignCenter: {
        alignItems: 'center',
    },
    textAlignLeft: {
        alignItems: 'flex-start',
    },
    textAlignRight: {
        alignItems: 'flex-end',
    },
    nameContainer: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    userName: {
        fontWeight: 'bold',
        marginHorizontal: 5,
    },
    statusIcon: {
        marginHorizontal: 5,
    },
    errorBackground: {
        backgroundColor: '#f8d7da',
    },
});

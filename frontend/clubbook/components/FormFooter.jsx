import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

const FormFooter = ({ cancel, save }) => {
    return (
        <View style={styles.footer}>
            <TouchableOpacity style={styles.cancelButton} onPress={cancel.function}>
                <Text style={styles.buttonText}>{cancel.text}</Text>
            </TouchableOpacity>
            <TouchableOpacity style={styles.saveButton} onPress={save.function}>
                <Text style={styles.buttonText}>{save.text}</Text>
            </TouchableOpacity>
        </View>
    );
}

export default FormFooter;

const styles = StyleSheet.create({
    footer: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        backgroundColor: 'white',
        padding: 10,
        borderTopWidth: 1,
        borderColor: '#ddd',
    },
    saveButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center',
    },
    cancelButton: {
        padding: 10,
        borderRadius: 5,
        width: '45%',
        alignItems: 'center',
    },
    buttonText: {
        color: '#1162BF',
        fontWeight: 'bold',
        fontSize: 16,
    },
})
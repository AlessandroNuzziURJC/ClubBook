import React from "react";
import { View, Text, StyleSheet, TouchableOpacity } from 'react-native';

/**
 * FormFooter Component.
 * 
 * Renders a footer with "Cancel" and "Save" buttons, typically used at the bottom of a form. 
 * Each button triggers its respective action when pressed.
 * 
 * @component
 * @param {Object} props - Component properties.
 * @param {Object} props.cancel - Configuration for the cancel button.
 * @param {string} props.cancel.text - Text to display on the cancel button.
 * @param {Function} props.cancel.function - Function to execute when the cancel button is pressed.
 * @param {Object} props.save - Configuration for the save button.
 * @param {string} props.save.text - Text to display on the save button.
 * @param {Function} props.save.function - Function to execute when the save button is pressed.
 * @returns {JSX.Element} - A footer containing cancel and save buttons.
 */
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
import React from 'react';
import { View, Text, Modal, StyleSheet } from 'react-native';

/**
 * Toast component displays a temporary message overlay on the screen.
 *
 * @param {boolean} visible - Indicates if the toast should be visible.
 * @param {string} message - The message to display in the toast.
 * @param {function} onClose - Callback function to close the toast.
 * @returns {JSX.Element|null} The rendered toast component or null if not visible.
 */
const Toast = ({ visible, message, onClose }) => {
    if (!visible) return null;

    return (
        <Modal
            transparent={true}
            animationType="fade"
            visible={visible}
            onRequestClose={onClose}
        >
            <View style={styles.container}>
                <View style={styles.toast}>
                    <Text style={styles.message}>{message}</Text>
                </View>
            </View>
        </Modal>
    );
};

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'flex-end',
        alignItems: 'center',
        padding: 40,
    },
    toast: {
        backgroundColor: '#333',
        padding: 15,
        borderRadius: 10,
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
        elevation: 5,
        marginBottom: 60,
    },
    message: {
        color: '#fff',
        fontSize: 12,
        flex: 'flex-start',
    }
});

export default Toast;

// Toast.js
import React, { useState } from 'react';
import { View, Text, Modal, StyleSheet } from 'react-native';

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
        justifyContent: 'flex-end', // Alinea al final (parte inferior)
        alignItems: 'center', // Centra horizontalmente
        padding: 40, // Ajusta el padding si es necesario
    },
    toast: {
        backgroundColor: '#333', // Gris oscuro
        padding: 15,
        borderRadius: 10, // Bordes más redondeados
        flexDirection: 'row',
        justifyContent: 'center',
        alignItems: 'center',
        alignContent: 'center',
        shadowColor: '#000', // Sombra para profundidad
        shadowOffset: { width: 0, height: 2 },
        shadowOpacity: 0.3,
        shadowRadius: 4,
        elevation: 5, // Necesario para la sombra en Android
        marginBottom: 60, // Espacio desde el borde inferior
    },
    message: {
        color: '#fff', // Blanco para el texto
        fontSize: 12, // Tamaño de fuente
        flex: 'flex-start', // Ocupa el espacio disponible
    }
});

export default Toast;

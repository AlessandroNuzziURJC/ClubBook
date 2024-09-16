import React from 'react';
import { TouchableOpacity, Image, Text, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from "@expo/vector-icons";

const EditProfileButton = ({ visible, functionClick }) => {
    const navigation = useNavigation();

    if (!visible) return null;

    return (
        <TouchableOpacity style={styles.editContainer} onPress={functionClick}>
            <Text style={styles.edit}>Editar usuario</Text>
            <Ionicons name="pencil-outline" size={18} color="#1162BF" />
        </TouchableOpacity>
    );
};

const styles = StyleSheet.create({
    editContainer: {
        flexDirection: 'row',
        justifyContent: 'flex-start',
        paddingBottom: 3,
        marginBottom: 20
    },
    edit: {
        marginRight: 5,
        alignSelf: 'flex-end',
        textDecorationLine: 'underline',
        fontWeight: '500',
        color: '#1162BF',
    },
});

export default EditProfileButton;

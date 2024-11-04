import React from 'react';
import { TouchableOpacity, Image, Text, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';
import { Ionicons } from "@expo/vector-icons";

/**
 * EditProfileButton Component.
 * 
 * Renders a button that allows the user to edit their profile. When pressed, it triggers 
 * a specified function to handle the button click action.
 * 
 * @component
 * @param {Object} props - Component properties.
 * @param {boolean} props.visible - Determines if the button should be visible.
 * @param {Function} props.functionClick - Function to execute when the button is pressed.
 * @returns {JSX.Element|null} - A button with an edit icon, or `null` if `visible` is `false`.
 */
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

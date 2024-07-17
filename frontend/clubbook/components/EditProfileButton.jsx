import React from 'react';
import { TouchableOpacity, Image, Text, StyleSheet } from 'react-native';
import { useNavigation } from '@react-navigation/native';

const EditProfileButton = ({ visible }) => {
    const navigation = useNavigation();

    if (!visible) return null; 

    return (
        <TouchableOpacity style={styles.editContainer} onPress={() => navigation.navigate('ProfileEdit')}>
            <Image
                source={require('../assets/edit_icon.png')}
                style={styles.icon}
            />
            <Text style={styles.edit}>Editar usuario</Text>
        </TouchableOpacity>
    );
};

const styles = StyleSheet.create({
    editContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingBottom: 3,
    },
    icon: {
        width: 20,
        height: 20,
    },
    edit: {
        alignSelf: 'flex-end',
        textDecorationLine: 'underline',
        fontWeight: '300',
        color: '#1162BF',
    },
});

export default EditProfileButton;

import React, { useEffect, useState } from 'react';
import { FlatList, View, Image, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import ServerRequests from '../serverRequests/ServerRequests';
import Functions from '../functions/Functions';

/**
 * UsersFlatList component displays a list of users with their profile pictures, names, and phone numbers.
 *
 * @param {Object} props - Component props.
 * @param {Array} props.users - Array of user objects to be displayed.
 * @param {number} props.maxUsers - Maximum number of users to load in the list.
 * @param {function} props.loadMoreUsers - Function to call when the end of the list is reached to load more users.
 *
 * @returns {JSX.Element} A FlatList component displaying the users.
 */
const UsersFlatList = ({ users, maxUsers, loadMoreUsers}) => {
    const navigation = useNavigation();
    const [userImages, setUserImages] = useState(Array(maxUsers).fill(require('../assets/loading.gif')));

    /**
     * Fetches the user's photo from the server and updates the userImages state.
     *
     * @param {string} id - The user's ID.
     * @param {number} index - The index of the user in the users array.
     */
    const getImage = async (id, index) => {
        const response = await ServerRequests.getUserPhoto(id);
        if (response.ok) {
            const blob = await response.blob();
            const image = await Functions.blobToBase64(blob);
            if (image != null) {
                setUserImages((prevImages) => {
                    const newImages = [...prevImages];
                    newImages[index] = { uri: image };
                    return newImages;
                });
            }
        } else {
            setUserImages((prevImages) => {
                const newImages = [...prevImages];
                newImages[index] = require('../assets/error.png');
                return newImages;
            });
        }
    };

    useEffect(() => {
        users.forEach((user, index) => {
            getImage(user.id, index);
        });
    }, [users]);

    /**
     * Renders a user item in the FlatList.
     *
     * @param {Object} param0 - The item and index.
     * @param {Object} param0.item - The user object.
     * @param {number} param0.index - The index of the user in the users array.
     * @returns {JSX.Element} A View containing the user's image, name, and phone number.
     */
    const renderUser = ({ item, index }) => {
        return (
            <View style={styles.column}>
                <Image
                    source={userImages[index]}
                    style={styles.image}
                />
                <TouchableOpacity style={styles.rows} onPress={() => navigation.navigate('UserProfile', { item })}>
                    <View style={styles.profileinfo}>
                        <Text style={styles.name}>{`${item.firstName} ${item.lastName}`}</Text>
                    </View>
                    <View style={styles.contact}>
                        <Ionicons name="call" color='gray' size={14} style={styles.marginionicon} />
                        <Text style={styles.phonenumber}>{`${item.phoneNumber}`}</Text>
                    </View>
                    <Text style={styles.seemore}>Ver más</Text>
                </TouchableOpacity>
            </View>
        )
    };

    return (
        <FlatList
            data={users}
            renderItem={renderUser}
            keyExtractor={(item) => item.id.toString()}
            onEndReached={loadMoreUsers}
            onEndReachedThreshold={0.5}
            style={styles.content}
            contentContainerStyle={styles.contentContainer}
        />
    );
};

const styles = StyleSheet.create({
    column: {
        flexDirection: 'row',
        alignItems: 'center',
    },
    image: {
        width: 70,
        height: 70,
        borderRadius: 100,
        marginRight: 20,
        alignSelf: 'center',
    },
    rows: {
        borderTopWidth: 1,
        flex: 1,
        paddingTop: 10,
    },
    profileinfo: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        width: '100%',
    },
    contact: {
        flexDirection: 'row',
        alignItems: 'center',
        marginTop: 5,
    },
    name: {
        fontWeight: 'bold',
        fontSize: 16,
    },
    phonenumber: {
        fontSize: 14,
        color: 'gray',
    },
    marginionicon: {
        marginRight: 5,
    },
    seemore: {
        alignSelf: 'flex-end',
        color: '#1162BF',
        fontWeight: 'bold',
        marginTop: 15,
        marginBottom: 10,
    },
});

export default UsersFlatList;

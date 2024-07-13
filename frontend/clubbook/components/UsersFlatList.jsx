// UsersFlatList.jsx
import React, { useEffect, useState } from 'react';
import { FlatList, View, Image, Text, TouchableOpacity, StyleSheet } from 'react-native';
import { Ionicons } from '@expo/vector-icons';
import { useNavigation } from '@react-navigation/native';
import ServerRequests from '../serverRequests/ServerRequests';
import Functions from '../functions/Functions';

const UsersFlatList = ({ users, maxUsers, loadMoreUsers }) => {
    const navigation = useNavigation();
    const [userImages, setUserImages] = useState(Array(maxUsers).fill(require('../assets/loading.gif')));

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

    const renderUser = ({ item, index }) => {
        return (
            <View style={styles.column}>
                <Image
                    source={userImages[index]}
                    style={styles.image}
                />
                <TouchableOpacity style={styles.rows} onPress={() => navigation.navigate('StudentProfile', { item })}>
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

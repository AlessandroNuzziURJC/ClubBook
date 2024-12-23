import React from "react";
import { ScrollView, View, Text, StyleSheet, Image, RefreshControl } from "react-native";
import EditProfileButton from "./EditProfileButton";
import CloseSessionButton from "./CloseSession";
import Functions from "../functions/Functions";

/**
 * Profile Component.
 *
 * Displays user profile information including profile picture, personal details, and edit/logout buttons.
 * Also supports pull-to-refresh functionality.
 * 
 * @component
 * @param {Object} props - Component properties.
 * @param {boolean} props.editButton - Controls the visibility of edit and logout buttons.
 * @param {Function} props.onEditPress - Callback function triggered when the edit button is pressed.
 * @param {boolean} props.refreshing - Indicates if the profile is currently refreshing.
 * @param {Function} props.onRefresh - Function to call to refresh the profile information.
 * @param {Object} props.profilePicture - The user's profile picture (URI or local resource).
 * @param {Object} props.user - User data containing details like first name, last name, ID, address, phone, email, birthday, etc.
 * @returns {JSX.Element} - Rendered Profile component.
 */
const Profile = ({ editButton, onEditPress, refreshing, onRefresh, profilePicture, user }) => {
    return (
        <ScrollView style={styles.container} refreshControl={
            <RefreshControl refreshing={refreshing} onRefresh={onRefresh} />
        }>
            <View>
                <View style={styles.header}>
                    <View style={styles.columnContainer}>
                        <Text style={styles.pageTitle}>Perfil</Text>
                        {user.partner && (
                            <View style={styles.partnerContainer}>
                                <Text style={styles.partner}>Socio</Text>
                            </View>
                        )}
                        <EditProfileButton visible={editButton} functionClick={onEditPress} />
                    </View>
                    <Image
                        source={profilePicture ? profilePicture : require('../assets/loading.gif')}
                        style={styles.image}
                    />
                </View>

                <View style={styles.infoContainer}>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Nombre:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{`${user.firstName}`}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Apellidos:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{`${user.lastName}`}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>DNI/NIE:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.idCard}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Dirección:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.address}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de teléfono:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.phoneNumber}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Email:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{user.email}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Fecha de nacimiento:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>{Functions.convertDateEngToSpa(user.birthday)}</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de licencia:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>4R-123123123</Text>
                        </View>
                    </View>
                    <CloseSessionButton visible={editButton} />
                </View>
            </View>
        </ScrollView>
    );
}

export default Profile;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: '#fff',
        padding: 20
    },
    icon: {
        width: 20,
        height: 20
    },
    image: {
        width: 124,
        height: 124,
        justifyContent: 'flex-end',
        borderRadius: 100
    },
    header: {
        width: '100%',
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingTop: 20,
        marginBottom: 20
    },
    columnContainer: {
        flexDirection: 'column',
        flex: 1,
        paddingRight: 20
    },
    pageTitle: {
        fontSize: 24,
        fontWeight: 'bold'
    },
    editContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingBottom: 3,
    },
    edit: {
        alignSelf: 'flex-end',
        textDecorationLine: 'underline',
        fontWeight: '300',
        color: '#1162BF'
    },
    partnerContainer: {
        justifyContent: 'center',
        marginBottom: 10
    },
    partner: {
        fontSize: 18,
        fontWeight: '600'
    },
    infoContainer: {
        marginBottom: 20
    },
    labelDataContainer: {
        marginBottom: 15
    },
    label: {
        fontWeight: '500',
        fontSize: 18,
        marginBottom: 10
    },
    dataContainer: {
        backgroundColor: 'lightgray',
        padding: 10,
        borderRadius: 10
    },
    data: {
        fontSize: 16,
    },
});

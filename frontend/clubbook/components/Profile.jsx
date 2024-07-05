import React from "react";
import { ScrollView, View, Text, StyleSheet, Image, TouchableOpacity } from "react-native";

const Profile = () => {
    return (
        <ScrollView style={styles.container}>
            <View>
                <View style={styles.header}>
                    <View style={styles.columnContainer}>
                        <View style={styles.subheader}>
                            <Text style={styles.pageTitle}>Perfil</Text>
                            <TouchableOpacity style={styles.editContainer} onPress={() => console.log('Editar usuario')}>
                                <Image
                                    source={require('../assets/edit_icon.png')}
                                    style={styles.icon}
                                />
                                <Text style={styles.edit}>Editar usuario</Text>

                            </TouchableOpacity>
                        </View>
                        <View style={styles.partnerContainer}>
                            <Text style={styles.partner}>Socio</Text>
                        </View>
                    </View>
                    <Image
                        source={require('../assets/profilepicturedemo.jpeg')}
                        style={styles.image}
                    />
                </View>
                <View style={styles.infoContainer}>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Nombre:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>Alessandro Nuzzi Herrero</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>DNI/NIE:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>47466257F</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Dirección:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>C/ San Sebastián de los Reyes, 1</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de teléfono:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>+34 603636098</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Email:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>sandro.nuzzi.h@gmail.com</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Fecha de nacimiento:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>16/02/2001</Text>
                        </View>
                    </View>
                    <View style={styles.labelDataContainer}>
                        <Text style={styles.label}>Número de licencia:</Text>
                        <View style={styles.dataContainer}>
                            <Text style={styles.data}>4R-123123123</Text>
                        </View>
                    </View>
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
    subheader: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'flex-end',
        marginBottom: 10,
        marginTop: 20
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
    }
});

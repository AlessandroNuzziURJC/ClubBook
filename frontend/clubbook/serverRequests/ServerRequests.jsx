import Configuration from '../config/Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

const ServerRequest = {

    logIn: async (email, password) => {
        return await fetch(`${Configuration.API_URL}/auth/login`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ email, password }),
        });
    },

    getTokenAndId: async () => {
        const token = await AsyncStorage.getItem("userToken");
        const id = await AsyncStorage.getItem("id");
        return { token, id };
    },

    getUserData: async (data) => {
        return await fetch(`${Configuration.API_URL}/${data.id}/me`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getStudentsPage: async (data, page) => {
        return await fetch(`${Configuration.API_URL}/students?pageNumber=${page}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getStudentsSearchPage: async (data, search) => {
        return await fetch(`${Configuration.API_URL}/studentsSearch?search=${search}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getAllStudentsWithoutClassGroup: async () => {
        const data = await ServerRequest.getTokenAndId();
        return await fetch(`${Configuration.API_URL}/studentsWithoutClassGroup`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    postNewStudentsInClassGroup: async (id, studentsIds) => {
        const data = await ServerRequest.getTokenAndId();
        return await fetch(`${Configuration.API_URL}/${id}/addStudents`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            },
            body: JSON.stringify(studentsIds),
        });
    },

    getTeachersPage: async (data, page) => {
        return await fetch(`${Configuration.API_URL}/teachers?pageNumber=${page}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getAllTeachers: async () => {
        const data = await ServerRequest.getTokenAndId();
        return await fetch(`${Configuration.API_URL}/allTeachers`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getTeachersSearchPage: async (data, search) => {
        return await fetch(`${Configuration.API_URL}/teachersSearch?search=${search}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
    },

    getUserPhoto: async (id) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/${id}/profilePicture`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${data.token}`,
            }
        })
        return response;
    },

    requestLogout: async () => {
        const data = await ServerRequest.getTokenAndId();
        await fetch(`${Configuration.API_URL}/auth/logout`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${data.token}`,
            }
        });
        await AsyncStorage.removeItem('id');
        await AsyncStorage.removeItem('userToken');
        await AsyncStorage.removeItem('firstName');
        await AsyncStorage.removeItem('lastName');
        await AsyncStorage.removeItem('phoneNumber');
        await AsyncStorage.removeItem('birthday');
        await AsyncStorage.removeItem('address');
        await AsyncStorage.removeItem('idCard');
        await AsyncStorage.removeItem('partner');
    },

    getClassGroups: async() => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/classGroup`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
        return response;
    },

    getClassGroup: async(id) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/${id}/classGroup`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
        return response;
    },

    createClassGroup: async(classGroup) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/classGroup`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            },
            body: JSON.stringify(classGroup)
        });
        return response;
    },
    
    modifyClassGroup: async(classGroup) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/${classGroup.id}/classGroup`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            },
            body: JSON.stringify(classGroup)
        });
        return response;
    },

    deleteClassGroup: async (id) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/${id}/classGroup`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
        return response;
    },

    getAttendances: async (month, classGroupId) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/attendance/${month}/${classGroupId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        });
        return response;
    },

    saveAttendance: async (attendanceDto) => {
        const data = await ServerRequest.getTokenAndId();
        const response = await fetch(`${Configuration.API_URL}/attendance/new`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            },
            body: JSON.stringify(attendanceDto)
        });
        return response;
    }
}

export default ServerRequest;
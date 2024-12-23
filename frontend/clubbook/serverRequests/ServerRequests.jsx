import Configuration from '../config/Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

/**
 * Contains functions that need an answer from the server.
 */
const ServerRequest = {

    /*checkPushNotificationToken: async (pushToken) => {
        const data = await ServerRequest.getTokenAndId();
        return await fetch(`${Configuration.API_URL}/notification/token/${data.id}?notificationToken=${pushToken}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            }
        })
    },

    postPushNotificationToken: async (pushToken) => {
        const data = await ServerRequest.getTokenAndId();
        return await fetch(`${Configuration.API_URL}/notification/token`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${data.token}`,
            },
            body: JSON.stringify({
                'userId': Number(data.id),
                'token': "hola"  // Cambié "hola" por el token real
            })
        })
    },*/

    signUpUser: async (newUser) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/auth/signup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(newUser),
            });
        });
        return response;

    },

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

    manageToken: async (serverFunction) => {
        let response = await serverFunction();
        if (response.status === 401) {
            const responseValue = await response.json();

            if (responseValue.description === "The JWT token has expired") {
                const email = await AsyncStorage.getItem("email");
                const password = await AsyncStorage.getItem("userPassword");

                const renewTokenResponse = await ServerRequest.logIn(email, password);

                if (renewTokenResponse.ok) {
                    const newToken = await renewTokenResponse.json();
                    await AsyncStorage.setItem('userToken', newToken.data.token);
                    response = await serverFunction();
                }
            }
        }

        return response;
    },

    getUserData: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${data.id}/me`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            })
        });
        return response;
    },


    updateUser: async (user) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${data.id}/updateUser`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(user),
            });
        });
        return response;
    },

    updateProfilePicture: async (imageUri) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();

            const formData = new FormData();

            const response = await fetch(imageUri);
            const blob = await response.blob();

            formData.append('image', {
                uri: imageUri,
                type: blob.type,
                name: 'profilePicture.jpg'
            });

            const token = await AsyncStorage.getItem('userToken');

            return await fetch(`${Configuration.API_URL}/${data.id}/uploadProfilePicture`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data'
                },
                body: formData
            });

        });
        return response;
    },

    getStudentsPage: async (page) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/students?pageNumber=${page}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getStudentsSearchPage: async (search) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/studentsSearch?search=${search}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getAllStudentsWithoutClassGroup: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/studentsWithoutClassGroup`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    postNewStudentsInClassGroup: async (id, studentsIds) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${id}/addStudents`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(studentsIds),
            });
        });
        return response;
    },

    removeStudentsInClassGroup: async (id, studentsIds) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${id}/removeStudents`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(studentsIds),
            });
        });
        return response;
    },

    getTeachersPage: async (page) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/teachers?pageNumber=${page}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getAllTeachers: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/allTeachers`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getTeachersSearchPage: async (search) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/teachersSearch?search=${search}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getAllAdministrators: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/administrator/all/${data.id}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    deleteUser: async (id) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/user/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            })
        });
        return response;
    },

    getUserPhoto: async (id) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${id}/profilePicture`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            })
        });
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
        await AsyncStorage.removeItem('userToken');
        await AsyncStorage.removeItem('email');
        await AsyncStorage.removeItem('userPassword');
        await AsyncStorage.removeItem('id');
        await AsyncStorage.removeItem('firstName');
        await AsyncStorage.removeItem('lastName');
        await AsyncStorage.removeItem('phoneNumber');
        await AsyncStorage.removeItem('birthday');
        await AsyncStorage.removeItem('role');
        await AsyncStorage.removeItem('address');
        await AsyncStorage.removeItem('idCard');
        await AsyncStorage.removeItem('partner');

    },

    getClassGroups: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/classGroup`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getClassGroup: async (id) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${id}/classGroup`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    createClassGroup: async (classGroup) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/classGroup`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(classGroup)
            });
        });
        return response;
    },

    modifyClassGroup: async (classGroup) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${classGroup.id}/classGroup`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(classGroup)
            });
        });
        return response;
    },

    deleteClassGroup: async (id) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/${id}/classGroup`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getAttendances: async (month, classGroupId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/attendance/${month}/${classGroupId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    saveAttendance: async (attendanceDto) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/attendance/new`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(attendanceDto)
            });
        });
        return response;
    },

    seasonStarted: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/season/started`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    seasonStart: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/season/start/${data.id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    seasonFinish: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/season/finish/${data.id}`, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    downloadPdf: async (classGroupId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/attendance/generatepdf/${classGroupId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getNotificationsByUserId: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notification/${data.id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getEventTypes: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/types`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    postNewEvent: async (newEventDto) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/new`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(newEventDto)
            });
        });
        return response;
    },

    editEvent: async (event) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/edit`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(event)
            });
        });
        return response;
    },

    getAllFutureEvents: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/all/${data.id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getAllPastEvents: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/past`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getMonthEvents: async (month, year) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/month/${month}/${year}/${data.id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getNextEvent: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/next/${data.id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    deleteEvent: async (id) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/${id}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getEventStudentsAttendance: async (eventId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event_attendance/${eventId}/students`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getEventTeachersAttendance: async (eventId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event_attendance/${eventId}/teachers`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getEventAttendanceByUserId: async (eventId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event_attendance/${eventId}/${data.id}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    updateAttendance: async (updateEventAttendance) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event_attendance/update`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(updateEventAttendance)
            });
        });
        return response;
    },

    downloadPdfEventAttendance: async (eventId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/event/generatepdf/${eventId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getNotebooks: async () => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/all`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getNotebookById: async (notebookId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/${notebookId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    getTodayEntryByNotebookId: async (notebookId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/entry/today/${notebookId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            });
        });
        return response;
    },

    updateNotebookAIConfiguration: async (notebookData) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/config`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(notebookData)
            });
        });
        return response;
    },

    addNotebookEntry: async (entry, notebookId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/entry/${notebookId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(entry)
            });
        });
        return response;
    },

    getNotebookEntries: async (notebookId, page) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/entry/${notebookId}?pageNumber=${page}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
            });
        });
        return response;
    },

    deleteEntry: async (notebookEntryId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/entry/${notebookEntryId}`, {
                method: 'DELETE',
                headers: {
                    'Authorization': `Bearer ${data.token}`,
                }
            })
        });
        return response;
    },

    editNotebookEntry: async (editEntry) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/entry`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
                body: JSON.stringify(editEntry),
            });
        });
        return response;
    },

    getInvalidDates: async (notebookId) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/invalidDates/${notebookId}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
            });
        });
        return response;
    },

    generateEntry: async (notebookId, date) => {
        const response = await ServerRequest.manageToken(async () => {
            const data = await ServerRequest.getTokenAndId();
            return await fetch(`${Configuration.API_URL}/notebook/generateEntry/${notebookId}?date=${date}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${data.token}`,
                },
            });
        });
        return response;
    }

}

export default ServerRequest;
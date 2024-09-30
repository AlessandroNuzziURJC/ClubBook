import Configuration from '../config/Configuration';
import AsyncStorage from '@react-native-async-storage/async-storage';

const Functions = {

    blobToBase64: (blob) => {
        return new Promise((resolve, reject) => {
            const reader = new FileReader();
            reader.onerror = reject;
            reader.onload = () => {
                resolve(reader.result);
            };
            reader.readAsDataURL(blob);
        });
    },

    convertDateEngToSpa: (date) => {
        const dateObj = new Date(date);

        const day = String(dateObj.getDate()).padStart(2, '0'); 
        const month = String(dateObj.getMonth() + 1).padStart(2, '0'); 
        const year = dateObj.getFullYear();

        return `${day}/${month}/${year}`;
    },

    convertDateSpaToEng: (date) => {
        const [day, month, year] = date.split('/');
        return `${year}-${month}-${day}`;
    }

}

export default Functions;
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
    },

    translateEngToSpaMonth: (month) => {
        switch (month){
            case 'January': return 'Enero';
            case 'February': return 'Febrero';
            case 'March': return 'Marzo';
            case 'April': return 'Abril';
            case 'May': return 'Mayo';
            case 'June': return 'Junio';
            case 'July': return 'Julio';
            case 'August': return 'Agosto';
            case 'September': return 'Septiembre';
            case 'October': return 'Octubre';
            case 'November': return 'Noviembre';
            case 'December': return 'Diciembre';
        }
    },

    translateEventTypes: (eventType) => {
        switch (eventType.toUpperCase()) {
            case 'COMPETITION': return 'Competición';
            case 'EXHIBITION': return 'Exhibición';
            case 'TRAINING': return 'Entrenamiento';
            default: return '';
        }
    }

}

export default Functions;
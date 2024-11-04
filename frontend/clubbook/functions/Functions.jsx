/**
 * A collection of utility functions for data manipulation and translation.
 */
const Functions = {

    /**
     * Converts a Blob object to a Base64 string.
     *
     * @param {Blob} blob - The Blob object to convert.
     * @returns {Promise<string>} A promise that resolves with the Base64 string representation of the Blob.
     */
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

    /**
     * Converts a date from English format (YYYY-MM-DD) to Spanish format (DD/MM/YYYY).
     *
     * @param {string} date - The date in English format.
     * @returns {string} The date in Spanish format.
     */
    convertDateEngToSpa: (date) => {
        const dateObj = new Date(date);

        const day = String(dateObj.getDate()).padStart(2, '0'); 
        const month = String(dateObj.getMonth() + 1).padStart(2, '0'); 
        const year = dateObj.getFullYear();

        return `${day}/${month}/${year}`;
    },

    /**
     * Converts a date from Spanish format (DD/MM/YYYY) to English format (YYYY-MM-DD).
     *
     * @param {string} date - The date in Spanish format.
     * @returns {string} The date in English format.
     */
    convertDateSpaToEng: (date) => {
        let [day, month, year] = date.split('/');
        if (day.length === 1) {
            day = '0' + day;
        }

        if (month.length === 1) {
            month = '0' + month;
        }
        return `${year}-${month}-${day}`;
    },

    /**
     * Converts a date in CSV format (MM/DD/YYYY) to ISO format (YYYY-MM-DD).
     *
     * @param {string} date - The date in CSV format.
     * @returns {string} The date in ISO format.
     */
    convertCSVDate: (date) => {
        let [month, day, year] = date.split('/');
        if (day.length === 1) {
            day = '0' + day;
        }

        if (month.length === 1) {
            month = '0' + month;
        }
        return `${year}-${month}-${day}`;
    },

    /**
     * Translates the name of a month from English to Spanish.
     *
     * @param {string} month - The name of the month in English.
     * @returns {string} The name of the month in Spanish.
     */
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

    /**
     * Translates event types from English to Spanish.
     *
     * @param {string} eventType - The event type in English.
     * @returns {string} The translated event type in Spanish.
     */
    translateEventTypes: (eventType) => {
        switch (eventType.toUpperCase()) {
            case 'COMPETITION': return 'Competición';
            case 'EXHIBITION': return 'Exhibición';
            case 'TRAINING': return 'Entrenamiento';
            default: return '';
        }
    },
    
    /**
     * Translates user roles from English to Spanish.
     *
     * @param {string} role - The user role in English.
     * @returns {string} The translated user role in Spanish.
     */
    translateRole: (role) => {
        switch (role) {
            case 'administrator':
                return 'administrador';
            case 'teacher':
                return 'profesor';
            case 'student':
                return 'alumno';
            default: return '';
        }
    }

}

export default Functions;
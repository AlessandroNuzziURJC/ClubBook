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
    
}

export default Functions;
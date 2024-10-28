import React, { useState } from 'react';
import { View, Text, TextInput, StyleSheet, TouchableOpacity, ScrollView, Modal } from 'react-native';
import { useNavigation, useRoute } from '@react-navigation/native';
import { Picker } from '@react-native-picker/picker';
import FormFooter from '../../components/FormFooter';
import ServerRequest from '../../serverRequests/ServerRequests';

const VirtualAssistantConfigFormScreen = () => {
  const route = useRoute();
  const { notebook } = route.params;
  const [sport, setSport] = useState(notebook ? notebook.sport : '');
  const [level, setLevel] = useState(notebook ? notebook.level : '');
  const [isModalVisible, setIsModalVisible] = useState(false);
  const [sportError, setSportError] = useState(false);
  const [levelError, setLevelError] = useState(false);
  const navigation = useNavigation();

  const validateFormData = () => {
    let isValid = true;

    if (!sport) {
      setSportError(true);
      isValid = false;
    } else {
      setSportError(false);
    }

    if (!level) {
      setLevelError(true);
      isValid = false;
    } else {
      setLevelError(false);
    }

    return isValid;
  };

  const handleSave = async () => {
    if (validateFormData()) {
      const formData = {
        id: notebook.id,
        sport: sport,
        level: level,
      };

      const response = await ServerRequest.updateNotebookAIConfiguration(formData);
      const result = await response.json();

      if (response.ok & result.data) {
        notebook.sport = sport;
        notebook.level = level;
        navigation.goBack();
      }
    }
  };

  return (
    <View style={styles.container}>
      <View style={styles.header}>
        <Text style={styles.pageTitle}>Configuración Asistente Virtual</Text>
      </View>
      <ScrollView style={styles.content}>
        <Text style={styles.infoText}>
          Es importante introducir estos datos para que el asistente virtual pueda ofrecerte sugerencias personalizadas en función del deporte, la edad y el nivel de habilidad de los participantes. Esto permitirá que las clases sean más efectivas y adaptadas a las necesidades específicas de cada grupo, optimizando así la experiencia de enseñanza y aprendizaje.
        </Text>

        <View style={styles.inputContainer}>
          <Text style={styles.label}>Deporte que se imparte</Text>
          <TextInput
            style={[styles.input, sportError && styles.inputError]} // Aplicar borde rojo si hay error
            placeholder="Introduce el deporte"
            value={sport}
            onChangeText={setSport}
          />
        </View>

        <Text style={styles.label}>Nivel de la clase</Text>
        <TouchableOpacity
          style={[styles.pickerContainer, levelError && styles.inputError]} // Aplicar borde rojo si hay error
          onPress={() => setIsModalVisible(!isModalVisible)}
        >
          <Text style={styles.pickerInput}>
            {level ? level : 'Selecciona un nivel'}
          </Text>
        </TouchableOpacity>

        {isModalVisible && (
          <Modal
            animationType="slide"
            transparent={true}
            visible={isModalVisible}
            onRequestClose={() => setIsModalVisible(false)}
          >
            <View style={styles.modalContainer}>
              <View style={styles.modalContent}>
                <Text style={styles.modalTitle}>Selecciona el nivel</Text>
                <Picker
                  selectedValue={level}
                  style={styles.picker}
                  onValueChange={(itemValue) => {
                    setLevel(itemValue);
                    setIsModalVisible(false);
                  }}
                >
                  <Picker.Item label="Selecciona un nivel" value="" />
                  <Picker.Item label="Principiante" value="Principiante" />
                  <Picker.Item label="Intermedio" value="Intermedio" />
                  <Picker.Item label="Avanzado" value="Avanzado" />
                </Picker>
                <TouchableOpacity
                  style={styles.closeButton}
                  onPress={() => setIsModalVisible(false)}
                >
                  <Text style={styles.closeButtonText}>Cerrar</Text>
                </TouchableOpacity>
              </View>
            </View>
          </Modal>
        )}

      </ScrollView>
      <View style={styles.footerContainer}>
        <FormFooter cancel={{ function: navigation.goBack, text: "Cancelar" }} save={{ function: handleSave, text: "Guardar" }} />
      </View>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    paddingTop: 20,
  },
  header: {
    flexDirection: 'row',
    alignItems: 'flex-end',
    justifyContent: 'space-between',
    paddingTop: 20,
    marginBottom: 20,
    paddingLeft: 20,
    paddingRight: 20,
  },
  pageTitle: {
    fontSize: 24,
    fontWeight: 'bold',
  },
  content: {
    overflow: 'hidden',
    paddingBottom: 20,
    borderRadius: 10,
    paddingLeft: 20,
    paddingRight: 20,
  },
  infoText: {
    fontSize: 16,
    color: '#666',
    marginBottom: 10,
  },
  inputContainer: {
    marginVertical: 20,
  },
  label: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#333',
    marginBottom: 10,
  },
  input: {
    height: 45,
    fontSize: 16,
    color: '#333',
    borderRadius: 8,
    backgroundColor: '#f5f5f5',
    paddingHorizontal: 12,
    borderColor: '#ddd',
    borderWidth: 1,
  },
  inputError: {
    borderColor: 'red', // Borde rojo en caso de error
  },
  pickerContainer: {
    backgroundColor: '#f5f5f5',
    borderRadius: 8,
    paddingHorizontal: 12,
    justifyContent: 'center',
    height: 45,
    borderColor: '#ddd',
    borderWidth: 1,
  },
  pickerInput: {
    fontSize: 16,
    color: '#333',
    textAlign: 'center',
  },
  modalContainer: {
    flex: 1,
    justifyContent: "center",
    alignItems: "center",
    backgroundColor: "rgba(0, 0, 0, 0.5)",
  },
  modalContent: {
    width: "80%",
    backgroundColor: "#fff",
    borderRadius: 8,
    padding: 20,
    elevation: 5,
  },
  modalTitle: {
    fontSize: 18,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  closeButton: {
    marginTop: 15,
    backgroundColor: '#1162BF',
    padding: 10,
    borderRadius: 5,
    alignItems: 'center',
  },
  closeButtonText: {
    color: '#fff',
    fontWeight: 'bold',
  },
  button: {
    backgroundColor: '#1162BF',
    borderRadius: 8,
    paddingVertical: 12,
    paddingHorizontal: 20,
    alignItems: 'center',
    marginTop: 20,
  },
  buttonText: {
    color: '#fff',
    fontSize: 16,
    fontWeight: 'bold',
  },
});

export default VirtualAssistantConfigFormScreen;

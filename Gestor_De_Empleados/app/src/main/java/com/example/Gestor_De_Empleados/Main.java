package com.example.Gestor_De_Empleados;

import com.example.Gestor_De_Empleados.controladores.FormularioEmpleados;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main extends Application {

    private static final String CONFIG_FILE = "config.properties";
    private static Properties config;
    private static ObservableList<Empleado> empleados = FXCollections.observableArrayList();

    public static void main(String[] args) {
        loadConfig();
        loadData();
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Gestor_De_Empleados/vistas/formularioEmpleados.fxml"));
        HBox root = loader.load();

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Gestión de Empleados");
        primaryStage.show();

        FormularioEmpleados controller = loader.getController();
        controller.setData(empleados);
    }

    @Override
    public void stop() {
        saveData();
        saveConfig();
    }

    private static void loadConfig() {
        config = new Properties();
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            config.load(reader);
        } catch (IOException e) {
            System.out.println("No se pudo cargar el archivo de configuración. Se usará la configuración por defecto.");
            config.setProperty("fichero_binario", "empleados.dat");
            config.setProperty("fichero_xml", "empleados.xml");
            config.setProperty("fichero_json", "empleados.json");
            config.setProperty("id_empleado", "0");
        }
    }

    private static void saveConfig() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            config.store(writer, "Configuración del sistema de empleados");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadData() {
        String fileName = config.getProperty("fichero_binario");
        File file = new File(fileName);
        if (!file.exists()) {
            System.out.println("Archivo binario no encontrado. Se iniciará con una lista vacía.");
            empleados = FXCollections.observableArrayList();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            // Leer ArrayList y convertir a ObservableList
            List<Empleado> list = (List<Empleado>) ois.readObject();
            empleados = FXCollections.observableArrayList(list);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la información desde el archivo binario.");
            empleados = FXCollections.observableArrayList();
        }
    }

    private static void saveData() {
        String fileName = config.getProperty("fichero_binario");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            // Convertir ObservableList a ArrayList para serializar
            oos.writeObject(new ArrayList<>(empleados));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNextId() {
        int id = Integer.parseInt(config.getProperty("id_empleado"));
        config.setProperty("id_empleado", String.valueOf(id + 1));
        return id;
    }

    public static void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


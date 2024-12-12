package com.example.Gestor_De_Empleados.controladores;

import com.example.Gestor_De_Empleados.Main;
import com.example.Gestor_De_Empleados.modelo.Empleado;
import com.example.Gestor_De_Empleados.modelo.ListaEmpleados;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.net.URL;
import java.util.*;

import com.google.gson.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;

public class FormularioEmpleados implements Initializable {
    @FXML
    private TableView<Empleado> tableView;

    @FXML
    private TextField txtApellidos;

    @FXML
    private TextField txtDepartamento;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtSueldo;

    private ObservableList<Empleado> empleados;

    private int lastUsedId; // Variable para almacenar el último ID utilizado

    @FXML
    void borrar(ActionEvent event) {
        Empleado selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            empleados.remove(selected);
        } else {
            showAlert("Error", "Selecciona un empleado para borrar.");
        }
    }

    @FXML
    void exportarJSON(ActionEvent event) {
        try {
            List<Empleado> empleados = leerEmpleadosBinario();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writerWithDefaultPrettyPrinter().writeValue(new File("empleados.json"), empleados);
            System.out.println("Datos exportados a JSON correctamente.");
        } catch (IOException e) {
            System.err.println("Error al exportar a JSON: " + e.getMessage());
        }
    }

    @FXML
    void exportarXML(ActionEvent event) {
        try {
            List<Empleado> empleados = leerEmpleadosBinario();
            ListaEmpleados listaEmpleados = new ListaEmpleados();
            listaEmpleados.setEmpleados(empleados);

            JAXBContext context = JAXBContext.newInstance(ListaEmpleados.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            File file = new File("empleados.xml");
            marshaller.marshal(listaEmpleados, file);

            System.out.println("Datos exportados a XML correctamente.");
        } catch (Exception e) {
            System.err.println("Error al exportar a XML: " + e.getMessage());
        }
    }

    @FXML
    void importarJSON(ActionEvent event) {
        try {
            File file = new File("empleados.json");
            if (!file.exists()) {
                System.out.println("El archivo empleados.json no existe.");
                return;
            }

            ObjectMapper mapper = new ObjectMapper();
            List<Empleado> empleadosImportados = Arrays.asList(mapper.readValue(file, Empleado[].class));

            List<Empleado> empleadosExistentes = leerEmpleadosBinario();
            for (Empleado emp : empleadosImportados) {
                emp.setId(++lastUsedId); // Asignar un nuevo ID único
                empleadosExistentes.add(emp);
            }

            guardarEmpleadosBinario(empleadosExistentes);
            tableView.getItems().addAll(empleadosImportados);
            System.out.println("Datos importados desde JSON correctamente.");
        } catch (Exception e) {
            System.err.println("Error al importar desde JSON: " + e.getMessage());
        }
    }

    @FXML
    void importarXML(ActionEvent event) {
        try {
            File file = new File("empleados.xml");
            if (!file.exists()) {
                System.out.println("El archivo empleados.xml no existe.");
                return;
            }

            JAXBContext context = JAXBContext.newInstance(ListaEmpleados.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ListaEmpleados listaEmpleados = (ListaEmpleados) unmarshaller.unmarshal(file);
            List<Empleado> empleadosImportados = listaEmpleados.getEmpleados();

            List<Empleado> empleadosExistentes = leerEmpleadosBinario();
            for (Empleado emp : empleadosImportados) {
                emp.setId(++lastUsedId); // Asignar un nuevo ID único
                empleadosExistentes.add(emp);
            }

            guardarEmpleadosBinario(empleadosExistentes);
            tableView.getItems().addAll(empleadosImportados);
            System.out.println("Datos importados desde XML correctamente.");
        } catch (Exception e) {
            System.err.println("Error al importar desde XML: " + e.getMessage());
        }
    }

    @FXML
    void actualizar(ActionEvent event) {
        Empleado selected = tableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            String departamento = txtDepartamento.getText();
            String sueldoStr = txtSueldo.getText();

            // Validaciones
            if (nombre.length() > 30) {
                showAlert("Error", "El nombre no puede tener más de 30 caracteres.");
                return;
            }
            if (apellidos.length() > 60) {
                showAlert("Error", "Los apellidos no pueden tener más de 60 caracteres.");
                return;
            }
            if (departamento.length() > 30) {
                showAlert("Error", "El departamento no puede tener más de 30 caracteres.");
                return;
            }

            try {
                double sueldo = Double.parseDouble(sueldoStr);
                if (sueldo < 0 || sueldo > 99999.99) {
                    showAlert("Error", "El sueldo debe estar entre 0 y 99,999.99.");
                    return;
                }

                selected.setNombre(nombre);
                selected.setApellidos(apellidos);
                selected.setDepartamento(departamento);
                selected.setSueldo(sueldo);
                tableView.refresh();
            } catch (NumberFormatException e) {
                showAlert("Error", "Sueldo debe ser un número válido.");
            }
        } else {
            showAlert("Error", "Selecciona un empleado para actualizar.");
        }
    }

    @FXML
    void insertar(ActionEvent event) {
        try {
            String nombre = txtNombre.getText();
            String apellidos = txtApellidos.getText();
            String departamento = txtDepartamento.getText();
            double sueldo = Double.parseDouble(txtSueldo.getText());

            // Validaciones
            if (nombre.length() > 30) {
                showAlert("Error", "El nombre no puede tener más de 30 caracteres.");
                return;
            }
            if (apellidos.length() > 60) {
                showAlert("Error", "Los apellidos no pueden tener más de 60 caracteres.");
                return;
            }
            if (departamento.length() > 30) {
                showAlert("Error", "El departamento no puede tener más de 30 caracteres.");
                return;
            }
            if (sueldo < 0 || sueldo > 99999.99) {
                showAlert("Error", "El sueldo debe estar entre 0 y 99,999.99.");
                return;
            }

            // Obtener el siguiente ID único
            int idEmpleado = Main.getNextId();  // Obtenemos el siguiente ID y lo incrementamos

            // Crear un nuevo empleado con un ID único
            Empleado empleado = new Empleado(idEmpleado, nombre, apellidos, departamento, sueldo);

            // Añadir empleado al archivo binario
            List<Empleado> empleados = leerEmpleadosBinario();
            empleados.add(empleado);
            guardarEmpleadosBinario(empleados);

            // Actualizar tabla y limpiar campos
            tableView.getItems().add(empleado);
            txtNombre.clear();
            txtApellidos.clear();
            txtDepartamento.clear();
            txtSueldo.clear();
        } catch (Exception e) {
            System.err.println("Error al insertar empleado: " + e.getMessage());
        }
    }

    private List<Empleado> leerEmpleadosBinario() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("empleados.dat"))) {
            return (List<Empleado>) ois.readObject();
        } catch (Exception e) {
            return new ArrayList<>(); // Si no existe el archivo, devuelve lista vacía
        }
    }

    private void guardarEmpleadosBinario(List<Empleado> empleados) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("empleados.dat"))) {
            oos.writeObject(empleados);
        } catch (IOException e) {
            System.err.println("Error al guardar empleados: " + e.getMessage());
        }
    }

    private void actualizarLastUsedId(int id) {
        try (FileWriter writer = new FileWriter("config.properties")) {
            Properties properties = new Properties();
            properties.setProperty("lastUsedId", String.valueOf(id));
            properties.store(writer, null);
        } catch (IOException e) {
            System.err.println("Error al actualizar el archivo de configuración: " + e.getMessage());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TableColumn<Empleado, String> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Empleado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));

        TableColumn<Empleado, String> colApellidos = new TableColumn<>("Apellidos");
        colApellidos.setCellValueFactory(new PropertyValueFactory<>("apellidos"));

        TableColumn<Empleado, String> colDepartamento = new TableColumn<>("Departamento");
        colDepartamento.setCellValueFactory(new PropertyValueFactory<>("departamento"));

        TableColumn<Empleado, Double> colSueldo = new TableColumn<>("Sueldo");
        colSueldo.setCellValueFactory(new PropertyValueFactory<>("sueldo"));

        tableView.getColumns().addAll(colId, colNombre, colApellidos, colDepartamento, colSueldo);

        // Cargar el último ID utilizado desde el archivo de configuración
        lastUsedId = cargarLastUsedId();

        // Cargar empleados desde el archivo binario
        List<Empleado> empleados = leerEmpleadosBinario();
        tableView.getItems().addAll(empleados);
    }

    private int cargarLastUsedId() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            return Integer.parseInt(properties.getProperty("lastUsedId", "0"));
        } catch (IOException e) {
            return 0; // Si no existe, retornar ID 0
        }
    }

    public void setData(ObservableList<Empleado> empleados) {
        this.empleados = empleados;
        tableView.setItems(empleados);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

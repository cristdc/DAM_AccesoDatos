package com.example.Gestor_De_Empleados.controladores;

import com.example.Gestor_De_Empleados.Empleado;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.*;
import java.util.List;
import com.google.gson.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlElement;

public class FormularioEmpleados {

    @FXML
    private Button btnActualizar;

    @FXML
    private Button btnBorrar;

    @FXML
    private Button btnExportarJSON;

    @FXML
    private Button btnExportarXML;

    @FXML
    private Button btnImportarJSON;

    @FXML
    private Button btnImportarXML;

    @FXML
    private Button btnInsertar;

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
        try (Writer writer = new FileWriter("empleados.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();

            // Asegúrate de que empleados no esté vacío
            if (empleados != null && !empleados.isEmpty()) {
                gson.toJson(empleados, writer);
            } else {
                showAlert("Error", "No hay empleados para exportar.");
            }
        } catch (IOException e) {
            showAlert("Error", "No se pudo exportar a JSON: " + e.getMessage());
        }
    }


    @FXML
    void exportarXML(ActionEvent event) {
        try {
            if (empleados != null && !empleados.isEmpty()) {
                JAXBContext context = JAXBContext.newInstance(EmpleadoListWrapper.class);
                Marshaller marshaller = context.createMarshaller();
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

                EmpleadoListWrapper wrapper = new EmpleadoListWrapper();
                wrapper.setEmpleados(empleados);

                // Asegúrate de que el archivo XML no esté bloqueado
                File xmlFile = new File("empleados.xml");
                marshaller.marshal(wrapper, xmlFile);
            } else {
                showAlert("Error", "No hay empleados para exportar.");
            }
        } catch (JAXBException e) {
            showAlert("Error", "No se pudo exportar a XML: " + e.getMessage());
        }
    }


    @FXML
    void importarJSON(ActionEvent event) {
        try (Reader reader = new FileReader("empleados.json")) {
            Gson gson = new Gson();
            Empleado[] imported = gson.fromJson(reader, Empleado[].class);

            // Convertir el array de empleados a ObservableList
            empleados.setAll(FXCollections.observableArrayList(imported));
        } catch (IOException e) {
            showAlert("Error", "No se pudo importar desde JSON.");
        }
    }

    @FXML
    void importarXML(ActionEvent event) {
        try {
            JAXBContext context = JAXBContext.newInstance(EmpleadoListWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            // Leer el archivo XML y convertirlo en el wrapper
            EmpleadoListWrapper wrapper = (EmpleadoListWrapper) unmarshaller.unmarshal(new File("empleados.xml"));

            // Convertir la lista de empleados en ObservableList
            empleados.setAll(FXCollections.observableArrayList(wrapper.getEmpleados()));
        } catch (JAXBException e) {
            showAlert("Error", "No se pudo importar desde XML.");
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

            int id = com.example.Gestor_De_Empleados.Main.getNextId();
            Empleado nuevo = new Empleado(id, nombre, apellidos, departamento, sueldo);
            empleados.add(nuevo);
        } catch (NumberFormatException e) {
            showAlert("Error", "Sueldo debe ser un número válido.");
        }
    }


    public void setData(ObservableList<Empleado> empleados) {
        this.empleados = empleados;
        tableView.setItems(empleados);
        inicializarColumnas();
    }

    private void inicializarColumnas() {
        TableColumn<Empleado, Integer> colId = new TableColumn<>("ID");
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
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Wrapper para lista de empleados para JAXB
    public static class EmpleadoListWrapper {
        private List<Empleado> empleados;

        @XmlElement(name = "empleado")
        public List<Empleado> getEmpleados() {
            return empleados;
        }

        public void setEmpleados(List<Empleado> empleados) {
            this.empleados = empleados;
        }
    }
}

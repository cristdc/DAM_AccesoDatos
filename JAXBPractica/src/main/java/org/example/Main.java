package org.example;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        try {
            // Crear un objeto Estudiante
            Estudiante estudiante1 = new Estudiante("Juan Perez", 20);
            Estudiante estudiante2 = new Estudiante("Ana Gómez", 22);

            // Definir el archivo XML de salida para Estudiante
            File estudianteFile = new File("estudiante.xml");

            // Serializar el objeto Estudiante a XML
            JAXBUtil.marshal(estudiante1, estudianteFile);
            System.out.println("Estudiante serializado a estudiante.xml");

            // Deserializar el objeto Estudiante desde XML
            Estudiante estudianteDeserializado = JAXBUtil.unmarshal(estudianteFile);
            System.out.println("Estudiante deserializado desde estudiante.xml:");
            System.out.println("Nombre: " + estudianteDeserializado.getNombre());
            System.out.println("Edad: " + estudianteDeserializado.getEdad());

            // Crear un objeto Curso con una lista de estudiantes
            Curso curso = new Curso("Curso de Java", Arrays.asList(estudiante1, estudiante2));

            // Definir el archivo XML de salida para Curso
            File cursoFile = new File("curso.xml");

            // Serializar el objeto Curso a XML
            JAXBContext context = JAXBContext.newInstance(Curso.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(curso, cursoFile);
            System.out.println("Curso serializado a curso.xml");

            // Deserializar el objeto Curso desde XML
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Curso cursoDeserializado = (Curso) unmarshaller.unmarshal(cursoFile);
            System.out.println("Curso deserializado desde curso.xml:");
            System.out.println("Nombre del curso: " + cursoDeserializado.getNombreCurso());
            for (Estudiante estudiante : cursoDeserializado.getEstudiantes()) {
                System.out.println("Estudiante: " + estudiante.getNombre() + ", Edad: " + estudiante.getEdad());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

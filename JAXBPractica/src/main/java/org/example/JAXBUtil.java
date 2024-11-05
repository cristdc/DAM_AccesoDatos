package org.example;

import javax.xml.bind.*;
import java.io.File;

public class JAXBUtil {

    // Métod0 para convertir un objeto Estudiante a XML
    public static void marshal(Estudiante estudiante, File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Estudiante.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  // Formatea la salida XML
        marshaller.marshal(estudiante, file);
    }

    // Métod0 para leer un objeto Estudiante desde un archivo XML
    public static Estudiante unmarshal(File file) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Estudiante.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (Estudiante) unmarshaller.unmarshal(file);
    }
}


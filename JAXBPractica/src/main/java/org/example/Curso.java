package org.example;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAccessType;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)  // Usa únicamente los campos privados para JAXB
public class Curso {
    @XmlElement
    private String nombreCurso;

    @XmlElement
    private List<Estudiante> estudiantes;

    // Constructor sin parámetros necesario para JAXB
    public Curso() {}

    // Constructor con parámetros
    public Curso(String nombreCurso, List<Estudiante> estudiantes) {
        this.nombreCurso = nombreCurso;
        this.estudiantes = estudiantes;
    }

    // Getters y Setters
    public String getNombreCurso() { return nombreCurso; }
    public void setNombreCurso(String nombreCurso) { this.nombreCurso = nombreCurso; }

    public List<Estudiante> getEstudiantes() { return estudiantes; }
    public void setEstudiantes(List<Estudiante> estudiantes) { this.estudiantes = estudiantes; }
}


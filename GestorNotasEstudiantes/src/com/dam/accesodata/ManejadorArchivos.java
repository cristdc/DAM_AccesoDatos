package com.dam.accesodata;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class ManejadorArchivos {
    private static final String ARCHIVO =
            "resources/notas_estudiantes.txt";

    public void añadirEstudiante(Estudiante estudiante) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ARCHIVO, true))) {
            writer.write(estudiante.toString());
            writer.newLine();
            System.out.println("Estudiante añadido: " + estudiante.getNombre());
        } catch (IOException e) {
            System.out.println("Error al añadir el estudiante: " + e.getMessage());
        }
    }


    public void mostrarEstudiantes() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes en la lista.");
        } else {
            System.out.println("\n--- Lista de Estudiantes ---");
            for (Estudiante estudiante : estudiantes) {
                System.out.println("Nombre: " + estudiante.getNombre() + "con la nota de: " + estudiante.getNota());
            }
        }
    }


    public void buscarEstudiante(String nombre) {
        List<Estudiante> estudiantes = leerEstudiantes();
        boolean encontrado = false;
        for (Estudiante estudiante : estudiantes) {
            if (estudiante.getNombre().equalsIgnoreCase(nombre)) {
                System.out.println("Estudiante encontrado: " + estudiante.getNombre() + ", Nota: " + estudiante.getNota());
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            System.out.println("Estudiante no encontrado.");
        }
    }


    public void calcularMedia() {
        List<Estudiante> estudiantes = leerEstudiantes();
        if (estudiantes.isEmpty()) {
            System.out.println("No hay estudiantes para calcular la media.");
            return;
        }

        double sumaNotas = 0;
        for (Estudiante estudiante : estudiantes) {
            sumaNotas += estudiante.getNota();
        }

        double media = sumaNotas / estudiantes.size();
        System.out.printf("La nota media es: %.2f\n", media);
    }


    private List<Estudiante> leerEstudiantes() {
        List<Estudiante> estudiantes = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String nombre = partes[0];
                    double nota = Double.parseDouble(partes[1]);
                    estudiantes.add(new Estudiante(nombre, nota));
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("El archivo no existe. Se creará un nuevo archivo cuando añadas un estudiante.");
        } catch (IOException e) {
            System.out.println("Error al leer el archivo: " + e.getMessage());
        }

        return estudiantes;
    }
}
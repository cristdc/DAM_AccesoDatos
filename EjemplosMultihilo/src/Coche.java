import java.util.Random;

public class Coche implements Runnable {
    private String nombre;
    private int distanciaRecorrida;
    private static final int DISTANCIA_META = 100;
    private static boolean carreraTerminada = false;

    public Coche(String nombre) {
        this.nombre = nombre;
        this.distanciaRecorrida = 0;
    }

    @Override
    public void run() {
        Random random = new Random();

        while (this.distanciaRecorrida < DISTANCIA_META && !carreraTerminada) {
            int avance = random.nextInt(10) + 1;
            this.distanciaRecorrida += avance;

            System.out.println(this.nombre + " ha avanzado " + this.distanciaRecorrida + " unidades.");

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            if (this.distanciaRecorrida >= Coche.DISTANCIA_META) {
                carreraTerminada = true;
                System.out.println(this.nombre + " ha ganado la carrera!");
            }
        }
    }
}
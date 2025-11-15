package aplicación;

/**
 * Representa las estadísticas de rendimiento de un usuario dentro del sistema.
 * Incluye aciertos, fallos, racha máxima y tiempo de uso total.
 */
public class Estadísticas {

    /**
     * Número total de respuestas correctas realizadas por el usuario.
     */
    int numAciertos;

    /**
     * Número total de respuestas incorrectas realizadas por el usuario.
     */
    int numFallos;

    /**
     * Mejor racha consecutiva de aciertos alcanzada por el usuario.
     */
    int mejorRacha;

    /**
     * Tiempo total de uso registrado para el usuario, expresado en segundos.
     */
    int tiempoUso;

    /**
     * Obtiene el número total de aciertos del usuario.
     *
     * @return el número de aciertos
     */
    public int getNumAciertos() {
        return numAciertos;
    }

    /**
     * Establece el número total de aciertos del usuario.
     *
     * @param numAciertos el valor a asignar
     */
    public void setNumAciertos(int numAciertos) {
        this.numAciertos = numAciertos;
    }

    /**
     * Obtiene el número total de fallos del usuario.
     *
     * @return el número de fallos
     */
    public int getNumFallos() {
        return numFallos;
    }

    /**
     * Establece el número total de fallos del usuario.
     *
     * @param numFallos el valor a asignar
     */
    public void setNumFallos(int numFallos) {
        this.numFallos = numFallos;
    }

    /**
     * Calcula el porcentaje de aciertos del usuario.
     * Si no hay respuestas registradas, devuelve 0.
     *
     * @return el porcentaje de aciertos como valor entre 0 y 100
     */
    public float getPorcentajeAciertos() {
        if (numAciertos + numFallos == 0) {
            return 0;
        }
        return (float) numAciertos / (numAciertos + numFallos) * 100;
    }

    /**
     * Obtiene la mejor racha de aciertos consecutivos del usuario.
     *
     * @return la mejor racha registrada
     */
    public int getMejorRacha() {
        return mejorRacha;
    }

    /**
     * Establece la mejor racha de aciertos consecutivos del usuario.
     *
     * @param mejorRacha el valor a asignar
     */
    public void setMejorRacha(int mejorRacha) {
        this.mejorRacha = mejorRacha;
    }

    /**
     * Obtiene el tiempo total de uso registrado.
     *
     * @return el tiempo de uso en segundos
     */
    public int getTiempoUso() {
        return tiempoUso;
    }

    /**
     * Establece el tiempo total de uso del usuario.
     *
     * @param tiempoUso el tiempo a asignar en segundos
     */
    public void setTiempoUso(int tiempoUso) {
        this.tiempoUso = tiempoUso;
    }
}

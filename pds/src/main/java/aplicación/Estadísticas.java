package aplicación;

import jakarta.persistence.Embeddable;

/**
 * Estadísticas de rendimiento SIMPLIFICADAS.
 * - Eliminado: racha (no tenía sentido sin historial)
 * - Añadido: experienciaTotal (XP acumulada de logros)
 */
@Embeddable
public class Estadísticas {

    /**
     * Número total de respuestas correctas.
     */
    private int numAciertos;

    /**
     * Número total de respuestas incorrectas.
     */
    private int numFallos;

    /**
     * Experiencia total acumulada (puntos de logros desbloqueados).
     * Se calcula sumando los puntos de todos los logros obtenidos.
     */
    private int experienciaTotal;

    /**
     * Tiempo total de uso en segundos.
     */
    private int tiempoUso;

    // Constructor
    public Estadísticas() {
        this.numAciertos = 0;
        this.numFallos = 0;
        this.experienciaTotal = 0;
        this.tiempoUso = 0;
    }

    // ==================== MÉTODOS DE ACTUALIZACIÓN ====================
    
    /**
     * Registra un acierto.
     */
    public void registrarAcierto() {
        numAciertos++;
    }
    
    /**
     * Registra un fallo.
     */
    public void registrarFallo() {
        numFallos++;
    }
    
    /**
     * Añade experiencia (puntos de logro desbloqueado).
     */
    public void añadirExperiencia(int puntos) {
        this.experienciaTotal += puntos;
    }
    
    /**
     * Añade tiempo de uso en segundos.
     */
    public void añadirTiempoUso(int segundos) {
        this.tiempoUso += segundos;
    }

    // ==================== GETTERS Y SETTERS ====================
    
    public int getNumAciertos() {
        return numAciertos;
    }

    public void setNumAciertos(int numAciertos) {
        this.numAciertos = numAciertos;
    }

    public int getNumFallos() {
        return numFallos;
    }

    public void setNumFallos(int numFallos) {
        this.numFallos = numFallos;
    }

    public int getExperienciaTotal() {
        return experienciaTotal;
    }

    public void setExperienciaTotal(int experienciaTotal) {
        this.experienciaTotal = experienciaTotal;
    }

    public int getTiempoUso() {
        return tiempoUso;
    }

    public void setTiempoUso(int tiempoUso) {
        this.tiempoUso = tiempoUso;
    }

    // ==================== MÉTODOS CALCULADOS ====================
    
    /**
     * Calcula el porcentaje de aciertos.
     */
    public float getPorcentajeAciertos() {
        int total = numAciertos + numFallos;
        if (total == 0) return 0;
        return (float) numAciertos / total * 100;
    }
    
    /**
     * Obtiene el total de preguntas respondidas.
     */
    public int getTotalPreguntas() {
        return numAciertos + numFallos;
    }
    
    /**
     * Calcula el nivel actual basado en la experiencia.
     * Fórmula: nivel = raíz cuadrada de (XP / 100)
     */
    public int getNivelActual() {
        return (int) Math.sqrt(experienciaTotal / 100.0);
    }
    
    /**
     * Calcula la XP necesaria para el siguiente nivel.
     */
    public int getXpParaSiguienteNivel() {
        int nivelActual = getNivelActual();
        int nivelSiguiente = nivelActual + 1;
        return (nivelSiguiente * nivelSiguiente) * 100;
    }
    
    /**
     * Calcula la XP que tenías al inicio del nivel actual.
     */
    public int getXpNivelActual() {
        int nivelActual = getNivelActual();
        return (nivelActual * nivelActual) * 100;
    }
    
    /**
     * Calcula el progreso en el nivel actual (0-100).
     */
    public int getProgresoNivel() {
        int xpActual = experienciaTotal;
        int xpInicioNivel = getXpNivelActual();
        int xpFinNivel = getXpParaSiguienteNivel();
        int xpNecesaria = xpFinNivel - xpInicioNivel;
        int xpProgreso = xpActual - xpInicioNivel;
        
        if (xpNecesaria == 0) return 0;
        return (xpProgreso * 100) / xpNecesaria;
    }
    
    /**
     * Obtiene el rango del usuario según su nivel.
     */
    public String getRango() {
        int nivel = getNivelActual();
        
        if (nivel >= 20) return "Leyenda";
        if (nivel >= 15) return "Diamante";
        if (nivel >= 12) return "Oro";
        if (nivel >= 9) return "Plata";
        if (nivel >= 6) return "Bronce";
        if (nivel >= 3) return "Aprendiz";
        return "Novato";
    }
    
    /**
     * Obtiene el tiempo de uso formateado (HH:MM:SS).
     */
    public String getTiempoUsoFormateado() {
        int horas = tiempoUso / 3600;
        int minutos = (tiempoUso % 3600) / 60;
        int segundos = tiempoUso % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}
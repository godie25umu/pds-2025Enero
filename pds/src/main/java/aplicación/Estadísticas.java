package aplicación;

import jakarta.persistence.Embeddable;


@Embeddable
public class Estadísticas {
    private int numAciertos;
    private int numFallos;
    private int experienciaTotal;
    private int tiempoUso;

    public Estadísticas() {
        this.numAciertos = 0;
        this.numFallos = 0;
        this.experienciaTotal = 0;
        this.tiempoUso = 0;
    }

    public void registrarAcierto() {
        numAciertos++;
    }

    public void registrarFallo() {
        numFallos++;
    }

    public void añadirExperiencia(int puntos) {
        this.experienciaTotal += puntos;
    }
    
    public void añadirTiempoUso(int segundos) {
        this.tiempoUso += segundos;
    }
    
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

    public float getPorcentajeAciertos() {
        int total = numAciertos + numFallos;
        if (total == 0) return 0;
        return (float) numAciertos / total * 100;
    }
    
    public int getTotalPreguntas() {
        return numAciertos + numFallos;
    }
    
    public int getNivelActual() {
        return (int) Math.sqrt(experienciaTotal / 100.0);
    }
    
    public int getXpParaSiguienteNivel() {
        int nivelActual = getNivelActual();
        int nivelSiguiente = nivelActual + 1;
        return (nivelSiguiente * nivelSiguiente) * 100;
    }
    
    public int getXpNivelActual() {
        int nivelActual = getNivelActual();
        return (nivelActual * nivelActual) * 100;
    }
    
    public int getProgresoNivel() {
        int xpActual = experienciaTotal;
        int xpInicioNivel = getXpNivelActual();
        int xpFinNivel = getXpParaSiguienteNivel();
        int xpNecesaria = xpFinNivel - xpInicioNivel;
        int xpProgreso = xpActual - xpInicioNivel;
        
        if (xpNecesaria == 0) return 0;
        return (xpProgreso * 100) / xpNecesaria;
        
    }
    
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
    
    public String getTiempoUsoFormateado() {
        int horas = tiempoUso / 3600;
        int minutos = (tiempoUso % 3600) / 60;
        int segundos = tiempoUso % 60;
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}
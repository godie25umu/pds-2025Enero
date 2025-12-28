package aplicación;


import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
public class ProgresoCurso {
    private int bloqueActual;
    private int preguntaActual;
    private LocalDateTime ultimaActualizacion;

    public ProgresoCurso() {}

    public ProgresoCurso(int bloqueActual, int preguntaActual) {
        this.bloqueActual = bloqueActual;
        this.preguntaActual = preguntaActual;
        this.ultimaActualizacion = LocalDateTime.now();
    }

    // Getters y Setters
    public int getBloqueActual() { return bloqueActual; }
    public void setBloqueActual(int bloqueActual) { 
        this.bloqueActual = bloqueActual; 
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public int getPreguntaActual() { return preguntaActual; }
    public void setPreguntaActual(int preguntaActual) { 
        this.preguntaActual = preguntaActual; 
        this.ultimaActualizacion = LocalDateTime.now();
    }

    public LocalDateTime getUltimaActualizacion() { return ultimaActualizacion; }
}
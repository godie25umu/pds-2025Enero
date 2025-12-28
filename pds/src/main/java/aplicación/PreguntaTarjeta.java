package aplicación;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_flashcard")
public class PreguntaTarjeta extends Pregunta {

    @Column(columnDefinition = "TEXT")
    private String informacion;

    public PreguntaTarjeta() { 
        super(); 
    }

    public PreguntaTarjeta(String enunciado, String informacion) {
        super(enunciado, "Flashcard_Visto"); 
        this.informacion = informacion;
    }

    @Override
    public boolean verificarRespuesta(String respuestaUsuario) {
        return respuestaUsuario != null;
    }

    @Override
    public Pregunta clone() {
        PreguntaTarjeta copia = new PreguntaTarjeta(this.getPregunta(), this.informacion);
        copia.setBloque(this.getBloque());
        return copia;
    }

    @Override
    public Pregunta cloneSinId() {
        return new PreguntaTarjeta(this.getPregunta(), this.informacion);
    }

    public String getInformacion() {
        return informacion;
    }
}
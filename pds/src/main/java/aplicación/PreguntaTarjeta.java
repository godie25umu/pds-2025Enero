package aplicación;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_flashcard")
public class PreguntaTarjeta extends Pregunta {

    @Column(columnDefinition = "TEXT")
    private String informacion;

    public PreguntaTarjeta() { super(); }

    public PreguntaTarjeta(String enunciado, String informacion) {
        super(enunciado, "Flashcard_Visto"); 
        this.informacion = informacion;
    }
}

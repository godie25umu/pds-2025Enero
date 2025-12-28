package aplicación;

import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_test")
public class PreguntaTest extends Pregunta {

    @ElementCollection
    @CollectionTable(name = "pregunta_test_opciones", joinColumns = @JoinColumn(name = "pregunta_id"))
    @Column(name = "opcion")
    private List<String> opciones = new ArrayList<>();

    public PreguntaTest() { 
        super(); 
    }

    public PreguntaTest(String enunciado, String respuesta, List<String> opciones) {
        super(enunciado, respuesta); 
        this.opciones = (opciones != null) ? new ArrayList<>(opciones) : new ArrayList<>();
    }

    @Override
    public boolean verificarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null) return false;
        return getRespuesta().trim().equalsIgnoreCase(respuestaUsuario.trim());
    }

    @Override
    public Pregunta clone() {
        PreguntaTest copia = new PreguntaTest(this.getPregunta(), this.getRespuesta(), new ArrayList<>(this.opciones));
        copia.setBloque(this.getBloque());
        return copia;
    }

    @Override
    public Pregunta cloneSinId() {
        return new PreguntaTest(this.getPregunta(), this.getRespuesta(), new ArrayList<>(this.opciones));
    }

    public List<String> getOpciones() {
        return opciones;
    }
}
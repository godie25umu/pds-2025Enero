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

    public PreguntaTest() { super(); }

    public PreguntaTest(String enunciado, String respuesta, List<String> opciones) {
        super(enunciado, respuesta); 
        this.opciones = opciones;
    }

    @Override
    public boolean verificarRespuesta(String respuestaUsuario) {
        return getRespuesta().equalsIgnoreCase(respuestaUsuario.trim());
    }
}

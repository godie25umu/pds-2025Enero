package aplicación;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_completar")
public class PreguntaCompletar extends Pregunta {

    private String respuestaCorrecta;

    public PreguntaCompletar() { super(); }

    public PreguntaCompletar(String enunciado, String respuestaCorrecta) {
        super(enunciado, respuestaCorrecta);
        this.respuestaCorrecta = respuestaCorrecta;
    }

    @Override
    public boolean verificarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null) return false;
        return respuestaCorrecta.trim().equalsIgnoreCase(respuestaUsuario.trim());
    }
}

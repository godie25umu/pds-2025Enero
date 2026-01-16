package aplicación;

import jakarta.persistence.*;

@Entity
@Table(name = "preguntas_completar")
public class PreguntaCompletar extends Pregunta {
    public PreguntaCompletar() { 
        super(); 
    }

    public PreguntaCompletar(String enunciado, String respuesta) {
        super(enunciado, respuesta);
    }

    @Override
    public boolean verificarRespuesta(String respuestaUsuario) {
        if (respuestaUsuario == null) return false;
        return getRespuesta().trim().equalsIgnoreCase(respuestaUsuario.trim());
    }

    @Override
    public Pregunta clone() {
        PreguntaCompletar copia = new PreguntaCompletar(this.getPregunta(), this.getRespuesta());
        copia.setBloque(this.getBloque());
        return copia;
        
    }

    @Override
    public Pregunta cloneSinId() {
        return new PreguntaCompletar(this.getPregunta(), this.getRespuesta());
    }
}
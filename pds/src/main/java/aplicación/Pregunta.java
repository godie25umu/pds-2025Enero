package aplicación;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="preguntas")
public class Pregunta {
		
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String pregunta;
	    private String respuesta;
		
	    @ManyToOne
	    @JoinColumn(name = "bloque_id")
	    private BloqueDeContenido bloque;
	    
	    public Pregunta() {
	    }
		
		public Pregunta(String pregunta, String respuesta) {
			if (pregunta == null || pregunta.isEmpty()) {
				throw new IllegalArgumentException("La pregunta no puede estar vacía.");
			}
			if (respuesta == null || respuesta.isEmpty()) {
				throw new IllegalArgumentException("La respuesta no puede estar vacía.");
			}
			this.pregunta = pregunta;
			this.respuesta = respuesta;
		}
		
		public String getRespuesta() {
			return respuesta;
		}
	    
	    public boolean verificarRespuesta(String respuestaUsuario) {
	    	return false;
	    }

		public String getPregunta() {
			return pregunta;
		}

		public void setPregunta(String pregunta) {
			this.pregunta = pregunta;
		}

		public void setBloque(BloqueDeContenido bloque) {
			this.bloque = bloque;
		}
		
		public BloqueDeContenido getBloque() {
			return this.bloque;
		}

		public Long getId() {
			return id;
		}

		@Override
		public Pregunta clone() {
			Pregunta copia = new Pregunta(this.pregunta, this.respuesta);
		    copia.setBloque(this.bloque);
		    return copia;
		}
		
		public Pregunta cloneSinId() {
			return new Pregunta(this.getPregunta(), this.getRespuesta());
		}
}


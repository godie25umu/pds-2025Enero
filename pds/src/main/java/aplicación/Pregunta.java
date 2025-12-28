package aplicación;

import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="preguntas")
public abstract class Pregunta {
		
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
	    
	    public abstract boolean verificarRespuesta(String respuestaUsuario);

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
		public abstract Pregunta clone();
		
		public abstract Pregunta cloneSinId();
}


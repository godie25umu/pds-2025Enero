package aplicación;

import java.util.List;

public interface Estrategia {
	List<Pregunta> aplicar(List<Pregunta> listaDePreguntas);
}

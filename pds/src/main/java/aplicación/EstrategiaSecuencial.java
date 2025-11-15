package aplicación;

import java.util.List;


public class EstrategiaSecuencial implements Estrategia{

	@Override
	public List<Pregunta> aplicar(List<Pregunta> preguntas) {
		return preguntas;
	}

}
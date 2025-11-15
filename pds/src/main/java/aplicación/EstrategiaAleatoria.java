package aplicación;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class EstrategiaAleatoria implements Estrategia {
	@Override
	public List<Pregunta> aplicar(List<Pregunta> preguntas) {
		List<Pregunta> aleatorio = new ArrayList<>(preguntas);
		Collections.shuffle(aleatorio);
		return aleatorio;
	}

}

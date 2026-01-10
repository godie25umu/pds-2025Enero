package aplicación;

import java.util.List;
import java.util.stream.Collectors;

public class EstrategiaRepeticiónEspaciada implements Estrategia {
    @Override
    public List<Pregunta> aplicar(List<Pregunta> listaDePreguntas) {
        return listaDePreguntas.stream().collect(Collectors.toList());
    }
}
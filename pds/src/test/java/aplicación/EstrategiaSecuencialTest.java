package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EstrategiaSecuencialTest {

    private EstrategiaSecuencial estrategia;
    private List<Pregunta> preguntas;

    @BeforeEach
    void setUp() {
        estrategia = new EstrategiaSecuencial();
        preguntas = new ArrayList<>();
        preguntas.add(new PreguntaCompletar("P1", "R1"));
        preguntas.add(new PreguntaCompletar("P2", "R2"));
    }

    @Test
    void testInmutabilidad() {
        List<Pregunta> resultado = estrategia.aplicar(preguntas);

        assertSame(preguntas, resultado, "Debe retornar exactamente la misma lista");
        assertEquals(2, resultado.size());
        assertEquals("P1", resultado.get(0).getPregunta());
    }

    @Test
    void testCasosExtremos() {
        List<Pregunta> vacia = new ArrayList<>();
        assertSame(vacia, estrategia.aplicar(vacia));

        List<Pregunta> una = List.of(new PreguntaCompletar("U", "R"));
        assertEquals(1, estrategia.aplicar(una).size());
    }
}
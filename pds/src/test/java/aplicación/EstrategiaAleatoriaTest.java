package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class EstrategiaAleatoriaTest {

    private EstrategiaAleatoria estrategia;
    private List<Pregunta> preguntas;

    @BeforeEach
    void setUp() {
        estrategia = new EstrategiaAleatoria();
        preguntas = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            preguntas.add(new PreguntaCompletar("P" + i, "R" + i));
        }
    }

    @Test
    void testContenido() {
        List<Pregunta> resultado = estrategia.aplicar(preguntas);

        assertAll("Verificación de la lista resultante",
            () -> assertNotNull(resultado),
            () -> assertEquals(preguntas.size(), resultado.size(), "Debe mantener el tamaño"),
            () -> assertTrue(resultado.containsAll(preguntas), "Debe contener todos los elementos originales"),
            () -> assertNotSame(preguntas, resultado, "Debe devolver una nueva lista (copia)")
        );
    }

    @Test
    void testAleatoriedadEInmutabilidad() {
        List<Pregunta> copiaOriginal = new ArrayList<>(preguntas);
        boolean ordenCambiado = false;
        for (int i = 0; i < 10; i++) {
            List<Pregunta> resultado = estrategia.aplicar(preguntas);
            if (!resultado.equals(preguntas)) {
                ordenCambiado = true;
                break;
            }
        }

        assertTrue(ordenCambiado, "La estrategia debería haber cambiado el orden al menos una vez");
        assertEquals(copiaOriginal, preguntas, "La lista original no debe haber sido modificada");
    }

    @Test
    void testCasosExtremos() {
        assertTrue(estrategia.aplicar(new ArrayList<>()).isEmpty());

        List<Pregunta> una = List.of(new PreguntaCompletar("U", "R"));
        assertEquals(1, estrategia.aplicar(una).size());
    }
}
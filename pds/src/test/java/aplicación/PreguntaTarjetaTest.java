package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaTarjetaTest {

    private PreguntaTarjeta pregunta;
    private final String ENUNCIADO = "Fotosíntesis";
    private final String INFO = "Proceso de energía lumínica a química";

    @BeforeEach
    void setUp() {
        pregunta = new PreguntaTarjeta(ENUNCIADO, INFO);
    }

    @Test
    void testCreacion() {
        assertEquals(ENUNCIADO, pregunta.getPregunta());
        assertEquals(INFO, pregunta.getInformacion());
        assertEquals("Flashcard_Visto", pregunta.getRespuesta());
    }

    @Test
    void testCasosVacios() {
        PreguntaTarjeta pConstructorVacio = new PreguntaTarjeta();
        assertNotNull(pConstructorVacio);
        assertThrows(IllegalArgumentException.class, () -> {
            new PreguntaTarjeta(null, "Información");
        }, "El sistema debe impedir crear preguntas sin enunciado");
    }
}
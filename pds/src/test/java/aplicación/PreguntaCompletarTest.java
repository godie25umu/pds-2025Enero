package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaCompletarTest {

    private PreguntaCompletar pregunta;

    @BeforeEach
    void setUp() {
        pregunta = new PreguntaCompletar("La capital de Francia es ____", "París");
    }

    @Test
    void testVerificacion() {
        assertTrue(pregunta.verificarRespuesta("París"), "Debe aceptar la respuesta exacta");
        assertFalse(pregunta.verificarRespuesta("Londres"), "No debe aceptar una respuesta incorrecta");
    }

    @Test
    void testRobustez() {
        assertTrue(pregunta.verificarRespuesta("  París  "), "Debe ignorar espacios en blanco extra");
        assertTrue(pregunta.verificarRespuesta("parís"), "Debe ser insensible a mayúsculas/minúsculas");
    }

    @Test
    void testCasosExtremos() {
        assertFalse(pregunta.verificarRespuesta(""), "No debe validar strings vacíos");
        assertFalse(pregunta.verificarRespuesta(null), "Debe manejar valores nulos sin romperse");
    }
}
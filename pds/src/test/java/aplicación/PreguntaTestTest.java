package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class PreguntaTestTest {
    private PreguntaTest pregunta;
    private final List<String> OPCIONES = Arrays.asList("París", "Londres", "Madrid");

    @BeforeEach
    void setUp() {
        pregunta = new PreguntaTest("¿Capital de Francia?", "París", OPCIONES);
    }

    @Test
    void testAtributosYContenido() {
        assertAll("Verificación de integridad de datos",
            () -> assertEquals("¿Capital de Francia?", pregunta.getPregunta()),
            () -> assertEquals(3, pregunta.getOpciones().size()),
            () -> assertTrue(pregunta.getOpciones().contains("Madrid")),
            () -> assertTrue(pregunta.getOpciones().contains(pregunta.getRespuesta()))
        );
    }

    @Test
    void testRobustez() {
        assertTrue(pregunta.verificarRespuesta("París"));
        assertTrue(pregunta.verificarRespuesta("  PARÍS  "), "Debe ser flexible con espacios y mayúsculas");      
        assertFalse(pregunta.verificarRespuesta("Londres"));
    }

    @Test
    void testCasosExtremos() {
        assertFalse(pregunta.verificarRespuesta(null));
        assertFalse(pregunta.verificarRespuesta(""));
        assertFalse(pregunta.verificarRespuesta("   "));
    }
}
package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LogroTest {

    private Logro logro;
    private final String NOMBRE = "Maestro del Test";
    private final int PUNTOS = 50;

    @BeforeEach
    void setUp() {
        logro = new Logro(NOMBRE, "Has superado todos los tests", PUNTOS);
        logro.setId(100L);
    }

    @Test
    void testIntegridad() {
        assertAll("Verificación de campos del logro",
            () -> assertEquals(100L, logro.getId()),
            () -> assertEquals(NOMBRE, logro.getNombre()),
            () -> assertEquals(PUNTOS, logro.getPuntos()),
            () -> assertTrue(logro.toString().contains(NOMBRE)),
            () -> assertTrue(logro.toString().contains(String.valueOf(PUNTOS)))
        );
    }

    @Test
    void testLogicaDeIgualdad() {
        Logro logroDuplicado = new Logro(NOMBRE, "Otra descripción", PUNTOS);
        logroDuplicado.setId(100L);

        Logro logroDiferente = new Logro("Novato", "Desc", 10);
        logroDiferente.setId(101L);

        assertEquals(logro, logroDuplicado, "Logros con mismo ID deben ser iguales");
        assertNotEquals(logro, logroDiferente, "Logros con distinto ID no deben ser iguales");
        assertEquals(logro.hashCode(), logroDuplicado.hashCode(), "HashCodes deben coincidir");
    }

    @Test
    void testCasosExtremos() {
        Logro logroCero = new Logro("Gratis", "Por registrarse", 0);
        assertEquals(0, logroCero.getPuntos());
        
        Logro logroNegativo = new Logro("Deudor", "Puntos negativos", -10);
        assertTrue(logroNegativo.getPuntos() < 0);
    }
}
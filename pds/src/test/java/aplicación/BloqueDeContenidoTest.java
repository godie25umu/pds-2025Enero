package aplicación;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BloqueDeContenidoTest {

    @Test
    void testGestionPreguntas() {
        BloqueDeContenido bloque = new BloqueDeContenido("Historia");
        bloque.agregarPregunta(new PreguntaTarjeta("¿1492?", "Descubrimiento"));
        
        BloqueDeContenido copia = bloque.cloneSinId();
        
        assertAll("Verificación de copia",
            () -> assertEquals(bloque.getNombre(), copia.getNombre()),
            () -> assertEquals(1, copia.getPreguntas().size()),
            () -> assertNotSame(bloque, copia, "Deben ser objetos distintos en memoria")
        );
    }
}
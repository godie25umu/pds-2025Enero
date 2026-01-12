package aplicación;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CursoTest {

    @Test
    void testGestionDeBloques() {
        Curso curso = new Curso("Programación Java", "Informática");
        BloqueDeContenido bloque = new BloqueDeContenido("Variables");

        curso.agregarBloque(bloque);

        assertEquals(1, curso.getBloques().size());
        assertEquals(curso, bloque.getCurso(), "El bloque debe conocer a su curso");

        curso.eliminarBloque(bloque);
        assertTrue(curso.getBloques().isEmpty());
        assertNull(bloque.getCurso(), "La relación debe romperse");
    }

    @Test
    void testNombre() {
        assertThrows(IllegalArgumentException.class, () -> new Curso("", "Dominio"));
        assertThrows(IllegalArgumentException.class, () -> new Curso(null, "Dominio"));
    }
}
package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioTest {
    private Usuario usuario;
    private Curso curso;

    @BeforeEach
    void setUp() {
        usuario = new Usuario("user1", "pass123");
        curso = new Curso("Matemáticas", "Ciencias");
    }

    @Test
    void testInscripcionYProgreso() {
        usuario.inscribirseEnCurso(curso);
        
        assertTrue(usuario.getCursos().contains(curso));
        assertNotNull(usuario.getProgresoDeCurso(curso), "Al inscribirse debe crearse un objeto progreso");

        usuario.actualizarProgreso(curso, 2, 5); 
        assertEquals(2, usuario.getProgresoDeCurso(curso).getBloqueActual());
    }

    @Test
    void testBloquesCompletados() {
        String nCurso = "Java";
        String nBloque = "POO";

        usuario.marcarBloqueCompletado(nCurso, nBloque);
        assertTrue(usuario.isBloqueCompletado(nCurso, nBloque));

        usuario.resetearProgresoCurso(new Curso(nCurso, ""));
        assertFalse(usuario.isBloqueCompletado(nCurso, nBloque), "Tras resetear, el bloque no debe figurar como completado");
    }
}
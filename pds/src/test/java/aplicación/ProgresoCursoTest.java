package aplicación;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class ProgresoCursoTest {

    @Test
    void testInicializacion() {
        ProgresoCurso progreso = new ProgresoCurso(1, 5);

        assertAll("Verificación de valores iniciales",
            () -> assertEquals(1, progreso.getBloqueActual()),
            () -> assertEquals(5, progreso.getPreguntaActual()),
            () -> assertNotNull(progreso.getUltimaActualizacion(), "La fecha de actualización debe generarse automáticamente")
        );
    }

    @Test
    void testActualizacionDeEstadoYFecha() throws InterruptedException {
        ProgresoCurso progreso = new ProgresoCurso(0, 0);
        LocalDateTime fechaInicial = progreso.getUltimaActualizacion();

        progreso.setBloqueActual(2);
        progreso.setPreguntaActual(10);

        assertAll("Verificación de actualización",
            () -> assertEquals(2, progreso.getBloqueActual()),
            () -> assertEquals(10, progreso.getPreguntaActual()),
            () -> assertTrue(progreso.getUltimaActualizacion().isAfter(fechaInicial), 
                "La fecha de última actualización debe ser posterior a la inicial tras un cambio")
        );
    }
}
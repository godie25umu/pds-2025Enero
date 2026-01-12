package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadísticasTest {
    private Estadísticas stats;

    @BeforeEach
    void setUp() {
        stats = new Estadísticas();
    }

    @Test
    void testCalculos() {
        assertEquals(0, stats.getTotalPreguntas());
        assertEquals(0, stats.getPorcentajeAciertos(), "Con 0 preguntas el porcentaje debe ser 0");

        stats.registrarAcierto(); 
        stats.registrarAcierto(); 
        stats.registrarFallo();   
        
        assertAll("Métricas básicas",
            () -> assertEquals(3, stats.getTotalPreguntas()),
            () -> assertEquals(2, stats.getNumAciertos()),
            () -> assertEquals(66.66, stats.getPorcentajeAciertos(), 0.01)
        );
    }

    @Test
    void testNiveles() {
        stats.añadirExperiencia(50);
        assertEquals(0, stats.getNivelActual());
        assertEquals(50, stats.getProgresoNivel(), "Progreso al 50% para nivel 1");
        assertEquals("Novato", stats.getRango());


        stats.setExperienciaTotal(900); 
        assertEquals(3, stats.getNivelActual());
        assertEquals("Aprendiz", stats.getRango());

        stats.setExperienciaTotal(40000);
        assertEquals("Leyenda", stats.getRango());
    }

    @Test
    void testTiempo() {
        stats.añadirTiempoUso(1);
        assertEquals("00:00:01", stats.getTiempoUsoFormateado());

        stats.setTiempoUso(3661); 
        assertEquals("01:01:01", stats.getTiempoUsoFormateado());

        stats.setTiempoUso(360000);
        assertEquals("100:00:00", stats.getTiempoUsoFormateado());
    }

    @Test
    void testProgresion() {

        stats.setExperienciaTotal(250);
        
        assertEquals(1, stats.getNivelActual());
        assertEquals(100, stats.getXpNivelActual());
        assertEquals(400, stats.getXpParaSiguienteNivel());
        assertEquals(50, stats.getProgresoNivel(), "250 XP es el 50% entre 100 y 400");
    }
}
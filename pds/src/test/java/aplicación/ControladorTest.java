package aplicación;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import controlador.Controlador;
import repositorio.Repositorio;
import java.util.List;

class ControladorTest {
    private Controlador controlador;
    private String usuarioTest = "testUser";
    private String passwordTest = "password123";

    @BeforeEach
    void setUp() {
        controlador = new Controlador();
        // Limpiar cualquier usuario previo con este nombre si existe
        controlador.registrarUsuario(usuarioTest, passwordTest);
    }

    @AfterEach
    void tearDown() {
        if (controlador != null) {
            // Solo cerrar el EntityManager, NO el Factory
            if (controlador.getEntityManager() != null && controlador.getEntityManager().isOpen()) {
                controlador.getEntityManager().close();
            }
        }
    }
    
    @AfterAll
    static void tearDownAll() {
        // Cerrar el factory solo una vez al final de todos los tests
        Repositorio.cerrarFactory();
    }

    @Test
    void testRegistroUsuarioExitoso() {
        String nuevoUsuario = "nuevoUser" + System.currentTimeMillis();
        boolean resultado = controlador.registrarUsuario(nuevoUsuario, "pass1234");
        
        assertTrue(resultado, "El registro de un nuevo usuario debe ser exitoso");
    }

    @Test
    void testRegistroUsuarioDuplicado() {
        controlador.registrarUsuario("duplicado", "password123");
        boolean resultado = controlador.registrarUsuario("duplicado", "password123");
        
        assertFalse(resultado, "No se debe permitir registrar un usuario duplicado");
    }

    @Test
    void testRegistroUsuarioContraseñaCorta() {
        boolean resultado = controlador.registrarUsuario("userCorto", "123");
        
        assertFalse(resultado, "No se debe permitir contraseñas menores a 6 caracteres");
    }

    @Test
    void testInicioSesionExitoso() {
        boolean resultado = controlador.iniciarSesion(usuarioTest, passwordTest);
        
        assertTrue(resultado, "El inicio de sesión con credenciales correctas debe ser exitoso");
        assertNotNull(controlador.getUsuarioActual(), "Debe haber un usuario actual tras iniciar sesión");
        assertEquals(usuarioTest, controlador.getUsuarioActual().getNickname());
    }

    @Test
    void testInicioSesionContraseñaIncorrecta() {
        boolean resultado = controlador.iniciarSesion(usuarioTest, "contraseñaIncorrecta");
        
        assertFalse(resultado, "El inicio de sesión con contraseña incorrecta debe fallar");
        assertNull(controlador.getUsuarioActual(), "No debe haber usuario actual si la sesión falla");
    }

    @Test
    void testInicioSesionUsuarioInexistente() {
        boolean resultado = controlador.iniciarSesion("usuarioInexistente", "password");
        
        assertFalse(resultado, "El inicio de sesión con usuario inexistente debe fallar");
    }

    @Test
    void testCerrarSesion() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        assertNotNull(controlador.getUsuarioActual());
        
        controlador.cerrarSesion();
        
        assertNull(controlador.getUsuarioActual(), "Tras cerrar sesión no debe haber usuario actual");
        assertNull(controlador.getCursoActual(), "Tras cerrar sesión no debe haber curso actual");
        assertNull(controlador.getBloqueActual(), "Tras cerrar sesión no debe haber bloque actual");
    }

    @Test
    void testCrearCurso() {
        Curso curso = controlador.crearCurso("Java Básico", "Programación");
        
        assertNotNull(curso, "El curso creado no debe ser nulo");
        assertEquals("Java Básico", curso.getNombre());
        assertEquals("Programación", curso.getDominio());
    }

    @Test
    void testSeleccionarCurso() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        Curso curso = controlador.crearCurso("Python", "Programación");
        
        controlador.seleccionarCurso(curso);
        
        assertEquals(curso, controlador.getCursoActual(), "El curso seleccionado debe ser el curso actual");
    }

    @Test
    void testAñadirBloqueAlCurso() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        Curso curso = controlador.crearCurso("JavaScript", "Web");
        controlador.seleccionarCurso(curso);
        
        BloqueDeContenido bloque = controlador.crearBloque("Variables");
        controlador.añadirBloqueAlCurso(bloque);
        
        assertTrue(curso.getBloques().contains(bloque), "El bloque debe estar en el curso");
        assertEquals(curso, bloque.getCurso(), "El curso del bloque debe ser el curso actual");
    }

    @Test
    void testIniciarEstudioBloque() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test Bloque");
        Pregunta p1 = new PreguntaCompletar("¿Qué es Java?", "Lenguaje de programación");
        Pregunta p2 = new PreguntaCompletar("¿Qué es Python?", "Lenguaje de programación");
        bloque.agregarPregunta(p1);
        bloque.agregarPregunta(p2);
        
        controlador.iniciarEstudio(bloque);
        
        assertEquals(bloque, controlador.getBloqueActual());
        assertNotNull(controlador.getPreguntaActual(), "Debe haber una pregunta actual");
        assertEquals(2, controlador.getTotalPreguntas());
        assertEquals(1, controlador.getIndicePreguntaActual());
    }

    @Test
    void testVerificarRespuestaCorrecta() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test");
        Pregunta pregunta = new PreguntaCompletar("2+2", "4");
        bloque.agregarPregunta(pregunta);
        
        controlador.iniciarEstudio(bloque);
        
        int aciertosAntes = controlador.getUsuarioActual().getEstadisticas().getNumAciertos();
        boolean resultado = controlador.verificarRespuesta("4");
        int aciertosDepues = controlador.getUsuarioActual().getEstadisticas().getNumAciertos();
        
        assertTrue(resultado, "La respuesta correcta debe retornar true");
        assertEquals(aciertosAntes + 1, aciertosDepues, "Los aciertos deben incrementarse");
        assertEquals(1, controlador.getRachaActualSesion(), "La racha debe ser 1");
    }

    @Test
    void testVerificarRespuestaIncorrecta() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test");
        Pregunta pregunta = new PreguntaCompletar("2+2", "4");
        bloque.agregarPregunta(pregunta);
        
        controlador.iniciarEstudio(bloque);
        
        int fallosAntes = controlador.getUsuarioActual().getEstadisticas().getNumFallos();
        boolean resultado = controlador.verificarRespuesta("5");
        int fallosDepues = controlador.getUsuarioActual().getEstadisticas().getNumFallos();
        
        assertFalse(resultado, "La respuesta incorrecta debe retornar false");
        assertEquals(fallosAntes + 1, fallosDepues, "Los fallos deben incrementarse");
        assertEquals(0, controlador.getRachaActualSesion(), "La racha debe resetearse a 0");
    }

    @Test
    void testSiguientePregunta() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test");
        bloque.agregarPregunta(new PreguntaCompletar("P1", "R1"));
        bloque.agregarPregunta(new PreguntaCompletar("P2", "R2"));
        
        controlador.iniciarEstudio(bloque);
        
        Pregunta primera = controlador.getPreguntaActual();
        boolean hayMas = controlador.siguientePregunta();
        Pregunta segunda = controlador.getPreguntaActual();
        
        assertTrue(hayMas, "Debe haber más preguntas");
        assertNotEquals(primera, segunda, "La pregunta debe cambiar");
        assertEquals(2, controlador.getIndicePreguntaActual());
    }

    @Test
    void testHayMasPreguntas() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test");
        bloque.agregarPregunta(new PreguntaCompletar("P1", "R1"));
        bloque.agregarPregunta(new PreguntaCompletar("P2", "R2"));
        
        controlador.iniciarEstudio(bloque);
        
        assertTrue(controlador.hayMasPreguntas(), "En la primera pregunta debe haber más");
        controlador.siguientePregunta();
        assertFalse(controlador.hayMasPreguntas(), "En la última pregunta no debe haber más");
    }

    @Test
    void testCambiarEstrategia() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        controlador.cambiarEstrategia("aleatoria");
        assertTrue(controlador.getUsuarioActual().getEstrategia() instanceof EstrategiaAleatoria);
        
        controlador.cambiarEstrategia("espaciada");
        assertTrue(controlador.getUsuarioActual().getEstrategia() instanceof EstrategiaRepeticiónEspaciada);
        
        controlador.cambiarEstrategia("secuencial");
        assertTrue(controlador.getUsuarioActual().getEstrategia() instanceof EstrategiaSecuencial);
    }

    @Test
    void testIniciarEstudioCompleto() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        Curso curso = new Curso("Curso Test", "Test");
        BloqueDeContenido b1 = new BloqueDeContenido("Bloque 1");
        BloqueDeContenido b2 = new BloqueDeContenido("Bloque 2");
        
        b1.agregarPregunta(new PreguntaCompletar("P1", "R1"));
        b2.agregarPregunta(new PreguntaCompletar("P2", "R2"));
        
        curso.agregarBloque(b1);
        curso.agregarBloque(b2);
        
        controlador.iniciarEstudioCompleto(curso);
        
        assertEquals(curso, controlador.getCursoActual());
        assertNull(controlador.getBloqueActual(), "Al estudiar curso completo no hay bloque específico");
        assertEquals(2, controlador.getTotalPreguntas(), "Debe incluir preguntas de todos los bloques");
    }

    @Test
    void testObtenerTodosLosLogros() {
        List<Logro> logros = controlador.obtenerTodosLosLogros();
        
        assertNotNull(logros, "La lista de logros no debe ser nula");
        assertFalse(logros.isEmpty(), "Debe haber logros predefinidos");
    }

    @Test
    void testRachasEnSesion() {
        controlador.iniciarSesion(usuarioTest, passwordTest);
        
        BloqueDeContenido bloque = new BloqueDeContenido("Test");
        bloque.agregarPregunta(new PreguntaCompletar("P1", "R1"));
        bloque.agregarPregunta(new PreguntaCompletar("P2", "R2"));
        bloque.agregarPregunta(new PreguntaCompletar("P3", "R3"));
        
        controlador.iniciarEstudio(bloque);
        
        controlador.verificarRespuesta("R1"); // Correcta
        assertEquals(1, controlador.getRachaActualSesion());
        
        controlador.siguientePregunta();
        controlador.verificarRespuesta("R2"); // Correcta
        assertEquals(2, controlador.getRachaActualSesion());
        assertEquals(2, controlador.getMejorRachaSesion());
        
        controlador.siguientePregunta();
        controlador.verificarRespuesta("Incorrecta"); // Incorrecta
        assertEquals(0, controlador.getRachaActualSesion(), "La racha debe resetearse");
        assertEquals(2, controlador.getMejorRachaSesion(), "La mejor racha se mantiene");
    }
}
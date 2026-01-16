package controlador;

import aplicación.*;
import repositorio.*;
import jakarta.persistence.EntityManager;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Controlador {
    
    private EntityManager em;
    private RepositorioUsuario repoUsuario;
    private RepositorioCurso repoCurso;
    private PersistenciaBloque repoBloque;
    private PersistenciaProgreso repoProgreso;
    private RepositorioLogro repoLogro;
    
    private Usuario usuarioActual;
    private Curso cursoActual;
    private BloqueDeContenido bloqueActual;
    private List<Pregunta> preguntasActuales;
    private int indicePreguntaActual;
    private final String CARPETA_CURSOS = "./cursos/";
    
    private int rachaActualSesion = 0;
    private int mejorRachaSesion = 0;
    
    private boolean estudiandoCursoCompleto = false;
    
    public Controlador() {
        this.em = Repositorio.getEntityManager();
        this.repoUsuario = new RepositorioUsuario(em);
        this.repoCurso = new RepositorioCurso(em);
        this.repoBloque = new PersistenciaBloque(em);
        this.repoProgreso = new PersistenciaProgreso(em);
        this.repoLogro = new RepositorioLogro(em);
        this.indicePreguntaActual = 0;
        
        inicializarLogrosPredefinidos();
    }
    
    private void inicializarLogrosPredefinidos() {
        List<Logro> logrosExistentes = obtenerTodosLosLogros();
        
        if (logrosExistentes.isEmpty()) {
            crearLogro("Primer Paso", "Responde tu primera pregunta correctamente", 10);
            crearLogro("Estudiante Novato", "Responde 10 preguntas", 25);
            crearLogro("Aprendiz Dedicado", "Responde 50 preguntas", 50);
            crearLogro("Estudiante Avanzado", "Responde 100 preguntas", 100);
            crearLogro("Erudito", "Responde 250 preguntas", 200);
            crearLogro("Maestro del Conocimiento", "Responde 500 preguntas", 500);
            
            crearLogro("Buen Ojo", "Consigue un 70% de aciertos con al menos 20 preguntas", 75);
            crearLogro("Perfeccionista", "Consigue un 90% de aciertos con al menos 50 preguntas", 150);
            
            crearLogro("Nivel 5", "Alcanza el nivel 5", 100);
            crearLogro("Nivel 10", "Alcanza el nivel 10", 300);
        }
    }
    
    private void crearLogro(String nombre, String descripcion, int puntos) {
        Logro logro = new Logro(nombre, descripcion, puntos);
        repoLogro.guardar(logro);
    }
        
    public List<Logro> obtenerTodosLosLogros() {
        return repoLogro.obtenerTodos();
    }
    
    private void otorgarLogro(String nombreLogro) {
        if (usuarioActual == null) return;
        
        Logro logro = repoLogro.buscarPorNombre(nombreLogro);
        if (logro == null) return;
        
        boolean tieneLogro = usuarioActual.getLogros().stream()
            .anyMatch(l -> l.getId().equals(logro.getId()));
        
        if (!tieneLogro) {
            usuarioActual.getLogros().add(logro);
            
            usuarioActual.getEstadisticas().añadirExperiencia(logro.getPuntos());
            
            repoUsuario.guardarUsuario(usuarioActual);
            System.out.println("¡Logro desbloqueado! " + nombreLogro + " (+" + logro.getPuntos() + " XP)");
        }
    }

    private void verificarYOtorgarLogros() {
        if (usuarioActual == null) return;
        
        Estadísticas stats = usuarioActual.getEstadisticas();
        int totalPreguntas = stats.getTotalPreguntas();
        int aciertos = stats.getNumAciertos();
        float porcentaje = stats.getPorcentajeAciertos();
        int nivel = stats.getNivelActual();
        
        if (aciertos >= 1) {
            otorgarLogro("Primer Paso");
        }
        if (totalPreguntas >= 10) {
            otorgarLogro("Estudiante Novato");
        }
        if (totalPreguntas >= 50) {
            otorgarLogro("Aprendiz Dedicado");
        }
        if (totalPreguntas >= 100) {
            otorgarLogro("Estudiante Avanzado");
        }
        if (totalPreguntas >= 250) {
            otorgarLogro("Erudito");
        }
        if (totalPreguntas >= 500) {
            otorgarLogro("Maestro del Conocimiento");
        }
        
        if (totalPreguntas >= 20 && porcentaje >= 70) {
            otorgarLogro("Buen Ojo");
        }
        if (totalPreguntas >= 50 && porcentaje >= 90) {
            otorgarLogro("Perfeccionista");
        }
        
        if (nivel >= 5) {
            otorgarLogro("Nivel 5");
        }
        if (nivel >= 10) {
            otorgarLogro("Nivel 10");
        }
    }
    
    
    public boolean registrarUsuario(String nickname, String contraseña) {
        try {
            if (nickname == null || nickname.trim().isEmpty()) {
                throw new IllegalArgumentException("El nickname no puede estar vacío");
            }
            if (contraseña == null || contraseña.length() < 6) {
                throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
            }
            
            if (repoUsuario.buscarPorNombre(nickname) != null) {
                return false;
            }
            
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setNickname(nickname);
            //nuevoUsuario.setContraseña(hashPassword(contraseña));
            nuevoUsuario.setContraseña(contraseña);
            nuevoUsuario.setEstadisticas(new Estadísticas());
            nuevoUsuario.setEstrategia(new EstrategiaSecuencial());
            
            repoUsuario.guardarUsuario(nuevoUsuario);
            return true;
        } catch (Exception e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }
    
    public boolean iniciarSesion(String nickname, String contraseña) {
        try {
            Usuario usuario = repoUsuario.buscarPorNombre(nickname);
            if (usuario != null &&
            		//verificarPassword(contraseña, usuario.getContraseña()
            				usuario.getContraseña().equals(contraseña)) {
                this.usuarioActual = usuario;
                if (usuario.getEstrategia() == null) {
                    usuario.setEstrategia(new EstrategiaSecuencial());
                }
                this.rachaActualSesion = 0;
                this.mejorRachaSesion = 0;
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
            return false;
        }
    }
    
    public void cerrarSesion() {
        guardarProgresoActual();
        this.usuarioActual = null;
        this.cursoActual = null;
        this.bloqueActual = null;
        this.preguntasActuales = null;
        this.indicePreguntaActual = 0;
        this.rachaActualSesion = 0;
        this.mejorRachaSesion = 0;
        this.estudiandoCursoCompleto = false;
    }
    
    /*private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al encriptar contraseña", e);
        }
    }
    
    private boolean verificarPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
    
    */
    
    
    public Curso crearCurso(String nombre, String dominio) {
        try {
            Curso curso = new Curso(nombre, dominio);
            repoCurso.guardar(curso);
            return curso;
        } catch (Exception e) {
            System.err.println("Error al crear curso: " + e.getMessage());
            return null;
        }
    }
    
    public void seleccionarCurso(Curso curso) {
        if (curso.getId() == null) {
            repoCurso.guardar(curso); 
        }
        this.cursoActual = curso;
        cargarProgreso();
    }
    
    public void añadirBloqueAlCurso(BloqueDeContenido bloque) {
        if (cursoActual != null) {
            cursoActual.agregarBloque(bloque);
            repoCurso.guardar(cursoActual);
        }
    }
    
    public List<Curso> obtenerCursosDesdeCarpeta() {
        List<Curso> cursos = new ArrayList<>();
        File carpeta = new File(CARPETA_CURSOS);
        
        if (!carpeta.exists()) carpeta.mkdirs();

        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.endsWith(".json"));
        if (archivos != null) {
            ObjectMapper mapper = new ObjectMapper();
            for (File archivo : archivos) {
                try {
                    Curso c = mapper.readValue(archivo, Curso.class);
                    cursos.add(c);
                } catch (IOException e) {
                    System.err.println("Fallo en: " + archivo.getName());
                    e.printStackTrace();
                }
            }
        }
        return cursos;
    }

    public void importarCurso(File archivoOrigen) throws IOException {
        File destino = new File(CARPETA_CURSOS + archivoOrigen.getName());
        java.nio.file.Files.copy(archivoOrigen.toPath(), destino.toPath(), 
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
        
    public BloqueDeContenido crearBloque(String nombre) {
        try {
            BloqueDeContenido bloque = new BloqueDeContenido(nombre);
            repoBloque.guardar(bloque);
            return bloque;
        } catch (Exception e) {
            System.err.println("Error al crear bloque: " + e.getMessage());
            return null;
        }
    }
    
    public void añadirPreguntaABloque(BloqueDeContenido bloque, Pregunta pregunta) {
        bloque.agregarPregunta(pregunta);
        repoBloque.guardar(bloque);
    }
    
    public void iniciarEstudioCompleto(Curso curso) {
        this.cursoActual = curso;
        this.bloqueActual = null;
        this.estudiandoCursoCompleto = true;
        
        List<Pregunta> todasLasPreguntas = new ArrayList<>();
        for (BloqueDeContenido bloque : curso.getBloques()) {
            todasLasPreguntas.addAll(bloque.getPreguntas());
        }
        
        if (usuarioActual != null && usuarioActual.getEstrategia() != null) {
            this.preguntasActuales = usuarioActual.getEstrategia().aplicar(todasLasPreguntas);
        } else {
            this.preguntasActuales = todasLasPreguntas;
        }
        
        this.indicePreguntaActual = 0;
        this.rachaActualSesion = 0;
    }

    public void iniciarEstudio(BloqueDeContenido bloque) {
        this.bloqueActual = bloque;
        this.estudiandoCursoCompleto = false;
        List<Pregunta> preguntasDelBloque = new ArrayList<>(bloque.getPreguntas());
        
        if (usuarioActual != null && usuarioActual.getEstrategia() != null) {
            this.preguntasActuales = usuarioActual.getEstrategia().aplicar(preguntasDelBloque);
        } else {
            this.preguntasActuales = preguntasDelBloque;
        }
        
        this.indicePreguntaActual = 0;
        this.rachaActualSesion = 0;
    }
    
    public Pregunta getPreguntaActual() {
        if (preguntasActuales != null && indicePreguntaActual < preguntasActuales.size()) {
            return preguntasActuales.get(indicePreguntaActual);
        }
        return null;
    }
    
    public boolean verificarRespuesta(String respuestaUsuario) {
        Pregunta preguntaActual = getPreguntaActual();
        if (preguntaActual == null) return false;
        
        boolean esCorrecta = preguntaActual.verificarRespuesta(respuestaUsuario);
        Estadísticas stats = usuarioActual.getEstadisticas();

        if (esCorrecta) {
            stats.registrarAcierto();
            rachaActualSesion++;
            
            if (rachaActualSesion > mejorRachaSesion) {
                mejorRachaSesion = rachaActualSesion;
            }
        } else {
            stats.registrarFallo();
            rachaActualSesion = 0;
            
            if (usuarioActual.getEstrategia() instanceof EstrategiaRepeticiónEspaciada) {
                int posicionInsercion = Math.min(
                    indicePreguntaActual + 3, 
                    preguntasActuales.size()
                );
                preguntasActuales.add(posicionInsercion, preguntaActual);
            }
        }
        
        verificarYOtorgarLogros();
        
        repoUsuario.guardarUsuario(usuarioActual);
        
        return esCorrecta;
    }
    
    public boolean siguientePregunta() {
        if (preguntasActuales != null && indicePreguntaActual < preguntasActuales.size() - 1) {
            indicePreguntaActual++;
            return true;
        }
        return false;
    }
    
    public boolean hayMasPreguntas() {
        return preguntasActuales != null && indicePreguntaActual < preguntasActuales.size() - 1;
    }
    
    public void finalizarSesionEstudio() {
        if (usuarioActual != null && bloqueActual != null && cursoActual != null) {
            usuarioActual.marcarBloqueCompletado(cursoActual.getNombre(), bloqueActual.getNombre());
            repoUsuario.guardarUsuario(usuarioActual);
            System.out.println("Bloque completado: " + bloqueActual.getNombre() + 
                             " del curso " + cursoActual.getNombre());
        } else if (usuarioActual != null && estudiandoCursoCompleto && cursoActual != null) {
            for (BloqueDeContenido bloque : cursoActual.getBloques()) {
                usuarioActual.marcarBloqueCompletado(cursoActual.getNombre(), bloque.getNombre());
            }
            repoUsuario.guardarUsuario(usuarioActual);
            System.out.println("Curso completo completado: " + cursoActual.getNombre());
        }
    }
    
    public boolean isBloqueCompletado(BloqueDeContenido bloque) {
        if (usuarioActual == null || bloque == null || cursoActual == null) {
            return false;
        }
        return usuarioActual.isBloqueCompletado(cursoActual.getNombre(), bloque.getNombre());
    }
    
    public BloqueDeContenido getPrimerBloqueNoCompletado(Curso curso) {
        if (curso == null || curso.getBloques().isEmpty()) {
            return null;
        }
        
        for (BloqueDeContenido bloque : curso.getBloques()) {
            if (!isBloqueCompletado(bloque) && !bloque.getPreguntas().isEmpty()) {
                return bloque;
            }
        }
        
        return curso.getBloques().stream()
            .filter(b -> !b.getPreguntas().isEmpty())
            .findFirst()
            .orElse(null);
    }
    
    public int getTotalPreguntas() {
        return preguntasActuales != null ? preguntasActuales.size() : 0;
    }
    
    public int getIndicePreguntaActual() {
        return indicePreguntaActual + 1;
    }
    
    public int getRachaActualSesion() {
        return rachaActualSesion;
    }
    
    public int getMejorRachaSesion() {
        return mejorRachaSesion;
    }
        
    private void cargarProgreso() {
        if (usuarioActual != null && cursoActual != null) {
            ProgresoCurso progreso = usuarioActual.getProgresoDeCurso(cursoActual);
            if (progreso == null) {
                progreso = new ProgresoCurso(0, 0);
                usuarioActual.getProgresosPorCurso().put(cursoActual.getId(), progreso);
            }
        }
    }
    
    private void guardarProgresoActual() {
        if (usuarioActual != null && cursoActual != null) {
            ProgresoCurso progreso = usuarioActual.getProgresoDeCurso(cursoActual);
            if (progreso != null) {
                repoProgreso.actualizarProgreso(
                    usuarioActual.getNickname(), 
                    cursoActual, 
                    progreso
                );
            }
        }
    }
        
    public void cambiarEstrategia(String tipoEstrategia) {
        if (usuarioActual == null) return;
        
        Estrategia nuevaEstrategia;
        switch (tipoEstrategia.toLowerCase()) {
            case "aleatoria":
                nuevaEstrategia = new EstrategiaAleatoria();
                break;
            case "espaciada":
                nuevaEstrategia = new EstrategiaRepeticiónEspaciada();
                break;
            case "secuencial":
            default:
                nuevaEstrategia = new EstrategiaSecuencial();
                break;
        }
        
        usuarioActual.setEstrategia(nuevaEstrategia);
    }
        
    public Usuario getUsuarioActual() {
        return usuarioActual;
    }
    
    public Curso getCursoActual() {
        return cursoActual;
    }
    
    public BloqueDeContenido getBloqueActual() {
        return bloqueActual;
    }
    
    public EntityManager getEntityManager() {
        return em;
    }
    
    public void cerrar() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        Repositorio.cerrarFactory();
    }
}
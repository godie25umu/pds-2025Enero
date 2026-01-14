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
    private int indicePreguntaActual;
    private RepositorioLogro repoLogro;

    
    private Usuario usuarioActual;
    private Curso cursoActual;
    private BloqueDeContenido bloqueActual;
    private List<Pregunta> preguntasActuales;
    private final String CARPETA_CURSOS = "./cursos/";
  
    private int rachaActualSesion = 0;
    private int mejorRachaSesion = 0;


    public Controlador() {
        this.em = Repositorio.getEntityManager();
        this.repoUsuario = new RepositorioUsuario(em);
        this.repoCurso = new RepositorioCurso(em);
        this.repoLogro = new RepositorioLogro(em);
        this.indicePreguntaActual = 0;

        
    }
    
   
    // ==================== GESTIÓN DE USUARIOS ====================
    
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
            nuevoUsuario.setContraseña(hashPassword(contraseña));
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
            if (usuario != null && verificarPassword(contraseña, usuario.getContraseña())) {
                this.usuarioActual = usuario;
                if (usuario.getEstrategia() == null) {
                    usuario.setEstrategia(new EstrategiaSecuencial());
                }

                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error al iniciar sesión: " + e.getMessage());
            return false;
        }
    }
    
    public void cerrarSesion() {
        this.usuarioActual = null;
        this.cursoActual = null;
        this.bloqueActual = null;
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
    
    public void seleccionarCurso(Curso curso) {
        if (curso.getId() == null) {
            repoCurso.guardar(curso); 
        }
        this.cursoActual = curso;
        cargarProgreso();
    }
    
    private String hashPassword(String password) {
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
    
    // ==================== GESTIÓN DE CURSOS ====================
    
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
    
    public Pregunta getPreguntaActual() {
        if (preguntasActuales != null && indicePreguntaActual < preguntasActuales.size()) {
            return preguntasActuales.get(indicePreguntaActual);
        }
        return null;
    }
    
    public int getIndicePreguntaActual() {
        return indicePreguntaActual + 1;
    }
    
    public int getTotalPreguntas() {
        return preguntasActuales != null ? preguntasActuales.size() : 0;
    }
    
    public int getRachaActualSesion() {
        return rachaActualSesion;
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
    
    private void verificarYOtorgarLogros() {
        if (usuarioActual == null) return;
        
        Estadísticas stats = usuarioActual.getEstadisticas();
        int totalPreguntas = stats.getTotalPreguntas();
        int aciertos = stats.getNumAciertos();
        float porcentaje = stats.getPorcentajeAciertos();
        int nivel = stats.getNivelActual();
        
        // LOGROS DE PROGRESIÓN
        if (aciertos >= 1) {
            otorgarLogro("🌱 Primer Paso");
        }
        if (totalPreguntas >= 10) {
            otorgarLogro("📚 Estudiante Novato");
        }
        if (totalPreguntas >= 50) {
            otorgarLogro("📖 Aprendiz Dedicado");
        }
        if (totalPreguntas >= 100) {
            otorgarLogro("🎓 Estudiante Avanzado");
        }
        if (totalPreguntas >= 250) {
            otorgarLogro("🏆 Erudito");
        }
        if (totalPreguntas >= 500) {
            otorgarLogro("🚀 Maestro del Conocimiento");
        }
        
        // LOGROS DE PRECISIÓN
        if (totalPreguntas >= 20 && porcentaje >= 70) {
            otorgarLogro("🎯 Buen Ojo");
        }
        if (totalPreguntas >= 50 && porcentaje >= 90) {
            otorgarLogro("💯 Perfeccionista");
        }
        
        // LOGROS DE NIVEL
        if (nivel >= 5) {
            otorgarLogro("⭐ Nivel 5");
        }
        if (nivel >= 10) {
            otorgarLogro("💎 Nivel 10");
        }
    
    
    }
    
    private void otorgarLogro(String nombreLogro) {
        if (usuarioActual == null) return;
        
        Logro logro = repoLogro.buscarPorNombre(nombreLogro);
        if (logro == null) return;
        
        boolean tieneLogro = usuarioActual.getLogros().stream()
            .anyMatch(l -> l.getId().equals(logro.getId()));
        
        if (!tieneLogro) {
            usuarioActual.getLogros().add(logro);
            
            // ⭐ AÑADIR XP DEL LOGRO
            usuarioActual.getEstadisticas().añadirExperiencia(logro.getPuntos());
            
            repoUsuario.guardarUsuario(usuarioActual);
            System.out.println("🎉 ¡Logro desbloqueado! " + nombreLogro + " (+" + logro.getPuntos() + " XP)");
        }
    }
    
    public boolean hayMasPreguntas() {
        return preguntasActuales != null && indicePreguntaActual < preguntasActuales.size() - 1;
    }
    
    public boolean siguientePregunta() {
        if (preguntasActuales != null && indicePreguntaActual < preguntasActuales.size() - 1) {
            indicePreguntaActual++;
            return true;
        }
        return false;
    }
    
    public void iniciarEstudioCompleto(Curso curso) {
        this.cursoActual = curso;
        this.bloqueActual = null;
        
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
    
    public int getMejorRachaSesion() {
        return mejorRachaSesion;
    }

    public void iniciarEstudio(BloqueDeContenido bloque) {
        this.bloqueActual = bloque;
        List<Pregunta> preguntasDelBloque = new ArrayList<>(bloque.getPreguntas());
        
        if (usuarioActual != null && usuarioActual.getEstrategia() != null) {
            this.preguntasActuales = usuarioActual.getEstrategia().aplicar(preguntasDelBloque);
        } else {
            this.preguntasActuales = preguntasDelBloque;
        }
        
        this.indicePreguntaActual = 0;
        this.rachaActualSesion = 0;
    }

    public void importarCurso(File archivoOrigen) throws IOException {
        File destino = new File(CARPETA_CURSOS + archivoOrigen.getName());
        java.nio.file.Files.copy(archivoOrigen.toPath(), destino.toPath(), 
            java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
    
   
    
    // ==================== GETTERS ====================
    
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
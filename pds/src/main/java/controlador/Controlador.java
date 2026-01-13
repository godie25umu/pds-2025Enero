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
    
    private Usuario usuarioActual;
    private Curso cursoActual;
    private BloqueDeContenido bloqueActual;
    private final String CARPETA_CURSOS = "./cursos/";
  
    
    public Controlador() {
        this.em = Repositorio.getEntityManager();
        this.repoUsuario = new RepositorioUsuario(em);
        this.repoCurso = new RepositorioCurso(em);

        
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
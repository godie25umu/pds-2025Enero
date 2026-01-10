package aplicación;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;

@Entity
@Table(name = "bloques_contenido")
public class BloqueDeContenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "curso_id")
    @JsonIgnore
    private Curso curso;

    @OneToMany(mappedBy = "bloque", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pregunta> preguntas = new ArrayList<>();

    public BloqueDeContenido() {
    }

    public BloqueDeContenido(String nombre) {
        if (nombre == null || nombre.isEmpty()) {
            throw new IllegalArgumentException("El nombre del bloque no puede estar vacío.");
        }
        this.nombre = nombre;
    }

    public void agregarPregunta(Pregunta pregunta) {
        this.preguntas.add(pregunta);
        pregunta.setBloque(this); 
    }

    public void agregarPreguntas(List<Pregunta> nuevasPreguntas) {
        for (Pregunta p : nuevasPreguntas) {
            this.agregarPregunta(p);
        }
    }

    public void eliminarPregunta(Pregunta pregunta) {
        this.preguntas.remove(pregunta);
        pregunta.setBloque(null);
    }

    public void hacerAleatorio() {
        Collections.shuffle(this.preguntas);
    }

    @Override
    public BloqueDeContenido clone() {
        BloqueDeContenido copia = new BloqueDeContenido(this.nombre);
        for (Pregunta p : this.preguntas) {
            copia.agregarPregunta(p.clone());
        }
        copia.setCurso(this.curso);
        return copia;
    }

    public BloqueDeContenido cloneSinId() {
        BloqueDeContenido copia = new BloqueDeContenido(this.nombre);
        for (Pregunta p : this.preguntas) {
            copia.agregarPregunta(p.cloneSinId());
        }
        return copia;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public List<Pregunta> getPreguntas() {
        return preguntas;
    }

    public void setPreguntas(List<Pregunta> preguntas) {
        this.preguntas = preguntas;
    }
}
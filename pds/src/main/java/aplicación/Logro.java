package aplicación;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "logros")
public class Logro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String icono;
    private int puntos;

    @ManyToMany(mappedBy = "logros")
    private List<Usuario> usuarios = new ArrayList<>();

    public Logro() {}
}

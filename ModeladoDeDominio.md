```mermaid
classDiagram
    class Usuario {
        - String nickname
        - String contraseña
        - Set~String~ bloquesCompletados
        + inscribirseEnCurso(Curso)
        + marcarBloqueCompletado(String, String)
    }

    class Estrategia {
        <<interface>>
        + aplicar(List~Pregunta~) List~Pregunta~
    }

    class EstrategiaSecuencial { }
    class EstrategiaAleatoria { }
    class RepeticionEspaciada { }

    class Curso {
        - Long id
        - String nombre
        - String dominio
    }

    class BloqueDeContenido {
        - Long id
        - String nombre
    }

    class Pregunta {
        <<abstract>>
        - Long id
        - String pregunta
        - String respuesta
        + verificarRespuesta(String) boolean
    }

    class PreguntaTest {
        - List~String~ opciones
    }

    class PreguntaCompletar { }

    class PreguntaTarjeta { }

    class Estadísticas {
        - int numAciertos
        - int numFallos
        - int experienciaTotal
        - int tiempoUso
        + getNivelActual() int
        + getProgresoNivel() int
    }

    class ProgresoCurso {
        - int bloqueActual
        - int preguntaActual
        - LocalDateTime ultimaActualizacion
    }

    class Logro {
        - Long id
        - String nombre
        - String descripcion
        - int puntos
    }

    Usuario "*" -- "*" Curso : inscritos
    Usuario "1" -- "1" Estadísticas : tiene
    Usuario "1" -- "0..1" Estrategia : usa
    Usuario "*" -- "*" Logro : obtiene
    Usuario "1" -- "*" ProgresoCurso : rastrea
    
    Curso "1" -- "*" BloqueDeContenido : contiene
    BloqueDeContenido "1" -- "*" Pregunta : tiene
    
    Pregunta <|-- PreguntaTest
    Pregunta <|-- PreguntaCompletar
    Pregunta <|-- PreguntaTarjeta
    
    Estrategia <|.. EstrategiaSecuencial
    Estrategia <|.. EstrategiaAleatoria
    Estrategia <|.. RepeticionEspaciada

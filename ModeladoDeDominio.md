```mermaid
classDiagram
    class Usuario {
        - String nombre
        - String email
        - String contraseña
        - int nivel
    }

    class EstrategiaAprendizaje {
    }

    class Secuencial {
    }
    class RepeticionEspaciada {
    }
    class Aleatoria {
    }

    class Curso {
        - String nombre
        - String dominio
    }

    class BloqueContenido {
        - String titulo
    }

    class Pregunta {
        - String enunciado
    }

    class PreguntaTest {
        - List<String> opciones
    }

    class Completar {
        - String respuestaCorrecta
    }

    class Flashcard {
        - String informacion
    }

    class Estadisticas {
        - int tiempoUso
        - int mejorRacha
        - logrosConseguidos
    }

    class Logro {
        - String nombre
        - String descripcion
        - String icono
        - int puntos
    }

    Usuario "(1,n)" -- "(0,n)" Curso : realiza
    Curso "(1,n)" -- "(1,1)" BloqueContenido : contiene
    BloqueContenido "(1,n)" -- "(1,1)" Pregunta : tiene
    Pregunta <|-- PreguntaTest
    Pregunta <|-- Completar
    Pregunta <|-- Flashcard
    EstrategiaAprendizaje <|-- Secuencial
    EstrategiaAprendizaje <|-- RepeticionEspaciada
    EstrategiaAprendizaje <|-- Aleatoria
    Usuario "(1,1)" -- "(1,1)" Estadisticas : tiene
    Usuario "(1,1)" -- "(1,n)" EstrategiaAprendizaje : aplica
    Usuario "(0,n)" -- "(0,n)" Logro : obtiene

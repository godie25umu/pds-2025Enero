# Casos de uso de la aplicación

## Identificador: Inicio de curso
**Actor(es):**  
- Estudiante  

**Objetivo:**  
Iniciar el curso (por primera vez) en el que el usuario adquirirá nuevos conocimientos sobre alguna materia.  

**Pasos:**  
1. El usuario accede a la aplicación y a la ventana de selección de cursos.  
2. El usuario selecciona el curso que quiere realizar y la estrategia de aprendizaje.  
3. El sistema carga el archivo de curso con la información del curso y usa su contenido para comenzar.   

---

## Caso de Uso: Realización del curso

**Actores:**
-Estudiante

**Objetivo:**
-Permitir que el usuario realize el curso hasta el final, incluso haciendo pausas de por medio.

**Precondiciones**
- El usuario está inscrito en el curso.

**Pasos**
1. El usuario selecciona un curso.
2. El sistema muestra las lecciones disponibles.
3. El usuario elige una lección.
4. El sistema presenta los ejercicios.
5. El usuario responde.
6. El sistema verifica y muestra datos de análisis.
7. Si el usuario completa la lección, el sistema registra el progreso de forma persistente.
8. Si el usuario falla, el sistema permite repetir ejercicios.
9. Al finalizar, el sistema muestra el progreso actual del curso.

---

## Identificador: Reaudación del curso
**Actor(es):**  
-Estudiante.
- 
**Objetivo:**  
El usuario debe poder reanudar el curso que antes ha dejado a medias.  

**Pasos:**  
1. El usuario accede a la aplicación y a la ventana de selección de cursos.  
2. El usuario selecciona el curso que quiere realizar.
3. El sistema reconoce que se tienen "datos de guardado".
4. Se usan estos datos para dejar el curso en el estado en el que estuvo por última vez.
5. Se continúa como en el caso "Realización del curso"   


---

## Identificador: Guardado de estadísticas
**Actor(es):**  
- Estudiante

**Objetivo:**  
El usuario debe poder conservar sus estadísticas de uso de la aplicación, como tiempo de uso o mejores rachas.  

**Pasos:**  
1. El usuario usa la aplicación.  
2. El sistema actualiza constantemente un archivo de persistencia con las estadísticas.  
3. La próxima vez que se abra la aplicación, el sistema revisará este archivo y restaurará el estado anterior.  

---


## Identificador: Compartir cursos
**Actor(es):**  
- Estudiante

**Objetivo:**  
El usuario podrá compartir un curso creado con otros usuarios.  

**Precondiciones:**  
- El curso ha sido creado previamente.  

**Pasos:**  
1. El usuario accede a la carpeta donde tiene almacenados los cursos.  
2. Envía a otro usuario el archivo de curso correspondiente al curso que desea compartir.  
3. El otro usuario lo recibe y lo coloca en su carpeta.  

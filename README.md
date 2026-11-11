# Monitoreo de Militancia UJC en la Facultad

**Universidad de Pinar del Río "Hermanos Saíz Montes de Oca"**
Facultad de Ciencias Técnicas — Departamento de Informática
Asignatura: Estructuras de Datos — Curso 2026-2027
Segundo Examen Parcial — **Equipo 5**

**Integrantes:**
- Lauren Rosa Barrios Bosque
- Liz Anel López Vives
- Osmany Manuel Ungo Seara
- Jorge Luis Hernández Avila
- José Javier Fernández Bravo

---

## Índice

1. [¿De qué trata el proyecto?](#1-de-qué-trata-el-proyecto)
2. [La problemática](#2-la-problemática)
3. [Cómo lo resolvimos](#3-cómo-lo-resolvimos)
4. [Las reglas del sistema](#4-las-reglas-del-sistema)
5. [El menú del programa](#5-el-menú-del-programa)
6. [Organización del proyecto](#6-organización-del-proyecto)
7. [Cómo ejecutar el programa](#7-cómo-ejecutar-el-programa)
8. [Casos de prueba](#8-casos-de-prueba)
9. [Declaración de uso de Inteligencia Artificial](#10-declaración-de-uso-de-inteligencia-artificial)

---

## 1. ¿De qué trata el proyecto?

Es un programa en Java, que se usa desde la consola, para llevar el control de la militancia de la UJC (Unión de Jóvenes Comunistas) en una facultad.

Con él se puede:

- Registrar a los estudiantes de cada carrera, organizados en grupos.
- Dar de baja a un estudiante de la facultad o solo de la UJC.
- Recibir solicitudes de ingreso a la UJC y atenderlas en el orden en que llegaron.
- Guardar un historial de los trámites realizados.
- Descubrir qué carreras tienen **alta militancia**, es decir, más de la mitad de su matrícula en la UJC.

---

## 2. La problemática

El ejercicio pide automatizar el control de una entidad (en nuestro caso, la militancia UJC de una facultad) usando las estructuras de datos que vimos en la asignatura:

| Lo que pide el ejercicio | Lo que hicimos en nuestro caso |
|---|---|
| Una **multilista**: una lista principal donde cada elemento tiene dentro otra lista | Facultad → Carreras → Grupos → Estudiantes |
| Una **pila** (el último en entrar es el primero en salir) para el historial | Expedientes de los trámites realizados |
| Una **cola** (el primero en llegar es el primero en ser atendido) | Solicitudes de ingreso a la UJC |
| Un **método recursivo** que recorra la multilista | Averiguar qué carreras tienen alta militancia |

De cada estudiante se guarda su **nombre**, su **número de lista** y si es **militante de la UJC** (sí o no). Nosotros agregamos además un **ID de 4 dígitos** para identificarlo sin confundirlo con otro.

---

## 3. Cómo lo resolvimos

### 3.1 La multilista: carreras, grupos y estudiantes

Una **multilista** es una lista cuyos elementos guardan, cada uno, otra lista. Para entenderlo, piensa en carpetas: la facultad tiene carpetas de carreras, cada carrera tiene carpetas de grupos y cada grupo tiene la lista de sus estudiantes.

```
Facultad de Ciencias Técnicas
 │
 ├── Ing. Informática ──────────────► Ing. Mecánica ──────► Otra ──► ...
 │      │
 │      ├── Grupo 1 ───────────────► Grupo 2
 │      │     │                        │
 │      │     ├── Ana (lista 1)        └── Luis (lista 1)
 │      │     ├── Pedro (lista 2)
 │      │     └── ... (hasta 25)
```

Cada elemento está en una "caja" que llamamos **nodo**, y cada caja sabe cuál es la siguiente de su lista:

- `NodoCarrera`: guarda el nombre de la carrera, su primer grupo y la siguiente carrera.
- `NodoGrupo`: guarda el nombre del grupo, su primer estudiante y el siguiente grupo.
- `NodoEstudiante`: guarda el ID, el nombre, el número de lista, si es militante y el siguiente estudiante.

La clase `GestorUJC` es la que maneja toda esta estructura.

### 3.2 La pila: el historial de expedientes

Una **pila** funciona como una pila de platos: el último que pones es el primero que sacas. Para el historial nos sirve porque lo que más interesa ver es **lo último que pasó**.

Cada vez que ocurre algo importante se guarda un **expediente** con el nombre del estudiante, su carrera, su grupo y el motivo. Los motivos posibles son:

- `Ingreso aprobado a la UJC`
- `Baja de la UJC`
- `Baja de estudiante`

Desde el menú se puede ver el último trámite, ver el historial completo (del más reciente al más antiguo) o vaciarlo.

### 3.3 La cola: las solicitudes de ingreso

Una **cola** funciona como la fila de una tienda: el primero que llega es el primero que se atiende. Así se tratan las solicitudes de ingreso a la UJC: se atienden en el mismo orden en que se pidieron.

Cuando se **solicita** el ingreso, el sistema comprueba que:

- El estudiante exista.
- Pertenezca a la carrera que se eligió.
- No sea ya militante.
- No tenga ya otra solicitud esperando en la cola.

Cuando se **procesa** una solicitud, se atiende la que está primera en la cola: el estudiante pasa a ser militante y se guarda un expediente en la pila. Si al procesarla se descubre que el estudiante o la carrera ya no existen, se muestra un error y no se rompe nada.

### 3.4 La recursividad: carreras con alta militancia

Un método **recursivo** es uno que se llama a sí mismo para resolver un problema más pequeño. Es como contar los libros de una fila: cuentas el primero y le pides al siguiente que cuente los que le quedan después, hasta que ya no queda nadie.

Aquí hay un método recursivo por cada nivel de la multilista, y cada uno le pasa el trabajo al nivel de abajo:

```
evaluarCarreras           → recorre las CARRERAS, una por una
   └─ contarEstudiantesDeGrupos → recorre los GRUPOS de esa carrera
          └─ contarEstudiantes     → recorre los ESTUDIANTES de ese grupo
```

La idea es esta:

1. Para cada carrera se cuenta cuántos estudiantes tiene en total y cuántos son militantes.
2. Si los militantes son **más de la mitad**, la carrera tiene alta militancia.
3. Se repite con la carrera siguiente hasta que no quedan más.

Los tres métodos terminan cuando llegan al final de su lista (cuando ya no hay nodo siguiente). A esa condición de parada se le llama **caso base**.

Para saber si son "más de la mitad" comparamos `militantes × 2 > total` en lugar de dividir. Es lo mismo, pero se evitan los decimales.

---

## 4. Las reglas del sistema

- **Cada estudiante tiene un ID de exactamente 4 dígitos** y no puede repetirse en toda la facultad.
- **Cada grupo tiene como máximo 25 estudiantes.** Al llegar el estudiante número 26 de una carrera, el sistema crea automáticamente el `Grupo 2`, y así sucesivamente.
- **El número de lista** se asigna en orden dentro del grupo: 1, 2, 3...
- **Al dar de baja a un estudiante, la lista se vuelve a numerar**, para que nunca queden números repetidos ni huecos.
- **Si un grupo queda vacío** y es el último de su carrera, el siguiente estudiante que llegue lo reutiliza.
- **Alta militancia** significa más del 50 %. Exactamente el 50 % **no** cuenta. Una carrera sin estudiantes no se evalúa.
- **Las carreras** vienen cargadas desde el inicio: Ing. Informática, Ing. Telecomunicaciones y Electrónica, Ing. Mecánica, Ing. Geológica, Ing. Industrial y Otra.
- **El programa no se cierra por errores del usuario.** Si se escribe una letra donde va un número, un ID que no tiene 4 dígitos, un nombre vacío o una opción fuera de rango, se muestra un mensaje y se vuelve a preguntar.

---

## 5. El menú del programa

| Opción | Qué hace |
|:---:|---|
| 1 | Agregar un estudiante (el grupo y el número de lista se asignan solos) |
| 2 | Dar de baja a un estudiante de la UJC (sigue en la facultad) |
| 3 | Dar de baja a un estudiante de la facultad |
| 4 | Ver la facultad completa: carreras, grupos y estudiantes |
| 5 | Evaluar las carreras con alta militancia (método recursivo) |
| 6 | Ver el último trámite registrado |
| 7 | Ver el historial completo |
| 8 | Vaciar el historial |
| 9 | Solicitar el ingreso de un estudiante a la UJC |
| 10 | Procesar la siguiente solicitud de ingreso |
| 11 | Ver cuál es la siguiente solicitud en espera |
| 12 | Ejecutar los casos de prueba automáticos |
| 13 | Salir |

---

## 6. Organización del proyecto

| Archivo | Para qué sirve |
|---|---|
| `Main.java` | Arranca el programa y muestra el menú |
| `GestorUJC.java` | La clase principal: maneja la multilista, la pila, la cola y el método recursivo |
| `IMultilista.java` | Lista las operaciones que debe tener el gestor |
| `NodoCarrera.java`, `NodoGrupo.java`, `NodoEstudiante.java` | Los tres niveles de la multilista |
| `Nodo.java` | Nodo genérico que usan la pila y la cola |
| `IStack.java`, `LinkedStack.java` | La pila (con enlaces entre nodos) |
| `IQueue.java`, `LinkedQueue.java` | La cola (con enlaces entre nodos) |
| `Expediente.java` | Un registro del historial (lo que guarda la pila) |
| `SolicitudIngreso.java` | Una solicitud de ingreso (lo que guarda la cola) |
| `Pruebas.java` | Los casos de prueba automáticos |

> **Nota sobre `GestorUJC.java`:** al final tiene una sección llamada *"Métodos de consulta para las pruebas"*. Son métodos cortos que solo devuelven información, como `existeEstudiante` o `tamanoCola`. El menú no los usa: están para que `Pruebas` pueda comprobar los resultados por sí sola, sin tener que leer lo que se imprime en pantalla.

---

## 7. Cómo ejecutar el programa

Se necesita tener instalado el **JDK de Java** (el proyecto se probó con Java 21).

Desde la carpeta donde están los archivos `.java`:

```bash
javac -encoding UTF-8 *.java
java Main
```

Si se quiere ejecutar solo las pruebas, sin pasar por el menú:

```bash
java Pruebas
```

> Si en la consola de Windows las tildes y la "ñ" se ven mal, es un problema de la consola y no del programa. Se arregla compilando con `-encoding UTF-8` (como arriba) o cambiando la consola a UTF-8.

---

## 8. Casos de prueba

Las pruebas están en la clase `Pruebas`. Se pueden ejecutar desde el **menú, opción 12**, o con `java Pruebas`.

Cada caso prepara una situación, la ejecuta y comprueba el resultado. Después de cada comprobación muestra `OK` o `FALLO`, y al final un resumen. Hay **30 casos con 36 comprobaciones**, y todas pasan.

Se dividen en tres grupos:

### 8.1 Pruebas positivas (todo sale bien)

| Caso | Qué se prueba |
|:---:|---|
| 1.1 | Con 25 estudiantes hay un solo grupo; el 26 crea el Grupo 2 y recibe el número de lista 1 |
| 1.2 | 13 de 26 militantes (50 %) **no** es alta militancia |
| 1.3 | 14 de 27 militantes (más del 50 %) **sí** es alta militancia |
| 1.4 | La cola atiende primero a quien llegó primero; al procesar, el estudiante pasa a ser militante |
| 1.5 | Dar de baja de la UJC a un militante funciona |
| 1.6 | La pila muestra primero el último trámite realizado |
| 1.7 | Dar de baja a un estudiante de la facultad funciona |
| 1.8 | Vaciar el historial deja la pila vacía |
| 1.9 | Tras dar de baja a alguien del medio, los números de lista se ajustan sin repetirse |
| 1.10 | Agregar un estudiante usando el menú (escribiendo los datos) funciona |

### 8.2 Pruebas negativas (cosas que salen mal a propósito)

El objetivo es comprobar que el programa **avisa del error y sigue funcionando**.

| Caso | Qué se intenta |
|:---:|---|
| 2.1 | Evaluar la militancia cuando no hay ningún estudiante |
| 2.2 | Procesar o ver una solicitud con la cola vacía |
| 2.3 | Ver el historial con la pila vacía |
| 2.4 | Dar de baja de la UJC a un ID que no existe |
| 2.5 | Dar de baja de la UJC a alguien que no es militante |
| 2.6 | Dar de baja de la facultad a un ID que no existe |
| 2.7 | Agregar un estudiante con un ID repetido |
| 2.8 | Agregar un estudiante en una carrera que no existe |
| 2.9 | Procesar una solicitud de un estudiante que ya no existe |
| 2.10 | Procesar una solicitud de una carrera que no existe |
| 2.11 | Solicitar el ingreso dos veces seguidas para el mismo estudiante |
| 2.12 | Solicitar el ingreso de alguien que ya es militante |
| 2.13 | Solicitar el ingreso eligiendo una carrera que no es la del estudiante |
| 2.14 | Solicitar el ingreso con un ID que no existe |
| 2.15 | Escribir letras, números fuera de rango o negativos en el menú |
| 2.16 | Escribir un ID inválido y un nombre vacío |

### 8.3 Pruebas de límite (situaciones extremas)

| Caso | Qué se prueba |
|:---:|---|
| 3.1 | Carreras sin estudiantes junto a otras con estudiantes (no debe fallar) |
| 3.2 | Un grupo que quedó vacío se reutiliza en lugar de crear otro |
| 3.3 | La cola sigue funcionando bien después de haberse vaciado |
| 3.4 | La pila sigue funcionando bien después de haberse vaciado |

### 8.4 ¿Cómo sabemos que las pruebas sirven?

Una prueba que siempre dice `OK` no demuestra nada. Por eso, durante la revisión se introdujeron **a propósito 10 errores** en el código (por ejemplo, aceptar IDs repetidos, contar el 50 % como alta militancia o no renumerar la lista tras una baja). Las pruebas detectaron los 10.

---

## 9. Declaración de uso de Inteligencia Artificial

En este proyecto se utilizó Inteligencia Artificial (Claude, de Anthropic) como herramienta de apoyo en las siguientes partes:

- **Revisión del código y corrección de errores**: se utilizó la IA para comprobar que el código compilara y para buscar situaciones en las que el programa pudiera fallar. 

- **Generación de casos de prueba**: se utilizó la IA para proponer los casos pruebas con la clase `Pruebas` (casos positivos, negativos y de límite) y los métodos de consulta que esa clase necesita para comprobar los resultados.

- **Diseño de la presentación**: se utilizó la IA para apoyar en la organización del contenido de la presentación oral y en la estructura de las diapositivas.

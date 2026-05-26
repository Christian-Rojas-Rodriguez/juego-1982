# TPI — Algoritmos 1 (2026 1° Cuatrimestre)

**Licenciatura en Ciencia de Datos**  
Trabajo Práctico Integrador · Versión 1.0 · 01/03/2026

---

> **Versión actual en desarrollo: MVP 1**  
> Un tipo de enemigo (HarrierEnemigo) · puntos · integración con HOME  
> Documentación: [`docs/v1/`](docs/v1/README.md)

---

## Enunciado del problema

Se propone el desarrollo de un videojuego bidimensional de estilo arcade clásico, inspirado en los "vertical scrolling shooters", bajo el título **"1982"**.

El juego recrea una dinámica en la que el/la jugador/a controla una aeronave militar argentina que enfrenta distintos objetivos enemigos. Toma como referencia estética y mecánica a los videojuegos tipo *1942*, adaptando el contexto a un escenario alusivo al **Conflicto del Atlántico Sur**.

La implementación debe contemplar como mínimo:
- Representación gráfica de los elementos del juego
- Interacción mediante controles
- Detección de colisiones
- Administración de estados del juego
- Gestión básica de puntajes o condiciones de victoria/derrota

Cada grupo integrará su módulo con los demás para construir un juego realizado por todo el curso.

---

## Módulos del juego (por grupo)

| Módulo | Descripción |
|--------|-------------|
| Lobby General | Home y pantalla de inicio del juego |
| Avión Skyhawk | Armada Argentina |
| Avión Pucará | Ejército Argentino |
| Avión Aermacchi MB-339 | Piloteado por Owen Crippa |
| Avión Mirage | Fuerza Aérea Argentina |
| Avión Super Etendard | Armada Argentina |
| Tutorial Interactivo | Tutorial de juego |

---

## Requisitos funcionales

### Estética
Gráficos pixel art respetando la estética simple de los videojuegos de la década del 80.

### Escalabilidad
El juego debe permitir que cada grupo agregue su módulo sin interferir en el funcionamiento de los demás.

### Gestión de recursos
Todos los grupos deben respetar un **diagrama de clases escalable unificado**, negociado entre los y las estudiantes.

### Pantalla de inicio
- El usuario puede elegir el módulo con el que quiera jugar.
- Se muestran las estadísticas de todos los juegos.

### Estadísticas (2 dimensiones)
1. **Estadísticas propias del módulo** — vistas y manejadas dentro del módulo del avión, con persistencia de datos.
2. **Estadísticas generales** — devueltas al HOME para agregación con los datos de todos los juegos.

---

## Cronograma de entregas

| Fecha | Tipo | Entregables |
|-------|------|-------------|
| 05/05/2026 | Seguimiento I | Documentación de Análisis |
| 19/05/2026 | Seguimiento II | Documentación de Diseño |
| 09/06/2026 | Final | Todos los entregables + defensa oral |

> Los seguimientos parciales no son obligatorios pero impactan en el criterio de organización del grupo.

---

## Entregables

### Documentación de Análisis (`docs/analisis/`)

Contenidos mínimos:

- **p1.** Objetivo y Alcance
- **p2.** Descripción de alto nivel del sistema
- **p3.** Requerimientos funcionales más relevantes
- **p4.** Requerimientos no funcionales

### Documentación de Diseño (`docs/diseño/`)

El sistema debe diseñarse bajo el paradigma orientado a objetos contemplando:

- **Extensibilidad** — el modelo debe poder extenderse fácilmente
- **Mantenibilidad** — evitar duplicación de código y desacoplar subsistemas
- **Gestión de errores** — jerarquía de excepciones que capture todos los errores en tiempo de ejecución

Contenidos mínimos:

- **p5.** Diagrama Conceptual
- **p6.** Diagrama de clases del Modelo
- **p7.** Diagrama de la Vista y Controlador
- **p8.** Diagrama de Secuencia de al menos 5 Casos de Uso (alta interacción entre clases)
- **p9.** Diagrama de Navegación de GUI

Contenidos opcionales:
- Diagrama de clases de un módulo o subsistema relevante
- Diagrama de secuencia con su diagrama de clases asociado
- Diagrama de estados con su diagrama de clases asociado

> Cada vista se compone de diagrama + descripción. Las vistas deben ser consistentes entre sí.

### Código fuente (`src/`)

- Implementación en Java (versión 8 o superior)
- El código compila sin errores
- La ejecución no arroja excepciones inesperadas
- Documentación de uso (se puede usar Javadoc)
- Se valoran pruebas unitarias con JUnit

### Presentación (defensa oral)

Contenidos recomendados:
- Presentación de integrantes y responsabilidades
- Breve descripción del análisis del problema
- Detalle de la solución diseñada (al menos 1 caso de uso de los p5–p9)
- Problemas encontrados y cómo se resolvieron
- Posibles mejoras pendientes
- Estrategias para incorporar nueva funcionalidad
- Demostración funcional con un caso de uso
- Conclusiones

> **Todos los integrantes deben estar presentes.** Los docentes pueden preguntar a cualquier integrante sobre cualquier aspecto del trabajo.

---

## Criterios de evaluación

### Evaluación grupal

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Análisis del problema** | No se interpreta correctamente o no hay documentación | Documentación suficiente y acorde a lo solicitado | Completa y argumentada, relacionando temas de clase | Evidencia bibliografía e investigación adicional |
| **Diseño en POO** | Incompleto o no acorde al análisis | Solución básica en POO | Completo, con conceptos de diseño, extensible y mantenible | Usa patrones de diseño, reflexiona sobre principios y atributos de calidad |
| **Implementación** | No compila o arroja errores inesperados | Resuelve el problema sin errores en compilación/ejecución | Incorpora buenas prácticas y está alineado al diseño | Conocimiento profundo del lenguaje, eficiencia o pruebas unitarias |
| **Organización y presentación** | Fuera de término o formato inconsistente | Formato consistente y presentación preparada | Usa herramientas de colaboración, versionado y organización | Gestiona el ciclo de vida del trabajo |

### Evaluación individual

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Participación en el grupo** | Sin evidencia de participación | Plantea ideas o escucha a los demás | Participa activamente en discusiones y resolución | Contribuye proactivamente y fomenta la colaboración |
| **Defensa del trabajo** | No asiste o no participa | Expone su rol y aportes con claridad | Responde con seguridad las preguntas | Relaciona conceptos de la materia y argumenta decisiones de diseño |

---

## Proceso ingenieril — Recomendaciones de la cátedra

- Formar un **comité inter-grupos** (un arquitecto o ScrumMaster por grupo) para establecer interfaces y contratos de software entre módulos.
- Cada grupo Scrum debería tener un **Integrador** que valide las interfaces.
- **Orientar el diseño a interfaces** para garantizar integrabilidad.
- Los errores de integración serán atribuidos a los grupos que no respeten el contrato.
- Los docentes deben aprobar los contratos de integración.

---

## Estructura del proyecto

```
juego-1982/
├── src/
│   ├── main/java/
│   │   ├── game/                    # Lógica principal del juego
│   │   │   ├── entities/
│   │   │   │   ├── player/          # Clases del jugador/aeronave
│   │   │   │   ├── enemies/         # Tipos de enemigos
│   │   │   │   └── projectiles/     # Disparos y proyectiles
│   │   │   ├── levels/              # Módulos/niveles y dificultad
│   │   │   ├── collision/           # Motor de colisiones
│   │   │   └── ui/                  # Pantallas, HUD, menús
│   │   ├── data/                    # Estadísticas y persistencia
│   │   │   ├── model/               # Modelos de estadísticas
│   │   │   ├── storage/             # Lectura/escritura CSV o JSON
│   │   │   └── stats/               # Cálculos: promedio, máx, mín, etc.
│   │   └── exceptions/              # Jerarquía de excepciones del sistema
│   └── test/java/                   # Pruebas unitarias (JUnit)
├── docs/
│   ├── analisis/                    # Documentación de Análisis (p1–p4)
│   └── diseño/                      # Diagramas de diseño (p5–p9)
├── assets/
│   ├── images/                      # Sprites, fondos, íconos pixel art
│   └── sounds/                      # Efectos de sonido
├── lib/                             # core.jar de Processing
├── .gitignore
└── README.md
```

---

## Requisitos técnicos

- Java 8 o superior
- Processing 4.x (`core.jar` en `/lib`)
- IntelliJ IDEA (recomendado)

## Cómo ejecutar

1. Agregar `lib/core.jar` como dependencia en IntelliJ
2. Ejecutar la clase principal con `PApplet.main(...)`

---

## Integrantes

| Nombre | Rol |
|--------|-----|
|        |     |
|        |     |
|        |     |

# Tests (JUnit 5)

Carpeta de tests del proyecto. Refleja la estructura de paquetes de
`src/main/java`, así cada test queda al lado (lógicamente) de la clase que prueba.

```
src/test/java/
├── EjemploTest.java                       # smoke test: confirma que JUnit anda
└── mirage/model/fisica/HitBoxTest.java    # ejemplo de test sobre una clase real
```

## Cómo correr los tests

### Opción A — IntelliJ IDEA (recomendado)
1. Abrir el proyecto. El módulo (`juego-1982.iml`) ya tiene configurada la
   carpeta `src/test/java` como *Test Sources* y la librería **junit5**
   (`lib/junit-platform-console-standalone-*.jar`).
2. Click derecho sobre `src/test/java` → **Run 'All Tests'**, o usar el
   ícono ▶ verde junto a cada clase/método `@Test`.

> Si IntelliJ no reconoce la carpeta, hacer click derecho sobre `src/test/java`
> → *Mark Directory as* → *Test Sources Root*.

### Opción B — Consola
Desde la raíz del repo:

```bash
JUNIT=lib/junit-platform-console-standalone-1.11.4.jar

# 1. Compilar main (necesita Processing) y tests
mkdir -p out/main out/test
find src/main/java -name '*.java' | xargs javac -cp "lib/core.jar" -d out/main
find src/test/java -name '*.java' | xargs javac -cp "lib/core.jar:$JUNIT:out/main" -d out/test

# 2. Ejecutar
java -jar "$JUNIT" execute -cp "lib/core.jar:out/main:out/test" \
  --scan-classpath --details=tree
```

## Cómo escribir un test nuevo

1. Crear la clase en el paquete espejo bajo `src/test/java` (ej: para
   `mirage.model.stats.EstadisticasMirage` → `src/test/java/mirage/model/stats/EstadisticasMirageTest.java`).
2. Usar las anotaciones de JUnit 5 (Jupiter):

```java
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MiClaseTest {
    @Test
    void haceLoQueDebe() {
        assertEquals(esperado, real);
    }
}
```

3. Para tests de funcionalidad todavía no implementada (TODOs del skeleton),
   usar `@Disabled("motivo")`: documenta el comportamiento esperado sin romper
   el build. Al implementar la clase, se quita la anotación y el test empieza a
   validar. Ver `HitBoxTest.java` como ejemplo.

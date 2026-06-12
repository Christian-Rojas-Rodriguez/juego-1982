import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test de humo (smoke test) — sirve solo para confirmar que JUnit 5 está
 * correctamente configurado en el proyecto. Si este test corre en verde,
 * el resto del equipo puede escribir tests siguiendo este mismo patrón.
 *
 * Cómo correr los tests:
 *   - IntelliJ: click derecho sobre src/test/java → "Run All Tests",
 *     o el ícono ▶ verde junto a cada método @Test.
 *   - Por consola: ver instrucciones en src/test/java/README.md
 */
class EjemploTest {

    @Test
    @DisplayName("JUnit 5 está configurado y corre")
    void junitFunciona() {
        assertTrue(true, "Si ves esto en verde, JUnit anda bien");
    }

    @Test
    @DisplayName("Ejemplo de aserción básica")
    void sumaBasica() {
        assertEquals(4, 2 + 2);
    }
}

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitTests {


    private PrintStream old;


    protected ByteArrayOutputStream baos;

    @BeforeEach
    public void setUp() {
        this.old = System.out; // Save a reference to the original stdout stream.
        this.baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }
    @Test
    public void testAdd(){
        BigNumArithmetic.main(new String[] { "AddInput.txt" });
        String output = this.baos.toString().trim();
        assertEquals("1 + 2 = 3" + System.lineSeparator() +
                "43838 + 4883 = 48721" + System.lineSeparator() + "2000 + 67 = 2067" +
                System.lineSeparator() + "10000000000 + 10000 = 10000010000", output);
    }
    @Test
    public void testMultiply() {
        BigNumArithmetic.main(new String[]{"MultiplyInput.txt"});
        String output = this.baos.toString().trim();
        assertEquals("999 * 666 = 665334" + System.lineSeparator() +
                "67 * 1 = 67" + System.lineSeparator() + "123 * 32123 = 3951129" +
                System.lineSeparator() + "10 * 0 = 0", output);
    }
    @Test
    public void testPower() {
        BigNumArithmetic.main(new String[]{"PowerInput.txt"});
        String output = this.baos.toString().trim();
        assertEquals("10 ^ 0 = 1" + System.lineSeparator() +
                "20 ^ 1 = 20" + System.lineSeparator() + "100 ^ 10 = 100000000000000000000" +
                System.lineSeparator() + "2 ^ 64 = 18446744073709551616", output);
    }
    @Test
    public void testThrowsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumArithmetic.main(new String[]{"IllegalArguments.txt"});
        });
    }
    @Test
    public void testIllegalArgumentNumber(){
        assertThrows(IllegalArgumentException.class, () -> {
            BigNumArithmetic.main(new String[]{"1", "2"});
        });
    }
    @Test
    public void testFileNotFound(){
        BigNumArithmetic.main(new String[]{"NoFile.txt"});
        String output = this.baos.toString().trim();
        assertEquals("File not found: NoFile.txt", output);
    }

    @AfterEach
    public void tearDown() {
        System.out.flush();
        System.setOut(this.old);
    }
}

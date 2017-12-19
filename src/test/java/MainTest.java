import org.junit.Test;

/**
 * Description: MainTest
 * Author: DIYILIU
 * Update: 2017-12-19 10:15
 */
public class MainTest {


    @Test
    public void test() {

        for (int i = 0; i < 1000; i++) {

            int random = new Double(Math.random() * 7 + 1).intValue();
            if (random == 8) {
                System.out.println(1);
            }
        }
    }
}

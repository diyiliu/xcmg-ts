import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    @Test
    public void test2() {

        long m = 3263183192064l;

        double a = m / (1024 * 1024 * 1024);

        System.out.println(a);
    }

    @Test
    public void test3(){

        double val = new BigDecimal(30.7704925537109).setScale(1, RoundingMode.DOWN).doubleValue();

        System.out.println(val);
    }
}

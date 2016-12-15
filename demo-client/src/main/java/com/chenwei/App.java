package com.chenwei;

import com.chenwei.denpasar.core.ServiceBeanFactory;
import com.chenwei.service.CalculateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 */
public class App {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void main(String[] args) {
        App app = new App();
        app.test();
    }

    private void test() {
        for (int i = 1; i < 1000; i++) {
            Thread t = new Thread(new TestRun(i, i + 4));
            t.start();
        }

    }

    class TestRun implements Runnable {

        int a, b = 0;

        public TestRun(int i, int j) {
            this.a = i;
            this.b = j;
        }

        @Override
        public void run() {
            try {
                CalculateService service = (CalculateService) ServiceBeanFactory.generateBean(CalculateService.class);
                int add = service.add(a, b);
                logger.info("{}+{} = {}", a, b, add);
            } catch (Exception e) {
                logger.error("发生异常");
            }
        }
    }
}

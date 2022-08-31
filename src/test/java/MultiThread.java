public class MultiThread implements Runnable {

    private final String browserName;
    private final Thread t;

    public MultiThread(String name) {
        browserName = name;
        t = new Thread(this, browserName);
        System.out.println("Thread " + browserName + " created");
        t.start();
    }

    @Override
    public void run() {
        System.out.println("Execution of thread " + browserName);
        TestClass testClass = new TestClass();
        testClass.verifySkillsTest(browserName);
        System.out.println("Thread " + browserName + " stopped");
    }

    public static class Main {
        public static void main(String[] args) {
            MultiThread t1 = new MultiThread("Chrome");
            MultiThread t2 = new MultiThread("Edge");
            try {
                t1.t.join();
                t2.t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
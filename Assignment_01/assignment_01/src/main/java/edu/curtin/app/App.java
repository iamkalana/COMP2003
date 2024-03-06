package edu.curtin.app;

public class App {
    public static void main(String[] args) {
        String path = ".";
        if (args.length > 0) {
            path = args[0];
        }
        Main main = new Main(path);
        main.run();
    }
}

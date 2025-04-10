import client.Repl;

public class Main {
    public static void main(String[] args) {
        String serverUrl = "http://localhost:8000";
        Repl repl = new Repl(serverUrl);
        repl.run();
    }
}


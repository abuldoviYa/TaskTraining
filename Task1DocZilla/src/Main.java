public class Main {
    public static void main(String[] args) {
        FileDependencyResolver resolver = new FileDependencyResolver();
        resolver.resolveDependencies("files");
    }
}
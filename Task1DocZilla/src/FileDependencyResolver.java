import java.io.*;
import java.nio.file.Paths;
import java.util.*;

class FileDependencyResolver {
    private Map<String, List<String>> dependencies = new HashMap<>();
    private Set<String> visited = new HashSet<>();
    private List<String> sortedFiles = new ArrayList<>();

    private String root;

    public void resolveDependencies(String rootFolder) {
        root = rootFolder;
        traverseFiles(rootFolder);
        buildDependencyList();
        if (!detectCyclicDependencies()) {
            concatenateFiles();
        } else {
            System.out.println("Error: Cyclic dependencies detected.");
        }
    }

    private void traverseFiles(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    traverseFiles(file.getPath());
                } else if (file.isFile()) {
                    processFile(file);
                }
            }
        }
    }

    private void processFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> fileDependencies = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("require")) {
                    String dependency = line.substring(line.indexOf('‘') + 1,
                            line.lastIndexOf('’'));
                    File folder = new File(root+"/"+dependency);
                    fileDependencies.add(folder.getPath());
                }
            }
            dependencies.put(file.getPath(), fileDependencies);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void buildDependencyList() {
        for (String file : dependencies.keySet()) {
            visit(file);
        }
    }

    private void visit(String file) {
        if (!visited.contains(file)) {
            visited.add(file);
            for (String dependency : dependencies.getOrDefault(file, Collections.emptyList())) {
                visit(dependency);
            }
            sortedFiles.add(file);
        }
    }

    private boolean detectCyclicDependencies() {
        return visited.size() < sortedFiles.size();
    }

    private void concatenateFiles() {
        String currentPath = Paths.get("").toAbsolutePath().toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"))) {
            for (String file : sortedFiles) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            System.out.println("Files concatenated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



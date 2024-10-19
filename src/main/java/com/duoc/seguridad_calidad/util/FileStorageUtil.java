package com.duoc.seguridad_calidad.util;

import com.duoc.seguridad_calidad.util.json.GrantedAuthorityDeserializer;
import com.duoc.seguridad_calidad.util.json.GrantedAuthoritySerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Component
public class FileStorageUtil {

    private static final String DATA_DIRECTORY = "data";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(GrantedAuthority.class, new GrantedAuthorityDeserializer());
        module.addSerializer(GrantedAuthority.class, new GrantedAuthoritySerializer());
        objectMapper.registerModule(module);
    }

    private String getFullPath(String relativePath) {
        return DATA_DIRECTORY + File.separator + relativePath;
    }

    public <T> void saveToFile(T object, String fileName) throws IOException {
        String fullPath = getFullPath(fileName);
        File file = new File(fullPath);
        file.getParentFile().mkdirs(); // Create parent directories if they don't exist

        objectMapper.writeValue(file, object);
        System.out.println("Saved to file: " + fullPath); // Log for debugging
    }

    public <T> T readFromFile(String fileName, Class<T> valueType) throws IOException {
        String filePath = getFullPath(fileName);
        File file = new File(filePath);
        if (!file.exists()) {
            System.out.println("File not found: " + filePath); // Log for debugging
            return null;
        }
        return objectMapper.readValue(file, valueType);
    }

    public <T> List<T> readAllFromDirectory(String subDirectory, Class<T> valueType) throws IOException {
        List<T> items = new ArrayList<>();

        // Ensure subDirectory does not include DATA_DIRECTORY
        if (subDirectory.startsWith(DATA_DIRECTORY)) {
            subDirectory = subDirectory.substring(DATA_DIRECTORY.length() + 1); // Remove 'data/' prefix
        }

        String directoryPath = getFullPath(subDirectory);

        File directory = new File(directoryPath);
        
        if (directory.exists() && directory.isDirectory()) {
            for (File file : directory.listFiles()) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    T item = objectMapper.readValue(file, valueType);
                    items.add(item);
                }
            }
        } else {
            System.out.println("Directory not found: " + directoryPath); // Log for debugging
        }

        System.out.println("Found " + items.size() + " items in directory: " + directoryPath); // Log for debugging
        return items;
    }

    public void deleteFile(String fileName) throws IOException {
        String filePath = getFullPath(fileName);
        boolean deleted = Files.deleteIfExists(Paths.get(filePath));
        if (deleted) {
            System.out.println("Deleted file: " + filePath); // Log for debugging
        } else {
            System.out.println("File not found for deletion: " + filePath); // Log for debugging
        }
    }
}
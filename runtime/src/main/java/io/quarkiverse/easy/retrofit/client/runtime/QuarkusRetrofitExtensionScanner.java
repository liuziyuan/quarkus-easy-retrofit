package io.quarkiverse.easy.retrofit.client.runtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

public class QuarkusRetrofitExtensionScanner {

    private static final String RETROFIT_EXTENSION_PROPERTIES = "META-INF/retrofit-extension.properties";
    private static final String RETROFIT_EXTENSION_CLASS_NAME = "retrofit.extension.name";

    /**
     * Scan packageName
     *
     * @return
     * @throws IOException
     */
    public Set<String> scan() throws IOException {
        Set<String> extensionNames = new HashSet<>();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        Enumeration<URL> resources = classLoader.getResources("META-INF/retrofit-extension.properties");
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.openStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] split = line.split("=");
                    if (RETROFIT_EXTENSION_CLASS_NAME.equalsIgnoreCase(split[0].trim())) {
                        String className = split[1].trim();
                        int lastDotIndex = className.lastIndexOf('.');
                        String packageName = className.substring(0, lastDotIndex);
                        extensionNames.add(packageName);
                    }
                }
            }
        }
        return extensionNames;
    }
}

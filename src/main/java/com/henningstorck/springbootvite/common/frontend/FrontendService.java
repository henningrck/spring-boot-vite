package com.henningstorck.springbootvite.common.frontend;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FrontendService {
    public static final String VITE_CLIENT = "http://localhost:5173/@vite/client";
    public static final String VITE_ENTRY = "http://localhost:5173/src/main.ts";
    public static final String MANIFEST_LOCATION = "classpath:frontend/.vite/manifest.json";
    public static final String FILE_FIELD_NAME = "file";
    public static final String CSS_FIELD_NAME = "css";
    public static final String PATH_PREFIX = "/";

    private final Logger logger = LoggerFactory.getLogger(FrontendService.class);
    private final FrontendProperties frontendProperties;

    private volatile JsonNode manifest;

    public FrontendService(FrontendProperties frontendProperties) {
        this.frontendProperties = frontendProperties;
    }

    public List<String> loadStylesheets() {
        return loadFilteredFrontendFiles(FrontendFileType.STYLESHEET);
    }

    public List<String> loadScripts() {
        return loadFilteredFrontendFiles(FrontendFileType.SCRIPT);
    }

    private List<String> loadFilteredFrontendFiles(FrontendFileType type) {
        return loadFrontendFiles().stream()
            .filter(frontendFile -> frontendFile.type() == type)
            .map(FrontendFile::path)
            .toList();
    }

    private List<FrontendFile> loadFrontendFiles() {
        if (IsViteDevServerRunning()) {
            return List.of(
                FrontendFile.script(VITE_CLIENT),
                FrontendFile.script(VITE_ENTRY)
            );
        }

        try {
            List<FrontendFile> frontendFiles = new ArrayList<>();

            if (this.manifest == null) {
                synchronized (this) {
                    if (this.manifest == null) {
                        this.manifest = loadManifest();
                    }
                }
            }

            for (JsonNode entry : manifest) {
                processEntry(entry, frontendFiles);
            }

            return frontendFiles;
        } catch (IOException e) {
            logger.error("Cannot load Vite manifest.", e);
            return Collections.emptyList();
        }
    }

    private boolean IsViteDevServerRunning() {
        if (!frontendProperties.isDevelopmentMode()) {
            return false;
        }

        try {
            URL url = URI.create(VITE_ENTRY).toURL();
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("HEAD");
            int responseCode = httpURLConnection.getResponseCode();
            return responseCode >= 200 && responseCode < 300;
        } catch (IOException e) {
            return false;
        }
    }

    private JsonNode loadManifest() throws IOException {
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        Resource resource = resourceLoader.getResource(MANIFEST_LOCATION);
        InputStream inputStream = resource.getInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readTree(inputStream);
    }

    private void processEntry(JsonNode entry, List<FrontendFile> files) {
        if (entry.has(FILE_FIELD_NAME)) {
            files.add(FrontendFile.script(PATH_PREFIX + entry.get(FILE_FIELD_NAME).asText()));
        }

        if (entry.has(CSS_FIELD_NAME)) {
            for (JsonNode css : entry.get(CSS_FIELD_NAME)) {
                files.add(FrontendFile.stylesheet(PATH_PREFIX + css.asText()));
            }
        }
    }
}

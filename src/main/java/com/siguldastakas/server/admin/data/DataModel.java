package com.siguldastakas.server.admin.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

public class DataModel {

    private static final Logger log = LoggerFactory.getLogger(DataModel.class);

    private static DataModel instance;

    public static void Initialize(Path dataPath) {
        instance = new DataModel(dataPath);
    }

    public static DataModel instance() {
        return instance;
    }

    private static final String ADMINS = "admins.json";

    private final ObjectMapper mapper = new ObjectMapper();;
    private Path dataPath;

    private DataModel(Path dataPath) {
        this.dataPath = dataPath;
    }

    public boolean isAdmin(String email) {
        Path path = dataPath.resolve(ADMINS);

        try {
            User[] admins;
            synchronized (mapper) {
                admins = mapper.readValue(path.toFile(), User[].class);
            }
            for (User user : admins) if (user.email.equalsIgnoreCase(email)) return true;
        } catch (IOException e) {
            log.error("Failed to read admins.json", e);
        }
        return false;
    }

}

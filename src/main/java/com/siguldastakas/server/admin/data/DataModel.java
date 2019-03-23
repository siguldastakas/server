package com.siguldastakas.server.admin.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private static final String SERIES = "series";
    private static final String INFO = "info.json";

    private final ObjectMapper mapper;
    private Path dataPath;

    private DataModel(Path dataPath) {
        this.dataPath = dataPath;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
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

    public Series[] allSeries() {
        List<Series> list = new ArrayList<>();

        try {
            synchronized (mapper) {
                for (Iterator<Path> it = Files.walk(dataPath.resolve(SERIES), 1).filter(Files::isDirectory).iterator(); it.hasNext(); ) {
                    Path dir = it.next();
                    Path infoPath = dir.resolve(INFO);
                    File infoFile = infoPath.toFile();
                    if (infoFile.exists()) {
                        Series series = mapper.readValue(infoFile, Series.class);
                        list.add(series);
                    }
                }
            }
        } catch (IOException e) {
            log.error("Failed to read series", e);
        }

        list.sort((o1, o2) -> - o1.date.compareTo(o2.date));
        return list.toArray(new Series[0]);
    }

    public Series series(String path) {
        try {
            synchronized (mapper) {
                Path infoPath = dataPath.resolve(SERIES).resolve(path).resolve(INFO);
                return mapper.readValue(infoPath.toFile(), Series.class);
            }
        } catch (IOException e) {
            log.error("Failed to read series", e);
        }

        return null;
    }

}

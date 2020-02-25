package com.siguldastakas.server.admin.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.xml.ser.ToXmlGenerator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.siguldastakas.server.admin.data.overall.OverallResults;
import com.siguldastakas.server.admin.iofxml.ResultList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

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
    private static final String RESULTS = "results";
    private static final String OVERALL = "overall.json";

    private final ObjectMapper mapper;
    private final XmlMapper xmlMapper;
    private Path dataPath;

    private DataModel(Path dataPath) {
        this.dataPath = dataPath;
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        xmlMapper = new XmlMapper();
        xmlMapper.configure(ToXmlGenerator.Feature.WRITE_XML_DECLARATION, true);
        xmlMapper.enable(SerializationFeature.INDENT_OUTPUT);
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

    public boolean hasOverallResults(String path) {
        return Files.exists(dataPath.resolve(SERIES).resolve(path).resolve(OVERALL));
    }

    private Path resultsPath(String path, int number) {
        return dataPath.resolve(SERIES).resolve(path).resolve(RESULTS + number + ".json");
    }

    private Path lofXmlPath(String path, int number) {
        return dataPath.resolve(SERIES).resolve(path).resolve(RESULTS + number + "-lof.xml");
    }

    public boolean hasResults(String path, int number) {
        return Files.exists(resultsPath(path, number));
    }

    public boolean hasLofXml(String path, int number) {
        return Files.exists(lofXmlPath(path, number));
    }

    public EventResults results(String path, int number) {
        try {
            synchronized (mapper) {
                File file = resultsPath(path, number).toFile();
                return file.exists() ? mapper.readValue(file, EventResults.class) : null;
            }
        } catch (IOException e) {
            log.error("Failed to read series", e);
        }

        return null;
    }

    public InputStream lofXml(String path, int number) {
        try {
            return new FileInputStream(lofXmlPath(path, number).toFile());
        } catch (IOException e) {
            log.error("Failed to read series", e);
        }

        return null;
    }

    public void save(String path, int number, EventResults results, ResultList lofXml, Path xml) {
        try {
            synchronized (mapper) {
                String name = RESULTS + number;
                Path directory = dataPath.resolve(SERIES).resolve(path);
                Path path1 = directory.resolve(name + ".json");
                mapper.writeValue(path1.toFile(), results);

                Path path2 = directory.resolve(name + ".xml");
                Files.move(xml, path2, REPLACE_EXISTING);

                Path path3 = directory.resolve(name + "-lof.xml");
                xmlMapper.writeValue(path3.toFile(), lofXml);
            }
        } catch (IOException e) {
            log.error("Failed to write results", e);
        }
    }

    public void save(String path, OverallResults results) {
        try {
            synchronized (mapper) {
                mapper.writeValue(dataPath.resolve(SERIES).resolve(path).resolve(OVERALL).toFile(), results);
            }
        } catch (IOException e) {
            log.error("Failed to write overall results", e);
        }
    }

}

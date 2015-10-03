package com.evanzeimet.queryinfo;

import com.evanzeimet.queryinfo.builder.QueryInfoBuilderTest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class QueryInfoTestUtils {

    public static String createActualJson(Object actual)
            throws JsonProcessingException {
        ObjectWriter writer = createObjectMapperWriter();
        String json = writer.writeValueAsString(actual);
        return dosToUnix(json);
    }

    public static ObjectWriter createObjectMapperWriter() {
        ObjectMapper objectMapper = new ObjectMapper();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(dateFormat);

        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentArraysWith(new DefaultPrettyPrinter.Lf2SpacesIndenter());

        return objectMapper.writer(prettyPrinter);
    }

    public static String dosToUnix(String rawContents) {
        String result;

        if (rawContents == null) {
            result = null;
        } else {
            result = rawContents.replaceAll("\\r\\n", "\n");
        }

        return result;
    }

    public static String getExpectedJson(Class<?> clazz, String filename)
            throws IOException {
        URL url = clazz.getResource(filename);
        File file = new File(url.getPath());
        String rawContents = FileUtils.readFileToString(file);

        return dosToUnix(rawContents);
    }
}

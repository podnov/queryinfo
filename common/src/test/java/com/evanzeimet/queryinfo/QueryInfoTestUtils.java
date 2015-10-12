package com.evanzeimet.queryinfo;

/*
 * #%L
 * queryinfo-common
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 Evan Zeimet
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class QueryInfoTestUtils {

	public static String createActualJson(Object actual)
			throws JsonProcessingException {
		ObjectWriter writer = createObjectWriter();
		String json = writer.writeValueAsString(actual);
		return dosToUnix(json);
	}

	@SuppressWarnings("deprecation")
	public static ObjectWriter createObjectWriter() {
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

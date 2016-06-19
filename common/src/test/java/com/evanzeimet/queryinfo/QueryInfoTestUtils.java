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
import java.lang.reflect.Type;
import java.net.URL;
import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class QueryInfoTestUtils {

	public static String createActualJson(Object actual)
			throws JsonProcessingException {
		String json = stringify(actual);
		return dosToUnix(json);
	}

	public static ObjectMapper createObjectMapper() {
		return new QueryInfoUtils().createObjectMapper();
	}

	@SuppressWarnings("deprecation")
	public static ObjectWriter createObjectWriter() {
		ObjectMapper objectMapper = createObjectMapper();

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

	public static String getFormattedJson(Class<?> clazz, String filename)
			throws IOException {
		URL url = clazz.getResource(filename);
		File file = new File(url.getPath());
		String rawContents = FileUtils.readFileToString(file);

		return dosToUnix(rawContents);
	}

	public static <T> T objectify(String json, Class<T> clazz) throws QueryInfoException {
		return new QueryInfoUtils().objectify(json, clazz);
	}

	public static <T> T objectify(String json, Type typeOfT) throws QueryInfoException {
		return new QueryInfoUtils().objectify(json, typeOfT);
	}

	public static String stringify(Object actual) {
		String result;
		ObjectWriter writer = createObjectWriter();

		try {
			result = writer.writeValueAsString(actual);
		} catch (JsonProcessingException e) {
			throw new QueryInfoRuntimeException("Could not stringify", e);
		}

		return result;
	}
}

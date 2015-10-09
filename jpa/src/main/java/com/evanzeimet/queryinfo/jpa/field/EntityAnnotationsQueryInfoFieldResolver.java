package com.evanzeimet.queryinfo.jpa.field;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.evanzeimet.queryinfo.QueryInfoException;
import com.evanzeimet.queryinfo.jpa.path.QueryInfoPathFactory;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class EntityAnnotationsQueryInfoFieldResolver<T> implements QueryInfoFieldInfoResolver<T> {

	private static final String NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT = String.format(".%n");

	private static final Pattern accessorFieldNamePattern = Pattern.compile("(?:get|set)?(.+)");

	private Class<T> entityClass;

	public EntityAnnotationsQueryInfoFieldResolver(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	protected QueryInfoFieldInfo createFieldInfo(Method annotatedMethod) throws QueryInfoException {
		QueryInfoField annotation = annotatedMethod.getAnnotation(QueryInfoField.class);

		String entityAttributeName = createFieldName(annotatedMethod);
		String fieldName = entityAttributeName;
		boolean isQueryable = annotation.isQueryable();
		boolean isSelectable = annotation.isSelectable();
		boolean isSortable = annotation.isSortable();

		return QueryInfoFieldInfoBuilder.create()
				.entityAttributeName(entityAttributeName)
				.fieldName(fieldName)
				.isQueryable(isQueryable)
				.isSelectable(isSelectable)
				.isSortable(isSortable)
				.build();
	}

	protected String createFieldName(Method annotatedMethod) throws QueryInfoException {
		String methodName = annotatedMethod.getName();
		QueryInfoField annotation = annotatedMethod.getAnnotation(QueryInfoField.class);

		String result = annotation.fieldName();

		if (StringUtils.isBlank(result)) {
			Matcher matcher = accessorFieldNamePattern.matcher(methodName);

			if (matcher.matches()) {
				String group = matcher.group(1);

				String firstLetter = group.substring(0, 1).toLowerCase();
				String theRest = group.substring(1);

				result = firstLetter + theRest;
			} else {
				String message = String.format("Could not match [%s] with pattern [%s]",
						methodName,
						accessorFieldNamePattern);
				throw new QueryInfoException(message);
			}
		}

		return result;
	}

	protected List<Method> findAnnotatedMethods(Class<T> entityClass) {
		Method[] methods = entityClass.getMethods();

		int methodCount = methods.length;
		List<Method> annotatedMethods = new ArrayList<Method>(methodCount);

		for (Method method : methods) {
			if (method.isAnnotationPresent(QueryInfoField.class)) {
				annotatedMethods.add(method);
			}
		}

		return annotatedMethods;
	}

	protected List<String> generateNonUniqueFieldNameMessages(ListMultimap<String, QueryInfoFieldInfo> fieldNameMap) {
		List<String> nonUniqueFieldNameTexts = new ArrayList<>();
		Iterator<String> fieldNames = fieldNameMap.keySet().iterator();

		while (fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			List<QueryInfoFieldInfo> fields = fieldNameMap.get(fieldName);
			int fieldInfoCount = fields.size();

			if (fieldInfoCount > 1) {
				List<String> entityAttributeNames = new ArrayList<>(fields.size());

				for (QueryInfoFieldInfo field : fields) {
					String entityAttributeName = field.getEntityAttributeName();
					entityAttributeNames.add(entityAttributeName);
				}

				String pathsText = StringUtils.join(entityAttributeNames, ", ");

				String nonUniqueFieldNameText = String.format("Found [%s] field infos for name [%s]: %s",
						fieldInfoCount,
						fieldName,
						pathsText);
				nonUniqueFieldNameTexts.add(nonUniqueFieldNameText);
			}
		}
		return nonUniqueFieldNameTexts;
	}

	protected ListMultimap<String, QueryInfoFieldInfo> mapFieldsToNames(
			List<QueryInfoFieldInfo> fieldInfos) {
		ListMultimap<String, QueryInfoFieldInfo> fieldNameMap = ArrayListMultimap.create();

		for (QueryInfoFieldInfo fieldInfo : fieldInfos) {
			String fieldName = fieldInfo.getFieldName();
			fieldNameMap.put(fieldName, fieldInfo);
		}

		return fieldNameMap;
	}

	@Override
	public List<QueryInfoFieldInfo> resolve(QueryInfoPathFactory<T> pathFactory)
			throws QueryInfoException {
		List<Method> annotatedMethods = findAnnotatedMethods(entityClass);

		int annotatedMethodCount = annotatedMethods.size();
		List<QueryInfoFieldInfo> result = new ArrayList<>(annotatedMethodCount);

		for (Method annotatedMethod : annotatedMethods) {
			QueryInfoFieldInfo fieldInfo = createFieldInfo(annotatedMethod);
			result.add(fieldInfo);
		}

		validateFieldNameUniqueness(result);

		return result;
	}

	protected void validateFieldNameUniqueness(List<QueryInfoFieldInfo> fieldInfos)
			throws QueryInfoException {
		ListMultimap<String, QueryInfoFieldInfo> fieldNameMap = mapFieldsToNames(fieldInfos);
		List<String> nonUniqueFieldNameMessages = generateNonUniqueFieldNameMessages(fieldNameMap);

		int nonUniqueFieldNameCount = nonUniqueFieldNameMessages.size();

		if (nonUniqueFieldNameCount > 0) {
			String entityName = entityClass.getCanonicalName();
			String nonUniqueFieldsNamesText = StringUtils.join(nonUniqueFieldNameMessages,
					NON_UNIQUE_FIELD_NAME_MESSAGE_JOIN_TEXT);

			String message = String.format("Found [%s] non-unique field names for entity [%s]:%n%s.",
					nonUniqueFieldNameCount,
					entityName,
					nonUniqueFieldsNamesText);
			throw new QueryInfoException(message);
		}
	}
}

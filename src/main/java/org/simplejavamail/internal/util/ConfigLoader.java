package org.simplejavamail.internal.util;

import org.simplejavamail.mailer.TransportStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static java.util.Collections.unmodifiableMap;
import static org.simplejavamail.internal.util.MiscUtil.checkArgumentNotEmpty;

/**
 * Contains list of possible properties names and can produce a map of property values, if provided as file "{@value #FILENAME}" on the classpath or
 * as environment property.
 */
public class ConfigLoader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigLoader.class);

	private static final String FILENAME = "simplejavamail.properties";

	/**
	 * Initially try to load properties from "{@value #FILENAME}".
	 *
	 * @see #loadProperties(String)
	 * @see #loadProperties(InputStream)
	 */
	private static final Map<Property, Object> RESOLVED_PROPERTIES = new HashMap<>();

	static {
		// static initializer block, because loadProperties needs to modify RESOLVED_PROPERTIES while loading
		// this is not possible when we are initializing the same field.
		// RESOLVED_PROPERTIES = loadProperties(FILENAME); <-- not possible
		loadProperties(FILENAME);
	}

	public enum Property {
		JAVAXMAIL_DEBUG("simplejavamail.javaxmail.debug"),
		TRANSPORT_STRATEGY("simplejavamail.transportstrategy"),
		SMTP_HOST("simplejavamail.smtp.host"),
		SMTP_PORT("simplejavamail.smtp.port"),
		SMTP_USERNAME("simplejavamail.smtp.username"),
		SMTP_PASSWORD("simplejavamail.smtp.password"),
		PROXY_HOST("simplejavamail.proxy.host"),
		PROXY_PORT("simplejavamail.proxy.port"),
		PROXY_USERNAME("simplejavamail.proxy.username"),
		PROXY_PASSWORD("simplejavamail.proxy.password"),
		PROXY_SOCKS5BRIDGE_PORT("simplejavamail.proxy.socks5bridge.port"),
		DEFAULT_FROM_NAME("simplejavamail.defaults.from.name"),
		DEFAULT_FROM_ADDRESS("simplejavamail.defaults.from.address"),
		DEFAULT_REPLYTO_NAME("simplejavamail.defaults.replyto.name"),
		DEFAULT_REPLYTO_ADDRESS("simplejavamail.defaults.replyto.address"),
		DEFAULT_TO_NAME("simplejavamail.defaults.to.name"),
		DEFAULT_TO_ADDRESS("simplejavamail.defaults.to.address"),
		DEFAULT_CC_NAME("simplejavamail.defaults.cc.name"),
		DEFAULT_CC_ADDRESS("simplejavamail.defaults.cc.address"),
		DEFAULT_BCC_NAME("simplejavamail.defaults.bcc.name"),
		DEFAULT_BCC_ADDRESS("simplejavamail.defaults.bcc.address");

		private final String key;

		Property(String key) {
			this.key = key;
		}
	}

	/**
	 * @return The value if not null or else the value from config file if provided or else <code>null</code>.
	 */
	public static <T> T valueOrProperty(T value, Property property) {
		return valueOrProperty(value, property, null);
	}

	/**
	 * @return The value if not null or else the value from config file if provided or else <code>defaultValue</code>.
	 */
	public static <T> T valueOrProperty(T value, Property property, T defaultValue) {
		if (value != null) {
			LOGGER.trace("using provided argument value {} for property {}", value, property);
			return value;
		} else if (hasProperty(property)) {
			final T propertyValue = getProperty(property);
			LOGGER.trace("using value {} from config file for property {}", propertyValue, property);
			return propertyValue;
		} else {
			LOGGER.trace("no value provided as argument or in config file for property {}, using default value {}", property, defaultValue);
			return defaultValue;
		}
	}

	public static synchronized boolean hasProperty(Property property) {
		return RESOLVED_PROPERTIES.containsKey(property);
	}

	public static synchronized <T> T getProperty(Property property) {
		//noinspection unchecked
		return (T) RESOLVED_PROPERTIES.get(property);
	}

	/**
	 * Loads properties from property file on the classpath, if provided. Calling this method only has effect on new Email and Mailer instances after
	 * this.
	 * <p>
	 * This method the internal list of properties and also returns the list to the caller.
	 */
	public static Map<Property, Object> loadProperties(String filename) {
		InputStream input = null;

		try {
			input = ConfigLoader.class.getClassLoader().getResourceAsStream(filename);
			if (input != null) {
				return loadProperties(input);
			} else {
				LOGGER.debug("Property file not found on classpath, skipping config file");
			}
		} catch (IOException e) {
			throw new IllegalStateException("error reading properties file from classpath: " + filename, e);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(), e);
				}
			}
		}
		return new HashMap<>();
	}

	/**
	 * Loads properties from {@link InputStream}. Calling this method only has effect on new Email and Mailer instances after this.
	 */
	public static synchronized Map<Property, Object> loadProperties(InputStream input)
			throws IOException {
		Properties prop = new Properties();
		prop.load(checkArgumentNotEmpty(input, "InputStream was null"));
		RESOLVED_PROPERTIES.clear();
		RESOLVED_PROPERTIES.putAll(readProperties(prop));
		return unmodifiableMap(RESOLVED_PROPERTIES);
	}

	/**
	 * @return All properties in priority of System property > File properties.
	 */
	private static Map<Property, Object> readProperties(Properties fileProperties) {
		Map<Property, Object> resolvedProps = new HashMap<>();
		for (Property prop : Property.values()) {
			resolvedProps.put(prop, parsePropertyValue(System.getProperty(prop.key)));
			if (resolvedProps.get(prop) == null) {
				String rawValue = (String) fileProperties.remove(prop.key);
				resolvedProps.put(prop, parsePropertyValue(rawValue));
			}
		}

		if (!fileProperties.isEmpty()) {
			throw new IllegalArgumentException("unknown properties provided " + fileProperties);
		}

		return resolvedProps;
	}

	/**
	 * @return The property value in boolean, integer or as original string value.
	 */
	static Object parsePropertyValue(String propertyValue) {
		if (propertyValue == null) {
			return null;
		}
		// read boolean value
		final Map<String, Boolean> booleanConversionMap = new HashMap<>();
		booleanConversionMap.put("0", false);
		booleanConversionMap.put("1", true);
		booleanConversionMap.put("false", false);
		booleanConversionMap.put("true", true);
		booleanConversionMap.put("no", false);
		booleanConversionMap.put("yes", true);
		if (booleanConversionMap.containsKey(propertyValue)) {
			return booleanConversionMap.get(propertyValue.toLowerCase());
		}
		// read number value
		try {
			return Integer.valueOf(propertyValue);
		} catch (NumberFormatException nfe) {
			// ok, so not a number
		}
		// read TransportStrategy value
		try {
			return TransportStrategy.valueOf(propertyValue);
		} catch (IllegalArgumentException nfe) {
			// ok, so not a TransportStrategy either
		}
		// return value as is (which should be string)
		return propertyValue;
	}
}
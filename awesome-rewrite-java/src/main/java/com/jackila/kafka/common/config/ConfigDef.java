package com.jackila.kafka.common.config;

import java.util.*;

/**
 * @Author: jackila
 * @Date: 17:25 2020-09-01
 */
public class ConfigDef {

    public static final Object NO_DEFAULT_VALUE = new String("");

    private final Map<String, ConfigKey> configKeys = new HashMap<>();
    private final List<String> groups = new LinkedList<>();
    private Set<String> configsWithNoParent;

    public Map<String, Object> parse(Map<String, ?> props) {

        //check undefined dependent configs
        return null;
    }


    public static class ConfigKey{

        public final String name;
        public final Type type;
        public final String documentation;
        public final Object defaultValue;
        public final Validator validator;
        public final Importance importance;
        public final String group;
        public final int orderInGroup;
        public final Width width;
        public final String displayName;
        public final List<String> dependents;
        public final Recommender recommender;

        public ConfigKey(String name, Type type, Object defaultValue, Validator validator,
                         Importance importance, String documentation, String group,
                         int orderInGroup, Width width, String displayName,
                         List<String> dependents, Recommender recommender) {
            this.name = name;
            this.type = type;
            this.defaultValue = defaultValue;
            this.validator = validator;
            this.importance = importance;
            if (this.validator != null && this.hasDefault())
                this.validator.ensureValid(name, defaultValue);
            this.documentation = documentation;
            this.dependents = dependents;
            this.group = group;
            this.orderInGroup = orderInGroup;
            this.width = width;
            this.displayName = displayName;
            this.recommender = recommender;
        }

        public boolean hasDefault() {
            return this.defaultValue != NO_DEFAULT_VALUE;
        }
    }


    /**
     * The config types
     */
    public enum Type {
        BOOLEAN, STRING, INT, SHORT, LONG, DOUBLE, LIST, CLASS, PASSWORD
    }

    /**
     * The importance level for a configuration
     */
    public enum Importance {
        HIGH, MEDIUM, LOW
    }

    /**
     * The width of a configuration value
     */
    public enum Width {
        NONE, SHORT, MEDIUM, LONG
    }


    /**
     * This is used by the {@link #validate(Map)} to get valid values for a configuration given the current
     * configuration values in order to perform full configuration validation and visibility modification.
     * In case that there are dependencies between configurations, the valid values and visibility
     * for a configuration may change given the values of other configurations.
     */
    public interface Recommender {

        /**
         * The valid values for the configuration given the current configuration values.
         * @param name The name of the configuration
         * @param parsedConfig The parsed configuration values
         * @return The list of valid values. To function properly, the returned objects should have the type
         * defined for the configuration using the recommender.
         */
        List<Object> validValues(String name, Map<String, Object> parsedConfig);

        /**
         * Set the visibility of the configuration given the current configuration values.
         * @param name The name of the configuration
         * @param parsedConfig The parsed configuration values
         * @return The visibility of the configuration
         */
        boolean visible(String name, Map<String, Object> parsedConfig);
    }

    /**
     * Validation logic the user may provide to perform single configuration validation.
     */
    public interface Validator {
        /**
         * Perform single configuration validation.
         * @param name The name of the configuration
         * @param value The value of the configuration
         */
        void ensureValid(String name, Object value);
    }

}

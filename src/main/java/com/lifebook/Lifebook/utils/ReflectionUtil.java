package com.lifebook.Lifebook.utils;

import org.reflections.Reflections;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtil {
    /**
     * Gets the simple names of all subtypes of the given class in the specified package.
     *
     * @param baseClass The base class to find subtypes for.
     * @param packageName The package to search for subtypes.
     * @param <T> The type of the base class.
     * @return A set of simple names of all subtypes.
     */
    public static <T> Set<String> getSubtypeSimpleNames(final Class<T> baseClass, final String packageName) {
        final Set<Class<? extends T>> subTypes = getSubTypeClasses(baseClass, packageName);

        return subTypes.stream()
            .map(Class::getSimpleName)
            .collect(Collectors.toSet());
    }

    /**
     * Gets the simple names of all subtypes of the given class in the specified package.
     *
     * @param baseClass The base class to find subtypes for.
     * @param packageName The package to search for subtypes.
     * @param <T> The type of the base class.
     * @return A set of simple names of all subtypes.
     */
    public static <T> Set<Class<? extends T>> getSubTypeClasses(final Class<T> baseClass, final String packageName) {
        Reflections reflections = new Reflections(packageName);
        return reflections.getSubTypesOf(baseClass);
    }
}

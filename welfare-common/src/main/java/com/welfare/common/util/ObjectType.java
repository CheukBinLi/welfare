/*******************************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *******************************************************************************/
package com.welfare.common.util;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.util.FastMap;

/**
 * Utilities for analyzing and converting Object types in Java
 * Takes advantage of reflection
 */
public class ObjectType {

    private final static Logger logger = LoggerFactory.getLogger(ObjectType.class);
    public static final String module = ObjectType.class.getName();

    public static final Object NULL = new NullObject();

    protected static FastMap<String, Class<?>> classCache = FastMap.newInstance();

    public static final String LANG_PACKAGE = "java.lang."; // We will test both the raw value and this + raw value
    public static final String SQL_PACKAGE = "java.sql.";   // We will test both the raw value and this + raw value

    /**
     * Loads a class with the current thread's context classloader.
     *
     * @param className The name of the class to load
     * @return The requested class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String className) throws ClassNotFoundException {
        int genericsStart = className.indexOf("<");
        if (genericsStart != -1) className = className.substring(0, genericsStart);

        // small block to speed things up by putting using preloaded classes for common objects, this turns out to help quite a bit...
        Class<?> theClass = CachedClassLoader.globalClassNameClassMap.get(className);

        if (theClass != null) return theClass;

        return loadClass(className, null);
    }

    /**
     * Loads a class with the current thread's context classloader.
     *
     * @param className The name of the class to load
     * @param loader    The ClassLoader to use
     * @return The requested class
     * @throws ClassNotFoundException
     */
    public static Class<?> loadClass(String className, ClassLoader loader) throws ClassNotFoundException {
        int genericsStart = className.indexOf("<");
        if (genericsStart != -1) className = className.substring(0, genericsStart);

        // Handle array classes. Details in http://java.sun.com/j2se/1.5.0/docs/guide/jni/spec/types.html#wp16437
        if (className.endsWith("[]")) {
            if (Character.isLowerCase(className.charAt(0)) && className.indexOf(".") < 0) {
                String prefix = className.substring(0, 1).toUpperCase();
                // long and boolean have other prefix than first letter
                if (className.startsWith("long")) {
                    prefix = "J";
                } else if (className.startsWith("boolean")) {
                    prefix = "Z";
                }
                className = "[" + prefix;
            } else {
                Class<?> arrayClass = loadClass(className.replace("[]", ""), loader);
                className = "[L" + arrayClass.getName().replace("[]", "") + ";";
            }
        }

        // small block to speed things up by putting using preloaded classes for common objects, this turns out to help quite a bit...
        Class<?> theClass = CachedClassLoader.globalClassNameClassMap.get(className);

        if (theClass != null) return theClass;

        if (loader == null) loader = Thread.currentThread().getContextClassLoader();

        try {
            theClass = Class.forName(className, true, loader);
        } catch (Exception e) {
            theClass = classCache.get(className);
            if (theClass == null) {
                theClass = Class.forName(className);
                if (theClass != null) {
                    if (classCache.putIfAbsent(className, theClass) == null) {
                        logger.debug("Loaded Class: " + theClass.getName());
                    }
                }
            }
        }

        return theClass;
    }

    /**
     * Returns an instance of the specified class.  This uses the default
     * no-arg constructor to create the instance.
     *
     * @param className Name of the class to instantiate
     * @return An instance of the named class
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object getInstance(String className) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Class<?> c = loadClass(className);
        Object o = c.newInstance();

        logger.debug("Instantiated object: " + o.toString());
        return o;
    }

    /**
     * Tests if a class properly implements the specified interface.
     *
     * @param objectClass   Class to test
     * @param interfaceName Name of the interface to test against
     * @return true if interfaceName is an interface of objectClass
     * @throws ClassNotFoundException
     */
    public static boolean interfaceOf(Class<?> objectClass, String interfaceName) throws ClassNotFoundException {
        Class<?> interfaceClass = loadClass(interfaceName);

        return interfaceOf(objectClass, interfaceClass);
    }

    /**
     * Tests if a class properly implements the specified interface.
     *
     * @param objectClass     Class to test
     * @param interfaceObject to test against
     * @return true if interfaceObject is an interface of the objectClass
     */
    public static boolean interfaceOf(Class<?> objectClass, Object interfaceObject) {
        Class<?> interfaceClass = interfaceObject.getClass();

        return interfaceOf(objectClass, interfaceClass);
    }

    /**
     * Returns an instance of the specified class using the constructor matching the specified parameters.
     *
     * @param className  Name of the class to instantiate
     * @param parameters Parameters passed to the constructor
     * @return An instance of the className
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Object getInstance(String className, Object[] parameters) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Class<?>[] sig = new Class<?>[parameters.length];
        for (int i = 0; i < sig.length; i++) {
            sig[i] = parameters[i].getClass();
        }
        Class<?> c = loadClass(className);
        Constructor<?> con = c.getConstructor(sig);
        Object o = con.newInstance(parameters);

        logger.debug("Instantiated object: " + o.toString());
        return o;
    }

    /**
     * Tests if an object properly implements the specified interface.
     *
     * @param obj           Object to test
     * @param interfaceName Name of the interface to test against
     * @return true if interfaceName is an interface of obj
     * @throws ClassNotFoundException
     */
    public static boolean interfaceOf(Object obj, String interfaceName) throws ClassNotFoundException {
        Class<?> interfaceClass = loadClass(interfaceName);

        return interfaceOf(obj, interfaceClass);
    }

    /**
     * Tests if an object properly implements the specified interface.
     *
     * @param obj             Object to test
     * @param interfaceObject to test against
     * @return true if interfaceObject is an interface of obj
     */
    public static boolean interfaceOf(Object obj, Object interfaceObject) {
        Class<?> interfaceClass = interfaceObject.getClass();

        return interfaceOf(obj, interfaceClass);
    }

    /**
     * Tests if an object properly implements the specified interface.
     *
     * @param obj            Object to test
     * @param interfaceClass Class to test against
     * @return true if interfaceClass is an interface of obj
     */
    public static boolean interfaceOf(Object obj, Class<?> interfaceClass) {
        Class<?> objectClass = obj.getClass();

        return interfaceOf(objectClass, interfaceClass);
    }

    /**
     * Tests if a class properly implements the specified interface.
     *
     * @param objectClass    Class to test
     * @param interfaceClass Class to test against
     * @return true if interfaceClass is an interface of objectClass
     */
    public static boolean interfaceOf(Class<?> objectClass, Class<?> interfaceClass) {
        while (objectClass != null) {
            Class<?>[] ifaces = objectClass.getInterfaces();

            for (Class<?> iface : ifaces) {
                if (iface == interfaceClass) return true;
            }
            objectClass = objectClass.getSuperclass();
        }
        return false;
    }

    /**
     * Tests if a class is a class of or a sub-class of the parent.
     *
     * @param objectClass Class to test
     * @param parentName  Name of the parent class to test against
     * @return true if objectClass is a class of or a sub-class of the parent
     * @throws ClassNotFoundException
     */
    public static boolean isOrSubOf(Class<?> objectClass, String parentName) throws ClassNotFoundException {
        Class<?> parentClass = loadClass(parentName);

        return isOrSubOf(objectClass, parentClass);
    }

    /**
     * Tests if a class is a class of or a sub-class of the parent.
     *
     * @param objectClass  Class to test
     * @param parentObject Object to test against
     * @return true if objectClass is a class of or a sub-class of the parent
     */
    public static boolean isOrSubOf(Class<?> objectClass, Object parentObject) {
        Class<?> parentClass = parentObject.getClass();

        return isOrSubOf(objectClass, parentClass);
    }

    /**
     * Tests if an object is an instance of or a sub-class of the parent.
     *
     * @param obj        Object to test
     * @param parentName Name of the parent class to test against
     * @return true if obj is an instance of or a sub-class of the parent
     * @throws ClassNotFoundException
     */
    public static boolean isOrSubOf(Object obj, String parentName) throws ClassNotFoundException {
        Class<?> parentClass = loadClass(parentName);

        return isOrSubOf(obj, parentClass);
    }

    /**
     * Tests if an object is an instance of or a sub-class of the parent.
     *
     * @param obj          Object to test
     * @param parentObject Object to test against
     * @return true if obj is an instance of or a sub-class of the parent
     */
    public static boolean isOrSubOf(Object obj, Object parentObject) {
        Class<?> parentClass = parentObject.getClass();

        return isOrSubOf(obj, parentClass);
    }

    /**
     * Tests if an object is an instance of or a sub-class of the parent.
     *
     * @param obj         Object to test
     * @param parentClass Class to test against
     * @return true if obj is an instance of or a sub-class of the parent
     */
    public static boolean isOrSubOf(Object obj, Class<?> parentClass) {
        Class<?> objectClass = obj.getClass();

        return isOrSubOf(objectClass, parentClass);
    }

    /**
     * Tests if a class is a class of or a sub-class of the parent.
     *
     * @param objectClass Class to test
     * @param parentClass Class to test against
     * @return true if objectClass is a class of or a sub-class of the parent
     */
    public static boolean isOrSubOf(Class<?> objectClass, Class<?> parentClass) {
        //Debug.logInfo("Checking isOrSubOf for [" + objectClass.getName() + "] and [" + objectClass.getName() + "]", module);
        while (objectClass != null) {
            if (objectClass == parentClass) return true;
            objectClass = objectClass.getSuperclass();
        }
        return false;
    }

    /**
     * Tests if a class is a class of a sub-class of or properly implements an interface.
     *
     * @param objectClass Class to test
     * @param typeObject  Object to test against
     * @return true if objectClass is a class of a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Class<?> objectClass, Object typeObject) {
        Class<?> typeClass = typeObject.getClass();

        return instanceOf(objectClass, typeClass);
    }

    /**
     * Tests if a class is a class of a sub-class of or properly implements an interface.
     *
     * @param objectClass Class to test
     * @param typeName    name to test against
     * @return true if objectClass is a class or a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Class<?> objectClass, String typeName) {
        return instanceOf(objectClass, typeName, null);
    }

    /**
     * Tests if an object is an instance of a sub-class of or properly implements an interface.
     *
     * @param obj        Object to test
     * @param typeObject Object to test against
     * @return true if obj is an instance of a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Object obj, Object typeObject) {
        Class<?> typeClass = typeObject.getClass();

        return instanceOf(obj, typeClass);
    }

    /**
     * Tests if an object is an instance of a sub-class of or properly implements an interface.
     *
     * @param obj      Object to test
     * @param typeName name to test against
     * @return true if obj is an instance of a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Object obj, String typeName) {
        return instanceOf(obj, typeName, null);
    }

    /**
     * Tests if a class is a class of a sub-class of or properly implements an interface.
     *
     * @param objectClass Class to test
     * @param typeName    Object to test against
     * @param loader
     * @return true if objectClass is a class of a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Class<?> objectClass, String typeName, ClassLoader loader) {
        Class<?> infoClass = loadInfoClass(typeName, loader);

        if (infoClass == null)
            throw new IllegalArgumentException("Illegal type found in info map (could not load class for specified type)");

        return instanceOf(objectClass, infoClass);
    }

    /**
     * Tests if an object is an instance of a sub-class of or properly implements an interface.
     *
     * @param obj      Object to test
     * @param typeName Object to test against
     * @param loader
     * @return true if obj is an instance of a sub-class of, or properly implements an interface
     */
    public static boolean instanceOf(Object obj, String typeName, ClassLoader loader) {
        Class<?> infoClass = loadInfoClass(typeName, loader);

        if (infoClass == null) {
            throw new IllegalArgumentException("Illegal type found in info map (could not load class for specified type)");
        }

        return instanceOf(obj, infoClass);
    }

    public static Class<?> loadInfoClass(String typeName, ClassLoader loader) {
        //Class infoClass = null;
        try {
            return ObjectType.loadClass(typeName, loader);
        } catch (SecurityException se1) {
            throw new IllegalArgumentException("Problems with classloader: security exception (" +
                    se1.getMessage() + ")");
        } catch (ClassNotFoundException e1) {
            try {
                return ObjectType.loadClass(LANG_PACKAGE + typeName, loader);
            } catch (SecurityException se2) {
                throw new IllegalArgumentException("Problems with classloader: security exception (" +
                        se2.getMessage() + ")");
            } catch (ClassNotFoundException e2) {
                try {
                    return ObjectType.loadClass(SQL_PACKAGE + typeName, loader);
                } catch (SecurityException se3) {
                    throw new IllegalArgumentException("Problems with classloader: security exception (" +
                            se3.getMessage() + ")");
                } catch (ClassNotFoundException e3) {
                    throw new IllegalArgumentException("Cannot find and load the class of type: " + typeName +
                            " or of type: " + LANG_PACKAGE + typeName + " or of type: " + SQL_PACKAGE + typeName +
                            ":  (" + e3.getMessage() + ")");
                }
            }
        }
    }

    /**
     * Tests if an object is an instance of a sub-class of or properly implements an interface.
     *
     * @param obj       Object to test
     * @param typeClass Class to test against
     * @return true if obj is an instance of a sub-class of typeClass
     */
    public static boolean instanceOf(Object obj, Class<?> typeClass) {
        if (obj == null) return true;
        Class<?> objectClass = obj.getClass();
        return instanceOf(objectClass, typeClass);
    }

    /**
     * Tests if a class is a class of a sub-class of or properly implements an interface.
     *
     * @param objectClass Class to test
     * @param typeClass   Class to test against
     * @return true if objectClass is a class or sub-class of, or implements typeClass
     */
    public static boolean instanceOf(Class<?> objectClass, Class<?> typeClass) {
        if (typeClass.isInterface() && !objectClass.isInterface()) {
            return interfaceOf(objectClass, typeClass);
        } else {
            return isOrSubOf(objectClass, typeClass);
        }
    }

    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Object value) {
        if (value == null) return true;

        if (value instanceof String) return ((String) value).length() == 0;
        if (value instanceof Collection) return ((Collection<? extends Object>) value).size() == 0;
        if (value instanceof Map) return ((Map<? extends Object, ? extends Object>) value).size() == 0;
        if (value instanceof CharSequence) return ((CharSequence) value).length() == 0;
        if (value instanceof IsEmpty) return ((IsEmpty) value).isEmpty();

        // These types would flood the log
        // Number covers: BigDecimal, BigInteger, Byte, Double, Float, Integer, Long, Short
        if (value instanceof Boolean) return false;
        if (value instanceof Number) return false;
        if (value instanceof Character) return false;
        if (value instanceof java.util.Date) return false;

        return false;
    }

    @SuppressWarnings("serial")
    public static final class NullObject implements Serializable {
        public NullObject() {
        }

        @Override
        public String toString() {
            return "ObjectType.NullObject";
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other instanceof NullObject) {
                // should do equality of object? don't think so, just same type
                return true;
            } else {
                return false;
            }
        }
    }
}

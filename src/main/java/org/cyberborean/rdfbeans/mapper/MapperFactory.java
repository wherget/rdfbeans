package org.cyberborean.rdfbeans.mapper;

import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.cyberborean.rdfbeans.reflect.RDFBeanInfo;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class MapperFactory {
	protected static Map<Class<?>, Class<? extends RDFMapper<?>>> mapperRegistry = new HashMap<>();

	static {
		mapperRegistry.put(String.class, StringMapper.class);
	}

	public static <T> RDFMapper<T> getMapperFor(Class<T> mappedClass, RepositoryConnection repository) throws RDFBeanException {
		Class<? extends RDFMapper<T>> mapperClass = lookupMapperFor(mappedClass);
		return instantiateMapper(mapperClass, mappedClass, repository);
	}

	@SuppressWarnings("unchecked")
	private static <T> Class<? extends RDFMapper<T>> lookupMapperFor(Class<T> mappedClass) {
		Class<? extends RDFMapper> mapperClass;
		if (RDFBeanInfo.isRdfBeanClass(mappedClass)) {
			mapperClass = RDFBeanMapper.class;
		} else {
			mapperClass = mapperRegistry.get(mappedClass);
		}
		return (Class<? extends RDFMapper<T>>) mapperClass;
	}

	private static <P> RDFMapper<P> instantiateMapper(Class<? extends RDFMapper<P>> mapperClass, Class<P> propertyClass, RepositoryConnection repository) throws RDFBeanException {
		try {
			Constructor<? extends RDFMapper<P>> cons = mapperClass.getConstructor(Class.class, RepositoryConnection.class);
			return cons.newInstance(propertyClass, repository);
		} catch (NoSuchMethodException e) {
			throw new RDFBeanException("Mapper class " + mapperClass.getName() + " doesn't have required constructor", e);
		} catch (IllegalAccessException e) {
			throw new RDFBeanException("Mapper class " + mapperClass.getName() + "'s constructor isn't public", e);
		} catch (InstantiationException e) {
			throw new RDFBeanException("Could not instantiate mapper class " + mapperClass.getName(), e);
		} catch (InvocationTargetException e) {
			throw new RDFBeanException("Could not invoke mapper class " + mapperClass.getName() + "'s constructor", e);
		}
	}

}

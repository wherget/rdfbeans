package org.cyberborean.rdfbeans.mapper;

import org.cyberborean.rdfbeans.datatype.DefaultDatatypeMapper;
import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.cyberborean.rdfbeans.exceptions.RDFBeanValidationException;
import org.cyberborean.rdfbeans.reflect.RDFBeanInfo;
import org.cyberborean.rdfbeans.reflect.RDFProperty;
import org.eclipse.rdf4j.common.iteration.Iterations;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryConnection;

import java.lang.reflect.Method;
import java.util.List;

public class RDFBeanMapper<BeanType> extends RDFMapper<BeanType> {
	private RDFBeanInfo beanInfo;

	public RDFBeanMapper(Class<BeanType> targetClass, RepositoryConnection connection) throws RDFBeanValidationException {
		super(targetClass, connection);
		beanInfo = RDFBeanInfo.get(targetClass);
	}

	@Override
	public BeanType deserialize(Resource resource) throws RDFBeanException {
		BeanType object = constructInstance();
		for (RDFProperty property : beanInfo.getProperties()) {
			Object value = deserializeProperty(property, resource);
			property.setValue(object, value);
		}
		return object;
	}

	@Override
	public BeanType deserialize(Literal literal) throws RDFBeanException {
		throw new RDFBeanException("Tried to deserialize a Literal with the RDFBeanMapper.");
	}

	private boolean isWritable(RDFProperty property) {
		Method writeMethod = property.getPropertyDescriptor().getWriteMethod();
		return (writeMethod != null && writeMethod.isAccessible());
	}

	private BeanType constructInstance() throws RDFBeanException {
		try {
			return (BeanType) beanInfo.getRDFBeanClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RDFBeanException("Failed to construct instance of RDFBean " + beanInfo.getRDFBeanClass().getName(), e);
		}
	}

	private Object deserializeProperty(RDFProperty property, Resource container) throws RDFBeanException {
		List<Statement> propertyStatements = Iterations.asList(repository.getStatements(container, property.getUri(), null));
		if (propertyStatements.isEmpty()) {
			// property is not defined in repo
			return null;
		}
		RDFMapper<? extends Object> propertyMapper = MapperFactory.getMapperFor((Class<? extends Object>)property.getPropertyType(), repository);
		return propertyMapper.deserialize(propertyStatements.iterator().next().getObject());
	}
}

package org.cyberborean.rdfbeans.mapper;

import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public abstract class RDFMapper<MappedType> {
	protected RepositoryConnection repository;
	protected Class<MappedType> mappedClass;

	public RDFMapper(Class<MappedType> type, RepositoryConnection connection) {
		repository = connection;
		mappedClass = type;
	}

	public MappedType deserialize(Value value) throws RDFBeanException {
		if (value instanceof Resource) {
			return deserialize((Resource) value);
		} else if (value instanceof Literal) {
			return deserialize((Literal) value);
		} else {
			throw new RDFBeanException("Don't know how to deserialize something which is neither Resource nor Literal.");
		}
	}

	public abstract MappedType deserialize(Resource resource) throws RDFBeanException;

	public abstract MappedType deserialize(Literal literal) throws RDFBeanException;
}

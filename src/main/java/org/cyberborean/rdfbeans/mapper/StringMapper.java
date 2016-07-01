package org.cyberborean.rdfbeans.mapper;

import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.repository.RepositoryConnection;

public class StringMapper extends RDFMapper<String> {

	public StringMapper(Class<String> type, RepositoryConnection connection) {
		super(type, connection);
	}

	@Override
	public String deserialize(Resource resource) throws RDFBeanException {
		return resource.stringValue();
	}

	@Override
	public String deserialize(Literal literal) throws RDFBeanException {
		return literal.stringValue();
	}
}

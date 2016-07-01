package org.cyberborean.rdfbeans.test.mapper;

import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.cyberborean.rdfbeans.mapper.MapperFactory;
import org.cyberborean.rdfbeans.mapper.RDFBeanMapper;
import org.cyberborean.rdfbeans.mapper.RDFMapper;
import org.cyberborean.rdfbeans.mapper.StringMapper;
import org.cyberborean.rdfbeans.test.entities.DatatypeTestClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertThat;

public class MapperFactoryTest {
	@Test
	public void AnnotatedBeanProducesBeanMapper() throws RDFBeanException {
		RDFMapper<DatatypeTestClass> mapper = MapperFactory.getMapperFor(DatatypeTestClass.class, null);
		assertThat(mapper, instanceOf(RDFBeanMapper.class));
	}

	@Test
	public void producesStringMapper() throws RDFBeanException {
		RDFMapper<String> mapper = MapperFactory.getMapperFor(String.class, null);
		assertThat(mapper, instanceOf(StringMapper.class));
	}
}

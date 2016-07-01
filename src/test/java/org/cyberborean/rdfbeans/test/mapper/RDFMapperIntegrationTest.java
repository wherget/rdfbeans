package org.cyberborean.rdfbeans.test.mapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.cyberborean.rdfbeans.annotations.RDF;
import org.cyberborean.rdfbeans.annotations.RDFBean;
import org.cyberborean.rdfbeans.annotations.RDFNamespaces;
import org.cyberborean.rdfbeans.exceptions.RDFBeanException;
import org.cyberborean.rdfbeans.mapper.MapperFactory;
import org.cyberborean.rdfbeans.mapper.RDFMapper;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.sail.memory.MemoryStore;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RDFMapperIntegrationTest {

	@RDFNamespaces("ex = http://example.com/")
	@RDFBean("ex:ModelClass")
	public static class MapperTest {
		@RDF("ex:stringProperty")
		@Getter @Setter
		public String stringProperty;
	}

	private SailRepository repo;
	private RepositoryConnection conn;

	@Before
	public void setupManager() throws Exception {
		repo = new SailRepository(new MemoryStore());
		repo.initialize();
		RepositoryConnection initialFillConn = repo.getConnection();
		initialFillConn.add(getClass().getResourceAsStream("mapperModel.ttl"), "", RDFFormat.TURTLE);
		initialFillConn.close();
		conn = repo.getConnection();
		new MapperTest();
	}

	@Test
	public void deserializes() throws RDFBeanException {
		RDFMapper<MapperTest> mapper = MapperFactory.getMapperFor(MapperTest.class, conn);
		IRI modelResource = SimpleValueFactory.getInstance().createIRI("http://example.com/model");
		MapperTest deserialized = mapper.deserialize(modelResource);
		assertThat(deserialized, notNullValue());
		assertThat(deserialized.stringProperty, notNullValue());
		assertThat(deserialized.stringProperty, is("stringValue"));
	}
}

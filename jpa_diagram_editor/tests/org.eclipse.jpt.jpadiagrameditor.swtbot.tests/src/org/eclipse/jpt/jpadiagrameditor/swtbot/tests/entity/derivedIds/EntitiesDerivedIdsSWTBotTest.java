package org.eclipse.jpt.jpadiagrameditor.swtbot.tests.entity.derivedIds;

import org.eclipse.jpt.jpadiagrameditor.swtbot.tests.internal.AbstractSwtBotEditorTest;
import org.eclipse.jpt.jpadiagrameditor.swtbot.tests.utils.Utils;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SWTBotJunit4ClassRunner.class)
public class EntitiesDerivedIdsSWTBotTest extends AbstractSwtBotEditorTest {
	
	protected static String TEST_PROJECT = "Test_" + System.currentTimeMillis();
	
	@BeforeClass
	public static void beforeClass() throws Exception {
		createJPa20Project(TEST_PROJECT);
	}
	
	
	/**
	 * Create two entities in the diagram. From the second entity, remove the default
	 * primary key attribute. From the "Derived Identifiers" select "One-to-One" unidirectional
	 * relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testSimpleDerivedIdWithoutDefaultPK(){
		Utils.sayTestStarted("testSimpleDerivedIdWithoutDefaultPK");
		relUtils.simpleDerivedIdWithoutDefaultPK(false);
		Utils.sayTestFinished("testSimpleDerivedIdWithoutDefaultPK");
	}
	
	/**
	 * Create two entities in the diagram. From the "Derived Identifiers" select "One-to-One"
	 * unidirectional relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * with the MapsId annotation in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testSimpleDerivedIdWithDefaultPK(){
		Utils.sayTestStarted("testSimpleDerivedIdWithDefaultPK");
		relUtils.simpleDerivedIdWithDefaultPK(false);
		Utils.sayTestFinished("testSimpleDerivedIdWithDefaultPK");
	}
	
	
	/**
	 * Create two entities and one embeddable in the diagram. From the second entity, remove the default
	 * primary key attribute. From the "Composition" section, select "Embed Single object" and embed the
	 * embeddable into the entity2. From the "JPA Details" view, change the mapping of the embedded attribute
	 * in the entity2 to EmbeddedId. From the "Derived Identifiers" select "One-to-One"
	 * unidirectional relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * with the MapsId annotation in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithEmbeddedPK(){
		Utils.sayTestStarted("testDerivedIdWithEmbeddedPK");
		relUtils.derivedIdWithEmbeddedPK(false);
		Utils.sayTestFinished("testDerivedIdWithEmbeddedPK");
	}
	
	/**
	 * Create two entities in the diagram. Create a simple java class. From the second entity, remove the default
	 * primary key attribute. Use the created java class as IDClass in entity2. From the "Derived Identifiers" select "One-to-One"
	 * unidirectional relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithIdClassPK() throws Exception{
		Utils.sayTestStarted("testDerivedIdWithIdClassPK");
		relUtils.derivedIdWithIdClassPK(false);
		Utils.sayTestFinished("testDerivedIdWithIdClassPK");
	}
	
	/**
	 * Create two entities in the diagram. Create a simple java class. Remove the default
	 * primary key attribute from both entities. Use the created java class as IDClass in both entities.
	 * From the "Derived Identifiers" select "One-to-One" unidirectional relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithSameIdClassPK() throws Exception{
		Utils.sayTestStarted("testDerivedIdWithSameIdClassPK");
		relUtils.derivedIdWithSameIdClassPK(false);
		Utils.sayTestFinished("testDerivedIdWithSameIdClassPK");
	}
	
	/**
	 * Create two entities in the diagram. Create two simple java class. Remove the default
	 * primary key attribute from both entities. Use the first java class as IDClass in the first entity and the second
	 * java class as IdClass for the second entity. From the "Derived Identifiers" select "One-to-One"
	 * unidirectional relation feature and click first on the second entity and then on the first one.
	 * Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Assert that a new helper attribute is automatically added in the second java class and its type is the type
	 * of the first java class, used as IDClass.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithDifferentIdClassPK() throws Exception{
		Utils.sayTestStarted("testDerivedIdWithDifferentIdClassPK");
		relUtils.derivedIdWithDifferentIdClassPK(false);
		Utils.sayTestFinished("testDerivedIdWithDifferentIdClassPK");
	}	
	
	/**
	 * Create two entities and one embeddable in the diagram. Remove the default primary key attribute from both entities. 
	 * Embed the embeddable in both entities and change the mappig of the embedded attributes to EmbeddedIds.
	 * From the "Derived Identifiers" select "One-to-One" unidirectional relation feature and click first on the second
	 * entity and then on the first one. Assert that the connection appears. Assert that the owner attribute of the relation is mapped
	 * with MapsId in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithSameEmbeddedPK(){
		Utils.sayTestStarted("testDerivedIdWithSameEmbeddedPK");
		relUtils.derivedIdWithSameEmbeddedPK(false);
		Utils.sayTestFinished("testDerivedIdWithSameEmbeddedPK");
	}
	
	/**
	 * Create two entities and two embeddable in the diagram. Remove the default primary key attribute from both entities. 
	 * Embed the first embeddable in the first enetity and the second one in the second entity. Change the mapping of the
	 * embedded attributes to EmbeddedIds. From the "Derived Identifiers" select "One-to-One" unidirectional relation feature
	 * and click first on the second entity and then on the first one. Assert that the connection appears. Assert that the owner
	 * attribute of the relation is mapped with MapsId in the second entity and there is no "Relation Attributes" section.
	 * Assert that e new helper attribute is added in the second embeddable and its type is of the first embeddable.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithDifferentEmbeddedPK(){
		Utils.sayTestStarted("testDerivedIdWithDifferentEmbeddedPK");
		relUtils.derivedIdWithDifferentEmbeddedPK(false);
		Utils.sayTestFinished("testDerivedIdWithDifferentEmbeddedPK");
	}
	
	/**
	 * Create two entities and one embeddable in the diagram. Create a simple java class. Remove the default primary key attribute
	 * from both entities. Set the java class as IDClass to the first entity and embed the embeddable in the second entity.
	 * Change the mapping of the embedded attributes to EmbeddedIds. From the "Derived Identifiers" select "One-to-One" unidirectional relation feature
	 * and click first on the second entity and then on the first one. Assert that the connection appears. Assert that the owner
	 * attribute of the relation is mapped with MapsId in the second entity and there is no "Relation Attributes" section.
	 * Assert that e new helper attribute is added in the embeddable and its type is of the java class used as IdClass.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithIdClassAndEmbeddedPK() throws Exception {
		Utils.sayTestStarted("testDerivedIdWithIdClassAndEmbeddedPK");
		relUtils.derivedIdWithIdClassAndEmbeddedPK(false);
		Utils.sayTestFinished("testDerivedIdWithIdClassAndEmbeddedPK");
	}
	
	
	/**
	 * Create two entities and one embeddable in the diagram.Remove the default primary key attribute
	 * from both entities. Set the embeddable as IDClass to the first entity and embed the embeddable in the second entity.
	 * Change the mapping of the embedded attributes to EmbeddedIds. From the "Derived Identifiers" select "One-to-One" unidirectional relation feature
	 * and click first on the second entity and then on the first one. Assert that the connection appears. Assert that the owner
	 * attribute of the relation is mapped with MapsId in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithIdClassAndSameEmbeddedPK() throws Exception {
		Utils.sayTestStarted("testDerivedIdWithIdClassAndSameEmbeddedPK");
		relUtils.derivedIdWithIdClassAndSameEmbeddedPK(false);
		Utils.sayTestFinished("testDerivedIdWithIdClassAndSameEmbeddedPK");
	}

	/**
	 * Create two entities and one embeddable in the diagram. Create a simple java class. Remove the default primary key attribute
	 * from both entities. Embed the embeddable in the first entity and set the java class as IDClass to the second entity.
	 * Change the mapping of the embedded attributes to EmbeddedIds. From the "Derived Identifiers" select "One-to-One" unidirectional relation feature
	 * and click first on the second entity and then on the first one. Assert that the connection appears. Assert that the owner
	 * attribute of the relation is mapped as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Assert that e new helper attribute is added in the java class and its type is of the embeddable.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithEmbeddedPkAndIdClass() throws Exception {
		Utils.sayTestStarted("testDerivedIdWithEmbeddedPkAndIdClass");
		relUtils.derivedIdWithEmbeddedPkAndIdClass(false);
		Utils.sayTestFinished("testDerivedIdWithEmbeddedPkAndIdClass");
	}
	
	/**
	 * Create two entities and one embeddable in the diagram. Remove the default primary key attribute
	 * from both entities. Embed the embeddable in the first entity and set the embeddable as IDClass to the second entity.
	 * Change the mapping of the embedded attributes to EmbeddedIds. From the "Derived Identifiers" select "One-to-One" unidirectional relation feature
	 * and click first on the second entity and then on the first one. Assert that the connection appears. Assert that the owner
	 * attribute of the relation is mapped as primary key attribute in the second entity and there is no "Relation Attributes" section.
	 * Test that the created relation is successfully deleted. Repeats all steps for the other three
	 * types of relation also.
	 */
	@Test
	public void testDerivedIdWithEmbeddedPkAndSameIdClass() throws Exception {
		Utils.sayTestStarted("testDerivedIdWithEmbeddedPkAndSameIdClass");
		relUtils.derivedIdWithEmbeddedPkAndSameIdClass(false);
		Utils.sayTestFinished("testDerivedIdWithEmbeddedPkAndSameIdClass");
	}
}

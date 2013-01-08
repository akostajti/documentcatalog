package net.docca.backend.convert.hocr.attributes;

import net.docca.test.util.EqualityAsserts;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * simple test cases for checking if the hocr attribute implementation classes work properly.
 * 
 * @author Akos Tajti <akos.tajti@gmail.com>
 *
 */
@Test(groups = {"mustrun", "hocr"} )
public class HocrAttributeTests {
	@Test
	public void testBoundingBox() {
		BoundingBox bbox1 = new BoundingBox(0, 300, 1000, 2100);

		// test the getters
		Assert.assertEquals(bbox1.getHeight(), 1800);
		Assert.assertEquals(bbox1.getWidth(), 1000);
		Assert.assertEquals(bbox1.getLeft(), 0);
		Assert.assertEquals(bbox1.getBottom(), 300);
		Assert.assertEquals(bbox1.getRight(), 1000);
		Assert.assertEquals(bbox1.getTop(), 2100);

		// test equals and hashcode
		BoundingBox bbox2 = new BoundingBox(0, 300, 1000, 2100); // equal to bbox1
		BoundingBox[] notEquals = new BoundingBox[] {
				new BoundingBox(0, 500, 1000, 2100), new BoundingBox(1, 300, 1000, 2100),
				new BoundingBox(0, 300, 900, 2100), new BoundingBox(0, 300, 1000, 2200),
				new BoundingBox(10, 500, 900, 2400)
		};

		EqualityAsserts.assertEqualsAndHashcodeWork(bbox1, bbox2, notEquals);
	}
}

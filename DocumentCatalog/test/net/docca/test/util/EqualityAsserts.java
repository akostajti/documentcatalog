package net.docca.test.util;

import org.testng.Assert;

public class EqualityAsserts {
	public static void assertEqualsAndHashcodeWork(Object target, Object equalObject, Object[] notEquals) {
		Assert.assertTrue(target.equals(target));
		Assert.assertTrue(target.equals(equalObject));
		Assert.assertTrue(equalObject.equals(target));
		for (Object notEqual: notEquals) {
			Assert.assertFalse(target.equals(notEqual));
			Assert.assertFalse(notEqual.equals(target));
		}
		Assert.assertFalse(target.equals(null));
		Assert.assertFalse(target.equals("almafa"));

		Assert.assertEquals(target.hashCode(), target.hashCode());
		Assert.assertEquals(target.hashCode(), target.hashCode());
		Assert.assertNotEquals(target.hashCode(), notEquals[0].hashCode());
	}

}

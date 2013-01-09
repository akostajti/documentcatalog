package net.docca.backend.convert.hocr.elements;

import net.docca.backend.convert.hocr.attributes.BoundingBox;

import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = {"mustrun"})
public class HocrElementTests {
    @Test
    public void testEnums() {
	HocrElement.TagNames.values();
	HocrElement.TagNames.valueOf("p");
    }
    @Test
    public void testWord() {
	BoundingBox bbox = new BoundingBox(0, 100, 1000, 1400);
	Word word = new Word("word_1", bbox, "almafa");

	Assert.assertEquals(word.getId(), "word_1");
	Assert.assertEquals(word.getBoundingBox(), bbox);
	Assert.assertEquals("almafa", word.getTextContent());
    }
}

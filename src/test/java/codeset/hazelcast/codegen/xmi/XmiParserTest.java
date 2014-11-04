package codeset.hazelcast.codegen.xmi;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.w3c.dom.Document;

import codeset.hazelcast.codegen.xmi.XmiParser;

public class XmiParserTest {

    @Test
    public void testParse() {

        XmiParser parser = new XmiParser();
        String fileName = "src/test/resources/test-model.xml";
        Document document = parser.parse(fileName);
        assertNotNull(document);

    }

    @Test
    public void testExceptions() {

        XmiParser parser = new XmiParser();
        String fileName = null;
        try {
            parser.parse(fileName);
            fail("Should probably throw exception with a null fileName");
        } catch(Exception e) {}
        fileName = "";
        try {
            parser.parse(fileName);
            fail("Should probably throw exception with an empty fileName");
        } catch(Exception e) {}

    }

}

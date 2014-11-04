package codeset.hazelcast.codegen.xmi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import codeset.hazelcast.codegen.xmi.XmiModel;
import codeset.hazelcast.codegen.xmi.XmiParser;

public class XmiModelTest {

    private XmiParser parser = new XmiParser();
    private static XPath xpath;

    @BeforeClass
    public static void beforeClass() {
        initXPath();
    }

    @Test
    public void testInit() {

        Document document = Mockito.mock(Document.class);
        XmiModel xmiModel = new XmiModel(document);
        // xmiModel should be possible to query now
        Node node = Mockito.mock(Node.class);
        try {
            xmiModel.getId(node);
        } catch(IllegalStateException e) {
            assertEquals("Failed to find id attribute of node", e.getMessage());
        }

    }

    @Test
    public void testId() {

        XmiModel xmiModel = getXmiModel("src/test/resources/test-model.xml");
        String id = "EAID_32244815_FF6A_4f20_8FDE_278EF6FFA5A5";
        Node node = xmiModel.getNodeById(id);
        assertNotNull(node);
        try {
            xmiModel.getNodeById(null);
            fail("Should throw exception on null");
        } catch(IllegalArgumentException e) {
        }
        try {
            xmiModel.getNodeById("");
            fail("Should throw exception on null");
        } catch(IllegalArgumentException e) {
        }
        String result = xmiModel.getId(node);
        assertEquals(id, result);

    }

    @Test
    public void testGetClassNodes() throws XPathExpressionException {

        XmiModel xmiModel = getXmiModel("src/test/resources/test-model.xml");
        List<Node> classNodes = xmiModel.getClassNodes();
        assertTrue(classNodes.size() > 0);
        for(Node classNode : classNodes) {
            assertEquals("uml:Class", ((Node) xpath.compile("./@xmi:type").evaluate(classNode, XPathConstants.NODE)).getNodeValue());
        }

    }

    @Test
    public void testGetName() {

        XmiModel xmiModel = getXmiModel("src/test/resources/test-model.xml");
        String id = "EAID_32244815_FF6A_4f20_8FDE_278EF6FFA5A5";
        Node node = xmiModel.getNodeById(id);
        String name = xmiModel.getName(node);
        assertEquals("Asset", name);

    }

    @Test
    public void testGetTypeId() {

    }

    @Test
    public void testIsAbstract() {

        XmiModel xmiModel = getXmiModel("src/test/resources/test-model.xml");
        String id = "EAID_32244815_FF6A_4f20_8FDE_278EF6FFA5A5";
        Node node = xmiModel.getNodeById(id);
        assertTrue(xmiModel.isAbstract(node));

    }

    @Test
    public void testGetParent() {

        XmiModel xmiModel = getXmiModel("src/test/resources/test-model.xml");
        String id = "EAID_32244815_FF6A_4f20_8FDE_278EF6FFA5A5";
        Node node = xmiModel.getNodeById(id);
        Node parent = xmiModel.getParent(node);
        //assertEquals("Entity", xmiModel.getName(parent));

    }

    @Test
    public void testGetFields() {

    }

    @Test
    public void testGetAssociation() {

    }

    @Test
    public void testExceptions() {

        Document document = null;
        try {
            XmiModel xmiModel = new XmiModel(document);
            fail("Null document should throw exception");
        } catch(IllegalArgumentException e) {
        }

    }

    private XmiModel getXmiModel(String fileName) {

        Document document = parser.parse(fileName);
        return new XmiModel(document);

    }

    /**
     * Register the XMI salient namespaces xmi, uml and xml.
     */
    private static void initXPath() {

        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(new NamespaceContext() {
            public String getNamespaceURI(String prefix) {
                if (prefix == null) throw new NullPointerException("Namespace cannot null");
                else if ("xmi".equals(prefix)) return "http://www.omg.org/spec/XMI/20110701";
                else if ("uml".equals(prefix)) return "http://www.omg.org/spec/UML/20110701";
                else if ("xml".equals(prefix)) return XMLConstants.XML_NS_URI;
                return XMLConstants.NULL_NS_URI;
            }
            public String getPrefix(String uri) {
                throw new UnsupportedOperationException();
            }
            public Iterator<?> getPrefixes(String uri) {
                throw new UnsupportedOperationException();
            }
        });

    }

}

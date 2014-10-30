package codeset.hazelcast.codegen.xmi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represent the UML model and provide the access methods into its data which
 * the class builders can use. The purpose of this class is to decouple the
 * builders from the XMI and the XPath required to get data from it.
 * 
 * By using different sets of XPath, different XMI versions can be supported
 * without changing the builder logic.
 * 
 * @author ingemar.svensson
 *
 */
public class XmiModel {

    private Document document;
    private XPath xpath;

    /**
     * Construct a new XMI Model based on a provided XML document.
     * @param document a valid XMI model.
     */
    public XmiModel(Document document) {
        if(document == null) {
            throw new IllegalArgumentException("Document cannot be null");
        }
        this.document = document;
        initXPath();
    }

    /**
     * Look up all classes in the XMI model.
     * @return a list of nodes of matching classes.
     */
    public List<Node> getClassNodes() {

        List<Node> classNodes = new ArrayList<>();
        NodeList nodeList = (NodeList) query("/xmi:XMI/uml:Model//packagedElement[@xmi:type=\"uml:Class\"]", XPathConstants.NODESET);
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            classNodes.add(node);
        }
        return classNodes;

    }

    /**
     * Get the UD of a node.
     * @param node the node. duh.
     * @return the ID for the node.
     */
    public String getId(Node node) {

        Node idNode = (Node) query(node, "./@xmi:id", XPathConstants.NODE);
        if(idNode == null) {
            throw new IllegalStateException("Failed to find id attribute of node");
        }
        return idNode.getNodeValue();

    }

    /**
     * Find a node by its ID.
     * @param id the ID of the node.
     * @return a matching node or throw an exception. Something is wrong with the XMI
     *         we're trying to find references to non-existing nodes.
     * @throws IllegalArgumentException if the id can't be found.
     */
    public Node getNodeById(String id) {

        Node node = (Node) query("//packagedElement[@xmi:id=\"" + id + "\"]", XPathConstants.NODE);
        if(node == null) {
            throw new IllegalArgumentException("Failed to find node with id attribute: " + id);
        }
        return node;

    }

    /**
     * Get the name of a node.
     * @param node the node to get the name for.
     * @return the name.
     * @throws IllegalArgumentException if the name can't be found.
     */
    public String getName(Node node) {

        Node nameNode = (Node) query(node, "./@name", XPathConstants.NODE);
        if(nameNode == null) {
            throw new IllegalArgumentException("Failed to find name attribute on node " + node.getNodeName());
        }
        return nameNode.getNodeValue();

    }

    /**
     * Get the ID of the type of a node.
     * @param node the node.
     * @return the ID.
     */
    public String getTypeId(Node node) {

        Node typeNode = (Node) query(node, "./type/@xmi:idref", XPathConstants.NODE);
        if(typeNode == null) {
            throw new IllegalArgumentException("Failed to find type ID on node");
        }
        return typeNode.getNodeValue();

    }

    /**
     * Check if a class node is defined as abstract.
     * @param node a class node.
     * @return true if the class is abstract, false otherwise.
     */
    public boolean isAbstract(Node node) {

        Node abstractNode = (Node) query(node, "./@isAbstract", XPathConstants.NODE);
        return (abstractNode != null && abstractNode.getNodeValue().equals("true"));

    }

    /**
     * Get the parent node of the provided node.
     * @param node the node to get the parent of.
     * @return the parent if exists. Null otherwise.
     */
    public Node getParent(Node node) {
        return (Node) query(node, "./generalization/@general", XPathConstants.NODE);
    }

    /**
     * Get all simple fields/attributes of a provided node. Simple fields are
     * single value fields with known types.
     * @param node the node to get the fields for.
     * @return all fields that were found. 
     *         Never null, empty list is returned if no matches found.
     */
    public List<Node> getSimpleFields(Node node) {

        List<Node> fieldNodes = new ArrayList<>();
        NodeList nodeList = (NodeList) query(node, "./ownedAttribute[@name]", XPathConstants.NODESET);
        for(int i = 0; i < nodeList.getLength(); i++) {
            Node fieldNode = nodeList.item(i);
            fieldNodes.add(fieldNode);
        }
        return fieldNodes;

    }

    /**
     * Find a node's associations i.e. other class nodes it is related to.
     * @param node the node to find associations for.
     * @return a list of associations. Never null, empty list if no matches found.
     */
    public Node getAssociation(Node node) {
        return (Node) query("./@association", XPathConstants.NODE);
    }

    public String getPackage(String rootPackage, Node packageNode, String parentPackageName) {

        String name = getName(packageNode);
        if(parentPackageName != null) {
            parentPackageName = name + "." + parentPackageName;
        } else {
            parentPackageName = name;
        }
        if(parentPackageName.startsWith(rootPackage)) {
            return parentPackageName;
        }
        Node parentNode = packageNode.getParentNode();
        if(parentNode != null) {
            parentPackageName = getPackage(rootPackage, parentNode, parentPackageName);
        }
        return parentPackageName;

    }

    /**
     * Query the XMI document by XPath.
     * @param expression the XPath.
     * @param qname the type of node.
     * @return matching result.
     */
    Object query(String expression, QName qname) {
        try {
            return xpath.compile(expression).evaluate(document, qname);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Invalid xpath expression " + expression, e);
        }
    }

    /**
     * Query the passed in node by XPath.
     * @param node the node to run the query on.
     * @param expression the XPath.
     * @param qname the type of node.
     * @return matching result.
     */
    Object query(Node node, String expression, QName qname) {
        try {
            return xpath.compile(expression).evaluate(node, qname);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Invalid xpath expression " + expression, e);
        }
    }

    /**
     * Register the XMI salient namespaces xmi, uml and xml.
     */
    private void initXPath() {

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
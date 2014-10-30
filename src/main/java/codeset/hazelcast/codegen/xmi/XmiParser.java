package codeset.hazelcast.codegen.xmi;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

/**
 * Parse XML files on the classpath.
 * 
 * @author ingemar.svensson
 *
 */
public class XmiParser {

    /**
     * Parse a file and return the XML document. Very importantly, it's namespace
     * aware.
     * @param fileName a file name for something on the classpath.
     * @return the XML document.
     */
    public Document parse(String fileName) {

        InputStream is = null;
        try {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
    
            is = this.getClass().getResourceAsStream(fileName);

            return documentBuilder.parse(is);

        } catch(Exception e) {
            throw new IllegalStateException("Failed to read the XMI file: ", e);
        } finally {
            if(is != null) {
                try {
                    is.close();
                } catch (IOException e) {}
            }
        }

    }

}
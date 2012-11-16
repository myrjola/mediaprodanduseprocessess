package aalto.media.newsml;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.ImageInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;

public class ImageParser {

    public static String readXMP(String filename) {
        //sanselan to read xmp
        File imagefile = new File(filename);

        try {
            return Sanselan.getXmpXml(imagefile);

        } catch (ImageReadException | IOException e) {
            // TODO Auto-generated catch block
        }

        return "";

    }

    public static ImageInfo readImageInfo(String filename) {
        //sanselan to read xmp
        File imagefile = new File(filename);
        try {
            return Sanselan.getImageInfo(imagefile);

        } catch (ImageReadException | IOException e) {
            // TODO Auto-generated catch block
            return null;
        }
    }


    public static String getNewsItemGuid(String xmp) {
        try {
            XMPMeta meta = XMPMetaFactory.parseFromString(xmp);
            return meta.getProperty(XMPConst.NS_DC, "identifier").toString();
        } catch (XMPException | java.lang.NullPointerException e) {
            // TODO Auto-generated catch block
            return "";
        }


    }

    public static NewsItem retrieveNewsItemWithGuid(String guid)
    {

        return new NewsItem();
    }

    public static void iterateImages(File[] files, ArrayList<NewsItem> newsItems) {
    	// Process all newsItem XML documents using Java DOM (Document Object Model)
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
        for (File file : files) {
            //System.out.println(file.getAbsolutePath());
            if (file.isDirectory()) {
                iterateImages(file.listFiles(), newsItems); // Calls same method again.
            } else {
                String guid = getNewsItemGuid(ImageParser.readXMP(file.getAbsolutePath()));
                //System.out.println(guid);
                for (NewsItem ni: newsItems) {
                    if (ni.getGuid().equals(guid)) {

                		try {
                			documentBuilder = documentFactory.newDocumentBuilder();
                		} catch (ParserConfigurationException e) {
                			e.printStackTrace();
                		}
                		// Reads the NewsItem-xml document
                		Document xmlDocument = null;
                		XPath xpath = XPathFactory.newInstance().newXPath();

                	    try {
							xmlDocument = documentBuilder.parse(ni.getFile().getAbsolutePath());
						} catch (SAXException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                	    
                	    // Change itemClass to ninat:image
                	    XPathExpression expr;
						try {
							expr = xpath.compile("/newsItem/itemMeta/itemClass/@qcode");

							NodeList nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
							nodes.item(0).setTextContent("ninat:image");
						} catch (XPathExpressionException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}
                	                    	    
                	    // Add image metadata
                    	ImageInfo ii = ImageParser.readImageInfo(file.getAbsolutePath());

                	    Element contentSet = xmlDocument.createElement("contentSet");
                	    Element remoteContent = xmlDocument.createElement("remoteContent");
                	    contentSet.appendChild(remoteContent);
                	    remoteContent.setAttribute("href", "file://" + file.getPath());
                	    remoteContent.setAttribute("version", "1");
                	    remoteContent.setAttribute("size", Integer.toString(ii.getBitsPerPixel() * ii.getHeight() * ii.getWidth()));
                	    remoteContent.setAttribute("contenttype", ii.getMimeType());
                	    remoteContent.setAttribute("rendition", "rnd:web");
                	    remoteContent.setAttribute("width", Integer.toString(ii.getWidth()));
                	    remoteContent.setAttribute("height", Integer.toString(ii.getHeight()));
                	    
                	    xmlDocument.getElementsByTagName("newsItem").item(0).appendChild(contentSet);
                	    
                	    
                		TransformerFactory transfac = TransformerFactory.newInstance();
                		transfac.setAttribute("indent-number", new Integer(3)); // Set indent

                		Transformer trans;
						try {
							trans = transfac.newTransformer();
						} catch (TransformerConfigurationException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							return;
						}
                		trans.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indent

                		StringWriter sw = new StringWriter();
                		StreamResult result = new StreamResult(sw);
                		DOMSource source = new DOMSource(xmlDocument);
                		try {
							trans.transform(source, result);
						} catch (TransformerException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                		String xmlString = sw.toString();

                		System.out.println(xmlString);
                	    
                        System.out.println("Found match in " + ni.toString());

                    } else {
                        //System.out.println(ni.getGuid() + " not matching " + guid);
                    }

                }
            }
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {

        PackageGenerator pg = new PackageGenerator(args[0]);
        PackageItem pi = pg.generatePackage();
        

        iterateImages(new File(args[1]).listFiles(), pi.newsItems);

    }

}

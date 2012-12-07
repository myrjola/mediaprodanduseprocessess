package aalto.media.newsml;

import java.io.IOException;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import aalto.media.newsml.PackageItem;

public class PackageWriter {

	public String createPackageItemXML(PackageItem packageItem)
			throws ParserConfigurationException, TransformerException,
			IOException {

		if (packageItem == null)
			packageItem = new PackageItem();

		DocumentBuilderFactory dbfactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = dbfactory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();

		// create the root packageItem element and add it to the document
		Element packageItem_element = doc.createElement("packageItem");
		packageItem_element.setAttribute("xmlns", packageItem.XMLNS);
		packageItem_element.setAttribute("xmlns:xsi", packageItem.XMLNS_XSI);
		packageItem_element.setAttribute("standard", packageItem.STANDARD);
		packageItem_element.setAttribute("standardversion",
				packageItem.STANDARD_VERSION);
		packageItem_element
				.setAttribute("conformance", packageItem.CONFORMANCE);
		packageItem_element.setAttribute("xsi:schemaLocation",
				packageItem.XSI_SCHEMALOCATION);
		packageItem_element.setAttribute("guid", packageItem.getGuid());
		packageItem_element.setAttribute("version", packageItem.getVersion());
		doc.appendChild(packageItem_element);

		// create child element, add an attribute, and add to root
		Element catalogRef = doc.createElement("catalogRef");
		catalogRef.setAttribute("href", packageItem.CATALOGREF);
		packageItem_element.appendChild(catalogRef);

		Element itemMeta = doc.createElement("itemMeta");
		packageItem_element.appendChild(itemMeta);

		Element itemClass = doc.createElement("itemClass");
		itemClass.setAttribute("qcode", packageItem.ITEMCLASS);
		itemMeta.appendChild(itemClass);

		Element provider = doc.createElement("provider");
		provider.setAttribute("literal", packageItem.PROVIDER);
		itemMeta.appendChild(provider);

		Element versionCreated = doc.createElement("versionCreated");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String now = sdf.format(new Date());
		Text text = doc.createTextNode(now);
		versionCreated.appendChild(text);
		itemMeta.appendChild(versionCreated);

		// ...

		Element contentMeta = doc.createElement("contentMeta");
		packageItem_element.appendChild(contentMeta);
        Element contributor = doc.createElement("contributor");
        contentMeta.appendChild(contributor);
        Element contributorName = doc.createElement("name");
        contributor.appendChild(contributorName);
        if (packageItem.getContributorName() != null) {
            contributorName.appendChild(doc.createTextNode(packageItem.getContributorName()));
        }
        Element headline = doc.createElement("headline");
        contentMeta.appendChild(headline);
        if (packageItem.getHeadline() != null) {
            headline.appendChild(doc.createTextNode(packageItem.getHeadline()));
        }

		// ...

		Element groupSet = doc.createElement("groupSet");
		packageItem_element.appendChild(groupSet);
        groupSet.setAttribute("root", "G1");
        Element group1 = doc.createElement("group");
        groupSet.appendChild(group1);
        group1.setAttribute("id", "G1");

        if (packageItem.getNewsItems() != null) {
            for (NewsItem n: packageItem.getNewsItems()) {
                Element item = doc.createElement("itemRef");
                item.setAttribute("residref", n.getGuid());
                item.setAttribute("contenttype", "application/vnd.iptc.g2.newsitem+xml");
                group1.appendChild(item);
            }
        }


		// ...

		// Output the XML

		TransformerFactory transfac = TransformerFactory.newInstance();
		transfac.setAttribute("indent-number", new Integer(3)); // Set indent

		Transformer trans = transfac.newTransformer();
		trans.setOutputProperty(OutputKeys.INDENT, "yes"); // Enable indent

		StringWriter sw = new StringWriter();
		StreamResult result = new StreamResult(sw);
		DOMSource source = new DOMSource(doc);
		trans.transform(source, result);
		String xmlString = sw.toString();

		return xmlString;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PackageWriter xmlWriter = new PackageWriter();
		try {
			xmlWriter.createPackageItemXML(null);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

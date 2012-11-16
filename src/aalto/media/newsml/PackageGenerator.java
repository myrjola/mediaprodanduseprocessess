package aalto.media.newsml;

/**
 * PackageGenerator class that includes methods for
 * listing files in folder and reading XML documents, and
 * processing of their contents.
 * 
 * Add more code for NewsML-G2 package generation.
 */

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import aalto.media.newsml.PackageWriter;

public class PackageGenerator {
	
	// XPath expressions for retrieving newsItem elements
	private final static String GUID_XPATH = "/newsItem/@guid"; 
	private final static String VERSION_XPATH = "/newsItem/@version"; 
	private final static String VERSION_CREATED_XPATH = "/newsItem/itemMeta/versionCreated";
	private final static String TYPE_ROLE_XPATH = "/newsItem/itemMeta/role/name";
	private final static String DEPARTMENT_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:department']/name";
	private final static String CATEGORIES_XPATH = "/newsItem/contentMeta/subject[@type='cpnat:category']/name";
	
	private String newsItemFolder;
	private ArrayList<NewsItem> newsItems;
	
	public PackageGenerator(String newsItemFolder) {
		this.newsItemFolder = newsItemFolder;
		listItems();
		PackageItem packageItem = generatePackage();
        writePackageToFile(packageItem, "10talous.xml");
	}
	
	private void listItems() {
		
		newsItems = new ArrayList<NewsItem>();
		
		// List all file in given folder that ends with '.xml'
		File[] allNewsItems = new File(newsItemFolder).listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File file) {
				if (file.isFile() && file.getName().endsWith(".xml")) return true;
				return false;
			}
		});
		
		// Process all newsItem XML documents using Java DOM (Document Object Model)
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = null;
		try {
			documentBuilder = documentFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}


		// Reads all the XML documents listed
		Document xmlDocument = null;
		XPath xpath = XPathFactory.newInstance().newXPath();
		for (File newsItemFile : allNewsItems) {
			try {
				xmlDocument = documentBuilder.parse(newsItemFile);
			} catch (SAXException | IOException e) {
				e.printStackTrace();
			}
			
			XPathExpression expr;
			NodeList nodes;
			
			NewsItem newsItem = new NewsItem();
			try {
				//Get guid of the NewsItem
				expr = xpath.compile(GUID_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String guid = nodes.item(0).getTextContent();
				newsItem.setGuid(guid);
				
				//Get version of the NewsItem
				expr = xpath.compile(VERSION_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String version = nodes.item(0).getTextContent();
				newsItem.setVersion(version);
				
				//Get date and time when current version of the NewsItem was sent
				expr = xpath.compile(VERSION_CREATED_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String versionCreated = nodes.item(0).getTextContent();
				newsItem.setVersion_created(versionCreated);
				
				//Get type of news item article
				expr = xpath.compile(TYPE_ROLE_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String typeRole = nodes.item(0).getTextContent();
				newsItem.setType_role(typeRole);
				
				expr = xpath.compile(DEPARTMENT_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String department = nodes.item(0).getTextContent();
				newsItem.setDepartment(department);
				
				//Get NewsItem categories
				expr = xpath.compile(CATEGORIES_XPATH);
				nodes = (NodeList)expr.evaluate(xmlDocument, XPathConstants.NODESET);
				String[] categories = new String[nodes.getLength()];
				for (int i = 0; i < nodes.getLength(); i++) {
					categories[i] = nodes.item(i).getTextContent();
				}
				newsItem.setCategories(categories);
				
				/*
				 * Add your own code here, e.g. rest of the needed elements from newsItem.
				 */
				
				// Adds current news item to newsItems-list
				newsItems.add(newsItem);
				
			} catch (XPathExpressionException e) {

				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Method for generating packageItem from newsItem list.
	 */
	
	private PackageItem generatePackage() {
		// An example for generating packageItem

		// Finds all items from specific department
		ArrayList<NewsItem> packageItems = new ArrayList<NewsItem>();
		for (int i = 0; i < newsItems.size(); i++) {
			NewsItem item = newsItems.get(i);
			System.out.println(item.getDepartment());
			if (item.getDepartment().equals(("Talous"))) { // You can use your own rules here.
				packageItems.add(item);
			}
		}
		// Sort items by date (newest first)
		Collections.sort(packageItems, new NewsItemComparator());
		
		// Creates packageItem containing first 10 items
		int items = 10;
		if (packageItems.size() < 10) items = packageItems.size();
		
		PackageItem packageItem = new PackageItem();
		packageItem.setHeadline("Talouden kymmenen kuuminta.");
		packageItem.setContributorName("GruppSex");
		
		for (int i = 0; i < items; i++) {
			System.out.println("Adding news item " + packageItems.get(i).getGuid() + " (" +
				packageItems.get(i).getVersion_created() + ")");
			packageItem.addNewsItem(packageItems.get(i));
		}
		return packageItem;
	}

	/*
	 * Method for storing packageItem as a XML document.
	 */
	
	private void writePackageToFile(PackageItem packageItem, String filePath) {
        PackageWriter writer = new PackageWriter();
        try {
			String packagexml = writer.createPackageItemXML(packageItem);
            FileWriter fw = new FileWriter(filePath);
            fw.append(packagexml);
            fw.close();
		} catch (ParserConfigurationException | TransformerException
				| IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Comparator class for sorting NewsItems by date they were sent to customers.
	 */
	
	private class NewsItemComparator implements Comparator<NewsItem>{
		@Override
		public int compare(NewsItem item1, NewsItem item2) {
			return item1.getVersion_created_date().compareTo(item2.getVersion_created_date());
		}
	}

	public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));
		PackageGenerator packageGenerator = 
				new PackageGenerator("path_to_newsitems");
	}
}

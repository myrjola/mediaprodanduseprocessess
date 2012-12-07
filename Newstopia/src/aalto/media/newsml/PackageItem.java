package aalto.media.newsml;

import java.util.ArrayList;

public class PackageItem {
    
    
    /* 
     * Constant fields in packageItem element
     */
    static final String STANDARD = "NewsML-G2";
    static final String STANDARD_VERSION = "2.8";
    static final String CONFORMANCE = "power";
    static final String XMLNS = "http://iptc.org/std/nar/2006-10-01/";
    static final String XMLNS_XSI = "http://www.w3.org/2001/XMLSchema-instance";
    static final String XSI_SCHEMALOCATION = "http://iptc.org/std/nar/2006-10-01/" +
            "../specification/NewsML-G2_2.9-spec-All-Power.xsd";
    static final String CATALOGREF = "http://www.iptc.org/std/catalog/catalog.IPTC-G2-standards_16.xml";
    
    /* 
     * Constant fields in itemMeta element
     */
    static final String ITEMCLASS = "ninat:composite";
    static final String PROVIDER = "STT";
    private static final String GENERATOR = "YourGroup's NewsML Package Generator";
    
    /*
     * Fields in itemMeta element
     */
    private String guid;
    
    //...
    
    /*
     * Fields in contentMetadata element
     */
    private String contributorName;
    private String headline;

    //...
    
    
    /*
     * Fields in groupSet element
     */
    private ArrayList<GroupItem> groupItems;

    /*
     * Newsitems in package
     */
    public ArrayList<NewsItem> newsItems;
    
    public PackageItem() {
        groupItems = new ArrayList<GroupItem>();
        newsItems = new ArrayList<NewsItem>();
    }   
    
    /*
     * Getters and setters
     */
    //...
    
    
    /*
     * Method for adding news items into package
     */

    public void addNewsItem(NewsItem newsItem) {
        newsItems.add(newsItem);
        // Implement a mechanism for adding new newsitems
        // use some kind of id generation system
        
        
    }

    private class GroupItem {
        private String id;
        // Add more fields here. See example packageItem.
        //...

        public GroupItem() {};
        
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }

    public String getGuid() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the contributorName for this instance.
     *
     * @return The contributorName.
     */
    public String getContributorName() {
        return this.contributorName;
    }

    /**
     * Sets the contributorName for this instance.
     *
     * @param contributorName The contributorName.
     */
    public void setContributorName(String contributorName) {
        this.contributorName = contributorName;
    }

    /**
     * Gets the headline for this instance.
     *
     * @return The headline.
     */
    public String getHeadline() {
        return this.headline;
    }

    /**
     * Sets the headline for this instance.
     *
     * @param headline The headline.
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * Gets the newsItems for this instance.
     *
     * @return The newsItems.
     */
    public ArrayList<NewsItem> getNewsItems() {
        return this.newsItems;
    }

    public String getVersion() {
        // TODO Auto-generated method stub
        return null;
    }
}

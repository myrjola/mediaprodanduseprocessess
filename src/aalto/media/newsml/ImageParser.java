package aalto.media.newsml;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;
import com.adobe.xmp.impl.XMPMetaParser;

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
        for (File file : files) {
            //System.out.println(file.getAbsolutePath());
            if (file.isDirectory()) {
                iterateImages(file.listFiles(), newsItems); // Calls same method again.
            } else {
                String guid = getNewsItemGuid(ImageParser.readXMP(file.getAbsolutePath()));
                //System.out.println(guid);
                for (NewsItem ni: newsItems) {
                    if (ni.getGuid().equals(guid)) {
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

package aalto.media.newsml;

import java.io.File;
import java.io.IOException;

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
            e.printStackTrace();
        }

        return "";

    }

    public static String getNewsItemGuid(String xmp) {
        try {
            XMPMeta meta = XMPMetaFactory.parseFromString(xmp);
            return meta.getProperty(XMPConst.NS_DC, "identifier").toString();
        } catch (XMPException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }


    }

    public static NewsItem retrieveNewsItemWithGuid(String guid)
    {

        return new NewsItem();
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(getNewsItemGuid(ImageParser.readXMP(args[0])));

    }

}

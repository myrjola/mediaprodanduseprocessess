package aalto.media.newsml;

import java.io.File;
import java.io.IOException;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

public class ImageParser {

    public static String readXMP(String filename)
    {
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

    public static String getNewsItemGuid(String xmp)
    {
        return "";

    }

    public static NewsItem retrieveNewsItemWithGuid(String guid)
    {

        return new NewsItem();
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println(ImageParser.readXMP(args[0]));

    }

}

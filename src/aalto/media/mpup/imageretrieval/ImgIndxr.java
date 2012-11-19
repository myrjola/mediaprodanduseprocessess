package aalto.media.mpup.imageretrieval;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import net.semanticmetadata.lire.DocumentBuilder;
import net.semanticmetadata.lire.DocumentBuilderFactory;
import net.semanticmetadata.lire.ImageSearchHits;
import net.semanticmetadata.lire.ImageSearcher;
import net.semanticmetadata.lire.ImageSearcherFactory;
import net.semanticmetadata.lire.impl.ChainedDocumentBuilder;
import net.semanticmetadata.lire.impl.ColorLayoutDocumentBuilder;

import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;

import com.adobe.xmp.XMPConst;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.adobe.xmp.XMPMetaFactory;

/**
 * ImgIndexr based on LIRE (Lucene Image Retriever) by
 * 
 * Lux Mathias, Savvas A. Chatzichristofis. Lire: Lucene Image Retrieval – An
 * Extensible Java CBIR Library. In proceedings of the 16th ACM International
 * Conference on Multimedia, pp. 1085-1088, Vancouver, Canada, 2008
 * 
 */

public class ImgIndxr {

	/*
	 * Method for creating an image index.
	 * 
	 * @param imageFolder Folder containing the images
	 * 
	 * @return
	 * 
	 * @throws IOException
	 */

	public static String createIndex(String imageFolder) throws IOException {
		File[] imageFiles = new File(imageFolder).listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				if (file.isFile() && file.getName().endsWith(".jpg"))
					return true;
				return false;
			}
		});

		ChainedDocumentBuilder builder = null;
		File lireIndexFolder = new File(imageFolder, "lire_index");
		if (!IndexReader.indexExists(FSDirectory.open(lireIndexFolder))) {
			lireIndexFolder.mkdirs();

			builder = new ChainedDocumentBuilder();

			// Using the Color histogram as visual feature
			builder.addBuilder((DocumentBuilderFactory
					.getColorHistogramDocumentBuilder()));
			builder.addBuilder((DocumentBuilderFactory
					.getEdgeHistogramBuilder()));
			builder.addBuilder((DocumentBuilderFactory.getColorLayoutBuilder()));
			builder.addBuilder((DocumentBuilderFactory.getCEDDDocumentBuilder()));

			IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_36,
					new WhitespaceAnalyzer(Version.LUCENE_36));
			IndexWriter iw = new IndexWriter(FSDirectory.open(lireIndexFolder),
					conf);

			// Loop through the images

			int imgs = imageFiles.length;
			System.out.println(imgs + " images in folder.");
			int i = 0;

			long currentTime = 0;
			long timeElapsed = 0;
			double avgImageProcessingTime = 0.0;

			String xmpString = null;
			XMPMeta xmp;

			for (File imageFile : imageFiles) {
				currentTime = System.currentTimeMillis();
				String imageFilePath = imageFile.getAbsolutePath();
				System.out.print("Indexing image (" + i + "/" + imgs + "): "
						+ imageFilePath);

				// Check if image has Dublin Core identifier.
				boolean dc_identifier_exists = false;

				try {
					xmpString = Sanselan.getXmpXml(imageFile);
				} catch (ImageReadException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (xmpString != null) {
					try {
						xmp = XMPMetaFactory.parseFromString(xmpString);
						// Get the Dublin Core identifier from image metadata
						String dc_identifier = xmp.getPropertyString(
								XMPConst.NS_DC, "identifier");
						if (dc_identifier != null && dc_identifier.length() > 0) {
							dc_identifier_exists = true;
						}
					} catch (XMPException e) {
						e.printStackTrace();
					}
				}

				if (!dc_identifier_exists) { // Index with LIRE
					try {
						BufferedImage img = ImageIO.read(new FileInputStream(
								imageFilePath));
						Document document = builder.createDocument(img,
								imageFilePath);
						iw.addDocument(document);
					} catch (Exception e) {
						System.err.print("Error reading image or indexing it.");
						e.printStackTrace();
					}
				}
				i++;
				long elapsed = System.currentTimeMillis() - currentTime;
				timeElapsed += elapsed;
				avgImageProcessingTime = (double) timeElapsed / (double) i;
				System.out
						.println("Done in "
								+ elapsed
								+ " ms. "
								+ " ("
								+ "Elapsed time "
								+ ((float) timeElapsed / 60000)
								+ " min. "
								+ "Estimated time left "
								+ (((float) avgImageProcessingTime * (imgs - i)) / 60000)
								+ " min.)");
			}
			iw.close();
		}

		System.out.println("Finished indexing.");

		return lireIndexFolder.getAbsolutePath();
	}

	/*
	 * Method for searching existing image index based on their similarity.
	 * 
	 * @param imageFilePath Path to the image that is searched
	 * 
	 * @param lire_index Path to LIRE index folder
	 */

	public static void searchImages(String imageFilePath, String lire_index) {
		IndexReader ir = null;
		ImageSearcher searcher1 = null;
		ImageSearcher searcher2 = null;
		ImageSearcher searcher3 = null;
		ImageSearcher searcher4 = null;
		BufferedImage img = null;
		boolean imageFileOK = false;

		int maxHits = 20; // Number of images to be retrieved.

		try {
			ir = IndexReader.open(FSDirectory.open(new File(lire_index)));

			// Change the searcher-type to correspond with your index.
			searcher1 = ImageSearcherFactory
					.createColorHistogramImageSearcher(maxHits);
			searcher2 = ImageSearcherFactory
					.createEdgeHistogramImageSearcher(maxHits);
			searcher3 = ImageSearcherFactory
					.createColorLayoutImageSearcher(maxHits);
			searcher4 = ImageSearcherFactory.createCEDDImageSearcher(maxHits);
			img = ImageIO.read(new File(imageFilePath));
			imageFileOK = true;
		} catch (CorruptIndexException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (imageFileOK) {
			ImageSearchHits hits = null;
			try {
				System.out
						.println("Results for search based on Color Histogram");
				hits = searcher1.search(img, ir);
				for (int i = 0; i < hits.length(); i++) {
					String fileName = hits.doc(i).getValues(
							DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
					// Print out predicted similarity values for maxHits number
					// of images.
					System.out.println(hits.score(i) + ": \t" + fileName);
				}

				System.out
						.println("Results for search based on Edge Histogram");
				hits = searcher2.search(img, ir);
				for (int i = 0; i < hits.length(); i++) {
					String fileName = hits.doc(i).getValues(
							DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
					// Print out predicted similarity values for maxHits number
					// of images.
					System.out.println(hits.score(i) + ": \t" + fileName);
				}

				System.out.println("Results for search based on Color Layout:");
				hits = searcher3.search(img, ir);
				for (int i = 0; i < hits.length(); i++) {
					String fileName = hits.doc(i).getValues(
							DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
					// Print out predicted similarity values for maxHits number
					// of images.
					System.out.println(hits.score(i) + ": \t" + fileName);
				}

				System.out.println("Results for search based on CEDD:");
				hits = searcher4.search(img, ir);
				for (int i = 0; i < hits.length(); i++) {
					String fileName = hits.doc(i).getValues(
							DocumentBuilder.FIELD_NAME_IDENTIFIER)[0];
					// Print out predicted similarity values for maxHits number
					// of images.
					System.out.println(hits.score(i) + ": \t" + fileName);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out
					.println("Usage: ImgIndxr [path to imgdir] [path to img file]");
			return;
		}
		try {
			String indexLocation = ImgIndxr.createIndex(args[0]);
			ImgIndxr.searchImages(args[1], indexLocation);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
package lineage2.gameserver.vaesoli;

import lineage2.gameserver.Config;
import lineage2.gameserver.idfactory.IdFactory;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.network.serverpackets.NpcHtmlMessage;
import lineage2.gameserver.network.serverpackets.PledgeCrest;
import gov.nasa.worldwind.formats.dds.DDSConverter;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;

/**
 * @author Melua Cette classe gère le parsing des html, la génération et l'envoi
 *         des DDS
 */
public class MyDDS {

	private static final Logger _log = Logger.getLogger(MyDDS.class.getName());

	/**
	 * remplace les balises d'images et envoie les DDS au client cette methode
	 * ne se charge pas de l'envoi du html
	 * 
	 * @param client
	 *            Player qui reçoit les DDS (le même que le html)
	 * @param packet
	 *            le HTML à parser
	 */
	public static void sendDDS(Player client, NpcHtmlMessage packet) {
		if (packet.getHtml() == null) {
			return;
		}
		boolean root = (client.getTarget() == null || client.getTarget() instanceof NpcInstance);
		String name = client.getName().toLowerCase();
		Pattern pattern = Pattern.compile("<img_int>([-_a-zA-Z0-9\\.]+)</img_int>");
		Matcher matcher = pattern.matcher(packet.getHtml());
		while (matcher.find()) {
			try {
				String sequence = matcher.group(0);
				String img_int = matcher.group(1);
				int tempId = IdFactory.getInstance().getNextId();
				File image = new File(Config.DATAPACK_ROOT + "/data/images" + ((root) ? "" : "/" + name) + "/" + img_int);
				ImageIcon info = new ImageIcon(Config.DATAPACK_ROOT + "/data/images" + ((root) ? "" : "/" + name) + "/" + img_int);
				// convert common image (png, bmp, jpg, ...) to dds (DirectDraw
				// Surface) - image has to have dimensions of power of 2
				// (2,4,8,16,32,64,...)
				PledgeCrest crestImage = new PledgeCrest(tempId, DDSConverter.convertToDDS(image).array());
				// in htm use <img src=\"Crest.crest_" + Config.SERVER_ID +"_" +
				// imgId + "\" width=32 height=16> - use the image dimensions
				packet.replace(sequence,
						"<img src=\"Crest.crest_" + Config.REQUEST_ID + "_" + tempId + "\" width=" + info.getIconWidth() + " height=" + info.getIconHeight()
								+ ">");
				// send the dds as byte array to client through PledgeCrest
				// packet - random id can be used (e.g. named imgId)
				client.sendPacket(crestImage);
			} catch (Exception e) {
				_log.warning(e.getMessage());
			}
		}
	}

	/**
	 * télécharge les images et les sauvegarde remplace les balises d'images
	 * externes et enregistre la description parsée
	 * 
	 */
	public static String prepareDDS(Player player, String description) {
		Pattern pattern = Pattern.compile("<img_ext>(http://[0-9a-zA-Z]+(\\.png|\\.jpg|\\.bmp))</img_ext>");
		Matcher matcher = pattern.matcher(description);
		while (matcher.find()) {
			try {
				int imgId = IdFactory.getInstance().getNextId();
				String sequence = matcher.group(0);
				String img_ext = matcher.group(1);
				String extension = matcher.group(2);
				// vérifier la taille (puissance de 2)
				ImageIcon info = new ImageIcon(img_ext); // URL
				if ((info.getIconHeight() > 0 && (info.getIconHeight() & (info.getIconHeight() - 1)) == 0)
						&& (info.getIconWidth() > 0 && (info.getIconWidth() & (info.getIconWidth() - 1)) == 0)) {

					// télécharger avec l'URL trouvée
					BufferedInputStream in = new BufferedInputStream(new URL(img_ext).openStream());
					FileOutputStream fos = new FileOutputStream(Config.DATAPACK_ROOT + "/data/images/" + player.getName().toLowerCase() + "/" + imgId
							+ extension);
					BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
					byte[] data = new byte[1024];
					int x = 0;
					while ((x = in.read(data, 0, 1024)) >= 0) {
						bout.write(data, 0, x);
					}
					bout.close();
					in.close();

					// remplacer la desc
					description.replace(sequence, "<img_int>" + imgId + extension + "</img_int>");
				}
			} catch (Exception e) {
				_log.warning(e.getMessage());
			}
		}
		return description;
	}
}

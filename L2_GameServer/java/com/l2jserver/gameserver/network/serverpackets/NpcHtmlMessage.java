/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.network.serverpackets;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import java.util.logging.Logger;
import gov.nasa.worldwind.formats.dds.DDSConverter;

import com.l2jserver.Config;
import com.l2jserver.gameserver.cache.HtmCache;
import com.l2jserver.gameserver.idfactory.IdFactory;
import com.l2jserver.gameserver.model.L2ItemInstance;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.clientpackets.RequestBypassToServer;



/**
 *
 * the HTML parser in the client knowns these standard and non-standard tags and attributes
 * VOLUMN
 * UNKNOWN
 * UL
 * U
 * TT
 * TR
 * TITLE
 * TEXTCODE
 * TEXTAREA
 * TD
 * TABLE
 * SUP
 * SUB
 * STRIKE
 * SPIN
 * SELECT
 * RIGHT
 * PRE
 * P
 * OPTION
 * OL
 * MULTIEDIT
 * LI
 * LEFT
 * INPUT
 * IMG
 * I
 * HTML
 * H7
 * H6
 * H5
 * H4
 * H3
 * H2
 * H1
 * FONT
 * EXTEND
 * EDIT
 * COMMENT
 * COMBOBOX
 * CENTER
 * BUTTON
 * BR
 * BR1
 * BODY
 * BAR
 * ADDRESS
 * A
 * SEL
 * LIST
 * VAR
 * FORE
 * READONL
 * ROWS
 * VALIGN
 * FIXWIDTH
 * BORDERCOLORLI
 * BORDERCOLORDA
 * BORDERCOLOR
 * BORDER
 * BGCOLOR
 * BACKGROUND
 * ALIGN
 * VALU
 * READONLY
 * MULTIPLE
 * SELECTED
 * TYP
 * TYPE
 * MAXLENGTH
 * CHECKED
 * SRC
 * Y
 * X
 * QUERYDELAY
 * NOSCROLLBAR
 * IMGSRC
 * B
 * FG
 * SIZE
 * FACE
 * COLOR
 * DEFFON
 * DEFFIXEDFONT
 * WIDTH
 * VALUE
 * TOOLTIP
 * NAME
 * MIN
 * MAX
 * HEIGHT
 * DISABLED
 * ALIGN
 * MSG
 * LINK
 * HREF
 * ACTION
 *
 *
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:57 $
 */
public final class NpcHtmlMessage extends L2GameServerPacket
{
	// d S
	// d is usually 0, S is the html text starting with <html> and ending with </html>
	//
	private static final String _S__1B_NPCHTMLMESSAGE = "[S] 19 NpcHtmlMessage";
	private static Logger _log = Logger.getLogger(RequestBypassToServer.class.getName());
	private int _npcObjId;
	private String _html;
	private int _itemId = 0;
	private boolean _validate = true;
	
	/**
	 * 
	 * @param npcObjId
	 * @param text
	 * @param itemId
	 */
	public NpcHtmlMessage(int npcObjId, int itemId)
	{
		_npcObjId = npcObjId;
		_itemId = itemId;
	}
	
	/**
	 * @param _characters
	 */
	public NpcHtmlMessage(int npcObjId, String text)
	{
		_npcObjId = npcObjId;
		setHtml(text);
	}
	
	public NpcHtmlMessage(int npcObjId)
	{
		_npcObjId = npcObjId;
	}
	
	/**
	 * disable building bypass validation cache for this packet
	 */
	public void disableValidation()
	{
		_validate = false;
	}

    /**
     * @see NpcHtmlMessage#sendDDS(com.l2jserver.gameserver.model.actor.instance.L2PcInstance, boolean)
     */
    public void sendDDS(L2PcInstance client) {
        sendDDS(client, false);
    }

    /**
     * remplace les balises d'images et envoie les DDS au client
     * cette méthode ne se charge pas de l'envoi du html
     * @param client L2PcInstance qui recoit les DDS (le meme que le html)
     * @param npc boolean emplacement des images à la racine du dossier
     */
    public void sendDDS(L2PcInstance client, boolean npc) {
        if (_html == null) {
            return;
        }
        Pattern pattern = Pattern.compile("<img_int>([a-zA-Z_0-9\\.]+)</img_int>");
        Matcher matcher = pattern.matcher(_html);
        while (matcher.find()) {
            try {
                String sequence = matcher.group(0);
                String img_int = matcher.group(1);
                int tempId = IdFactory.getInstance().getNextId();
                File image = new File(Config.DATAPACK_ROOT + "/data/images/" + ((npc) ? "" : client.getName().toLowerCase()) + "/" + img_int);
                ImageIcon info = new ImageIcon(Config.DATAPACK_ROOT + "/data/images" + ((npc) ? "" : "/" + client.getName().toLowerCase()) + "/" + img_int);
                // convert common image (png, bmp, jpg, ...) to dds (DirectDraw Surface) - image has to have dimensions of power of 2 (2,4,8,16,32,64,...)
                PledgeCrest crestImage = new PledgeCrest(tempId, DDSConverter.convertToDDS(image).array());
                // in htm use <img src=\"Crest.crest_" + Config.SERVER_ID +"_" + imgId + "\" width=32 height=16> - use the image dimensions
                this.replace(sequence, "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + tempId + "\" width=" + info.getIconWidth() + " height=" + info.getIconHeight() + ">");
                // send the dds as byte array to client through PledgeCrest packet - random id can be used (e.g. named imgId)
                client.sendPacket(crestImage);
            } catch (Exception e) {
                _log.warning(e.getMessage());
            }
        }
    }

	
	@Override
	public void runImpl()
	{
		if (Config.BYPASS_VALIDATION && _validate)
			buildBypassCache(getClient().getActiveChar());
	}
	
	public void setHtml(String text)
	{
		if(text.length() > 8192)
		{
			_log.warning("Html is too long! this will crash the client!");
			_html = "<html><body>Html was too long</body></html>";
			return;
		}
		_html = text; // html code must not exceed 8192 bytes
	}
	
	public boolean setFile(String prefix, String path)
	{
		String content = HtmCache.getInstance().getHtm(prefix, path);
		
		if (content == null)
		{
			setHtml("<html><body>My Text is missing:<br>"+path+"</body></html>");
			_log.warning("missing html page "+path);
			return false;
		}
		
		setHtml(content);
		return true;
	}
	
	public void replace(String pattern, String value)
	{
		_html = _html.replaceAll(pattern, value.replaceAll("\\$", "\\\\\\$"));
	}

        /**
         * @author Melua
         * remplace toutes les occurences de %itemId_***% dans le html
         */
        public void retrieveAllItemName()
        {
        Pattern pattern = Pattern.compile("%itemId_[1-9]+%");
        Matcher matcher = pattern.matcher(_html);
        while(matcher.find())
            {
            String sequence = matcher.group();
            int itemId = Integer.parseInt(sequence.substring(8, sequence.length() - 1));
            String itemName = new L2ItemInstance(-1, itemId).getItemName();
            this.replace(sequence, itemName);
            }
        }
        
	
	private final void buildBypassCache(L2PcInstance activeChar)
	{
		if (activeChar == null)
			return;
		
		activeChar.clearBypass();
		int len = _html.length();
		for (int i = 0; i < len; i++)
		{
			int start = _html.indexOf("\"bypass ", i);
			int finish = _html.indexOf("\"", start + 1);
			if (start < 0 || finish < 0)
				break;
			
			if (_html.substring(start+8, start+10).equals("-h"))
				start += 11;
			else
				start += 8;
			
			i = finish;
			int finish2 = _html.indexOf("$", start);
			if (finish2 < finish && finish2 > 0)
				activeChar.addBypass2(_html.substring(start, finish2).trim());
			else
				activeChar.addBypass(_html.substring(start, finish).trim());
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x19);
		
		writeD(_npcObjId);
		writeS(_html);
		writeD(_itemId);
	}
	
	/* (non-Javadoc)
	 * @see com.l2jserver.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__1B_NPCHTMLMESSAGE;
	}
	
}

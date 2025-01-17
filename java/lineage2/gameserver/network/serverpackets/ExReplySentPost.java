/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.mail.Mail;

public class ExReplySentPost extends L2GameServerPacket
{
	private final Mail mail;
	
	public ExReplySentPost(Mail mail)
	{
		this.mail = mail;
	}
	
	// ddSSS dx[hddQdddhhhhhhhhhh] Qd
	@Override
	protected void writeImpl()
	{
		writeEx(0xAE);
		writeD(mail.getType().ordinal());
		// Type = Normal
		writeD(mail.getMessageId());
		writeD(0x00);// unknown1
		writeS(mail.getSenderName());
		writeS(mail.getTopic());
		writeS(mail.getBody());
		writeD(mail.getAttachments().size());
		
		for (ItemInstance item : mail.getAttachments())
		{
			writeItemInfo(item);
			writeD(item.getObjectId());
		}
		
		writeQ(mail.getPrice());
		writeD(0x00); // unk
		writeD(mail.isReturnable());
		writeD(mail.getReceiverId());
	}
}
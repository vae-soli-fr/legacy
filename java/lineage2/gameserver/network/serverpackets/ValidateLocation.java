package lineage2.gameserver.network.serverpackets;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.utils.Location;

/**
 * format dddddd (player id, target id, distance, startx, starty, startz)
 * <p>
 */
public class ValidateLocation extends L2GameServerPacket
{
	private int _chaObjId;
	private Location _loc;

	public ValidateLocation(Creature cha)
	{
		_chaObjId = cha.getObjectId();
		_loc = cha.getLoc();
	}

	@Override
	protected final void writeImpl()
	{
		writeC(0x79);

		writeD(_chaObjId);
		writeD(_loc.x);
		writeD(_loc.y);
		writeD(_loc.z);
		writeD(_loc.h);
	}
}
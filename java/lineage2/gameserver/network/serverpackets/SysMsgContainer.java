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

import java.util.ArrayList;
import java.util.List;

import lineage2.gameserver.model.Creature;
import lineage2.gameserver.model.GameObject;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.Skill;
import lineage2.gameserver.model.Summon;
import lineage2.gameserver.model.base.ClassId;
import lineage2.gameserver.model.base.Element;
import lineage2.gameserver.model.entity.residence.Residence;
import lineage2.gameserver.model.instances.DoorInstance;
import lineage2.gameserver.model.instances.NpcInstance;
import lineage2.gameserver.model.instances.StaticObjectInstance;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.network.serverpackets.components.SystemMsg;
import lineage2.gameserver.utils.Location;

/**
 * @author VISTALL
 * @param <T>
 * @date 14:27/08.03.2011
 */
public abstract class SysMsgContainer<T extends SysMsgContainer<T>> extends L2GameServerPacket
{
	public static enum Types
	{
		TEXT, // 0
		NUMBER, // 1
		NPC_NAME, // 2
		ITEM_NAME, // 3
		SKILL_NAME, // 4
		RESIDENCE_NAME, // 5
		LONG, // 6
		ZONE_NAME, // 7
		ITEM_NAME_WITH_AUGMENTATION, // 8
		ELEMENT_NAME, // 9
		INSTANCE_NAME, // 10 d
		STATIC_OBJECT_NAME, // 11
		PLAYER_NAME, // 12 S
		SYSTEM_STRING, // 13 d
		UNK_14,
		CLASS_NAME,
		DAMAGE
	}
	
	protected SystemMsg _message;
	protected List<IArgument> _arguments;
	
	@Deprecated
	protected SysMsgContainer(int messageId)
	{
		this(SystemMsg.valueOf(messageId));
	}
	
	protected SysMsgContainer(SystemMsg message)
	{
		if (message == null)
		{
			throw new IllegalArgumentException("SystemMsg is null");
		}
		
		_message = message;
		_arguments = new ArrayList<>(_message.size());
	}
	
	protected void writeElements()
	{
		if (_message.size() != _arguments.size())
		{
			throw new IllegalArgumentException("Wrong count of arguments: " + _message);
		}
		
		writeD(_message.getId());
		writeD(_arguments.size());
		
		for (IArgument argument : _arguments)
		{
			argument.write(this);
		}
	}
	
	// ==================================================================================================
	public T addName(GameObject object)
	{
		if (object == null)
		{
			return add(new StringArgument(null));
		}
		
		if (object.isNpc())
		{
			return add(new NpcNameArgument(((NpcInstance) object).getId() + 1000000));
		}
		else if (object instanceof Summon)
		{
			return add(new NpcNameArgument(((Summon) object).getId() + 1000000));
		}
		else if (object.isItem())
		{
			return add(new ItemNameArgument(((ItemInstance) object).getId()));
		}
		else if (object.isPlayer())
		{
			return add(new PlayerNameArgument((Player) object));
		}
		else if (object.isDoor())
		{
			return add(new StaticObjectNameArgument(((DoorInstance) object).getDoorId()));
		}
		else if (object instanceof StaticObjectInstance)
		{
			return add(new StaticObjectNameArgument(((StaticObjectInstance) object).getUId()));
		}
		
		return add(new StringArgument(object.getName()));
	}
	
	public T addInstanceName(int id)
	{
		return add(new InstanceNameArgument(id));
	}
	
	public T addSysString(int id)
	{
		return add(new SysStringArgument(id));
	}
	
	public T addClassName(int i)
	{
		return add(new ClassNameArgument(i));
	}
	
	public T addClassName(ClassId i)
	{
		return add(new ClassNameArgument(i.getId()));
	}
	
	public T addSkillName(Skill skill)
	{
		return addSkillName(skill.getDisplayId(), skill.getDisplayLevel());
	}
	
	public T addSkillName(int id, int level)
	{
		return add(new SkillArgument(id, level));
	}
	
	public T addItemName(int item_id)
	{
		return add(new ItemNameArgument(item_id));
	}
	
	public T addItemNameWithAugmentation(ItemInstance item)
	{
		return add(new ItemNameWithAugmentationArgument(item.getId(), item.getAugmentationId()));
	}
	
	public T addZoneName(Creature c)
	{
		return addZoneName(c.getX(), c.getY(), c.getZ());
	}
	
	public T addZoneName(Location loc)
	{
		return add(new ZoneArgument(loc.getX(), loc.getY(), loc.getZ()));
	}
	
	public T addZoneName(int x, int y, int z)
	{
		return add(new ZoneArgument(x, y, z));
	}
	
	public T addResidenceName(Residence r)
	{
		return add(new ResidenceArgument(r.getId()));
	}
	
	public T addResidenceName(int i)
	{
		return add(new ResidenceArgument(i));
	}
	
	public T addElementName(int i)
	{
		return add(new ElementNameArgument(i));
	}
	
	public T addElementName(Element i)
	{
		return add(new ElementNameArgument(i.getId()));
	}
	
	public T addInteger(double i)
	{
		return add(new IntegerArgument((int) i));
	}
	
	public T addLong(long i)
	{
		return add(new LongArgument(i));
	}
	
	public T addString(String t)
	{
		return add(new StringArgument(t));
	}
	
	@SuppressWarnings("unchecked")
	protected T add(IArgument arg)
	{
		_arguments.add(arg);
		return (T) this;
	}
	
	public static abstract class IArgument
	{
		void write(SysMsgContainer<?> m)
		{
			m.writeD(getType().ordinal());
			writeData(m);
		}
		
		abstract Types getType();
		
		abstract void writeData(SysMsgContainer<?> message);
	}
	
	public static class IntegerArgument extends IArgument
	{
		private final int _data;
		
		public IntegerArgument(int da)
		{
			_data = da;
		}
		
		@Override
		public void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_data);
		}
		
		@Override
		Types getType()
		{
			return Types.NUMBER;
		}
	}
	
	public static class NpcNameArgument extends IntegerArgument
	{
		public NpcNameArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.NPC_NAME;
		}
	}
	
	public static class ItemNameArgument extends IntegerArgument
	{
		public ItemNameArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.ITEM_NAME;
		}
	}
	
	public static class ItemNameWithAugmentationArgument extends IArgument
	{
		private final int _itemId;
		private final int _augmentationId;
		
		public ItemNameWithAugmentationArgument(int itemId, int augmentationId)
		{
			_itemId = itemId;
			_augmentationId = augmentationId;
		}
		
		@Override
		Types getType()
		{
			return Types.ITEM_NAME_WITH_AUGMENTATION;
		}
		
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_itemId);
			message.writeD(_augmentationId);
		}
	}
	
	public static class InstanceNameArgument extends IntegerArgument
	{
		public InstanceNameArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.INSTANCE_NAME;
		}
	}
	
	public static class SysStringArgument extends IntegerArgument
	{
		public SysStringArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.SYSTEM_STRING;
		}
	}
	
	public static class ResidenceArgument extends IntegerArgument
	{
		public ResidenceArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.RESIDENCE_NAME;
		}
	}
	
	public static class StaticObjectNameArgument extends IntegerArgument
	{
		public StaticObjectNameArgument(int da)
		{
			super(da);
		}
		
		@Override
		Types getType()
		{
			return Types.STATIC_OBJECT_NAME;
		}
	}
	
	public static class LongArgument extends IArgument
	{
		private final long _data;
		
		public LongArgument(long da)
		{
			_data = da;
		}
		
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeQ(_data);
		}
		
		@Override
		Types getType()
		{
			return Types.LONG;
		}
	}
	
	public static class StringArgument extends IArgument
	{
		private final String _data;
		
		public StringArgument(String da)
		{
			_data = da == null ? "null" : da;
		}
		
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeS(_data);
		}
		
		@Override
		Types getType()
		{
			return Types.TEXT;
		}
	}
	
	public static class SkillArgument extends IArgument
	{
		private final int _skillId;
		private final int _skillLevel;
		
		public SkillArgument(int t1, int t2)
		{
			_skillId = t1;
			_skillLevel = t2;
		}
		
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_skillId);
			message.writeD(_skillLevel);
		}
		
		@Override
		Types getType()
		{
			return Types.SKILL_NAME;
		}
	}
	
	public static class ZoneArgument extends IArgument
	{
		private final int _x;
		private final int _y;
		private final int _z;
		
		public ZoneArgument(int t1, int t2, int t3)
		{
			_x = t1;
			_y = t2;
			_z = t3;
		}
		
		@Override
		void writeData(SysMsgContainer<?> message)
		{
			message.writeD(_x);
			message.writeD(_y);
			message.writeD(_z);
		}
		
		@Override
		Types getType()
		{
			return Types.ZONE_NAME;
		}
	}
	
	public static class ElementNameArgument extends IntegerArgument
	{
		public ElementNameArgument(int type)
		{
			super(type);
		}
		
		@Override
		Types getType()
		{
			return Types.ELEMENT_NAME;
		}
	}
	
	public static class PlayerNameArgument extends StringArgument
	{
		public PlayerNameArgument(Creature creature)
		{
			super(creature.getName());
		}
		
		@Override
		Types getType()
		{
			return Types.PLAYER_NAME;
		}
	}
	
	public static class ClassNameArgument extends SysMsgContainer.IntegerArgument
	{
		public ClassNameArgument(int classId)
		{
			super(classId);
		}
		
		@Override
		SysMsgContainer.Types getType()
		{
			return SysMsgContainer.Types.CLASS_NAME;
		}
	}
}

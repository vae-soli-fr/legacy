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
import java.util.Collections;
import java.util.List;

import lineage2.gameserver.model.quest.QuestNpcLogInfo;
import lineage2.gameserver.model.quest.QuestState;

/**
 * @author VISTALL
 * @date 14:50/26.02.2011
 */
public class ExQuestNpcLogList extends L2GameServerPacket
{
	private int _questId;
	private List<int[]> _logList = Collections.emptyList();
	
	public ExQuestNpcLogList(QuestState state)
	{
		_questId = state.getQuest().getQuestIntId();
		int cond = state.getCond();
		List<QuestNpcLogInfo> vars = state.getQuest().getNpcLogList(cond);
		
		if (vars == null)
		{
			return;
		}
		
		_logList = new ArrayList<>(vars.size());
		
		for (QuestNpcLogInfo entry : vars)
		{
			int[] i = new int[2];
			i[0] = entry.getNpcIds()[0] + 1000000;
			i[1] = state.getInt(entry.getVarName());
			_logList.add(i);
		}
	}
	
	@Override
	protected void writeImpl()
	{
		writeEx(0xC6);
		writeD(_questId);
		writeC(_logList.size());
		
		for (int i = 0; i < _logList.size(); i++)
		{
			int[] values = _logList.get(i);
			writeD(values[0]);
			writeC(0); // npc index?
			writeD(values[1]);
		}
	}
}

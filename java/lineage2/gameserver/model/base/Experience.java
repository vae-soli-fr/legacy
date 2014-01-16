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
package lineage2.gameserver.model.base;

import lineage2.gameserver.Config;

/**
 * @author Mobius
 * @version $Revision: 1.0 $
 */
public class Experience
{
	/**
	 * Field LEVEL.
	 */
	public final static long LEVEL[] =
	{
		-1L,
		0L, // level 0->1
		68L, // level 1->2
		363L, // level 2->3
		1168L, // level 3->4
		2884L, // level 4->5
		6038L, // level 5->6
		11287L, // level 6->7
		19423L, // level 7->8
		31378L, // level 8->9
		48229L, // level 9->10
		71202L, // level 10->11
		101677L, // level 11->12
		141193L, // level 12->13
		191454L, // level 13->14
		254330L, // level 14->15
		331867L, // level 15->16
		426288L, // level 16->17
		540000L, // level 17->18
		675596L, // level 18->19
		835862L, // level 19->20
		920357L, // level 20->21
		1015431L, // level 21->22
		1123336L, // level 22->23
		1246808L, // level 23->24
		1389235L, // level 24->25
		1554904L, // level 25->26
		1749413L, // level 26->27
		1980499L, // level 27->28
		2260321L, // level 28->29
		2634751L, // level 29->30
		2844287L, // level 30->31
		3093068L, // level 31->32
		3389496L, // level 32->33
		3744042L, // level 33->34
		4169902L, // level 34->35
		4683988L, // level 35->36
		5308556L, // level 36->37
		6074376L, // level 37->38
		7029248L, // level 38->39
		8342182L, // level 39->40
		8718976L, // level 40->41
		9289560L, // level 41->42
		9991807L, // level 42->43
		10856075L, // level 43->44
		11920512L, // level 44->45
		13233701L, // level 45->46
		14858961L, // level 46->47
		16882633L, // level 47->48
		19436426L, // level 48->49
		22977080L, // level 49->50
		24605660L, // level 50->51
		26635948L, // level 51->52
		29161263L, // level 52->53
		32298229L, // level 53->54
		36193556L, // level 54->55
		41033917L, // level 55->56
		47093035L, // level 56->57
		54711546L, // level 57->58
		64407353L, // level 58->59
		77947292L, // level 59->60
		85775204L, // level 60->61
		95595386L, // level 61->62
		107869713L, // level 62->63
		123174171L, // level 63->64
		142229446L, // level 64->65
		165944812L, // level 65->66
		195677269L, // level 66->67
		233072222L, // level 67->68
		280603594L, // level 68->69
		335732975L, // level 69->70
		383597045L, // level 70->71
		442752112L, // level 71->72
		516018015L, // level 72->73
		606913902L, // level 73->74
		719832095L, // level 74->75
		860289228L, // level 75->76
		1035327669L, // level 76->77
		1259458516L, // level 77->78
		1534688053L, // level 78->79
		1909610088L, // level 79->80
		2342785974L, // level 80->81
		2861857696L, // level 81->82
		3478378664L, // level 82->83
		4211039578L, // level 83->84
		5078544041L, // level 84->85
		Math.round(10985069426L*1.5), // level 85->86
		Math.round(19192594397L*1.5), // level 86->87
		Math.round(33533938399L*1.5), // level 87->88
		Math.round(43503026615L*1.5), // level 88->89
		Math.round(61895085913L*1.5), // level 89->90
		Math.round(84465260437L*1.5), // level 90->91
		Math.round(112359133751L*1.5), // level 91->92
		Math.round(146853833970L*1.5), // level 92->93
		Math.round(189558054903L*1.5), // level 93->94
		Math.round(242517343994L*1.5), // level 94->95
		Math.round(343490462139L*1.5), // level 95->96
		Math.round(538901012155L*1.5), // level 96->97
		Math.round(923857608218L*1.5), // level 97->98
		Math.round(1701666675991L*1.5), // level 98->99
		Math.round(1801666675991L*1.5) // level 99->100
	};
	
	/**
	 * Method penaltyModifier.
	 * @param count long
	 * @param percents double
	 * @return double
	 */
	public static double penaltyModifier(long count, double percents)
	{
		return Math.max(1. - ((count * percents) / 100), 0);
	}
	
	/**
	 * Method getMaxLevel.
	 * @return int
	 */
	public static int getMaxLevel()
	{
		return Config.ALT_MAX_LEVEL;
	}
	
	/**
	 * Method getMaxSubLevel.
	 * @return int
	 */
	public static int getMaxSubLevel()
	{
		return Config.ALT_MAX_SUB_LEVEL;
	}
	
	/**
	 * Method getLevel.
	 * @param thisExp long
	 * @return int
	 */
	public static int getLevel(long thisExp)
	{
		int level = 0;
		for (int i = 0; i < LEVEL.length; i++)
		{
			long exp = LEVEL[i];
			if (thisExp >= exp)
			{
				level = i;
			}
		}
		return level;
	}
	
	/**
	 * Method getExpForLevel.
	 * @param lvl int
	 * @return long
	 */
	public static long getExpForLevel(int lvl)
	{
		if (lvl >= Experience.LEVEL.length)
		{
			return 0;
		}
		return Experience.LEVEL[lvl];
	}
	
	/**
	 * Method getExpPercent.
	 * @param level int
	 * @param exp long
	 * @return double
	 */
	public static double getExpPercent(int level, long exp)
	{
		return ((exp - getExpForLevel(level)) / ((getExpForLevel(level + 1) - getExpForLevel(level)) / 100.0D)) * 0.01D;
	}
	
	/**
	 * Method getMaxDualLevel.
	 * @return int
	 */
	public static int getMaxDualLevel()
	{
		return Config.ALT_MAX_DUAL_SUB_LEVEL;
	}
}

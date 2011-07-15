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

/**
 * Shows a REAL TIME movie to the player. After it is shown, client
 * sends us EndScenePlayer with the specified scene ID.
 * The client MUST be in the correct position, because the
 * camera's position depend's on current character's position.
 *
 * @author JIV
 */

public class ExStartScenePlayer extends L2GameServerPacket
{
	private static final String _S__FE99_PLAYQUESTMOVIE = "[S] FE:99 ExStartScenePlayer";
	
	private int _movieId;

    // Lindvior
    public static final int LINDVIOR = 1;                       // Lindvior spawns and disappear at Keuceus base	44500ms     Keucereus Alliance Base

    // Ekimus
    public static final int EKIMUS_OPENING = 2;                 // Ekimus intro										62000ms     Heart of Infinity
    public static final int EKIMUS_SUCCESS = 3;                 // Ekimus death										18000ms     Heart of Infinity
    public static final int EKIMUS_FAIL = 4;                    // Ekimus wins										17000ms     Heart of Infinity

    // Tiat
    public static final int TIAT_OPENING = 5;               	// Tiat intro										54200ms     Jinryong's Throne (Seed of Destruction)
    public static final int TIAT_SUCCESS = 6;           		// Tiat death										26100ms     Jinryong's Throne (Seed of Destruction)
    public static final int TIAT_FAIL = 7;                  	// Tiat win											24800ms     Jinryong's Throne (Seed of Destruction)

    // Seven Signs Quests
    public static final int SEVSIGNS_SERIES_OF_DOUBT = 8; 		// Death in mines           						26000ms     no place
    public static final int SEVSIGNS_DYING_MESSAGE = 9;  		// Priest praying									27000ms     no place
    public static final int SEVSIGNS_MAMMONS_CONTRACT = 10;     // Story of Shunaiman								98000ms     no place
    public static final int SEVSIGNS_SECRET_RITUAL_PRIEST = 11; // 4 Priests performing a ritual					30000ms     Sanctum (of the Lords of Dawn) Altar
    public static final int SEVSIGNS_SEAL_EMPEROR_A = 12;       // Request of Anakim							    18000ms     Lilith's Room (Disciple's Necropolis past)
    public static final int SEVSIGNS_SEAL_EMPEROR_B = 13;       // Sealing seal 									26000ms     Lilith's Room (Disciple's Necropolis past)
    public static final int SEVSIGNS_EMBRYO = 14;				// Defeat the vilain    							28000ms     (Sanctum of the Lords of) Dawn Hideout

    // Freya
    public static final int FREYA_OPENING = 15;                 // freya says hello                                 53500ms     Ice Queen Castle
    public static final int FREYA_PHASE_CHANGE_A = 16;          // summon statues                                   21100ms     Ice Queen Castle
    public static final int FREYA_PHASE_CHANGE_B = 17;          // casting heavy ice                                21500ms     Ice Queen Castle
    public static final int KEGOR_INTRUSION = 18;               // support from strangers                           27000ms     Ice Queen Castle
    public static final int FREYA_ENDING_A = 19;                // Freya fall                                       16000ms     Ice Queen Castle
    public static final int FREYA_ENDING_B = 20;                // Sirra take the power                             56000ms     Ice Queen Castle
    public static final int FREYA_FORCED_DEFEAT = 21;           // Freya wins with throne                           21000ms     Ice Queen Castle
    public static final int FREYA_DEFEAT = 22;                  // Freya wins without throne                        20500ms     Ice Queen Castle
    public static final int ICE_HEAVY_KNIGHT_SPAWN = 23;        // Invocated Ice Knight                              7000ms     Ice Queen Castle

    // Airship
    public static final int LANDING_KSERTH_A = 1000; 			// Ship landing on Keuceus base port A              10000ms     Keucereus Alliance Base
    public static final int LANDING_KSERTH_B = 1001; 			// Ship landing on Keuceus base port B				10000ms     Keucereus Alliance Base
    public static final int LANDING_INFINITY = 1002; 			// Ship landing on seed of infinity					10000ms     Infinity Airstrip
    public static final int LANDING_DESTRUCTION = 1003; 		// Ship landing on seed of destruction				10000ms     Destruction Airstrip
    public static final int LANDING_ANNIHILATION = 1004; 		// Ship landing on seed of annihilation				15000ms     Annihilation Airstrip
	
	public ExStartScenePlayer(int id)
	{
		_movieId = id;
	}
	
	@Override
	public void writeImpl()
	{
		writeC(0xfe);
		writeH(0x99);
		writeD(_movieId);
	}
	
	@Override
	public String getType()
	{
		return _S__FE99_PLAYQUESTMOVIE;
	}
}

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
package lineage2.gameserver.model.entity.residence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import lineage2.commons.dao.JdbcEntityState;
import lineage2.commons.dbutils.DbUtils;
import lineage2.commons.math.SafeMath;
import lineage2.gameserver.Announcements;
import lineage2.gameserver.dao.CastleDAO;
import lineage2.gameserver.dao.CastleHiredGuardDAO;
import lineage2.gameserver.dao.ClanDataDAO;
import lineage2.gameserver.data.xml.holder.ManorDataHolder;
import lineage2.gameserver.data.xml.holder.ResidenceHolder;
import lineage2.gameserver.database.DatabaseFactory;
import lineage2.gameserver.instancemanager.CastleManorManager;
import lineage2.gameserver.instancemanager.SpawnManager;
import lineage2.gameserver.model.GameObjectsStorage;
import lineage2.gameserver.model.Player;
import lineage2.gameserver.model.items.ItemInstance;
import lineage2.gameserver.model.items.Warehouse;
import lineage2.gameserver.model.pledge.Clan;
import lineage2.gameserver.network.serverpackets.EventTrigger;
import lineage2.gameserver.network.serverpackets.ExCastleState;
import lineage2.gameserver.network.serverpackets.L2GameServerPacket;
import lineage2.gameserver.network.serverpackets.components.NpcString;
import lineage2.gameserver.templates.StatsSet;
import lineage2.gameserver.templates.item.ItemTemplate;
import lineage2.gameserver.templates.item.support.MerchantGuard;
import lineage2.gameserver.templates.manor.CropProcure;
import lineage2.gameserver.templates.manor.SeedProduction;
import lineage2.gameserver.utils.GameStats;
import lineage2.gameserver.utils.Log;

import org.napile.primitive.maps.IntObjectMap;
import org.napile.primitive.maps.IntObjectMap.Entry;
import org.napile.primitive.maps.impl.CTreeIntObjectMap;
import org.napile.primitive.maps.impl.HashIntObjectMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import gnu.trove.set.TIntSet;
import gnu.trove.set.hash.TIntHashSet;

public class Castle extends Residence
{
	private static final Logger _log = LoggerFactory.getLogger(Castle.class);
	private static final String CASTLE_MANOR_DELETE_PRODUCTION = "DELETE FROM castle_manor_production WHERE castle_id=?;";
	private static final String CASTLE_MANOR_DELETE_PRODUCTION_PERIOD = "DELETE FROM castle_manor_production WHERE castle_id=? AND period=?;";
	private static final String CASTLE_MANOR_DELETE_PROCURE = "DELETE FROM castle_manor_procure WHERE castle_id=?;";
	private static final String CASTLE_MANOR_DELETE_PROCURE_PERIOD = "DELETE FROM castle_manor_procure WHERE castle_id=? AND period=?;";
	private static final String CASTLE_UPDATE_CROP = "UPDATE castle_manor_procure SET can_buy=? WHERE crop_id=? AND castle_id=? AND period=?";
	private static final String CASTLE_UPDATE_SEED = "UPDATE castle_manor_production SET can_produce=? WHERE seed_id=? AND castle_id=? AND period=?";
	private final IntObjectMap<MerchantGuard> _merchantGuards = new HashIntObjectMap<>();
	private final IntObjectMap<List<Fortress>> _relatedFortresses = new CTreeIntObjectMap<>();
	private final IntObjectMap<TIntSet> _relatedFortressesIds = new CTreeIntObjectMap<>();
	private List<CropProcure> _procure;
	private List<SeedProduction> _production;
	private List<CropProcure> _procureNext;
	private List<SeedProduction> _productionNext;
	private boolean _isNextPeriodApproved;
	private int _TaxPercent;
	private double _TaxRate;
	private long _treasury;
	private long _collectedShops;
	private long _collectedSeed;
	private final NpcString _npcStringName;
	private ResidenceSide _residenceSide = ResidenceSide.NEUTRAL;
	private final Set<ItemInstance> _spawnMerchantTickets = new CopyOnWriteArraySet<>();
	
	public Castle(StatsSet set)
	{
		super(set);
		_npcStringName = NpcString.valueOf(1001000 + _id);
	}
	
	@Override
	public void init()
	{
		super.init();
		
		for (Entry<TIntSet> entry : _relatedFortressesIds.entrySet())
		{
			_relatedFortresses.remove(entry.getKey());
			TIntSet list = entry.getValue();
			List<Fortress> list2 = new ArrayList<>(list.size());
			
			for (int i : list.toArray())
			{
				Fortress fortress = ResidenceHolder.getInstance().getResidence(Fortress.class, i);
				
				if (fortress == null)
				{
					continue;
				}
				
				list2.add(fortress);
				fortress.addRelatedCastle(this);
			}
			
			_relatedFortresses.put(entry.getKey(), list2);
		}
		
		broadcastResidenceState();
	}
	
	@Override
	public ResidenceType getType()
	{
		return ResidenceType.Castle;
	}
	
	// This method sets the castle owner; null here means give it back to NPC
	@Override
	public void changeOwner(Clan newOwner)
	{
		if (newOwner != null)
		{
			if (newOwner.getHasFortress() != 0)
			{
				Fortress oldFortress = ResidenceHolder.getInstance().getResidence(Fortress.class, newOwner.getHasFortress());
				
				if (oldFortress != null)
				{
					oldFortress.changeOwner(null);
				}
			}
			
			if (newOwner.getCastle() != 0)
			{
				Castle oldCastle = ResidenceHolder.getInstance().getResidence(Castle.class, newOwner.getCastle());
				
				if (oldCastle != null)
				{
					oldCastle.changeOwner(null);
				}
			}
		}
		
		Clan oldOwner = null;
		
		if ((getOwnerId() > 0) && ((newOwner == null) || (newOwner.getClanId() != getOwnerId())))
		{
			removeSkills();
			setTaxPercent(null, 0);
			cancelCycleTask();
			oldOwner = getOwner();
			
			if (oldOwner != null)
			{
				long amount = getTreasury();
				
				if (amount > 0)
				{
					Warehouse warehouse = oldOwner.getWarehouse();
					
					if (warehouse != null)
					{
						warehouse.addItem(ItemTemplate.ITEM_ID_ADENA, amount);
						addToTreasuryNoTax(-amount, false, false);
						Log.add(getName() + "|" + -amount + "|Castle:changeOwner", "treasury");
					}
				}
				
				for (Player clanMember : oldOwner.getOnlineMembers(0))
				{
					if ((clanMember != null) && (clanMember.getInventory() != null))
					{
						clanMember.getInventory().validateItems();
					}
				}
				
				oldOwner.setHasCastle(0);
			}
		}
		
		if (newOwner != null)
		{
			newOwner.setHasCastle(getId());
		}
		
		updateOwnerInDB(newOwner);
		rewardSkills();
		update();
	}
	
	// This method loads castle
	@Override
	protected void loadData()
	{
		_TaxPercent = 0;
		_TaxRate = 0;
		_treasury = 0;
		_procure = new ArrayList<>();
		_production = new ArrayList<>();
		_procureNext = new ArrayList<>();
		_productionNext = new ArrayList<>();
		_isNextPeriodApproved = false;
		_owner = ClanDataDAO.getInstance().getOwner(this);
		CastleDAO.getInstance().select(this);
		CastleHiredGuardDAO.getInstance().load(this);
	}
	
	public void setTaxPercent(int p)
	{
		_TaxPercent = Math.min(Math.max(0, p), 100);
		_TaxRate = _TaxPercent / 100.0;
	}
	
	public void setTreasury(long t)
	{
		_treasury = t;
	}
	
	private void updateOwnerInDB(Clan clan)
	{
		_owner = clan; // Update owner id property
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement("UPDATE clan_data SET hasCastle=0 WHERE hasCastle=? LIMIT 1");
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			
			if (clan != null)
			{
				statement = con.prepareStatement("UPDATE clan_data SET hasCastle=? WHERE clan_id=? LIMIT 1");
				statement.setInt(1, getId());
				statement.setInt(2, getOwnerId());
				statement.execute();
				clan.broadcastClanStatus(true, false, false);
			}
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public int getTaxPercent()
	{
		if (_TaxPercent > 30)
		{
			_TaxPercent = 30;
		}
		
		return _TaxPercent;
	}
	
	public int getTaxPercent0()
	{
		return _TaxPercent;
	}
	
	public long getCollectedShops()
	{
		return _collectedShops;
	}
	
	public long getCollectedSeed()
	{
		return _collectedSeed;
	}
	
	public void setCollectedShops(long value)
	{
		_collectedShops = value;
	}
	
	public void setCollectedSeed(long value)
	{
		_collectedSeed = value;
	}
	
	// This method add to the treasury
	/**
	 * Add amount to castle instance's treasury (warehouse).
	 * @param amount
	 * @param shop
	 * @param seed
	 */
	public void addToTreasury(long amount, boolean shop, boolean seed)
	{
		if (getOwnerId() <= 0)
		{
			return;
		}
		
		if (amount == 0)
		{
			return;
		}
		
		if ((amount > 1) && (_id != 5) && (_id != 8)) // If current castle instance is not Aden or Rune
		{
			Castle royal = ResidenceHolder.getInstance().getResidence(Castle.class, _id >= 7 ? 8 : 5);
			
			if (royal != null)
			{
				long royalTax = (long) (amount * royal.getTaxRate()); // Find out what royal castle gets from the current castle instance's income
				
				if (royal.getOwnerId() > 0)
				{
					royal.addToTreasury(royalTax, shop, seed); // Only bother to really add the tax to the treasury if not npc owned
					
					if (_id == 5)
					{
						Log.add("Aden|" + royalTax + "|Castle:adenTax", "treasury");
					}
					else if (_id == 8)
					{
						Log.add("Rune|" + royalTax + "|Castle:runeTax", "treasury");
					}
				}
				
				amount -= royalTax; // Subtract royal castle income from current castle instance's income
			}
		}
		
		addToTreasuryNoTax(amount, shop, seed);
	}
	
	/**
	 * Add amount to castle instance's treasury (warehouse), no tax paying.
	 * @param amount
	 * @param shop
	 * @param seed
	 */
	public void addToTreasuryNoTax(long amount, boolean shop, boolean seed)
	{
		if (getOwnerId() <= 0)
		{
			return;
		}
		
		if (amount == 0)
		{
			return;
		}
		
		GameStats.addAdena(amount);
		// Add to the current treasury total. Use "-" to substract from treasury
		_treasury = SafeMath.addAndLimit(_treasury, amount);
		
		if (shop)
		{
			_collectedShops += amount;
		}
		
		if (seed)
		{
			_collectedSeed += amount;
		}
		
		setJdbcState(JdbcEntityState.UPDATED);
		update();
	}
	
	public int getCropRewardType(int crop)
	{
		int rw = 0;
		
		for (CropProcure cp : _procure)
		{
			if (cp.getId() == crop)
			{
				rw = cp.getReward();
			}
		}
		
		return rw;
	}
	
	// This method updates the castle tax rate
	public void setTaxPercent(Player activeChar, int taxPercent)
	{
		setTaxPercent(taxPercent);
		setJdbcState(JdbcEntityState.UPDATED);
		update();
		
		if (activeChar != null)
		{
			activeChar.sendMessage(getName() + " castle tax changed to " + taxPercent + "%.");
		}
	}
	
	public double getTaxRate()
	{
		if (_TaxRate > 0.15)
		{
			_TaxRate = 0.15;
		}
		
		return _TaxRate;
	}
	
	public long getTreasury()
	{
		return _treasury;
	}
	
	public List<SeedProduction> getSeedProduction(int period)
	{
		return period == CastleManorManager.PERIOD_CURRENT ? _production : _productionNext;
	}
	
	public List<CropProcure> getCropProcure(int period)
	{
		return period == CastleManorManager.PERIOD_CURRENT ? _procure : _procureNext;
	}
	
	public void setSeedProduction(List<SeedProduction> seed, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_production = seed;
		}
		else
		{
			_productionNext = seed;
		}
	}
	
	public void setCropProcure(List<CropProcure> crop, int period)
	{
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			_procure = crop;
		}
		else
		{
			_procureNext = crop;
		}
	}
	
	public synchronized SeedProduction getSeed(int seedId, int period)
	{
		for (SeedProduction seed : getSeedProduction(period))
		{
			if (seed.getId() == seedId)
			{
				return seed;
			}
		}
		
		return null;
	}
	
	public synchronized CropProcure getCrop(int cropId, int period)
	{
		for (CropProcure crop : getCropProcure(period))
		{
			if (crop.getId() == cropId)
			{
				return crop;
			}
		}
		
		return null;
	}
	
	public long getManorCost(int period)
	{
		List<CropProcure> procure;
		List<SeedProduction> production;
		
		if (period == CastleManorManager.PERIOD_CURRENT)
		{
			procure = _procure;
			production = _production;
		}
		else
		{
			procure = _procureNext;
			production = _productionNext;
		}
		
		long total = 0;
		
		if (production != null)
		{
			for (SeedProduction seed : production)
			{
				total += ManorDataHolder.getInstance().getSeedBuyPrice(seed.getId()) * seed.getStartProduce();
			}
		}
		
		if (procure != null)
		{
			for (CropProcure crop : procure)
			{
				total += crop.getPrice() * crop.getStartAmount();
			}
		}
		
		return total;
	}
	
	// Save manor production data
	public void saveSeedData()
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION);
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			
			if (_production != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_production.size()];
				
				for (SeedProduction s : _production)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
			
			if (_productionNext != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[_productionNext.size()];
				
				for (SeedProduction s : _productionNext)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	// Save manor production data for specified period
	public void saveSeedData(int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PRODUCTION_PERIOD);
			statement.setInt(1, getId());
			statement.setInt(2, period);
			statement.execute();
			DbUtils.close(statement);
			List<SeedProduction> prod = null;
			prod = getSeedProduction(period);
			
			if (prod != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_production VALUES ";
				String values[] = new String[prod.size()];
				
				for (SeedProduction s : prod)
				{
					values[count] = "(" + getId() + "," + s.getId() + "," + s.getCanProduce() + "," + s.getStartProduce() + "," + s.getPrice() + "," + period + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	// Save crop procure data
	public void saveCropData()
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE);
			statement.setInt(1, getId());
			statement.execute();
			DbUtils.close(statement);
			
			if (_procure != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procure.size()];
				
				for (CropProcure cp : _procure)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_CURRENT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
			
			if (_procureNext != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[_procureNext.size()];
				
				for (CropProcure cp : _procureNext)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + CastleManorManager.PERIOD_NEXT + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	// Save crop procure data for specified period
	public void saveCropData(int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_MANOR_DELETE_PROCURE_PERIOD);
			statement.setInt(1, getId());
			statement.setInt(2, period);
			statement.execute();
			DbUtils.close(statement);
			List<CropProcure> proc = null;
			proc = getCropProcure(period);
			
			if (proc != null)
			{
				int count = 0;
				String query = "INSERT INTO castle_manor_procure VALUES ";
				String values[] = new String[proc.size()];
				
				for (CropProcure cp : proc)
				{
					values[count] = "(" + getId() + "," + cp.getId() + "," + cp.getAmount() + "," + cp.getStartAmount() + "," + cp.getPrice() + "," + cp.getReward() + "," + period + ")";
					count++;
				}
				
				if (values.length > 0)
				{
					query += values[0];
					
					for (int i = 1; i < values.length; i++)
					{
						query += "," + values[i];
					}
					
					statement = con.prepareStatement(query);
					statement.execute();
					DbUtils.close(statement);
				}
			}
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public void updateCrop(int cropId, long amount, int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_UPDATE_CROP);
			statement.setLong(1, amount);
			statement.setInt(2, cropId);
			statement.setInt(3, getId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("Error adding crop data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public void updateSeed(int seedId, long amount, int period)
	{
		Connection con = null;
		PreparedStatement statement = null;
		
		try
		{
			con = DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(CASTLE_UPDATE_SEED);
			statement.setLong(1, amount);
			statement.setInt(2, seedId);
			statement.setInt(3, getId());
			statement.setInt(4, period);
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("Error adding seed production data for castle " + getName() + "!", e);
		}
		finally
		{
			DbUtils.closeQuietly(con, statement);
		}
	}
	
	public boolean isNextPeriodApproved()
	{
		return _isNextPeriodApproved;
	}
	
	public void setNextPeriodApproved(boolean val)
	{
		_isNextPeriodApproved = val;
	}
	
	public void addRelatedFortress(int type, int fortress)
	{
		TIntSet fortresses = _relatedFortressesIds.get(type);
		
		if (fortresses == null)
		{
			_relatedFortressesIds.put(type, fortresses = new TIntHashSet());
		}
		
		fortresses.add(fortress);
	}
	
	public int getDomainFortressContract()
	{
		List<Fortress> list = _relatedFortresses.get(Fortress.DOMAIN);
		
		if (list == null)
		{
			return 0;
		}
		
		for (Fortress f : list)
		{
			if ((f.getContractState() == Fortress.CONTRACT_WITH_CASTLE) && (f.getCastleId() == getId()))
			{
				return f.getId();
			}
		}
		
		return 0;
	}
	
	@Override
	public void update()
	{
		CastleDAO.getInstance().update(this);
	}
	
	public NpcString getNpcStringName()
	{
		return _npcStringName;
	}
	
	public IntObjectMap<List<Fortress>> getRelatedFortresses()
	{
		return _relatedFortresses;
	}
	
	public void addMerchantGuard(MerchantGuard merchantGuard)
	{
		_merchantGuards.put(merchantGuard.getItemId(), merchantGuard);
	}
	
	public MerchantGuard getMerchantGuard(int itemId)
	{
		return _merchantGuards.get(itemId);
	}
	
	public IntObjectMap<MerchantGuard> getMerchantGuards()
	{
		return _merchantGuards;
	}
	
	public Set<ItemInstance> getSpawnMerchantTickets()
	{
		return _spawnMerchantTickets;
	}
	
	@Override
	public void startCycleTask()
	{
	}
	
	public void setResidenceSide(ResidenceSide side)
	{
		_residenceSide = side;
		setJdbcState(JdbcEntityState.UPDATED);
		update();
	}
	
	public ResidenceSide getResidenceSide()
	{
		return _residenceSide;
	}
	
	public boolean isCastleTypeLight()
	{
		return (getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0);
	}
	
	public void broadcastResidenceState()
	{
		L2GameServerPacket trigger = new ExCastleState(this);
		
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}
		
		Announcements.getInstance().announceToAll(new ExCastleState(this));
		broadcastResidenceSupport();
		// int n = 1;
	}
	
	public void broadcastResidenceSupport()
	{
		if (getId() == 1)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Gludio Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_gludio");
				SpawnManager.getInstance().spawn("spawn_light_gludio");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Gludio Castle is on Light Side");
			}
			else
			{
				setResidenceSide(ResidenceSide.DARK);
				Announcements.getInstance().announceToAll("The Gludio Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_gludio");
				SpawnManager.getInstance().despawn("spawn_light_gludio");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Gludio Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 2)
		{
			//
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Dion Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_dion");
				SpawnManager.getInstance().spawn("spawn_light_dion");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Dion Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Dion Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_dion");
				SpawnManager.getInstance().despawn("spawn_light_dion");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Dion Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 3)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Giran Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_giran");
				SpawnManager.getInstance().spawn("spawn_light_giran");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Giran Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Giran Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_giran");
				SpawnManager.getInstance().despawn("spawn_light_giran");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Giran Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 4)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Oren Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_oren");
				SpawnManager.getInstance().spawn("spawn_light_oren");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Oren Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Oren Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_oren");
				SpawnManager.getInstance().despawn("spawn_light_oren");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Oren Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 5)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Aden Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_aden");
				SpawnManager.getInstance().spawn("spawn_light_aden");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Aden Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Aden Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_aden");
				SpawnManager.getInstance().despawn("spawn_light_aden");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Aden Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 6)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Innadril Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_heine");
				SpawnManager.getInstance().spawn("spawn_light_heine");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Innadril Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Innadril Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_heine");
				SpawnManager.getInstance().despawn("spawn_light_heine");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Innadril Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 7)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Schuttgart Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_shutgart");
				SpawnManager.getInstance().spawn("spawn_light_shutgart");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Schuttgart Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Schuttgart Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_shutgart");
				SpawnManager.getInstance().despawn("spawn_light_shutgart");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Schuttgart Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 8)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Rune Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_rune");
				SpawnManager.getInstance().spawn("spawn_light_rune");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Rune Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Rune Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_rune");
				SpawnManager.getInstance().despawn("spawn_light_rune");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Rune Castle is on Dark Side");
			}
			
			return;
		}
		
		if (getId() == 9)
		{
			if ((getResidenceSide().ordinal() == 1) || (getResidenceSide().ordinal() == 0))
			{
				setResidenceSide(ResidenceSide.LIGHT);
				Announcements.getInstance().announceToAll("The Goddard Castle went to the Light side");
				SpawnManager.getInstance().despawn("spawn_dark_goddart");
				SpawnManager.getInstance().spawn("spawn_light_goddart");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 0);
				_log.info("Territory of Goddard Castle is on Light Side");
			}
			else
			{
				Announcements.getInstance().announceToAll("The Goddard Castle went to the Dark side");
				SpawnManager.getInstance().spawn("spawn_dark_goddart");
				SpawnManager.getInstance().despawn("spawn_light_goddart");
				// broadcastPacket(getResidenceSide().ordinal(),true,false);
				setTaxPercent(null, 30);
				_log.info("Territory of Goddard Castle is on Dark Side");
			}
			
			return;
		}
	}
	
	public void broadcastPacket(int value, boolean b, boolean message)
	{
		// _value = value;
		// _b = b;
		// _message = message;
		L2GameServerPacket trigger = new EventTrigger(value, b);
		
		for (Player player : GameObjectsStorage.getAllPlayersForIterate())
		{
			player.sendPacket(trigger);
		}
	}
}
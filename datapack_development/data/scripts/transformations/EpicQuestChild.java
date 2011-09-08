package transformations;

import com.l2jserver.gameserver.datatables.SkillTable;
import com.l2jserver.gameserver.instancemanager.TransformationManager;
import com.l2jserver.gameserver.model.L2Transformation;

/**
 * La transformation est aussi utilisée en RP par les joueurs
 */

public class EpicQuestChild extends L2Transformation
{
	private static final int[] SKILLS = { 5437, 960, 619 };
	
	public EpicQuestChild()
	{
		// id, colRadius, colHeight
		super(112, 5, 12.3);
	}
	
	@Override
	public void onTransform()
	{
		if (getPlayer().getTransformationId() != 112 || getPlayer().isCursedWeaponEquipped())
			return;
		
		transformedSkills();
	}
	
	public void transformedSkills()
	{
		// Dissonance
		getPlayer().addSkill(SkillTable.getInstance().getInfo(5437, 1), false);
		// Race Running
		getPlayer().addSkill(SkillTable.getInstance().getInfo(960, 1), false);
                // Transform Dispel
		getPlayer().addSkill(SkillTable.getInstance().getInfo(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(SKILLS);
	}
	
	@Override
	public void onUntransform()
	{
		removeSkills();
	}
	
	public void removeSkills()
	{
		// Dissonance
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(5437, 1), false);
		// Race Running
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(960, 1), false);
                // Transform Dispel
		getPlayer().removeSkill(SkillTable.getInstance().getInfo(619, 1), false);
		
		getPlayer().setTransformAllowedSkills(EMPTY_ARRAY);
	}
	
	public static void main(String[] args)
	{
		TransformationManager.getInstance().registerTransformation(new EpicQuestChild());
	}
}

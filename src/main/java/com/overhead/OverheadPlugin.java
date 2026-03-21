package com.overhead;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@PluginDescriptor(
		name = "All Overhead Prayers",
		description = "Places of the active prayers on top of your head."
)
public class OverheadPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private OverheadOverlay overlay;



	public Map<Prayer, BufferedImage> prayerImages;

	@Override
	protected void startUp() throws Exception
	{

		prayerImages = Map.ofEntries(
				Map.entry(Prayer.AUGURY,ImageUtil.loadImageResource(getClass(),"/Augury.png")),
				Map.entry(Prayer.BURST_OF_STRENGTH,ImageUtil.loadImageResource(getClass(),"/Burst_of_Strength.png")),
				Map.entry(Prayer.CHIVALRY,ImageUtil.loadImageResource(getClass(),"/Chivalry.png")),
				Map.entry(Prayer.CLARITY_OF_THOUGHT,ImageUtil.loadImageResource(getClass(),"/Clarity_of_Thought.png")),
				Map.entry(Prayer.DEADEYE,ImageUtil.loadImageResource(getClass(),"/Deadeye.png")),
				Map.entry(Prayer.EAGLE_EYE,ImageUtil.loadImageResource(getClass(),"/Eagle_Eye.png")),
				Map.entry(Prayer.HAWK_EYE,ImageUtil.loadImageResource(getClass(),"/Hawk_Eye.png")),
				Map.entry(Prayer.IMPROVED_REFLEXES,ImageUtil.loadImageResource(getClass(),"/Improved_Reflexes.png")),
				Map.entry(Prayer.INCREDIBLE_REFLEXES,ImageUtil.loadImageResource(getClass(),"/Incredible_Reflexes.png")),
				Map.entry(Prayer.MYSTIC_LORE,ImageUtil.loadImageResource(getClass(),"/Mystic_Lore.png")),
				Map.entry(Prayer.MYSTIC_MIGHT,ImageUtil.loadImageResource(getClass(),"/Mystic_Might.png")),
				Map.entry(Prayer.MYSTIC_VIGOUR,ImageUtil.loadImageResource(getClass(),"/Mystic_Vigour.png")),
				Map.entry(Prayer.MYSTIC_WILL,ImageUtil.loadImageResource(getClass(),"/Mystic_Will.png")),
				Map.entry(Prayer.PIETY,ImageUtil.loadImageResource(getClass(),"/Piety.png")),
				Map.entry(Prayer.PRESERVE,ImageUtil.loadImageResource(getClass(),"/Preserve.png")),
				Map.entry(Prayer.RAPID_RESTORE,ImageUtil.loadImageResource(getClass(),"/Rapid_Restore.png")),
				Map.entry(Prayer.RAPID_HEAL,ImageUtil.loadImageResource(getClass(),"/Rapid_Heal.png")),
				Map.entry(Prayer.PROTECT_ITEM,ImageUtil.loadImageResource(getClass(),"/Protect_Item.png")),
				Map.entry(Prayer.RIGOUR,ImageUtil.loadImageResource(getClass(),"/Rigour.png")),
				Map.entry(Prayer.ROCK_SKIN,ImageUtil.loadImageResource(getClass(),"/Rock_Skin.png")),
				Map.entry(Prayer.SHARP_EYE,ImageUtil.loadImageResource(getClass(),"/Sharp_Eye.png")),
				Map.entry(Prayer.STEEL_SKIN,ImageUtil.loadImageResource(getClass(),"/Steel_Skin.png")),
				Map.entry(Prayer.SUPERHUMAN_STRENGTH,ImageUtil.loadImageResource(getClass(),"/Superhuman_Strength.png")),
				Map.entry(Prayer.THICK_SKIN,ImageUtil.loadImageResource(getClass(),"/Thick_Skin.png"))


		);
		overlayManager.add(overlay);

	}

	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(overlay);
	}

	private final Prayer[] prayersToCheck = new Prayer[]{
			Prayer.AUGURY,Prayer.BURST_OF_STRENGTH,Prayer.CHIVALRY,Prayer.CLARITY_OF_THOUGHT,
			Prayer.IMPROVED_REFLEXES,Prayer.INCREDIBLE_REFLEXES,Prayer.MYSTIC_LORE,
			Prayer.MYSTIC_WILL,Prayer.PIETY,Prayer.PRESERVE,Prayer.PROTECT_ITEM,Prayer.RAPID_HEAL,
			Prayer.RAPID_RESTORE,Prayer.RIGOUR,Prayer.ROCK_SKIN,Prayer.SHARP_EYE,Prayer.STEEL_SKIN,
			Prayer.SUPERHUMAN_STRENGTH,Prayer.THICK_SKIN,Prayer.ULTIMATE_STRENGTH
	};

	public List<Prayer> activePrayers = new ArrayList<>();
	@Subscribe
	public void onGameTick(GameTick event){
		List<Prayer> tempActivePrayers = new ArrayList<>();
		// Deadeye/Mytic Might/Eagle Eye/Mistic Vigour is not supported by isPrayerActive so I have to manually do this to check.
		boolean deadeyeUnlocked = client.getVarbitValue(Varbits.PRAYER_DEADEYE_UNLOCKED) == 1;

		boolean rangedPrayerActive = client.getVarbitValue(Varbits.PRAYER_EAGLE_EYE) == 1;

		if (rangedPrayerActive)
		{
			if (deadeyeUnlocked)
			{
				// it's Deadeye
				tempActivePrayers.add(Prayer.DEADEYE);
			}
			else
			{
				tempActivePrayers.add(Prayer.EAGLE_EYE);
			}
		}

		boolean vigourUnlocked = client.getVarbitValue(Varbits.PRAYER_MYSTIC_VIGOUR_UNLOCKED) == 1;

		boolean magePrayerActive = client.getVarbitValue(Varbits.PRAYER_MYSTIC_VIGOUR) == 1;

		if (magePrayerActive)
		{
			if (vigourUnlocked)
			{
				// it's Deadeye
				tempActivePrayers.add(Prayer.MYSTIC_VIGOUR);
			}
			else
			{
				tempActivePrayers.add(Prayer.MYSTIC_MIGHT);
			}
		}

		for(Prayer p : prayersToCheck){
			if(client.isPrayerActive(p)){
				tempActivePrayers.add(p);
			}
		}
		var deletingActivePrayers = activePrayers;
		activePrayers = tempActivePrayers;
		deletingActivePrayers.clear();
	}

}

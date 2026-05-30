package com.overhead;

import javax.inject.Inject;

import com.google.inject.Provides;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.GameTick;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.ImageUtil;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	@Inject
	private OverheadConfig config;

	@Provides
	OverheadConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OverheadConfig.class);
	}

	public Map<String, Prayer> prayerNames;

	public Map<Prayer, BufferedImage[]> prayerImages;

	@Override
	protected void startUp() throws Exception
	{

		prayerImages = Map.ofEntries(
				Map.entry(Prayer.AUGURY, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Augury.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Augury.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Augury.png")
				}),
				Map.entry(Prayer.BURST_OF_STRENGTH, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Burst_of_Strength.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Burst_of_Strength.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Burst_of_Strength.png")
				}),
				Map.entry(Prayer.CHIVALRY, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Chivalry.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Chivalry.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Chivalry.png")
				}),
				Map.entry(Prayer.CLARITY_OF_THOUGHT, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Clarity_of_Thought.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Clarity_of_Thought.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Clarity_of_Thought.png")
				}),
				Map.entry(Prayer.DEADEYE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Deadeye.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Deadeye.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Deadeye.png")
				}),
				Map.entry(Prayer.EAGLE_EYE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Eagle_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Eagle_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Eagle_Eye.png")
				}),
				Map.entry(Prayer.HAWK_EYE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Hawk_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Hawk_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Hawk_Eye.png")
				}),
				Map.entry(Prayer.IMPROVED_REFLEXES, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Improved_Reflexes.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Improved_Reflexes.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Improved_Reflexes.png")
				}),
				Map.entry(Prayer.INCREDIBLE_REFLEXES, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Incredible_Reflexes.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Incredible_Reflexes.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Incredible_Reflexes.png")
				}),
				Map.entry(Prayer.MYSTIC_LORE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Mystic_Lore.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Mystic_Lore.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Mystic_Lore.png")
				}),
				Map.entry(Prayer.MYSTIC_MIGHT, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Mystic_Might.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Mystic_Might.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Mystic_Might.png")
				}),
				Map.entry(Prayer.MYSTIC_VIGOUR, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Mystic_Vigour.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Mystic_Vigour.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Mystic_Vigour.png")
				}),
				Map.entry(Prayer.MYSTIC_WILL, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Mystic_Will.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Mystic_Will.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Mystic_Will.png")
				}),
				Map.entry(Prayer.PIETY, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Piety.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Piety.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Piety.png")
				}),
				Map.entry(Prayer.PRESERVE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Preserve.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Preserve.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Preserve.png")
				}),
				Map.entry(Prayer.RAPID_RESTORE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Rapid_Restore.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Rapid_Restore.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Rapid_Restore.png")
				}),
				Map.entry(Prayer.RAPID_HEAL, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Rapid_Heal.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Rapid_Heal.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Rapid_Heal.png")
				}),
				Map.entry(Prayer.PROTECT_ITEM, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Protect_Item.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Protect_Item.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Protect_Item.png")
				}),
				Map.entry(Prayer.RIGOUR, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Rigour.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Rigour.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Rigour.png")
				}),
				Map.entry(Prayer.ROCK_SKIN, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Rock_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Rock_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Rock_Skin.png")
				}),
				Map.entry(Prayer.SHARP_EYE, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Sharp_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Sharp_Eye.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Sharp_Eye.png")
				}),
				Map.entry(Prayer.STEEL_SKIN, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Steel_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Steel_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Steel_Skin.png")
				}),
				Map.entry(Prayer.SUPERHUMAN_STRENGTH, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Superhuman_Strength.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Superhuman_Strength.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Superhuman_Strength.png")
				}),
				Map.entry(Prayer.THICK_SKIN, new BufferedImage[]{
						ImageUtil.loadImageResource(getClass(), "/big/Thick_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/medium/Thick_Skin.png"),
						ImageUtil.loadImageResource(getClass(), "/small/Thick_Skin.png")
				})
		);
		prayerNames = Map.ofEntries(
				Map.entry("augury", Prayer.AUGURY),
				Map.entry("burst of strength", Prayer.BURST_OF_STRENGTH),
				Map.entry("chivalry", Prayer.CHIVALRY),
				Map.entry("clarity of thought", Prayer.CLARITY_OF_THOUGHT),
				Map.entry("deadeye", Prayer.DEADEYE),
				Map.entry("eagle eye", Prayer.EAGLE_EYE),
				Map.entry("hawk eye", Prayer.HAWK_EYE),
				Map.entry("improved reflexes", Prayer.IMPROVED_REFLEXES),
				Map.entry("incredible reflexes", Prayer.INCREDIBLE_REFLEXES),
				Map.entry("mystic lore", Prayer.MYSTIC_LORE),
				Map.entry("mystic might", Prayer.MYSTIC_MIGHT),
				Map.entry("mystic vigour", Prayer.MYSTIC_VIGOUR),
				Map.entry("mystic will", Prayer.MYSTIC_WILL),
				Map.entry("piety", Prayer.PIETY),
				Map.entry("preserve", Prayer.PRESERVE),
				Map.entry("rapid restore", Prayer.RAPID_RESTORE),
				Map.entry("rapid heal", Prayer.RAPID_HEAL),
				Map.entry("protect item", Prayer.PROTECT_ITEM),
				Map.entry("rigour", Prayer.RIGOUR),
				Map.entry("rock skin", Prayer.ROCK_SKIN),
				Map.entry("sharp eye", Prayer.SHARP_EYE),
				Map.entry("steel skin", Prayer.STEEL_SKIN),
				Map.entry("superhuman strength", Prayer.SUPERHUMAN_STRENGTH),
				Map.entry("thick skin", Prayer.THICK_SKIN)
		);
		updatePrayersToFilter();
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
			Prayer.SUPERHUMAN_STRENGTH,Prayer.THICK_SKIN,Prayer.ULTIMATE_STRENGTH,Prayer.HAWK_EYE
	};

	private List<Prayer> prayersToFilter = new ArrayList<>();

	private void updatePrayersToFilter(){
		String text = config.filterPrayers()
				.replace("\r\n", "\n")
				.replace('\r', '\n');

		prayersToFilter = Arrays.stream(text.split("[,\n]+"))
				.map(String::trim)
				.map(String::toLowerCase)
				.filter(s -> !s.isEmpty())
				.map(prayerNames::get)
				.collect(Collectors.toList());
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("alloverheadprayers"))
		{
			return;
		}

		if (event.getKey().equals("filterPrayers"))
		{
			updatePrayersToFilter();
		}
	}



	public List<Prayer> activePrayers = new ArrayList<>();
	@Subscribe
	public void onGameTick(GameTick event){
		List<Prayer> tempActivePrayers = new ArrayList<>();
		// Deadeye/Mytic Might/Eagle Eye/Mistic Vigour is not supported by isPrayerActive, so I have to manually do this to check.
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
			if(client.isPrayerActive(p) && !prayersToFilter.contains(p)){
				tempActivePrayers.add(p);
			}
		}
		var deletingActivePrayers = activePrayers;
		activePrayers = tempActivePrayers;
		deletingActivePrayers.clear();
	}

}

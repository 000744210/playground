package com.overhead;

import net.runelite.client.config.*;

@ConfigGroup("alloverheadprayers")
public interface OverheadConfig extends Config
{
    enum PrayerIconSize
    {
        BIG,
        MEDIUM,
        SMALL
    }

    @ConfigItem(
            keyName = "filterPrayers",
            name = "Hide prayer icons by name",
            description = "Type the full name of the prayer. Use commas or lines to separate prayer names."
    )
    default String filterPrayers()
    {
        return "";
    }


    @ConfigItem(
            keyName = "iconSize",
            name = "Icon Size",
            description = "Select the prayer icon size"
    )
    default PrayerIconSize iconSize()
    {
        return PrayerIconSize.MEDIUM;
    }


}
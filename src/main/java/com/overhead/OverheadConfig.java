package com.overhead;

import net.runelite.client.config.*;

@ConfigGroup("alloverheadprayers")
public interface OverheadConfig extends Config
{


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
            keyName = "showSmallIcons",
            name = "Use Small Icons",
            description = "Uses smaller prayer icons instead of full-size ones"
    )
    default boolean showSmallIcons()
    {
        return true;
    }


}
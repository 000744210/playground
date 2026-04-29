package com.overhead;

import java.awt.*;
import java.awt.image.BufferedImage;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;

import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

@Slf4j
public class OverheadOverlay extends Overlay
{

    private final Client client;
    private final OverheadPlugin plugin;




    @Inject
    public OverheadOverlay(Client client, OverheadPlugin plugin)
    {
        this.client = client;
        this.plugin = plugin;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.UNDER_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Player player = client.getLocalPlayer();

        LocalPoint lp = player.getLocalLocation();

        boolean isSkulled = client.getLocalPlayer().getSkullIcon() != -1;

        // Adjust height in 3D space
        int zOffset = player.getLogicalHeight()+24; // tweak this

        Point point = Perspective.localToCanvas(
                client,
                lp,
                client.getPlane(),
                zOffset
        );

        if (point == null)
            return null;

        boolean isOverheadActive = player.getOverheadIcon() != null;

        int[] overheadActivePattern = new int[] {-30,+30,-60,+60,-90,+90,-120,+120};
        int[] noOverheadAndEvenPattern = new int[] {-15,+15,-45,+45,-75,+75,-105,+105};
        int[] noOverheadAndOddPattern = new int[] {0,-30,+30,-60,+60,-90,+90,-120,+120};

        int[] chosenPattern = null;

        if(isOverheadActive){
            chosenPattern = overheadActivePattern;
        }else if(plugin.activePrayers.size() % 2 == 0){
            chosenPattern = noOverheadAndEvenPattern;
        }else{
            chosenPattern = noOverheadAndOddPattern;
        }


        boolean isOverheadTextActive = player.getOverheadText() != null;

        for(int i = 0;i<plugin.activePrayers.size();i++){
            Prayer prayer = plugin.activePrayers.get(i);
            BufferedImage image = plugin.prayerImages.get(prayer);

            int yOffset = 0;
            if(isSkulled){
                yOffset = -28;
            }

            // Chat text changes the overhead offset.
            if(isOverheadTextActive){
                yOffset = yOffset - 5;
            }

            int offset = chosenPattern[i];
            graphics.drawImage(
                    image,
                    point.getX() - image.getWidth() / 2+offset,
                    point.getY()-30 + yOffset,
                    null
            );

        }

        return null;
    }

}
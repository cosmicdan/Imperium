package com.cosmicdan.minecraftempires.client.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.cosmicdan.minecraftempires.Main;
import com.cosmicdan.minecraftempires.medata.player.EntityPlayerME;
import com.cosmicdan.minecraftempires.server.PacketHandler;
import com.cosmicdan.minecraftempires.server.SyncPlayerME;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

// this class is responsible for drawing the "Log" GUI.
public class GuiLog extends GuiScreen {
    static final ResourceLocation texture = new ResourceLocation("minecraftempires:textures/gui/playerlog.png");
    
    private static final int BUTTON_CLOSE = 0;
    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREV = 2;
    private static final int guiWidthHeight = 230;
    private static final int guiContentWidth = 148;
    private static final int guiContentHeight = 180;
    
    private int totalPages = 0;
    private int currPage = 0;
    private PageButton buttonNextPage;
    private PageButton buttonPreviousPage;
    private GuiButton buttonClose;
    private List<List<String>> eventPageContent = new ArrayList<List<String>>();
    private int eventPageLineHeight = 9;


    public GuiLog(EntityPlayer player) {
        // construct data here
        //SyncPlayerME syncPlayerME = new SyncPlayerME(player);
        //PacketHandler.packetReqest.sendToServer(syncPlayerME);
        PacketHandler.packetReqest.sendToServer(new SyncPlayerME());
        //System.out.println(syncPlayerME.eventListDone);
        //syncPlayerME.
        //System.out.println(playerME.eventListDone);
        //EventLogBuilder playerEvents = new EventLogBuilder(playerME); 
        //String content = playerEvents.eventLog;
        // this is just a sample string to test the runtime-generated pagination 
        String content = "111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111";
        NiceText pageData = new NiceText(content, NiceText.Fonts.VANILLA, guiContentWidth, guiContentHeight);
        eventPageContent = pageData.pageData;
        eventPageLineHeight = pageData.lineHeight;
        // we also want to set the initial page of event log to the last page (most recent)
        totalPages = currPage = eventPageContent.size() - 1;
    }
    
    // draw the GUI elements
    @Override
    public void initGui() {
        int guiXStart = (width - guiWidthHeight) / 2;
        this.buttonList.clear();
        this.buttonList.add(this.buttonClose = new CloseButton(BUTTON_CLOSE, guiXStart + 170, 8));
        this.buttonList.add(this.buttonNextPage = new PageButton(BUTTON_NEXT, guiXStart + 170, 200, true));
        this.buttonList.add(this.buttonPreviousPage = new PageButton(BUTTON_PREV, guiXStart + 150, 200, false));
    }
    
    // draw the book GUI background/frame
    @Override
    public void drawScreen(int mouseX, int mouseY, float renderPartials) {
        // set stuff for translucency (minor, removes aliasing)
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        // assign the "Log" texture
        mc.renderEngine.bindTexture(texture);
        int guiXStart = (width - guiWidthHeight) / 2;
        int guiYStart = 0;
        int guiContentXStart = guiXStart + 42;
        int guiContentYStart = 16;
        // set the bounds for the book GUI
        drawTexturedModalRect(guiXStart, guiYStart, 0, 0, guiWidthHeight, guiWidthHeight);
        
        // small time-saver
        FontRenderer text = this.fontRendererObj;
        
        int drawTextY = guiContentYStart;
        for (String line : eventPageContent.get(currPage)) {
            text.drawString(line, guiContentXStart, drawTextY, 0xFFFFFF);
            drawTextY += eventPageLineHeight;
        }
        
        // pass it on
        super.drawScreen(mouseX, mouseY, renderPartials);
    }

    // the actions for GUI buttons
    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case BUTTON_CLOSE:
                // command to close the current GUI window (i.e. this book)
                mc.displayGuiScreen(null);
                break;
            case BUTTON_NEXT:
                if (currPage < totalPages) ++currPage;
                break;
            case BUTTON_PREV:
                if (currPage > 0) --currPage;
                break;
        }
    }

    // we don't want this GUI to pause the game
    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    // hook for keyboard keys
    @Override
    protected void keyTyped(char c, int key) {
        //char lowerCase = Character.toLowerCase(c);
        switch (key) {
            case Keyboard.KEY_ESCAPE:
            case Keyboard.KEY_E:
                // Escape = close book
                mc.displayGuiScreen(null);
                break;
            case Keyboard.KEY_A:
            case Keyboard.KEY_LEFT:
                if (currPage > 0) --currPage;
                break;
            case Keyboard.KEY_D:
            case Keyboard.KEY_RIGHT:
                if (currPage < totalPages) ++currPage;
                break;
        }
    }
    
    // page next/previous buttons. Has some hard-coded values for my texture UV map.
    static class PageButton extends GuiButton {
        private final boolean isNextPage;

        public PageButton(int id, int xPos, int yPos, boolean isNextPage) {
            super(id, xPos, yPos, 16, 16, "");
            this.isNextPage = isNextPage;
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                // translucency stuff
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                mc.renderEngine.bindTexture(texture);
                int uPos = 0;
                int vPos = 231;
                if (isHovered) {
                    uPos += 17;
                }
                if (!this.isNextPage) {
                    uPos += 35;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, uPos, vPos, 16, 16);
            }
        }
    }
    
    // page close button. As above, has some hard-coded values for UV mapping
    static class CloseButton extends GuiButton {
        public CloseButton(int id, int xPos, int yPos) {
            super(id, xPos, yPos, 16, 16, "");
        }

        public void drawButton(Minecraft mc, int mouseX, int mouseY) {
            if (this.visible) {
                boolean isHovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_BLEND);
                mc.renderEngine.bindTexture(texture);
                int uPos = 70;
                int vPos = 231;
                if (isHovered) {
                    uPos += 17;
                }
                this.drawTexturedModalRect(this.xPosition, this.yPosition, uPos, vPos, 16, 16);
            }
        }
    }
}

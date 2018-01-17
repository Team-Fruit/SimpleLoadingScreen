package net.teamfruit.simpleloadingscreen.api.renderer;

import java.util.List;

public interface IFontRenderer {

	int drawStringWithShadow(String text, int x, int y, int color);

	int drawString(String text, int x, int y, int color);

	int drawString(String text, int x, int y, int color, boolean dropShadow);

	int getStringWidth(String text);

	int getCharWidth(char character);

	String trimStringToWidth(String text, int width);

	String trimStringToWidth(String text, int width, boolean reserve);

	void drawSplitString(String text, int x, int y, int width, int color);

	int splitStringWidth(String text, int width);

	void setUnicodeFlag(boolean flag);

	boolean getUnicodeFlag();

	void setBidiFlag(boolean flag);

	List<String> listFormattedStringToWidth(String text, int width);

	boolean getBidiFlag();

	int getFontHeight();

}
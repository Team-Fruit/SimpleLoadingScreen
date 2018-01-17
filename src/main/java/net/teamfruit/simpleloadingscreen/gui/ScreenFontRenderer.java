package net.teamfruit.simpleloadingscreen.gui;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.teamfruit.simpleloadingscreen.api.renderer.IFontRenderer;

public class ScreenFontRenderer implements IFontRenderer {
	private final FontRenderer fontRenderer;

	public ScreenFontRenderer(final FontRenderer fontRenderer) {
		this.fontRenderer = fontRenderer;
	}

	@Override
	public int drawStringWithShadow(final String text, final int x, final int y, final int color) {
		return this.fontRenderer.drawStringWithShadow(text, x, y, color);
	}

	@Override
	public int drawString(final String text, final int x, final int y, final int color) {
		return this.fontRenderer.drawString(text, x, y, color);
	}

	@Override
	public int drawString(final String text, final int x, final int y, final int color, final boolean dropShadow) {
		return this.fontRenderer.drawString(text, x, y, color, dropShadow);
	}

	@Override
	public int getStringWidth(final String text) {
		return this.fontRenderer.getStringWidth(text);
	}

	@Override
	public int getCharWidth(final char character) {
		return this.fontRenderer.getCharWidth(character);
	}

	@Override
	public String trimStringToWidth(final String text, final int width) {
		return this.fontRenderer.trimStringToWidth(text, width);
	}

	@Override
	public String trimStringToWidth(final String text, final int width, final boolean reserve) {
		return this.fontRenderer.trimStringToWidth(text, width, reserve);
	}

	@Override
	public void drawSplitString(final String text, final int x, final int y, final int width, final int color) {
		this.fontRenderer.drawSplitString(text, x, y, width, color);
	}

	@Override
	public int splitStringWidth(final String text, final int width) {
		return this.fontRenderer.splitStringWidth(text, width);
	}

	@Override
	public void setUnicodeFlag(final boolean flag) {
		this.fontRenderer.setUnicodeFlag(flag);
	}

	@Override
	public boolean getUnicodeFlag() {
		return this.fontRenderer.getUnicodeFlag();
	}

	@Override
	public void setBidiFlag(final boolean flag) {
		this.fontRenderer.setBidiFlag(flag);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<String> listFormattedStringToWidth(final String text, final int width) {
		return this.fontRenderer.listFormattedStringToWidth(text, width);
	}

	@Override
	public boolean getBidiFlag() {
		return this.fontRenderer.getBidiFlag();
	}

	@Override
	public int getFontHeight() {
		return this.fontRenderer.FONT_HEIGHT;
	}

	public static FontRenderer getFontRenderer(final IFontRenderer fontRenderer) {
		if (fontRenderer instanceof ScreenFontRenderer)
			return ((ScreenFontRenderer) fontRenderer).fontRenderer;
		else
			throw new IllegalArgumentException("Invalid FontRenderer");
	}
}

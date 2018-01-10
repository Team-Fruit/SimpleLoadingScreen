package net.teamfruit.simpleloadingscreen.progress;

import net.teamfruit.simpleloadingscreen.api.IProgressBar;

public class ScreenProgressBar implements IProgressBar {
	private final String title;
	private final int steps;
	private final int step;
	private final String message;

	public ScreenProgressBar(final String title, final int steps, final int step, final String message) {
		this.title = title;
		this.steps = steps;
		this.step = step;
		this.message = message;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public int getSteps() {
		return this.steps;
	}

	@Override
	public int getStep() {
		return this.step;
	}

	@Override
	public String getMessage() {
		return this.message;
	}
}

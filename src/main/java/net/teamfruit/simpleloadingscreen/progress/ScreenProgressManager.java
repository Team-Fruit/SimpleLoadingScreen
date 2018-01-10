package net.teamfruit.simpleloadingscreen.progress;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;

import cpw.mods.fml.common.ProgressManager;
import cpw.mods.fml.common.ProgressManager.ProgressBar;
import net.teamfruit.simpleloadingscreen.api.IProgressBar;

@SuppressWarnings("deprecation")
public class ScreenProgressManager {
	public List<IProgressBar> getProgressBars() {
		final List<IProgressBar> bars = Lists.newArrayList();
		for (final Iterator<ProgressBar> itr = ProgressManager.barIterator(); itr.hasNext();) {
			final ProgressBar bar = itr.next();
			bars.add(getProgressBar(bar));
		}
		return bars;
	}

	public IProgressBar getProgressBar(final ProgressBar bar) {
		return new ScreenProgressBar(bar.getTitle(), bar.getSteps(), bar.getStep(), bar.getMessage());
	}
}

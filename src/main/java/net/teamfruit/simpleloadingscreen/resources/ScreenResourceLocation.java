package net.teamfruit.simpleloadingscreen.resources;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.simpleloadingscreen.api.IResourceLocation;

public class ScreenResourceLocation implements IResourceLocation {
	private final ResourceLocation resourceLocation;

	public ScreenResourceLocation(final ResourceLocation resourceLocation) {
		this.resourceLocation = resourceLocation;
	}

	@Override
	public String getResourcePath() {
		return this.resourceLocation.getResourcePath();
	}

	@Override
	public String getResourceDomain() {
		return this.resourceLocation.getResourceDomain();
	}

	@Override
	public String toString() {
		return this.resourceLocation.toString();
	}

	@Override
	public boolean equals(final Object p_equals_1_) {
		return this.resourceLocation.equals(p_equals_1_);
	}

	@Override
	public int hashCode() {
		return this.resourceLocation.hashCode();
	}

	public static ResourceLocation getLocation(final IResourceLocation resourceLocation) {
		if (resourceLocation instanceof ScreenResourceLocation)
			return ((ScreenResourceLocation) resourceLocation).resourceLocation;
		else
			return new ResourceLocation(resourceLocation.getResourceDomain(), resourceLocation.getResourcePath());
	}
}

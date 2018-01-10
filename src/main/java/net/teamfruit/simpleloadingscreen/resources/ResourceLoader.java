package net.teamfruit.simpleloadingscreen.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;

import com.google.common.collect.Lists;

import net.minecraft.client.resources.FileResourcePack;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;

public class ResourceLoader {

	private final List<IResourcePack> resourcePacks = Lists.newArrayList();

	public ResourceLoader() {
	}

	public void addResourcePack(final IResourcePack resourcePack) {
		this.resourcePacks.add(resourcePack);
	}

	public void addResourcePackLocation(final File resourcePack) {
		addResourcePack(createResourcePack(resourcePack));
	}

	public void addAssetsLocation(final File resourcePack) {
		addResourcePack(new AssetsResourcePack(resourcePack));
	}

	public InputStream open(final ResourceLocation loc) throws IOException {
		for (final ListIterator<IResourcePack> itr = this.resourcePacks.listIterator(this.resourcePacks.size()); itr.hasPrevious();) {
			final int index = itr.previousIndex();
			final IResourcePack resourcePack = itr.previous();
			if (resourcePack.resourceExists(loc)||index<=0)
				return resourcePack.getInputStream(loc);
		}
		return null;
	}

	public static IResourcePack createResourcePack(final File file) {
		if (file.isDirectory())
			return new FolderResourcePack(file);
		else
			return new FileResourcePack(file);
	}
}

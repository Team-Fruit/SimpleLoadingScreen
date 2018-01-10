package net.teamfruit.simpleloadingscreen.resources;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

import org.apache.commons.io.filefilter.DirectoryFileFilter;

import com.google.common.collect.Sets;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.resources.FolderResourcePack;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class AssetsResourcePack extends FolderResourcePack {
	public AssetsResourcePack(final File assetsDir) {
		super(assetsDir);
	}

	private static String locationToName(final ResourceLocation location) {
		return String.format("%s/%s", location.getResourceDomain(), location.getResourcePath());
	}

	@Override
	public InputStream getInputStream(final ResourceLocation location) throws IOException {
		return getInputStreamByName(locationToName(location));
	}

	@Override
	public boolean resourceExists(final ResourceLocation location) {
		return hasResourceName(locationToName(location));
	}

	@Override
	public Set<String> getResourceDomains() {
		final Set<String> hashset = Sets.newHashSet();
		final File file1 = this.resourcePackFile;

		if (file1.isDirectory()) {
			final File[] afile = file1.listFiles((FileFilter) DirectoryFileFilter.DIRECTORY);
			final int i = afile.length;

			for (int j = 0; j<i; ++j) {
				final File file2 = afile[j];
				final String s = getRelativeName(file1, file2);

				if (!s.equals(s.toLowerCase()))
					logNameNotLowercase(s);
				else
					hashset.add(s.substring(0, s.length()-1));
			}
		}

		return hashset;
	}
}
package net.teamfruit.simpleloadingscreen.style;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.common.base.Charsets;
import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import net.teamfruit.simpleloadingscreen.Log;

public class StyleObjectLoader {
	private final Gson gson = new GsonBuilder().registerTypeAdapterFactory(new StyleObjectAdapterFactory()).create();

	public StyleObjectModel read(final ByteSource source, final boolean returnNull) {
		try (JsonReader jsonReader = new JsonReader(new InputStreamReader(source.openBufferedStream(), Charsets.UTF_8))) {
			return this.gson.fromJson(jsonReader, StyleObjectModel.class);
		} catch (final IOException e) {
			if (!(e instanceof FileNotFoundException))
				Log.log.info("Failed to load style", e);

			if (returnNull)
				return null;

			return new StyleObjectModel();
		}
	}

	public StyleObjectModel read(final File file, final boolean returnNull) {
		return read(Files.asByteSource(file), returnNull);
	}

	public void write(final File file, final StyleObjectModel object) throws IOException {
		file.getParentFile().mkdirs();
		try (JsonWriter writer = new JsonWriter(new OutputStreamWriter(new FileOutputStream(file)))) {
			writer.setIndent("  ");
			this.gson.toJson(object, StyleObjectModel.class, writer);
		}
	}
}

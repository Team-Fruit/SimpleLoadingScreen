package net.teamfruit.simpleloadingscreen.style;

import java.io.IOException;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class StyleObjectAdapterFactory implements TypeAdapterFactory {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> type) {
		final Class<? super T> rawType = type.getRawType();
		if (!StyleObjectModel.class.isAssignableFrom(rawType))
			return null;

		final TypeToken<StyleObjectModel> modelType = (TypeToken<StyleObjectModel>) type;
		final TypeAdapter<StyleObjectModel> delegate = gson.getDelegateAdapter(this, modelType);
		final TypeAdapter<Map> mapadapter = gson.getAdapter(Map.class);
		final TypeAdapter<JsonElement> adapter = gson.getAdapter(JsonElement.class);

		return (TypeAdapter<T>) new TypeAdapter<StyleObjectModel>() {
			@Override
			public void write(final JsonWriter out, final StyleObjectModel value) throws IOException {
				final JsonElement mapelement = mapadapter.toJsonTree(value.property);
				final JsonElement element = delegate.toJsonTree(value);
				adapter.write(out, mapelement);
				adapter.write(out, element);
			}

			@Override
			public StyleObjectModel read(final JsonReader in) throws IOException {
				final JsonElement element = adapter.read(in);
				final StyleObjectModel model = delegate.fromJsonTree(element);
				model.property = mapadapter.fromJsonTree(element);
				return model;
			}
		};
	}
}
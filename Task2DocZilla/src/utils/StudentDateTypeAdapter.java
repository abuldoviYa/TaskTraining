package utils;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentDateTypeAdapter implements JsonDeserializer<Date> {

    private final SimpleDateFormat dateFormat;

    public StudentDateTypeAdapter(String dateFormatPattern) {
        this.dateFormat = new SimpleDateFormat(dateFormatPattern);
    }

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            String dateString = json.getAsString();
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}


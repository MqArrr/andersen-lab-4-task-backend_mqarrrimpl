package lab.andersen.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        try {
            return new Timestamp(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new JsonParseException("Error parsing timestamp", e);
        }
    }
}

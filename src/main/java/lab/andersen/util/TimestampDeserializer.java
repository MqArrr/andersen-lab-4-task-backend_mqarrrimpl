package lab.andersen.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

    @Override
    public Timestamp deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        String dateString = json.getAsString();
        try {
            return new Timestamp(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            return new Timestamp(1);//это не код, а говно, мб стоит поправить
        }
    }
}

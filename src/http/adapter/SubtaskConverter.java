package http.adapter;

import com.google.gson.*;
import model.Subtask;
import service.Status;
import service.TypeTask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SubtaskConverter implements JsonSerializer<Subtask>, JsonDeserializer<Subtask> {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public JsonElement serialize(Subtask subtask, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("type", TypeTask.SUBTASK.toString());
        object.addProperty("id", subtask.getId());
        object.addProperty("name", subtask.getName());
        object.addProperty("description", subtask.getDescription());
        object.addProperty("status", subtask.getStatus().toString());
        object.addProperty("startTime", subtask.getStartTime().format(dtf));
        object.addProperty("duration", subtask.getDuration().toString());
        object.addProperty("epicId", subtask.getEpicId());
        return object;
    }

    public Subtask deserialize(JsonElement json, Type type,
                               JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = new String(object.get("name").getAsString());
        String description = new String(object.get("description").getAsString());
        Status status = Status.valueOf(object.get("status").getAsString());
        LocalDateTime startTime = LocalDateTime.parse(object.get("startTime").getAsString(),dtf);
        Duration duration = Duration.parse(object.get("duration").getAsString());
        int epicId = object.get("epicId").getAsInt();
        if (object.get("id") != null) {
            int id = object.get("id").getAsInt();
            return new Subtask(name,description,id,status,epicId,startTime,duration);
        }
        Subtask s = new Subtask(name,description,status,startTime,duration);
        s.setEpicId(epicId);
        return (s);
    }
}
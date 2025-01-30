package http.adapter;

import com.google.gson.*;
import model.Task;
import service.Status;
import service.TypeTask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskConverter implements JsonSerializer<Task>, JsonDeserializer<Task> {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public JsonElement serialize(Task task, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("type", TypeTask.TASK.toString());
        object.addProperty("id", task.getId());
        object.addProperty("name", task.getName());
        object.addProperty("description", task.getDescription());
        object.addProperty("status", task.getStatus().toString());
        object.addProperty("startTime", task.getStartTime().format(dtf));
        object.addProperty("duration", task.getDuration().toString());
        return object;
    }

    public Task deserialize(JsonElement json, Type type,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();

        String name = new String(object.get("name").getAsString());
        String description = new String(object.get("description").getAsString());
        Status status = Status.valueOf(object.get("status").getAsString());
        LocalDateTime startTime = LocalDateTime.parse(object.get("startTime").getAsString(),dtf);
        Duration duration = Duration.parse(object.get("duration").getAsString());
        if (object.get("id") != null) {
            int id = object.get("id").getAsInt();
            return new Task(name,description,id,status,startTime,duration);
        }
        return new Task(name,description,status,startTime,duration);
    }
}
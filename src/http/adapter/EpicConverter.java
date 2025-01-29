package http.adapter;

import com.google.gson.*;
import model.Epic;
import model.Subtask;
import service.Status;
import service.TypeTask;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

public class EpicConverter implements JsonSerializer<Epic>, JsonDeserializer<Epic> {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public JsonElement serialize(Epic epic, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();

        JsonArray subtaskArray = new JsonArray(epic.getArraySubtask().size());
        for (Subtask el : epic.getArraySubtask()) {
            subtaskArray.add(new SubtaskConverter().serialize(el, typeOfSrc, context));
        }
        object.addProperty("type", TypeTask.EPIC.toString());
        object.addProperty("id", epic.getId());
        object.addProperty("name", epic.getName());
        object.addProperty("description", epic.getDescription());
        object.addProperty("status", epic.getStatus().toString());
        LocalDateTime startNull = Optional.ofNullable(epic.getStartTime()).orElse(LocalDateTime.now());
        object.addProperty("startTime", startNull.format(dtf));
        object.addProperty("endTime", Optional.ofNullable(epic.getEndTime()).orElse(startNull).format(dtf));
        object.addProperty("duration",
                Optional.ofNullable(epic.getDuration()).orElse(Duration.ZERO).toString());
        object.add("subtaskArray", subtaskArray);
        return object;
    }

    public Epic deserialize(JsonElement json, Type type,
                            JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();


        String name = new String(object.get("name").getAsString());
        String description = new String(object.get("description").getAsString());
        if (object.get("id") != null) {
            Status status = Status.valueOf(object.get("status").getAsString());

            LocalDateTime startTime = LocalDateTime.parse(object.get("startTime").getAsString(),dtf);
            Duration duration = Duration.parse(object.get("duration").getAsString());
            LocalDateTime endTime = LocalDateTime.parse(object.get("endTime").getAsString(),dtf);
            JsonArray arrayJson = object.get("subtaskArray").getAsJsonArray();
            ArrayList<Subtask> subtaskArray = new ArrayList<>();
            for (JsonElement el : arrayJson) {
                Subtask sub = new SubtaskConverter().deserialize(el,type,context);
                subtaskArray.add(sub);
            }
            int id = object.get("id").getAsInt();
            Epic epic = new Epic(name,description,id,subtaskArray);
            if (object.size() == 9) {
                epic.setStartTime(startTime);
                epic.setDuration(duration);
                epic.setEndTime(endTime);
                epic.setStatus(status);
            }
            return epic;
        }
        Epic epicNew = new Epic(name,description,Status.NEW);
        epicNew.setArraySubtask(new ArrayList<>());
        return epicNew;
    }
}
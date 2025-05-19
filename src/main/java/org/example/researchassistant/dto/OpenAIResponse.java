package org.example.researchassistant.dto;

import java.util.List;

public class OpenAIResponse {
    private List<Choice> choices;
    private String id;
    private String object;
    private long created;
    private String model;

    public static class Choice {
        private int index;
        private Message message;
        private String finishReason;

        // Getters and setters
        public int getIndex() { return index; }
        public void setIndex(int index) { this.index = index; }
        public Message getMessage() { return message; }
        public void setMessage(Message message) { this.message = message; }
        public String getFinishReason() { return finishReason; }
        public void setFinishReason(String finishReason) { this.finishReason = finishReason; }
    }

    public static class Message {
        private String role;
        private String content;

        // Getters and setters
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // Getters and setters for main class
    public List<Choice> getChoices() { return choices; }
    public void setChoices(List<Choice> choices) { this.choices = choices; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getObject() { return object; }
    public void setObject(String object) { this.object = object; }
    public long getCreated() { return created; }
    public void setCreated(long created) { this.created = created; }
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    @Override
    public String toString() {
        return "OpenAIResponse{" +
                "id='" + id + '\'' +
                ", object='" + object + '\'' +
                ", created=" + created +
                ", model='" + model + '\'' +
                ", choices=" + choices +
                '}';
    }
}
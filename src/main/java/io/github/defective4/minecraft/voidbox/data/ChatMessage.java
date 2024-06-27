package io.github.defective4.minecraft.voidbox.data;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class ChatMessage {

    public static class Builder {
        private List<ChatMessage> extra, with;
        private String text, translate, color;

        public ChatMessage build() {
            return new ChatMessage(text, translate, color, extra == null ? null : extra.toArray(new ChatMessage[0]),
                    with == null ? null : with.toArray(new ChatMessage[0]));
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder extra(ChatMessage extra) {
            if (this.extra == null) this.extra = new ArrayList<>();
            this.extra.add(extra);
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder translate(String translate) {
            this.translate = translate;
            return this;
        }

        public Builder with(ChatMessage with) {
            if (this.with == null) this.with = new ArrayList<>();
            this.with.add(with);
            return this;
        }
    }

    public String color;
    public final ChatMessage[] extra;
    public final String text;
    public final String translate;
    public final ChatMessage[] with;

    private ChatMessage(String text, String translate, String color, ChatMessage[] extra, ChatMessage[] with) {
        this.text = text;
        this.translate = translate;
        this.color = color;
        this.extra = extra;
        this.with = with;
    }

    public ChatMessage setColor(String color) {
        this.color = color;
        return this;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public static ChatMessage text(String text) {
        return new Builder().text(text).build();
    }

    public static ChatMessage translate(String key, ChatMessage... with) {
        Builder bd = new Builder().translate(key);
        for (ChatMessage element : with) bd.with(element);
        return bd.build();
    }
}

package com.example.nebir.voztexto.Bot;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by nebir on 05/02/2016.
 */
class PandoraBots implements ChatterBot {
    private final String botid;

    public PandoraBots(String botid) {
        this.botid = botid;
    }

    public ChatterBotSession createSession() {
        return new Session();
    }

    private class Session implements ChatterBotSession {
        private final Map<String, String> vars;

        public Session() {
            vars = new LinkedHashMap<String, String>();
            vars.put("botid", botid);
            vars.put("custid", UUID.randomUUID().toString());
        }

        public ChatterBotThought think(ChatterBotThought thought) throws Exception {
            vars.put("input", thought.getText());

            String response = Utils.request("http://www.pandorabots.com/pandora/talk-xml", null, vars);

            ChatterBotThought responseThought = new ChatterBotThought();

            responseThought.setText(Utils.xPathSearch(response, "//result/that/text()"));

            return responseThought;
        }

        public String think(String text) throws Exception {
            ChatterBotThought thought = new ChatterBotThought();
            thought.setText(text);
            return think(thought).getText();
        }
    }
}

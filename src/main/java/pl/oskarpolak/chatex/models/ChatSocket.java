package pl.oskarpolak.chatex.models;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableWebSocket
public class ChatSocket extends TextWebSocketHandler implements WebSocketConfigurer {

    private List<WebSocketSession> users = new ArrayList<>();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(this, "/chat").setAllowedOrigins("*");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        users.remove(session);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        users.add(session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        for (WebSocketSession user : users) {
            user.sendMessage(message);
        }
    }
}
package com.study.whiteboard.controller;

import com.study.whiteboard.model.DrawMessage;
import com.study.whiteboard.service.DrawingStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class WhiteboardController {

    private static final Logger log = LoggerFactory.getLogger(WhiteboardController.class);
    private final DrawingStorageService storageService;
    private final SimpMessagingTemplate messagingTemplate;

    public WhiteboardController(DrawingStorageService storageService, SimpMessagingTemplate messagingTemplate) {
        this.storageService = storageService;
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * Nhận tọa độ vẽ từ một người dùng trong một phòng
     */
    @MessageMapping("/room/{roomId}/draw")
    public void broadcastDrawing(@DestinationVariable String roomId, DrawMessage message) {
        message.setRoomId(roomId);
        storageService.addDrawing(message);
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/draw", message);
    }

    /**
     * Nhận yêu cầu xóa trắng bảng vẽ của một phòng
     */
    @MessageMapping("/room/{roomId}/clear")
    public void broadcastClear(@DestinationVariable String roomId, Map<String, String> payload) {
        log.info("Nhận yêu cầu xóa bảng từ user: {} tại phòng: {}", payload.get("senderId"), roomId);
        storageService.clearRoom(roomId);
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/clear", payload);
    }

    /**
     * Nhận yêu cầu Undo của một người dùng trong phòng
     */
    @MessageMapping("/room/{roomId}/undo")
    public void broadcastUndo(@DestinationVariable String roomId, Map<String, String> payload) {
        String senderId = payload.get("senderId");
        log.info("Nhận yêu cầu Undo từ user: {} tại phòng: {}", senderId, roomId);
        
        String undoneStrokeId = storageService.undoLastStroke(senderId, roomId);
        
        Map<String, String> response = Map.of(
            "senderId", senderId,
            "strokeId", undoneStrokeId != null ? undoneStrokeId : ""
        );
        messagingTemplate.convertAndSend("/topic/room/" + roomId + "/undo", response);
    }

    /**
     * REST API: Trả về toàn bộ nét vẽ của một phòng
     */
    @GetMapping("/api/drawings/{roomId}")
    @ResponseBody
    public List<DrawMessage> getRoomDrawings(@PathVariable String roomId) {
        return storageService.getAllDrawings(roomId);
    }
}

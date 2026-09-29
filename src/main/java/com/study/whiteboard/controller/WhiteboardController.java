package com.study.whiteboard.controller;

import com.study.whiteboard.model.DrawMessage;
import com.study.whiteboard.service.DrawingStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class WhiteboardController {

    private static final Logger log = LoggerFactory.getLogger(WhiteboardController.class);
    private final DrawingStorageService storageService;

    public WhiteboardController(DrawingStorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * Nhận tọa độ vẽ từ một người dùng tại '/app/draw'
     * và lập tức phát thanh (Broadcast) đến tất cả những ai đang subscribe '/topic/draw'
     */
    @MessageMapping("/draw")
    @SendTo("/topic/draw")
    public DrawMessage broadcastDrawing(DrawMessage message) {
        storageService.addDrawing(message);
        return message;
    }

    /**
     * Nhận yêu cầu xóa trắng bảng vẽ tại '/app/clear'
     * và phát thanh đến '/topic/clear' để tất cả các màn hình tự xóa sạch
     */
    @MessageMapping("/clear")
    @SendTo("/topic/clear")
    public Map<String, String> broadcastClear(Map<String, String> payload) {
        log.info("Nhận yêu cầu xóa trắng bảng từ user: {}", payload.get("senderId"));
        storageService.clearAll();
        return payload;
    }

    /**
     * Nhận yêu cầu Undo tại '/app/undo'
     * Xóa nét vẽ cuối cùng của user đó trong DB, và phát thanh ID của nét vẽ đó
     * để các client tự xóa nó khỏi màn hình.
     */
    @MessageMapping("/undo")
    @SendTo("/topic/undo")
    public Map<String, String> broadcastUndo(Map<String, String> payload) {
        String senderId = payload.get("senderId");
        log.info("Nhận yêu cầu Undo từ user: {}", senderId);
        
        String undoneStrokeId = storageService.undoLastStroke(senderId);
        
        // Trả về strokeId vừa xóa để các client biết mà xóa khỏi canvas
        return Map.of(
            "senderId", senderId,
            "strokeId", undoneStrokeId != null ? undoneStrokeId : ""
        );
    }

    /**
     * REST API: Trả về toàn bộ nét vẽ đã lưu cho người dùng mới kết nối
     */
    @GetMapping("/api/drawings")
    @ResponseBody
    public List<DrawMessage> getAllDrawings() {
        return storageService.getAllDrawings();
    }
}

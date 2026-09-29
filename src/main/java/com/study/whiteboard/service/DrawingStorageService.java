package com.study.whiteboard.service;

import com.study.whiteboard.model.DrawMessage;
import com.study.whiteboard.model.DrawMessageEntity;
import com.study.whiteboard.repository.DrawMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Lưu trữ nét vẽ vào Database thông qua Spring Data JPA.
 */
@Service
public class DrawingStorageService {

    private final DrawMessageRepository repository;

    public DrawingStorageService(DrawMessageRepository repository) {
        this.repository = repository;
    }

    /**
     * Lưu một nét vẽ mới vào Database
     */
    public void addDrawing(DrawMessage message) {
        DrawMessageEntity entity = new DrawMessageEntity();
        entity.setStrokeId(message.getStrokeId());
        entity.setPrevX(message.getPrevX());
        entity.setPrevY(message.getPrevY());
        entity.setCurrX(message.getCurrX());
        entity.setCurrY(message.getCurrY());
        entity.setColor(message.getColor());
        entity.setLineWidth(message.getLineWidth());
        entity.setSenderId(message.getSenderId());
        entity.setType(message.getType());
        entity.setRoomId(message.getRoomId());
        
        repository.save(entity);
    }

    /**
     * Trả về toàn bộ nét vẽ đã lưu của một phòng
     */
    public List<DrawMessage> getAllDrawings(String roomId) {
        return repository.findByRoomId(roomId).stream().map(entity -> {
            DrawMessage msg = new DrawMessage();
            msg.setStrokeId(entity.getStrokeId());
            msg.setPrevX(entity.getPrevX());
            msg.setPrevY(entity.getPrevY());
            msg.setCurrX(entity.getCurrX());
            msg.setCurrY(entity.getCurrY());
            msg.setColor(entity.getColor());
            msg.setLineWidth(entity.getLineWidth());
            msg.setSenderId(entity.getSenderId());
            msg.setType(entity.getType());
            msg.setRoomId(entity.getRoomId());
            return msg;
        }).collect(Collectors.toList());
    }

    /**
     * Xóa toàn bộ nét vẽ của một phòng
     */
    @Transactional
    public void clearRoom(String roomId) {
        repository.deleteByRoomId(roomId);
    }

    /**
     * Undo: Tìm nét vẽ cuối cùng của người dùng trong phòng và xóa nó.
     * @return strokeId vừa bị xóa, hoặc null nếu không tìm thấy
     */
    @Transactional
    public String undoLastStroke(String senderId, String roomId) {
        DrawMessageEntity lastStroke = repository.findFirstBySenderIdAndRoomIdOrderByIdDesc(senderId, roomId);
        if (lastStroke != null && lastStroke.getStrokeId() != null) {
            String strokeId = lastStroke.getStrokeId();
            repository.deleteByStrokeIdAndRoomId(strokeId, roomId);
            return strokeId;
        }
        return null;
    }
}

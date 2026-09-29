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
        
        repository.save(entity);
    }

    /**
     * Trả về toàn bộ nét vẽ đã lưu
     */
    public List<DrawMessage> getAllDrawings() {
        return repository.findAll().stream().map(entity -> {
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
            return msg;
        }).collect(Collectors.toList());
    }

    /**
     * Xóa toàn bộ nét vẽ
     */
    public void clearAll() {
        repository.deleteAll();
    }

    /**
     * Undo: Tìm nét vẽ cuối cùng của người dùng và xóa nó.
     * @return strokeId vừa bị xóa, hoặc null nếu không tìm thấy
     */
    @Transactional
    public String undoLastStroke(String senderId) {
        DrawMessageEntity lastStroke = repository.findFirstBySenderIdOrderByIdDesc(senderId);
        if (lastStroke != null && lastStroke.getStrokeId() != null) {
            String strokeId = lastStroke.getStrokeId();
            repository.deleteByStrokeId(strokeId);
            return strokeId;
        }
        return null;
    }
}

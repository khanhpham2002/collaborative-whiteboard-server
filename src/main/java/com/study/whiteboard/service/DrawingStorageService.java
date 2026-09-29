package com.study.whiteboard.service;

import com.study.whiteboard.model.DrawMessage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lưu trữ toàn bộ nét vẽ trong bộ nhớ (in-memory).
 * Khi người dùng mới kết nối, họ sẽ nhận được tất cả nét vẽ đã có
 * để vẽ lại lên canvas của mình.
 */
@Service
public class DrawingStorageService {

    private final List<DrawMessage> drawings = Collections.synchronizedList(new ArrayList<>());

    /**
     * Lưu một nét vẽ mới vào bộ nhớ
     */
    public void addDrawing(DrawMessage message) {
        drawings.add(message);
    }

    /**
     * Trả về toàn bộ nét vẽ đã lưu
     */
    public List<DrawMessage> getAllDrawings() {
        synchronized (drawings) {
            return new ArrayList<>(drawings);
        }
    }

    /**
     * Xóa toàn bộ nét vẽ (khi có người nhấn Clear)
     */
    public void clearAll() {
        drawings.clear();
    }
}

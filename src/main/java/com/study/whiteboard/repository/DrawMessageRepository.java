package com.study.whiteboard.repository;

import com.study.whiteboard.model.DrawMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawMessageRepository extends JpaRepository<DrawMessageEntity, Long> {
    
    // Tìm nét vẽ cuối cùng của một người dùng
    DrawMessageEntity findFirstBySenderIdOrderByIdDesc(String senderId);
    
    // Xóa tất cả các điểm thuộc về một nét vẽ
    void deleteByStrokeId(String strokeId);
}

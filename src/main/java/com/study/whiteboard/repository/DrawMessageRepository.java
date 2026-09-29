package com.study.whiteboard.repository;

import com.study.whiteboard.model.DrawMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DrawMessageRepository extends JpaRepository<DrawMessageEntity, Long> {
    
    // Tìm tất cả nét vẽ của một phòng
    List<DrawMessageEntity> findByRoomId(String roomId);

    // Tìm nét vẽ cuối cùng của một người dùng trong một phòng
    DrawMessageEntity findFirstBySenderIdAndRoomIdOrderByIdDesc(String senderId, String roomId);
    
    // Xóa tất cả các điểm thuộc về một nét vẽ trong phòng
    void deleteByStrokeIdAndRoomId(String strokeId, String roomId);

    // Xóa toàn bộ phòng
    void deleteByRoomId(String roomId);
}


package com.be.server.core.admin.chatlieu.repository;

import com.be.server.entity.ChatLieu;
import com.be.server.repository.ChatLieuRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ADChatLieuRepository extends ChatLieuRepository {

    Page<ChatLieu> findByMaContainingOrTenContaining(String ma, String ten, Pageable pageable);



    List<ChatLieu> findByTenContaining(String ten);

}

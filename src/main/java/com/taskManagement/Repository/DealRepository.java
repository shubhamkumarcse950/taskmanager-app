package com.taskManagement.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.DealEntity;

@Repository
public interface DealRepository extends JpaRepository<DealEntity, Long> {

	List<DealEntity> findByUserCode(String userCode);

	List<DealEntity> findByClientUserCode(String clientUserCode);

}

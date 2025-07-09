package com.taskManagement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.taskManagement.Entitys.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Long> {

}

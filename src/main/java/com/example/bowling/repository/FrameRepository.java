package com.example.bowling.repository;

import com.example.bowling.entity.FrameEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FrameRepository extends JpaRepository<FrameEntity, UUID> {
}

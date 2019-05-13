package com.frontend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.frontend.entities.Topic;

public interface TopicRepository extends JpaRepository<Topic, Long> {

}

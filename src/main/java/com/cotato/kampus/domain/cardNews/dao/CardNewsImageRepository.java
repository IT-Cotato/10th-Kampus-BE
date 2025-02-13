package com.cotato.kampus.domain.cardNews.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cotato.kampus.domain.cardNews.domain.CardNewsImage;

@Repository
public interface CardNewsImageRepository extends JpaRepository<CardNewsImage, Long> {
}

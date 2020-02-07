package com.haojing.service;

import com.haojing.entity.Carousel;

import java.util.List;

public interface CarouselService {
    public List<Carousel> queryAll(Integer isShow);
}

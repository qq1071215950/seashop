package com.haojing.service;

import com.haojing.bo.CarouselBO;
import com.haojing.entity.Carousel;

import java.util.List;

public interface CarouselService {

    public List<Carousel> queryAll(Integer isShow);

    /**
     * 添加轮播图信息
     * @param carouselBO
     */
    void addCarousel(CarouselBO carouselBO);


    /**
     * 通过轮播图id删除轮播图信息
     * @param carouselId
     */
    void deleteCarouselById(String carouselId);
}

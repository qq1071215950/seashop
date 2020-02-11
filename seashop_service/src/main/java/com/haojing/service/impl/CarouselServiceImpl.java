package com.haojing.service.impl;

import com.haojing.bo.CarouselBO;
import com.haojing.entity.Carousel;
import com.haojing.mapper.CarouselMapper;
import com.haojing.service.CarouselService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);
        List<Carousel> list = carouselMapper.selectByExample(example);
        return list;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addCarousel(CarouselBO carouselBO) {
        Carousel carousel = new Carousel();
        BeanUtils.copyProperties(carouselBO, carousel);
        carousel.setId(sid.nextShort());
        carousel.setCreateTime(new Date());
        carousel.setUpdateTime(new Date());
        carouselMapper.insert(carousel);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteCarouselById(String carouselId) {
        carouselMapper.deleteByPrimaryKey(carouselId);
    }
}

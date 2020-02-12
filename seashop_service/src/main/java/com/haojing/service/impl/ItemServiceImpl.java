package com.haojing.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.haojing.bo.ItemBO;
import com.haojing.bo.ItemsImgBO;
import com.haojing.bo.ItemsParamBO;
import com.haojing.bo.ItemsSpecBO;
import com.haojing.domain.ItemsCustom;
import com.haojing.domain.ItemsImgCustom;
import com.haojing.domain.ItemsParamCustom;
import com.haojing.domain.ItemsSpecCustom;
import com.haojing.entity.*;
import com.haojing.enums.CommentLevel;
import com.haojing.enums.YesOrNo;
import com.haojing.mapper.*;
import com.haojing.result.PagedGridResult;
import com.haojing.service.ItemService;
import com.haojing.utlis.DesensitizationUtil;
import com.haojing.vo.CommentLevelCountsVO;
import com.haojing.vo.ItemCommentVO;
import com.haojing.vo.SearchItemsVO;
import com.haojing.vo.ShopcartVO;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    @Autowired
    private ItemsMapper itemsMapper;
    @Autowired
    private ItemsImgMapper itemsImgMapper;
    @Autowired
    private ItemsSpecMapper itemsSpecMapper;
    @Autowired
    private ItemsParamMapper itemsParamMapper;
    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;
    @Autowired
    private ItemsMapperCustom itemsMapperCustom;
    @Autowired
    private Sid sid;
    @Autowired
    private RedisTemplate redisTemplate;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsCustom queryItemById(String itemId) {
        // 1 先从缓存中查询
        Items items = (Items) redisTemplate.opsForValue().get("item"+itemId);
        System.out.println("从缓存中获取");
        if (items == null){
            // 2 从数据库中获取
            items = itemsMapper.selectByPrimaryKey(itemId);
            System.out.println("从数据库中获取");
            // todo 注意该处涉及到网路传输，所以保存的对象需要实现序列化。
            redisTemplate.opsForValue().set("item"+itemId, items);
        }
        ItemsCustom itemsCustom = new ItemsCustom();
        BeanUtils.copyProperties(items,itemsCustom);
        return itemsCustom;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImgCustom> queryItemImgList(String itemId) {
        List<ItemsImg> itemsImgs = (List<ItemsImg>) redisTemplate.opsForValue().get("img"+itemId);
        System.out.println("图片从缓存中获取");
        if (CollectionUtils.isEmpty(itemsImgs)){
            Example itemsImgExp = new Example(ItemsImg.class);
            Example.Criteria criteria = itemsImgExp.createCriteria();
            criteria.andEqualTo("itemId", itemId);
            itemsImgs = itemsImgMapper.selectByExample(itemsImgExp);
            System.out.println("图片从数据库中获取");
            redisTemplate.opsForValue().set("img"+itemId, itemsImgs);
        }
        List<ItemsImgCustom> list = itemsImgs.stream().map(x -> {
            ItemsImgCustom itemsImgCustom = new ItemsImgCustom();
            BeanUtils.copyProperties(x, itemsImgCustom);
            return itemsImgCustom;
        }).collect(Collectors.toList());
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpecCustom> queryItemSpecList(String itemId) {
        List<ItemsSpec> itemsSpecs = (List<ItemsSpec>) redisTemplate.opsForValue().get("spc"+itemId);
        System.out.println("商品规格数据从缓存中获取");
        if (CollectionUtils.isEmpty(itemsSpecs)){
            Example itemsSpecExp = new Example(ItemsSpec.class);
            Example.Criteria criteria = itemsSpecExp.createCriteria();
            criteria.andEqualTo("itemId", itemId);
            itemsSpecs = itemsSpecMapper.selectByExample(itemsSpecExp);
            System.out.println("商品规格数据从数据库中获取");
            redisTemplate.opsForValue().set("spc"+itemId, itemsSpecs);
        }
        List<ItemsSpecCustom> list = itemsSpecs.stream().map(x -> {
            ItemsSpecCustom itemsSpecCustom = new ItemsSpecCustom();
            BeanUtils.copyProperties(x, itemsSpecCustom);
            return itemsSpecCustom;
        }).collect(Collectors.toList());
        return list;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParamCustom queryItemParam(String itemId) {
        ItemsParam itemsParam = (ItemsParam) redisTemplate.opsForValue().get("parm"+itemId);
        System.out.println("商品参数从缓存中获取");
        if (itemsParam == null){
            Example itemsParamExp = new Example(ItemsParam.class);
            Example.Criteria criteria = itemsParamExp.createCriteria();
            criteria.andEqualTo("itemId", itemId);
            itemsParam = itemsParamMapper.selectOneByExample(itemsParamExp);
            System.out.println("商品参数从数据库中获取");
            redisTemplate.opsForValue().set("parm"+itemId, itemsParam);
        }
        ItemsParamCustom itemsParamCustom = new ItemsParamCustom();
        BeanUtils.copyProperties(itemsParam,itemsParamCustom);
        return itemsParamCustom;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;

        CommentLevelCountsVO countsVO = new CommentLevelCountsVO();
        countsVO.setTotalCounts(totalCounts);
        countsVO.setGoodCounts(goodCounts);
        countsVO.setNormalCounts(normalCounts);
        countsVO.setBadCounts(badCounts);
        return countsVO;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("itemId", itemId);
        map.put("level", level);
        PageHelper.startPage(page, pageSize);

        List<ItemCommentVO> list = itemsMapperCustom.queryItemComments(map);
        for (ItemCommentVO vo : list) {
            // 对用户名进行脱敏感模糊处理
            vo.setNickname(DesensitizationUtil.commonDisplay(vo.getNickname()));
        }
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searhItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("keywords", keywords);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItems(map);
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searhItems(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("catId", catId);
        map.put("sort", sort);
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> list = itemsMapperCustom.searchItemsByThirdCat(map);
        return setterPagedGrid(list, page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String itemSpecIds) {
        String ids[] = itemSpecIds.split(",");
        List<String> specIdsList = new ArrayList<>();
        Collections.addAll(specIdsList, ids);
        return itemsMapperCustom.queryItemsBySpecIds(specIdsList);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String itemSpecId) {
        return itemsSpecMapper.selectByPrimaryKey(itemSpecId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String itemSpecId, int buyCounts) {
        // synchronized 不推荐使用，集群下无用，性能低下
        // 锁数据库: 不推荐，导致数据库性能低下
        // 分布式锁 zookeeper redis

        // lockUtil.getLock(); -- 加锁

        // 1. 查询库存
//        int stock = 10;

        // 2. 判断库存，是否能够减少到0以下
//        if (stock - buyCounts < 0) {
        // 提示用户库存不够
//            10 - 3 -3 - 5 = -1
//        }

        // lockUtil.unLock(); -- 解锁

        int result = itemsMapperCustom.decreaseItemSpecStock(itemSpecId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void upOrDownItems(List<String> itemIds, Integer type) {
        // todo 该方式在实际中不会用，性能低下
        // 1、商品商家
        if (type == 1){
            for (String itemsId: itemIds) {
                Items items = new Items();
                items.setId(itemsId);
                items.setOnOffStatus(1);
                items.setUpdatedTime(new Date());
                itemsMapper.updateByPrimaryKeySelective(items);
            }
        }
        // 2：商品下架
        if (type == 2){
            for (String itemsId: itemIds) {
                Items items = new Items();
                items.setId(itemsId);
                items.setOnOffStatus(2);
                items.setUpdatedTime(new Date());
                itemsMapper.updateByPrimaryKeySelective(items);
                redisTemplate.delete("item"+itemsId);
                redisTemplate.delete("img"+itemsId);
                redisTemplate.delete("spc"+itemsId);
                redisTemplate.delete("parm"+itemsId);
            }
        }


    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Boolean queryItemIsExit(String itemId) {
        Items items = itemsMapper.selectByPrimaryKey(itemId);
        return items == null ? false : true;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addItems(ItemBO itemBO) {
        Items items = new Items();
        String itemId = sid.nextShort();
        BeanUtils.copyProperties(itemBO, items);
        items.setId(itemId);
        items.setSellCounts(0);
        items.setCreatedTime(new Date());
        items.setUpdatedTime(new Date());
        int i = itemsMapper.insert(items);
        if (i != 1){
            throw new RuntimeException("添加商品失败");
        }
        // 商品参数数据
        ItemsParamBO itemsParamBO = itemBO.getItemsParamBO();
        String pid = sid.nextShort();
        ItemsParam itemsParam = new ItemsParam();
        BeanUtils.copyProperties(itemsParamBO, itemsParam);
        itemsParam.setId(pid);
        itemsParam.setItemId(itemId);
        itemsParam.setCreatedTime(new Date());
        itemsParam.setUpdatedTime(new Date());
        int i1 = itemsParamMapper.insert(itemsParam);
        if (i1 != 1){
            throw new RuntimeException("添加商品参数失败");
        }
        // 商品规格数据
        List<ItemsSpecBO> list = itemBO.getItemsSpecBOList();
        for (ItemsSpecBO itemsSpecBO : list){
            String specId = sid.nextShort();
            ItemsSpec itemsSpec = new ItemsSpec();
            BeanUtils.copyProperties(itemsSpecBO, itemsSpec);
            itemsSpec.setId(specId);
            itemsSpec.setItemId(itemId);
            itemsSpec.setCreatedTime(new Date());
            itemsSpec.setUpdatedTime(new Date());
            itemsSpecMapper.insert(itemsSpec);
        }
        // 商品图片信息
        List<ItemsImgBO> imgBOList = itemBO.getImgBOList();
        for (ItemsImgBO itemsImgBO : imgBOList) {
            String picId = sid.nextShort();
            ItemsImg itemsImg = new ItemsImg();
            BeanUtils.copyProperties(itemsImgBO, itemsImg);
            itemsImg.setId(picId);
            itemsImg.setItemId(itemId);
            itemsImg.setCreatedTime(new Date());
            itemsImg.setUpdatedTime(new Date());
            itemsImgMapper.insert(itemsImg);
        }
    }


    private PagedGridResult setterPagedGrid(List<?> list, Integer page) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId, Integer level) {
        ItemsComments condition = new ItemsComments();
        condition.setItemId(itemId);
        if (level != null) {
            condition.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(condition);
    }

}

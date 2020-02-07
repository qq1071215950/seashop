package com.haojing.vo;


import com.haojing.domain.ItemsCustom;
import com.haojing.domain.ItemsImgCustom;
import com.haojing.domain.ItemsParamCustom;
import com.haojing.domain.ItemsSpecCustom;
import lombok.Data;

import java.util.List;

/**
 * 商品详情VO
 */
@Data
public class ItemInfoVO {

    private ItemsCustom item;
    private List<ItemsImgCustom> itemImgList;
    private List<ItemsSpecCustom> itemSpecList;
    private ItemsParamCustom itemParams;

}

package com.esportzoo.esport.manager;

import com.esportzoo.common.util.MathUtil;
import com.esportzoo.common.util.RandomUtil;
import com.esportzoo.esport.connect.response.goods.GoodsDetailResponse;
import com.esportzoo.esport.constants.shop.ShopGoodsLabel;
import com.esportzoo.esport.vo.shop.ShopGoodsInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * @description:
 *
 * @author: Haitao.Li
 *
 * @create: 2019-07-25 14:34
 **/
@Component
public class ShopManager {

	private transient static final Logger logger = LoggerFactory.getLogger(ShopManager.class);

	@Value("${shop.image.host}")
	private String shopImageHost;

	public List<String> getGoodsImageList(String image) {
		List<String> list = new ArrayList<>();
		if (StringUtils.isNotBlank(image)) {
			String[] arr = image.split(",");
			for (String str : arr) {
				if (StringUtils.isNotBlank(str.trim())) {
					list.add(shopImageHost + str);
				}
			}
		}
		return list;
	}

	public GoodsDetailResponse toConvertResponse(ShopGoodsInfo shopGoodsInfo) {
		GoodsDetailResponse resp = new GoodsDetailResponse();
		String[] ignore = {"Icons"};
		BeanUtils.copyProperties(shopGoodsInfo, resp, ignore);

		resp.setPayScore(shopGoodsInfo.getPayScore());
		resp.setIcons(shopGoodsInfo.getIcons());
		/** 添加虚拟购买用户头像，补齐6个*/
		LinkedHashSet<String> icons = resp.getIcons();
		if (icons == null || icons.isEmpty() || icons.size() < 6) {
			int len = 6;
			if (icons==null){
				icons =  new LinkedHashSet<String>();
			}else {
				len = 6 - icons.size();
			}

			for (int i = 0; i < len; i++) {
				int random = RandomUtil.getRandom(10000, 10506);
				String randomAvartar = "https://rs.esportzoo.com/upload/schedule/user_icon/user_icon_" + random + ".jpg";
				icons.add(randomAvartar);
			}
			resp.setIcons(icons);
		}
		resp.setImageList(getGoodsImageList(resp.getImage()));
		if (null != shopGoodsInfo.getLabel() && shopGoodsInfo.getLabel() > 0) {
			resp.setLabelName(ShopGoodsLabel.valueOf(shopGoodsInfo.getLabel()).getDescription());
		}
		return resp;
	}

}

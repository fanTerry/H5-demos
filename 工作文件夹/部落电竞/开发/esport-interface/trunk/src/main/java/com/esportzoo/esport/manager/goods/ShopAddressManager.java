package com.esportzoo.esport.manager.goods;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.esportzoo.common.appmodel.domain.result.ModelResult;
import com.esportzoo.esport.client.service.shop.ShopAddressServiceClient;
import com.esportzoo.esport.constants.shop.ShopAddressDefaulted;
import com.esportzoo.esport.constants.shop.ShopAddressStatus;
import com.esportzoo.esport.domain.ShopAddress;

@Component
public class ShopAddressManager {

	@Autowired
	@Qualifier("shopAddressServiceClient")
	private ShopAddressServiceClient shopAddressServiceClient;

	public ShopAddress getShopAddressById(Long addressId) {
		if (addressId == null || addressId.longValue() <= 0)
			return null;
		ModelResult<ShopAddress> modelResult = shopAddressServiceClient.queryById(addressId);
		if (modelResult == null || !modelResult.isSuccess())
			return null;
		return modelResult.getModel();
	}

	public ShopAddress queryDefaultAddress(Long consumerId) {
		if (consumerId == null || consumerId.longValue() <= 0)
			return null;
		ShopAddress shopAddress = new ShopAddress();
		shopAddress.setUserId(consumerId);
		shopAddress.setDefaulted((short) ShopAddressDefaulted.DEFAULT.getIndex());
		shopAddress.setStatus((short) ShopAddressStatus.VALID.getIndex());
		ModelResult<List<ShopAddress>> modelResult = shopAddressServiceClient.queryList(shopAddress);
		if (modelResult == null || !modelResult.isSuccess() || modelResult.getModel() == null || modelResult.getModel().size() == 0) {
			return null;
		}
		return modelResult.getModel().get(0);
	}

}

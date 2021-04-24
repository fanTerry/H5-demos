package com.esportzoo.esport.controller.ws.constants;

import java.util.List;

import com.esportzoo.esport.constants.BaseType;

public class CmdType extends BaseType {
	 
		private static final long serialVersionUID = -8153531076690901527L;


		protected CmdType(Integer index, String description) {
			super(index, description);
		}

		/**  进入场景*/
		public static CmdType in = new CmdType(21, "进入场景");
		
		/** 离开场景*/
		public static CmdType out = new CmdType(22, "离开场景");
		
		/**  获取指定场景下的一组用户*/
		public static CmdType get = new CmdType(33, "获取场景内用户");
	 

		public static List<CmdType> getAllList() {
			return getAll(CmdType.class);
		}

		public static CmdType valueOf(Integer index) {
			return valueOf(CmdType.class, index);
		}
	}

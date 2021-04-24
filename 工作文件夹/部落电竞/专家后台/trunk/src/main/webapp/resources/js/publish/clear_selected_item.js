var clearSelectedItem = {};
clearSelectedItem.clear = function() {
	var game_id = $("#jq_improv_gameId").val();
	if(game_id == 401 || game_id == 402) {
		$("#sfc_table tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp p").each(function(){
				if($(this).hasClass("gm_bg2")){
					$(this).removeClass("gm_bg2");
				}
			});
		});
		return;
	}
	if(game_id == 4071) {
		if($("#jczq_2c1_table")) {
			$("#jczq_2c1_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
				$(this).find("td.Jq_sp p:first-child").each(function(){
					if($(this).hasClass("bgp_sp2")){
						$(this).removeClass("bgp_sp2");
					}
				});
				$(this).find("td.Jq_sp p:last-child").each(function(){
					if($(this).hasClass("bgp_sp2")){
						$(this).removeClass("bgp_sp2");
					}
				});
			});
			return;
		}
		if($("#jczq_dg_table")) {
			$("#jczq_dg_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
				$(this).find("td.Jq_sp p:first-child").each(function(){
					if($(this).hasClass("bgp_sp2")){
						$(this).removeClass("bgp_sp2");
					}
				});
				$(this).find("td.Jq_sp p:last-child").each(function(){
					if($(this).hasClass("bgp_sp2")){
						$(this).removeClass("bgp_sp2");
					}
				});
			});
			return;
		}
	}
	
	if(game_id == 4078) {
		$("#jczq_yp_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp").each(function(){
				if($(this).hasClass("gm_bg2")){
					$(this).removeClass("gm_bg2");
				}
			});
		});	
		return;
	}
	
	if(game_id == 4079) {
		$("#jczq_dxq_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp").each(function(){
				if($(this).hasClass("gm_bg2")){
					$(this).removeClass("gm_bg2")
				}
			});
		});
		return;
	}
		
	if(game_id == 4075) {
		$("#jczq_hh_table table.unfold").each(function(){
			$(this).find("tr").each(function(){
				var gameType = $(this).data("gametype");
				if(4071 == gameType){	//胜平负
					$(this).find("td:nth-child(n+2)").each(function(){
						if($(this).hasClass("chos_td")){
							$(this).removeClass("chos_td");
						}
					});
				}else if(4076 == gameType){	//让球胜平负
					var handicap = $(this).find("td:first").data("handicap");
					$(this).find("td:nth-child(n+2)").each(function(){
						if($(this).hasClass("chos_td")){
							$(this).removeClass("chos_td");
						}
					});
					
				}else if(4078 == gameType){	//亚盘
					$(this).find("td:nth-child(n+2)").each(function(){
						if($(this).hasClass("chos_td")){
							$(this).removeClass("chos_td");
						}
					});
				}else if(4079 == gameType){	//大小球
					$(this).find("td:nth-child(n+2)").each(function(){
						if($(this).hasClass("chos_td")){
							$(this).removeClass("chos_td");
						}
					});
				}
			});
		});
		return;
	}
	
	if(game_id == 4061) {
		$("#jclq_sf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp").each(function(){
				if($(this).hasClass("bgp_sp2")){
					$(this).removeClass("bgp_sp2");
				}
			});
		});
		return;
	}
	
	if(game_id == 4062) {
		$("#jclq_rfsf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp").each(function(){
				if($(this).hasClass("bgp_sp2")){
					$(this).removeClass("bgp_sp2");
				}
			});
		});
		return;
	}
	
	if(game_id == 4063) {
		$("#jclq_sfc_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("table td.Jq_sp").each(function(){
				if($(this).hasClass("bgp_sp2")){
					$(this).removeClass("bgp_sp2");
				}
			});
		});
		return;
	}
	
	if(game_id == 4064) {
		$("#jclq_dxf_table tbody:gt(0) tr:nth-child(n+2)").each(function(){
			$(this).find("td.Jq_sp").each(function(){
				if($(this).hasClass("bgp_sp2")){
					$(this).removeClass("bgp_sp2");
				}
			});
		});
		return;
	}
	
	if(game_id == 4065) {
		if($("#jclq_2c1_table")) {
			$("#jclq_hh_table table.unfold").each(function(){
				$(this).find("tr").each(function(){
					var gameType = $(this).data("gametype");
					if(publish.gameType.JJ_LC_RFSF == gameType){	//让分胜负
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}else if(publish.gameType.JJ_LC_DXF == gameType){	//大小分
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}else if(publish.gameType.JJ_LC_SF == gameType){	//胜负
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
						
					}else if(publish.gameType.JJ_LC_SFC == gameType){	//胜分差
						$(this).find("td.Jq_sp").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}
					
				});
			});
			return;
		}
		
		if($("#jclq_hh_table")) {
			$("#jclq_hh_table table.unfold").each(function(){
				$(this).find("tr").each(function(){
					var gameType = $(this).data("gametype");
					if(4062 == gameType){	//让分胜负
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}else if(4064 == gameType){	//大小分
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}else if(4061 == gameType){	//胜负
						$(this).find("td:nth-child(n+2)").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
						
					}else if(4063 == gameType){	//胜分差
						$(this).find("td.Jq_sp").each(function(){
							if($(this).hasClass("chos_td")){
								$(this).removeClass("chos_td");
							}
						});
					}
					
				});
			});
			return;
		}
	}
}

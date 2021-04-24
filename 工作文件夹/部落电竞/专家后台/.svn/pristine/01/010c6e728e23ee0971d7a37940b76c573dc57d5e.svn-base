<%@page import="org.apache.commons.lang3.ArrayUtils"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>预览</title>
<meta name="viewport"
	content="initial-scale=1, maximum-scale=1, user-scalable=no, width=device-width">
<meta name="format-detection" content="telephone=no">
<link href="/resources/css/ionic.css" rel="stylesheet">
<link href="/resources/css/hw_style.css" rel="stylesheet">
<link href="/resources/css/preview-style.css" rel="stylesheet">
<style>
body {
	width: 375px;
	margin: 50px auto 0;
	border: 1px solid #ccc;
	height: 667px;
	width: 375px;
	overflow-y: auto;
}
</style>
</head>

<body>
	<div class="wrap v2_tuijian q_homepageTop pr">
		<h1>专家解读预览</h1>
		<div class="q_bg_color"></div>
	</div>
	<div class="bg_fff">
		<div class="hw_referee">
			<h2>${previewVo.title}</h2>
			<div class="hw_date c999">
				<fmt:formatDate value="${previewVo.createTime.time}"
					pattern="MM-dd HH:mm" />
				<span class="hw_separate border bdr bdc_ddd"></span> 
				<span> 
					<c:choose>
						<c:when test="${previewVo.raceType == 1}">竞彩足球</c:when>
						<c:when test="${previewVo.raceType == 2}">竞彩篮球</c:when>
						<c:when test="${previewVo.raceType == 3}">14场胜负彩</c:when>
						<c:when test="${previewVo.raceType == 4}">任选9场</c:when>
						<c:otherwise>---</c:otherwise>
					</c:choose>
				</span> <span class="hw_separate border bdr bdc_ddd"></span> <span>未开赛</span>
			</div>
			<%-- <div class="q_sale_date border bda bdc_ddd bdradius20">
				开赛时间：
				<fmt:formatDate value="${previewVo.firstMatchTime.time}"
					pattern="MM-dd HH:mm" />
			</div> --%>
			<div class="abstractTitle"><i class="abstractTitle_icon"></i>
				${previewVo.summary }
			</div>
			<!-- <div class="v2_lock lineCenter pt20">
				<div class="border bdt bdc_e5"></div>
				<div class="lock_img">解读赛事</div>
			</div> -->

			<c:choose>
				<c:when test="${ previewVo.passType == 2}">
					<div class="matchListCard border bda bdc_ddd bdradius3">
						<ul>
							<c:forEach items="${previewVo.raceVoList}" var="raceVo">
								<li class="border bdb bdc_ddd"><!-- 每一场赛事选项 -->
					              <div class="matchTeam pr tac justify">
					                <div>
					                  <img src="http://l.aicaicdn.com${raceVo.homeTeamLogo}" alt="">
					                  <p>${raceVo.homeTeamName}</p>
					                </div>
					                <div>
					                  <p>${raceVo.leagueName}</p>
					                  <p><fmt:formatDate value="${raceVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
					                </div>
					                <div>
					                  <img src="http://l.aicaicdn.com${raceVo.guestTeamLogo}" alt=""> 
					                  <p>${raceVo.guestTeamName}</p>
					                </div>
					              </div>
					              <c:forEach items="${raceVo.playTypeVoList}" var="playTypeVo">
					              	<div class="matchCard border bdt bdc_ddd">
					                <div class="matchCard_2 clearfix">
					                  <div class="card_l fl border bdr bdc_454c5c">
					                    <p>
					                    	<c:choose>
												<c:when test="${playTypeVo.gameType == '4071' }">胜平负</c:when>
												<c:when test="${playTypeVo.gameType == '4076' }">让球胜平负</c:when>
												<c:when test="${playTypeVo.gameType == '4078' }">亚盘</c:when>
												<c:when test="${playTypeVo.gameType == '4079' }">大小球</c:when>
												
												<c:when test="${playTypeVo.gameType == '4061' }">胜负</c:when>
												<c:when test="${playTypeVo.gameType == '4062' }">让分胜负</c:when>
												<c:when test="${playTypeVo.gameType == '4063' }">胜分差</c:when>
												<c:when test="${playTypeVo.gameType == '4064' }">大小分</c:when>
												
												<c:otherwise>${playTypeVo.gameType}</c:otherwise>
											</c:choose>
					                    </p>
					                    <p>${playTypeVo.handicap }</p>
					                  </div>
					                   <div class="card_r_list fl justify">
					                     <c:forEach items="${playTypeVo.itemVoList}" var="itemVo">
					                     	<c:choose>
					                     		<c:when test="${playTypeVo.gameType == 4063}">
					                     			<c:if test="${itemVo.selected==true }">
					                     				<div class="active">
					                     				<p>
					                     					<c:choose>
																<c:when test="${itemVo.select=='01' || itemVo.select=='02' || itemVo.select=='03' 
																	|| itemVo.select=='04' || itemVo.select=='05' || itemVo.select=='06'}">主胜</c:when>
																<c:when test="${itemVo.select=='11' || itemVo.select=='12' || itemVo.select=='13'
																	|| itemVo.select=='14' || itemVo.select=='15' || itemVo.select=='16'}">客胜</c:when>
															</c:choose>
					                     				</p>
					                     				<p>
					                     					<c:choose>
																<c:when test="${itemVo.select == '01' || itemVo.select == '11'}">1-5</c:when>
																<c:when test="${itemVo.select == '02' || itemVo.select == '12'}">6-10</c:when>
																<c:when test="${itemVo.select == '03' || itemVo.select == '13'}">11-15</c:when>
																<c:when test="${itemVo.select == '04' || itemVo.select == '14'}">16-20</c:when>
																<c:when test="${itemVo.select == '05' || itemVo.select == '15'}">20-25</c:when>
																<c:when test="${itemVo.select == '06' || itemVo.select == '16'}">26+</c:when>
															</c:choose>
					                     				</p>
					                     				</div>
					                     			</c:if>
					                     		</c:when>
					                     		<c:otherwise>
					                     			<div class="card_r_3 <c:if test="${itemVo.selected == true}">active</c:if> border bdr bdb bdc_454c5c">
								                        <p>
															<!-- zq start -->
															<c:if test="${(playTypeVo.gameType==4071 || playTypeVo.gameType==4076)&&itemVo.select==3}">胜</c:if>
															<c:if test="${(playTypeVo.gameType==4071 || playTypeVo.gameType==4076)&&itemVo.select==1}">平</c:if>
															<c:if test="${(playTypeVo.gameType==4071 || playTypeVo.gameType==4076)&&itemVo.select==0}">负</c:if>
															
															<c:if test="${playTypeVo.gameType==4078&&itemVo.select==3}">胜</c:if>
															<c:if test="${playTypeVo.gameType==4078&&itemVo.select==0}">负</c:if>
															
															<c:if test="${playTypeVo.gameType==4079&&itemVo.select==3}">大球</c:if>
															<c:if test="${playTypeVo.gameType==4079&&itemVo.select==0}">小球</c:if>
															<!-- zq end -->
															<!-- lq start -->
																<!-- 胜负/让分胜负 -->
															<c:if test="${(playTypeVo.gameType==4061||playTypeVo.gameType==4062)&&itemVo.select==1}">主负</c:if>
															<c:if test="${(playTypeVo.gameType==4061||playTypeVo.gameType==4062)&&itemVo.select==2}">主胜</c:if>
																<!-- 大小分 -->
															<c:if test="${playTypeVo.gameType==4064&&itemVo.select==1}">大分</c:if>
															<c:if test="${playTypeVo.gameType==4064&&itemVo.select==2}">小分</c:if>
															<!-- lq end -->
														</p>
								                        <span>${itemVo.sp}</span>
								                     </div>
					                     		</c:otherwise>
					                     	</c:choose>
					                     </c:forEach>
					                    </div>
					                  </div>
					              </div>
					              </c:forEach>
					            </li>
							</c:forEach>
						</ul>
					</div>
				</c:when>
				<c:when
					test="${previewVo.gameType == 401 || previewVo.gameType == 402}">
					<div class="q_unscrambleCard_14">
						<dl>
							<dd>
								<ul class="justify">
									<li>
										<div class="Card_6_top">场次</div>
										<div class="Card_6_bottom">
											<span>主</span>
										</div>
										<div class="Card_6_top">推荐</div>
										<div class="Card_6_bottom">
											<span>客</span>
										</div>
									</li>

									<c:forEach items="${previewVo.matchVoList}" var="matchVo"
										varStatus="st">
										<li>
											<div class="Card_6_top">${st.count}</div>
											<div class="Card_6_bottom">
												<span>${matchVo.homeTeamName}</span>
											</div>
											<div class="Card_6_top active">${empty matchVo.result ? '-' : fn:replace(matchVo.result, ',', '')}
											</div>
											<div class="Card_6_bottom">
												<span>${matchVo.guestTeamName}</span>
											</div>
										</li>
									</c:forEach>
								</ul>
							</dd>
						</dl>
					</div>
				</c:when>
				<c:otherwise>
					<div class="matchListCard border bda bdc_ddd bdradius3">
						<ul>
							<c:forEach items="${previewVo.matchVoList}" var="matchVo">
								<c:if test="${matchVo.gameType == 4071}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName}</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>让球胜平负</p>
						                    <p></p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '3')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '1')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>平</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '0')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>负</p>
						                        <span>${fn:split(matchVo.sp, '-')[2]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
								</c:if>
								<c:if test="${matchVo.gameType == 4076}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>胜平负</p>
						                    <p>${matchVo.handicap}</p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '3')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '1')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>平</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '0')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>负</p>
						                        <span>${fn:split(matchVo.sp, '-')[2]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
									<%-- <li>
										<div class="Card_1 fl border bdr bdc_454c5c">
											<p class="fs12 c999">让球胜平负</p>
											<p class="fs12 c999">${matchVo.handicap}</p>
										</div>
										<div class="Card_2 fl border bdr bdc_454c5c">
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.homeTeamName}</span>
											</div>
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.guestTeamName}</span>
											</div>
										</div>
										<div class="Card_3 fl justify">
											<div
												<c:if test="${matchVo.result == '3'}">class="active"</c:if>>
												<p class="fs14 cfff">胜</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[0]}</p>
											</div>
											<div
												<c:if test="${matchVo.result == '1'}">class="active"</c:if>>
												<p class="fs14 cfff">平</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[1]}</p>
											</div>
											<div
												<c:if test="${matchVo.result == '0'}">class="active"</c:if>>
												<p class="fs14 cfff">负</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[2]}</p>
											</div>
										</div>
									</li> --%>
								</c:if>
								<c:if test="${matchVo.gameType == 4078}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>亚盘</p>
						                    <p>${matchVo.handicap}</p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '3')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '0')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>负</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
								</c:if>
								<c:if test="${matchVo.gameType == 4079}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>大小球</p>
						                    <p>${matchVo.handicap}</p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '3')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>大球</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '0')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>小球</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>									
								</c:if>

								<c:if test="${matchVo.gameType == 4061}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>胜负</p>
						                    <p></p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '1')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主负</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '2')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
								</c:if>
								<c:if test="${matchVo.gameType == 4062}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>让分胜负</p>
						                    <p></p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '1')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主负</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '2')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
									
									<%-- <li>
										<div class="Card_1 fl border bdr bdc_454c5c">
											<p class="fs12 c999">让分胜负</p>
											<p class="fs12 c999">${matchVo.handicap}</p>
										</div>
										<div class="Card_2 fl border bdr bdc_454c5c">
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.guestTeamName}</span>
											</div>
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.homeTeamName}</span>
											</div>
										</div>
										<div class="Card_3 fl justify">
											<div
												<c:if test="${fn:contains(matchVo.result, '2')}">class="active"</c:if>>
												<p class="fs14 cfff">主负</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[0]}</p>
											</div>
											<div
												<c:if test="${fn:contains(matchVo.result, '1')}">class="active"</c:if>>
												<p class="fs14 cfff">主胜</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[1]}</p>
											</div>
										</div>
									</li> --%>
								</c:if>
								<c:if test="${matchVo.gameType == 4063}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>胜分差</p>
						                    <p></p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <c:forEach items="${fn:split(matchVo.result, '-')}" var="rs">
												<div class="active">
													<p class="fs14 cfff">
														<c:choose>
															<c:when
																test="${rs=='01' || rs=='02' || rs=='03' || rs=='04' || rs=='05' || rs=='06'}">主胜</c:when>
															<c:when
																test="${rs=='11' || rs=='12' || rs=='13' || rs=='14' || rs=='15' || rs=='16'}">客胜</c:when>
														</c:choose>
													</p>
													<p class="fs12 c999">
														<c:choose>
															<c:when test="${rs == '01' || rs == '11'}">1-5</c:when>
															<c:when test="${rs == '02' || rs == '12'}">6-10</c:when>
															<c:when test="${rs == '03' || rs == '13'}">11-15</c:when>
															<c:when test="${rs == '04' || rs == '14'}">16-20</c:when>
															<c:when test="${rs == '05' || rs == '15'}">20-25</c:when>
															<c:when test="${rs == '06' || rs == '16'}">26+</c:when>
														</c:choose>
													</p>
												</div>
											</c:forEach>
						                     <%-- <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '3')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主负</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '0')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>主胜</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div> --%>
						                    </div>
						                  </div>
						              </div>
						            </li>
									
									<%-- <li>
										<div class="Card_1 fl border bdr bdc_454c5c">
											<p class="fs12 c999 lh40">胜分差</p>
										</div>
										<div class="Card_2 fl border bdr bdc_454c5c">
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.guestTeamName}</span>
											</div>
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.homeTeamName}</span>
											</div>
										</div>
										<div class="Card_3 fl justify">
											<c:forEach items="${fn:split(matchVo.result, '-')}" var="rs">
												<div class="active">
													<p class="fs14 cfff">
														<c:choose>
															<c:when
																test="${rs=='01' || rs=='02' || rs=='03' || rs=='04' || rs=='05' || rs=='06'}">主胜</c:when>
															<c:when
																test="${rs=='11' || rs=='12' || rs=='13' || rs=='14' || rs=='15' || rs=='16'}">客胜</c:when>
														</c:choose>
													</p>
													<p class="fs12 c999">
														<c:choose>
															<c:when test="${rs == '01' || rs == '11'}">1-5</c:when>
															<c:when test="${rs == '02' || rs == '12'}">6-10</c:when>
															<c:when test="${rs == '03' || rs == '13'}">11-15</c:when>
															<c:when test="${rs == '04' || rs == '14'}">16-20</c:when>
															<c:when test="${rs == '05' || rs == '15'}">20-25</c:when>
															<c:when test="${rs == '06' || rs == '16'}">26+</c:when>
														</c:choose>
													</p>
												</div>
											</c:forEach>
										</div>
									</li> --%>
								</c:if>
								<c:if test="${matchVo.gameType == 4064}">
									<li class="border bdb bdc_ddd">
						              <div class="matchTeam pr tac justify">
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
						                  <p>${matchVo.homeTeamName}</p>
						                </div>
						                <div>
						                  <p>${matchVo.leagueName }</p>
						                  <p><fmt:formatDate value="${matchVo.matchTime.time}" pattern="MM-dd HH:mm" /></p>
						                </div>
						                <div>
						                  <img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt=""> 
						                  <p>${matchVo.guestTeamName}</p>
						                </div>
						              </div>
						              <div class="matchCard border bdt bdc_ddd">
						                <div class="matchCard_2 clearfix">
						                  <div class="card_l fl border bdr bdc_454c5c">
						                    <p>大小分</p>
						                    <p></p>
						                  </div>
						                   <div class="card_r_list fl justify">
						                     <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '1')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>大分</p>
						                        <span>${fn:split(matchVo.sp, '-')[0]}</span>
						                     </div>
						                      <div class="card_r_3 <c:if test="${fn:contains(matchVo.result, '2')}">active</c:if> border bdr bdb bdc_454c5c">
						                        <p>小分</p>
						                        <span>${fn:split(matchVo.sp, '-')[1]}</span>
						                      </div>
						                    </div>
						                  </div>
						              </div>
						            </li>
									<%-- <li>
										<div class="Card_1 fl border bdr bdc_454c5c">
											<p class="fs12 c999">大小分</p>
											<p class="fs12 c999">${matchVo.handicap}</p>
										</div>
										<div class="Card_2 fl border bdr bdc_454c5c">
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.guestTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.guestTeamName}</span>
											</div>
											<div class="opponent_box">
												<div class="opponent_img">
													<img src="http://l.aicaicdn.com${matchVo.homeTeamLogo}" alt="">
												</div>
												<span class="team_name">${matchVo.homeTeamName}</span>
											</div>
										</div>
										<div class="Card_3 fl justify">
											<div <c:if test="${fn:contains(matchVo.result, '1')}">class="active"</c:if>>
												<p class="fs14 cfff">大分</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[0]}</p>
											</div>
											<div <c:if test="${fn:contains(matchVo.result, '2')}">class="active"</c:if>>
												<p class="fs14 cfff">小分</p>
												<p class="fs12 c999">${fn:split(matchVo.sp, '-')[1]}</p>
											</div>
										</div>
									</li> --%>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:otherwise>
			</c:choose>

			<div class="pt20">${previewVo.content}</div>
		</div>
	</div>
</body>
<script src="/resources/plugins/jquery/jquery-1.8.3.min.js"></script>
<script type="text/javascript">
	$("ul li:first-child ").addClass("active");
	$(document).on("click","ul li", function() {
		$(this).toggleClass("active");
	});
</script>
</html>
package com.linjia.web.controller;

import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.linjia.base.controller.BaseController;
import com.linjia.constants.Constants;
import com.linjia.tools.DateComFunc;
import com.linjia.tools.NetRequest;
import com.linjia.tools.Util;
import com.linjia.web.dao.AddressMapper;
import com.linjia.web.model.ActivityKaiTuanMain;
import com.linjia.web.model.ActivityKaiTuanUsers;
import com.linjia.web.model.ActivityPintuanBase;
import com.linjia.web.model.Address;
import com.linjia.web.model.PinTuanOrder;
import com.linjia.web.query.ActivityKaiTuanMainQuery;
import com.linjia.web.query.ActivityPintuanBaseQuery;
import com.linjia.web.service.ActivityKaiTuanMainService;
import com.linjia.web.service.ActivityKaiTuanUsersService;
import com.linjia.web.service.ActivityPintuanBaseService;
import com.linjia.web.service.OrderidGenerateService;
import com.linjia.web.service.PinTuanOrderService;
import com.linjia.web.service.ProductBannerImagesService;
import com.linjia.web.service.ProductTagsService;

/**
 * 拼团模块
 * 
 * @author xiangsy
 *
 */
@Controller
@RequestMapping("/pintuan")
public class ActivityPintuanController extends BaseController{
	private static Logger LOG = LoggerFactory.getLogger(ActivityPintuanController.class);
	
	@Autowired
	private ActivityPintuanBaseService activityPintuanBaseService;
	@Autowired
	private ProductBannerImagesService productBannerImagesService;
	@Autowired
	private ProductTagsService productTagsService;
	@Autowired
	private ActivityKaiTuanMainService activityKaiTuanMainService;
	@Autowired
	private ActivityKaiTuanUsersService activityKaiTuanUersService;
	@Autowired
	private PinTuanOrderService pinTuanOrderService;
	@Autowired
	private AddressMapper addressMapper;
	@Autowired
	private OrderidGenerateService orderidGenerateService;
	/**
	 * 取得拼团页面基本信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getBaseInfo")
	@ResponseBody
	public Object getBaseInfo(HttpServletRequest request){
		
		Map<String, Object> map = new HashMap<String, Object>();
//		String urlstr = "http://124.193.154.242:37080/h4rest-server/rest/h5rest-server/core/mdataservice/store/100007";
		String urlstr = "http://124.193.154.242:58280/hdcard-services/api/member/query_by_mobilenum?mobilenum=18510576310";
//		String urlstr = "http://yeeda-app-server.iyeeda.com/YidaAppServer_1_1/getMainPageList?usrid=13332&page=0&appv=235";
		String Authorization = "guest:guest";
		JSONObject obj = NetRequest.requestGet(urlstr, null, Authorization);
		System.out.println("result:"+obj.toString());
		map.put("result", obj);
		return map;
	}
	
	/**
	 * 取得拼团商品列表
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getPintuanProductList")
	@ResponseBody
	public Object getPintuanProductList(HttpServletRequest request,ActivityPintuanBaseQuery query){
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			List<ActivityPintuanBase> productList = activityPintuanBaseService.selectPintuanProductList(query);
			map.put("productList", productList);
			Util.writeOk(map);
		}catch(Exception e){
			map.put("message", "对不起，查询拼团列表出错！");
			Util.writeError(map);
			LOG.error("查询商品列表出错",e.getMessage());
		}
		
		return map;
	}
	
	/**
	 * 根据id取得拼团商品详情信息
	 * 
	 * @param id 商品id
	 * @return
	 */
	@RequestMapping("/getPintuanProductDetailInfo")
	@ResponseBody
	public Object getPintuanProductDetailInfo(HttpServletRequest request,@RequestParam int id){
		
		Map<String, Object> map = new HashMap<String, Object>();
		try{
			//取得基本信息
			ActivityPintuanBase activityPintuanBase = activityPintuanBaseService.selectDetailById(id);
			
			if(activityPintuanBase!=null){
				long productId = activityPintuanBase.getProductId();
				
				//取得商品轮播图
				List<String> bannerList = productBannerImagesService.selectAllByProductId(productId);
				activityPintuanBase.setBannerList(bannerList);
				
				//取得商品标签
				List<String> tagList = productTagsService.selectTagsByProductId(productId);
				activityPintuanBase.setTagList(tagList);
				map.put("productBaseDetail", activityPintuanBase);
				Util.writeOk(map);
			}
		}catch(Exception e){
			map.put("message", "对不起，查询拼团商品详情出错！");
			Util.writeError(map);
			e.printStackTrace();
			LOG.error("查询商品列表出错",e.getMessage());
		}
		return map;
	}
	
	/**
	 * 根据userId取得用户参加的所有开团列表
	 * 
	 * @param userId
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/getUserAllKaiTuanInfo")
	@ResponseBody
	public Object getUserAllKaiTuanInfo(HttpServletRequest request,ActivityKaiTuanMainQuery query){
		 Map<String, Object> map = new HashMap<String, Object>();
		 try{
			 if(query!=null){
				 List<ActivityKaiTuanMain> userAllKaiTuanList=activityKaiTuanMainService.selectUserAllKaiTuan(query);
				 map.put("myProductList", userAllKaiTuanList);
				 Util.writeOk(map);
			 }
		 }catch(Exception e){
			 e.printStackTrace();
			map.put("message", "对不起，查询用户拼团列表出错！");
			Util.writeError(map);
			LOG.error("查询商品列表出错",e.getMessage());
		}
		 return map;
	}
	 
	/**
	 * 获取单次参团的详细信息
	 * @param baseId 开团基数表id
	 * @param id 开团主表id
	 * @param request
	 * @return
	 */
	@RequestMapping("/getOneKaiTuanInfo")
	@ResponseBody
	public Object getOneKaiTuanInfo(HttpServletRequest request,ActivityKaiTuanMainQuery query){
		Map<String, Object> map = new HashMap<String, Object>();
		ActivityPintuanBase baseInfo=null;
		try{
			//获取拼团商品基本信息
			Integer baseId=query.getBaseId();
			if(!"".equals(baseId)){
				baseInfo = activityPintuanBaseService.selectDetailById(baseId);
				if(baseInfo!=null){
					map.put("productDetailInfo",baseInfo);
				}else{
					map.put("message", "团详情查询出错!");
					Util.writeError(map);
					return map;
				}
			}
			//获取本次参团信息
			if(!"".equals(query.getId())){
				ActivityKaiTuanMain actKtMain=activityKaiTuanMainService.selectKaiTuanActivity(query);
				map.put("pinTuanInfo",actKtMain);
				if(actKtMain!=null){
					Date endDate=baseInfo.getEndTime();
					//团购剩余时间
					long diffTime=0;
					//理论有效期
					Date creDate10hAfter=DateComFunc.addHour(actKtMain.getCreate_time(),Constants.PINTUAN_TIME_LIMIT.TIMELIMITNUM);
					Long diff10hAfter=DateComFunc.dateDiff(endDate,creDate10hAfter);
					Long diffendDate=DateComFunc.dateDiff(endDate,actKtMain.getCreate_time());
					if(diffendDate>0&&diff10hAfter<0){
						diffTime=DateComFunc.dateDiff(endDate,new Date());
					}else if(diffendDate>0&&diff10hAfter>0){
						 diffTime=DateComFunc.dateDiff(creDate10hAfter,new Date());
					}
					map.put("leaveTime",diffTime);
				}
				
				//获取参团用户头像
				Map<String,String> picMap=new HashMap<String,String>();
				picMap.put("kt_id", query.getId()+"");
				List<String> pics=activityKaiTuanUersService.selectOneKaiTuanUserPhoto(picMap);
				map.put("photos",pics);	
			}
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			//是否需要统计b_activity_main表已参团成功人数来统计
			map.put("invitePage", basePath+Constants.SHARE_PAGE.INVITEURL);
			map.put("send_price",Constants.PIN_TUAN.SEND_FEE);
			 Util.writeOk(map);
		}catch(Exception e){
			e.printStackTrace();
			map.put("message", "对不起，查询拼团详情出错！");
			Util.writeError(map);
			LOG.error("查询商品列表出错",e.getMessage());
		}
		return map;
	}
	
		/**
		 * 确认下单，添加开团信息，添加订单信息
		 * @param baseId 开团base表id
		 * @param userId 用户id
		 * @param address_id
		 * @param  remark
		 * @return
		 */
		@RequestMapping("/addOneKaiTuanInfo")
		@ResponseBody
		public Object addOneKaiTuanInfo(HttpServletRequest request,@RequestParam int baseId,@RequestParam String userId,@RequestParam Long address_id,@RequestParam String remark,@RequestParam int buyPersonNums){
			Map<String, Object> map = new HashMap<String, Object>();
			ActivityKaiTuanMain atMianInsertdto=new ActivityKaiTuanMain();
			ActivityKaiTuanUsers actUserDto=new ActivityKaiTuanUsers();
			int buyPersonNum=0;
			try{
				Integer ptId=baseId;
				if(ptId!=null&&ptId>0){
					//获取开团商品信息
					ActivityPintuanBase ptBaseQueryDto=activityPintuanBaseService.selectDetailById(ptId);
					//当前商品库存
					int kuCun=ptBaseQueryDto.getQuantity();
					if(kuCun<1){
						map.put("message", "对不起，库存不够,补货中！");	
						return map;
					}
					//当前参团商品结束时间
					Date endDate=ptBaseQueryDto.getEndTime();
					//获取当前时间
					Date curTime=new Date();
					LOG.debug("当前时间为:"+curTime+"/n当前参团商品结束时间为:"+endDate);
					long timeDiff=DateComFunc.dateDiff(endDate,curTime);
					if(timeDiff<300000){
						map.put("message", "对不起，该商品团购时间已结束!");	
						return map;
					}
					//生成拼团订单id
					Long orderId=orderidGenerateService.generatePintuanOrderId();
					int ktm=0;
					//插入拼团表数据
					if(buyPersonNums==2){
						buyPersonNum=2;
						//开团数据插入
						atMianInsertdto.setKt_userId(userId);
						atMianInsertdto.setBaseId(baseId);
						atMianInsertdto.setCur_num(0);
						int intNum=activityKaiTuanMainService.insertOneKaiTuanActivity(atMianInsertdto);
						if(intNum==1){
							//获取开团Id
							int	KtId=atMianInsertdto.getId();
							actUserDto.setUserId(userId.trim());
							actUserDto.setKt_id(KtId);
							//订单id
							actUserDto.setOrderId(orderId);
							ktm=activityKaiTuanUersService.insertOneKaiTuanUser(actUserDto);
						}
					}else{
						buyPersonNum=1;
						ktm=1;
					}
					if(ktm>0){
						//生成拼团订单
						PinTuanOrder pt=new PinTuanOrder();
						pt.setUser_id(userId);
						pt.setOrder_id(orderId);
						pt.setPay_status(0);
						pt.setBuyPersonNum(buyPersonNum);
						pt.setPt_id(atMianInsertdto.getId());
						pt.setPt_base_id(ptBaseQueryDto.getId());
						pt.setAdd_score(ptBaseQueryDto.getGiveScore());
						pt.setOnebuy_price(ptBaseQueryDto.getMarketPrice());
						BigDecimal price=null;
						BigDecimal total=null;
						if(buyPersonNums==2){
							 price=Util.mul(ptBaseQueryDto.getPtPrice(),Constants.PIN_TUAN.QUANTITY);
							 total=Util.add(price,Integer.parseInt(Constants.PIN_TUAN.SEND_FEE));
							 map.put("pt_price", ptBaseQueryDto.getPtPrice());
						}else{
							price=Util.mul(ptBaseQueryDto.getMarketPrice(),Constants.PIN_TUAN.QUANTITY);
							total=Util.add(price,Integer.parseInt(Constants.PIN_TUAN.SEND_FEE));
							map.put("pt_price", ptBaseQueryDto.getMarketPrice());
						}
						pt.setPrice(price);
						pt.setReal_price(total);
						pt.setP_name(ptBaseQueryDto.getpName());
						pt.setP_code(ptBaseQueryDto.getpCode());
						pt.setAddress_id(address_id);
						pt.setStatus(4);
						if(remark!=null && !"".equals(remark)){
							remark=URLDecoder.decode(remark);
						}
						pt.setRemark(remark);
						pt.setProduct_id(ptBaseQueryDto.getProductId());
						pt.setPt_status(1);
						pt.setPt_id(atMianInsertdto.getId());
						boolean b=pinTuanOrderService.insertPtOrder(pt);
						if(b){
							map.put("send_price", Constants.PIN_TUAN.SEND_FEE);
							map.put("p_name", ptBaseQueryDto.getpName());
							map.put("imagPath", ptBaseQueryDto.getImagePath());
							map.put("quantity", 1);
							map.put("order_id", orderId);
							map.put("price", pt.getReal_price());
							map.put("status", "ok");
							map.put("message", "恭喜你下单成功！");
							Util.writeOk(map);
							return map;
						}else{
							map.put("status", "fail");
							map.put("message", "生成订单失败！");
							return map;
						}
					}else{
						map.put("status", "fail");
						map.put("message", "插入用户数据失败！");
						return map;
					}
				}else{
					map.put("status", "fail");
					map.put("message", "未查到拼团商品信息！");
					return map;
				}
			}catch(Exception e){
				e.printStackTrace();
				map.put("message", "对不起，下单出现异常！");
				Util.writeError(map);
				LOG.error("下单出现异常",e.getMessage());
				return map;
			}
		}
		
		/**
		 * 接受邀请去参团
		 * @param id 开团主表id
		 * @param baseId 开团base表主键id
		 * @param userId 参团用户id
		 * @param address_id 地址id
		 * @return
		 */
		@RequestMapping("/joinOneKaiTuanInfo")
		@ResponseBody
		public Object joinOneKaiTuanInfo(HttpServletRequest request,ActivityKaiTuanMain model){
			Map<String, Object> map = new HashMap<String, Object>();
			ActivityKaiTuanUsers ktUserInsertDto=new ActivityKaiTuanUsers();
			ActivityKaiTuanMainQuery ktMainQueryDto=new ActivityKaiTuanMainQuery();
			Address address=null;
			try{
				//获取参团商品信息
				Integer baseId=model.getBaseId();
					if(baseId!=null){
						int baseIdVal=baseId.intValue();
						ActivityPintuanBase ptBaseQueryDto=activityPintuanBaseService.selectDetailById(baseIdVal);
						if(ptBaseQueryDto!=null){
							String userId=model.getUserId();
							if(userId!=null&&!(userId.isEmpty())){
								Long address_id=model.getAddress_id();
								if(address_id==null){
									map.put("status", "fail");
									map.put("message", "地址信息为空!");
									return map;
								}
								//取得用户最新使用地址
								address=addressMapper.selectByPrimaryKey(address_id);
								if(address==null){
									map.put("status", "fail");
									map.put("message", "未获取到地址信息");
									return map;
								}
							}else{
								map.put("status", "fail");
								map.put("message", "未获取到用户地址信息");
								return map;
							}
						}else{
							map.put("status", "fail");
							map.put("message", "未获取到拼团商品信息");
							return map;
						}
						if(ptBaseQueryDto!=null&&ptBaseQueryDto.getOnline()){
							//库存是否充足
							if(ptBaseQueryDto.getQuantity()>0){
								//当前参团商品人数上限
								int uperLimit=ptBaseQueryDto.getUpperLimit();
								LOG.debug("当前开团商品人数上限为:"+uperLimit);
								//当前参团商品人数下限
								int lowLimit=ptBaseQueryDto.getLowerLimit();
								LOG.debug("当前开团商品人数下限为:"+lowLimit);
								//获取团购主表id
								int tgId=model.getId();
								LOG.debug("团购主表id为:"+tgId);
								ktMainQueryDto.setId(tgId);
								//当前团购人数
								ActivityKaiTuanMain activityMain=activityKaiTuanMainService.selectKaiTuanActivity(ktMainQueryDto);
								if(activityMain==null){
									map.put("message", "对不起，为查询到该团信息");
									Util.writeError(map);
									return map;
								}else{
//									//查询该团是不是接收邀请人开的
//									String ktuserId=activityMain.getKt_userId();
//									if(ktuserId!=null&&(model.getUserId()).equals(ktuserId)){
//										map.put("message", "对不起,请勿重复参加该团!");
//										Util.writeError(map);
//										return map;
//									}
									String inusers=model.getUserId().trim();
									//查询已参团用户
									List<String> users=activityKaiTuanUersService.selectTeamUsers(tgId);
									if(users!=null&&users.size()>0){
										boolean cif=users.contains(inusers);
										if(cif){
											map.put("status", "fail");
											map.put("message", "对不起,请勿重复参加该团!");
											return map;
										}
									}
								}
								//当前商品此次团购已有人数
								int curNum=activityMain.getCur_num();
								//达到最低人数限制，成团，更新团购状态
								if(curNum+1>uperLimit){
									map.put("message", "对不起，本次团购已达人数上限,请开新团！");
									map.put("status", "fail");
									LOG.debug("当前团购已达人数上限！");
									return map;
								}
								//当前参团商品结束时间
								Date endDate=ptBaseQueryDto.getEndTime();
								//团购时间是否结束
								Long diffcreate=DateComFunc.dateDiff(endDate,activityMain.getCreate_time());
								Long orderId=orderidGenerateService.generatePintuanOrderId();
								if(diffcreate>0){
										//插入参团用户数据
										ktUserInsertDto.setUserId(model.getUserId().trim());
										ktUserInsertDto.setKt_id(model.getId());
										ktUserInsertDto.setOrderId(orderId);
										//插入数据，返回插入条数
										int intUserNum=activityKaiTuanUersService.insertOneKaiTuanUser(ktUserInsertDto);
										LOG.debug("接收邀请插入开团用户表数据条数为:"+intUserNum+"/n主键id为:"+ktUserInsertDto.getId());
	//									if(intNum==1){
	//										//更新产品库存
	//										//统计销售数量
	//										int salesNum=activityKaiTuanMainService.selectSumSalesNum(baseId);
	//										//更新库存，成团数，销售数量....?
	//										updBaseDto.setId(baseId);
	//									if(lowLimit==curNum+1){
	//										updBaseDto.setClusterNum(ptBaseQueryDto.getClusterNum()+1);
	//									}
	//											activityPintuanBaseService.updateActivityBaseInfo(updBaseDto);
	//									}
										if(intUserNum<1){
											map.put("message", "参团失败！");
											Util.writeFail(map);
											LOG.error("插入用户表数据失败！");
											return map;
										}
									}else{
										map.put("message", "对不起，本次团购已结束！");
										map.put("status", "ok");
										return map;
									}
									//生成拼团订单
									PinTuanOrder pt=new PinTuanOrder();
									pt.setUser_id(model.getUserId());
									pt.setOrder_id(orderId);
									pt.setPay_status(0);
									pt.setBuyPersonNum(2);
									pt.setPrice(ptBaseQueryDto.getPtPrice());
									pt.setPt_base_id(ptBaseQueryDto.getId());
									BigDecimal price=null;
									BigDecimal total=null;
//									if(model.getBuyPersonNums()==2){
										price=Util.mul(ptBaseQueryDto.getPtPrice(),Constants.PIN_TUAN.QUANTITY);
										total=Util.add(price,Integer.parseInt(Constants.PIN_TUAN.SEND_FEE));
//									}else{
//										price=Util.mul(ptBaseQueryDto.getMarketPrice(),Constants.PIN_TUAN.QUANTITY);
//										total=Util.add(price,Integer.parseInt(Constants.PIN_TUAN.SEND_FEE));
//									}
										pt.setPrice(price);
										pt.setReal_price(total);
										pt.setP_name(ptBaseQueryDto.getpName());
										pt.setP_code(ptBaseQueryDto.getpCode());
										pt.setAddress_id(model.getAddress_id());
										pt.setAdd_score(ptBaseQueryDto.getGiveScore());
										pt.setOnebuy_price(ptBaseQueryDto.getMarketPrice());
										pt.setStatus(4);
										pt.setRemark(model.getRemark());
										pt.setProduct_id(ptBaseQueryDto.getProductId());
										pt.setPt_status(1);
										pt.setPt_id(activityMain.getId());
										boolean b=pinTuanOrderService.insertPtOrder(pt);
										if(b){
											map.put("address", address);
											map.put("send_price", Constants.PIN_TUAN.SEND_FEE);
											map.put("p_name", ptBaseQueryDto.getpName());
											map.put("imagPath", ptBaseQueryDto.getImagePath());
											map.put("pt_price", ptBaseQueryDto.getPtPrice());
											map.put("quantity", 1);
											map.put("baseId",baseId);
											map.put("order_id", orderId);
										    map.put("price", pt.getReal_price());
											map.put("message","恭喜你，参团成功！");
										    Util.writeOk(map);
											return map;
										}else{
											map.put("status","fail");
											map.put("message","下单失败！");
											return map;
										}
								}else{
									map.put("message", "对不起，库存不足,补货中！");
									Util.writeFail(map);
									LOG.debug("当前开团商品库存不足！");
									return map;
								}
						}else{
							map.put("message","当前开团商品已下线！");
							Util.writeFail(map);
							LOG.debug("当前开团商品已下线！");
							map.put("message", "商品已下线！");
							return map;
						}
				}
			}catch(Exception e){
				e.printStackTrace();
				map.put("message","参加邀请发生异常！");
				Util.writeError(map);
				LOG.error("参加邀请发生异常",e.getMessage());
			}
			return map;
		}
	
		/**
		 * 点击去开团
		 * @param baseId 开团基数表id
		 * @param user_id 
		 * @return
		 */
		@RequestMapping("/goBeginCamp")
		@ResponseBody
		public Object goBeginCamp(HttpServletRequest request,@RequestParam int baseId ,@RequestParam String user_id){
			Map<String, Object> map = new HashMap<String, Object>();
			try{
				//取得基本信息
				ActivityPintuanBase activityPintuanBase = activityPintuanBaseService.selectDetailById(baseId);
				if(activityPintuanBase!=null){
					//取得用户最新使用地址
					Address address=addressMapper.selectOneByUserId(user_id);
					map.put("address", address);
					map.put("productInfo", activityPintuanBase);
					map.put("send_price", Constants.PIN_TUAN.SEND_FEE);
					Util.writeOk(map);
				}
			}catch(Exception e){
				Util.writeFail(map);
				Util.writeError(map);
				e.printStackTrace();
			}
			return map;
		}
		
		/**
		 * 确认下单，添加订单信息(单人购买参团(备用))
		 * @param baseId 开团base表id
		 * @param userId 用户id
		 * @param address_id
		 * @param  remark
		 * @return
		 */
		@RequestMapping("/addOneBuy")
		@ResponseBody
		public Object addOneBuy(HttpServletRequest request,@RequestParam int baseId,@RequestParam String userId,@RequestParam Long address_id,@RequestParam String remark){
			Map<String, Object> map = new HashMap<String, Object>();
			try{
				Integer ptId=baseId;
				if(ptId!=null){
					//获取开团商品信息
					ActivityPintuanBase ptBaseQueryDto=activityPintuanBaseService.selectDetailById(ptId);
					//当前商品库存
					int kuCun=ptBaseQueryDto.getQuantity();
					if(kuCun<1){
						map.put("message", "对不起，库存不够,补货中！");	
						return map;
					}
					//当前参团商品结束时间
					Date endDate=ptBaseQueryDto.getEndTime();
					//获取当前时间
					Date curTime=new Date();
					LOG.debug("当前时间为:"+curTime+"/n当前参团商品结束时间为:"+endDate);
					long timeDiff=DateComFunc.dateDiff(endDate,curTime);
					if(timeDiff<30000){
						map.put("message", "对不起，该商品团购时间已结束!");	
						map.put("status", "fail");
						return map;
					}
					Long orderId=orderidGenerateService.generatePintuanOrderId();
					//生成拼团订单
					PinTuanOrder pt=new PinTuanOrder();
					pt.setUser_id(userId);
					pt.setOrder_id(orderId);
					pt.setPay_status(0);
					pt.setBuyPersonNum(1);
					BigDecimal price=Util.mul(ptBaseQueryDto.getMarketPrice(),Constants.PIN_TUAN.QUANTITY);
					BigDecimal total=Util.add(price,Integer.parseInt(Constants.PIN_TUAN.SEND_FEE));
					pt.setPrice(price);
					pt.setReal_price(total);
					pt.setP_name(ptBaseQueryDto.getpName());
					pt.setP_code(ptBaseQueryDto.getpCode());
					pt.setPt_base_id(ptBaseQueryDto.getId());
					pt.setAdd_score(ptBaseQueryDto.getGiveScore());
					pt.setOnebuy_price(ptBaseQueryDto.getMarketPrice());
					pt.setAddress_id(address_id);
					pt.setStatus(4);
					if(remark!=null && !"".equals(remark)){
						remark=URLDecoder.decode(remark);
					}
					pt.setRemark(remark);
					pt.setProduct_id(ptBaseQueryDto.getProductId());
					pt.setPt_status(1);
					pt.setPt_id(ptId);
					boolean b=pinTuanOrderService.insertPtOrder(pt);
					if(b){
						map.put("send_price", Constants.PIN_TUAN.SEND_FEE);
						map.put("p_name", ptBaseQueryDto.getpName());
						map.put("imagPath", ptBaseQueryDto.getImagePath());
						map.put("quantity", 1);
						map.put("order_id", orderId);
						map.put("pt_price", ptBaseQueryDto.getMarketPrice());
						map.put("price", pt.getReal_price());
						map.put("status", "ok");
						map.put("message", "恭喜你下单成功！");
						Util.writeOk(map);
						return map;
					}else{
						map.put("message", "对不起，下单出错！");
						Util.writeError(map);
						return map;
					}
				}
				
			}catch(Exception e){
				map.put("message", "对不起，下单出现异常！");
				Util.writeError(map);
				LOG.error("下单出现异常",e.getMessage());
			}
			return map;
		}
		
		/**
		 * 测试支付模拟(备用)
		 * @param address_id
		 * @param  remark
		 * @return
		 */
		@RequestMapping("/orderPayTest")
		@ResponseBody
		public Object orderPayTest(HttpServletRequest request,@RequestParam Long orderId,@RequestParam int payType){
			Map<String, Object> map = new HashMap<String, Object>();
			Map<String, Object> befroemap=activityKaiTuanMainService.ptBeforePayed(orderId);
			boolean afterb=activityKaiTuanMainService.ptAfterPayed(orderId,payType);
			map.put("befroemap",befroemap);
			map.put("payAfter",afterb);
			return map;
		}
		
//		/**
//		 * 测试删除退款单(备用)
//		 * @param address_id
//		 * @param  remark
//		 * @return
//		 */
//		@RequestMapping("/delRefundOrder")
//		@ResponseBody
//		public Object delRefundOrder(HttpServletRequest request,@RequestParam Long orderId){
//			Map<String, Object> map = new HashMap<String, Object>();
//			boolean b=orderRefundService.deleteRefundOrderById(orderId);
//			map.put("delboolean",b);
//			return map;
//		}
		
}

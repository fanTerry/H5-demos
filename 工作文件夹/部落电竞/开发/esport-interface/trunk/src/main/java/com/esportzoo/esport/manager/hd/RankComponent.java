package com.esportzoo.esport.manager.hd;



import com.esportzoo.esport.hd.constants.HdEnable;

import com.esportzoo.esport.hd.entity.HdInfo;
import com.esportzoo.esport.hd.info.HdInfoServiceClient;
import com.esportzoo.esport.manager.RedisClientManager;

import com.esportzoo.esport.vo.quiz.QuizWinTopResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author lixisheng
 * @Date 2020/4/29 18:17
 */
@Component
public class RankComponent {

    private transient final Logger logger = LoggerFactory.getLogger(RankComponent.class);

    @Autowired
    private RedisClientManager redisClientManager;

    @Autowired
    @Qualifier("hdInfoServiceClient")
    private HdInfoServiceClient hdInfoServiceClient;

    private static final String ESPORT_QUIZ_WIN_RANK = "esport_quiz_win_rank";
    private static final Long MAX_TIME = 9999999999999L;
    private static final BigDecimal mulBigDecimal=new BigDecimal(String.valueOf(0.0000000001));


    private List<QuizWinTopResponse> buildRedisRankToBizDO(Set<ZSetOperations.TypedTuple<String>> result, long offset) {
        List<QuizWinTopResponse> rankList = new ArrayList<QuizWinTopResponse>(result.size());
        long rank = offset;
        for (ZSetOperations.TypedTuple<String> sub : result) {
            BigDecimal userRankScore = BigDecimal.valueOf(Math.abs(sub.getScore()));
            BigDecimal bigDecimal= userRankScore.setScale(2,BigDecimal.ROUND_DOWN);
            rankList.add(new QuizWinTopResponse(rank++,bigDecimal, Long.parseLong(sub.getValue())));
        }
        return rankList;
    }

    /**
     * 获取前n名的排行榜数据
     *
     * @param n
     * @return
     */
    public List<QuizWinTopResponse> getTopNRanks(int n) {
        Set<ZSetOperations.TypedTuple<String>> result = redisClientManager.zRangeWithScores(ESPORT_QUIZ_WIN_RANK, 0, n - 1);
        return buildRedisRankToBizDO(result, 1);
    }

    /**
     * 获取用户的排行榜位置
     *
     * @param userId
     * @return
     */
    public QuizWinTopResponse getRank(Long userId) {
        // 获取排行， 因为默认是0为开头，因此实际的排名需要+1
        Long rank = redisClientManager.zRank(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId));
        if (rank == null) {
            // 没有排行时，直接返回一个默认的
            return new QuizWinTopResponse(-1L, new BigDecimal(0), userId);
        }
        // 获取金额
        Double score = redisClientManager.zScore(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId));
        BigDecimal userRankScore = BigDecimal.valueOf(Math.abs(score));
        BigDecimal bigDecimal= userRankScore.setScale(2,BigDecimal.ROUND_DOWN);
        return new QuizWinTopResponse(rank + 1,bigDecimal, userId);
    }

    /**
     * 添加用户中奖总额，并获取最新的个人所在排行榜信息
     *
     * @param userId 操作的金额
     * @param score
     * @param betTime 订单的创建时间
     * @return
     */
    public QuizWinTopResponse updateRank(Long userId, BigDecimal score,Date betTime) {
        BigDecimal allScore=new BigDecimal(0.00);
        //step1 先查用户是否存在
        QuizWinTopResponse quizRank = getRank(userId);
        if (quizRank.getRank()<0){
            //不存在，添加到排行榜
            allScore=addAmount(score,betTime);
            redisClientManager.zAdd(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId), -allScore.doubleValue());
        }else {
            //用户存在排行榜
            BigDecimal rankScore = quizRank.getScore();
            BigDecimal add = rankScore.add(score);//相加
            allScore = addAmount(add, betTime);//拼接时间戳
            // 因为zset默认积分小的在前面，所以我们对score进行取反，这样用户的积分越大，对应的score越小，排名越高
            redisClientManager.zAdd(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId), -allScore.doubleValue());
        }
        Long rank = redisClientManager.zRank(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId));
        return new QuizWinTopResponse(rank + 1, allScore, userId);
    }


    /**
     * 删除指定的用户
     * @param userId
     * @return
     */
    public Long delUserRank(Long userId){
        Long zRemove = redisClientManager.zRemove(ESPORT_QUIZ_WIN_RANK,userId.toString());
        return zRemove;
    }

    /**
     * 移除所有排行榜信息，0开始，-1表示到最后
     * @return
     */
    public Long delRank(){
        Long removeCount = redisClientManager.zRemoveRange(ESPORT_QUIZ_WIN_RANK, 0, -1);
        return removeCount;
    }


    /**
     * 拼接排行榜的分，加上时间戳
     * @param amount  正数
     * @param betTime
     * @return
     */
    public BigDecimal addAmount(BigDecimal amount,Date betTime){
        BigDecimal bigDecimal = new BigDecimal(0.00);
        //判断是否大于0
        if (amount.compareTo(BigDecimal.ZERO)<1){
            return bigDecimal;
        }
        //传入的时间是否为空
        if (betTime==null){
            betTime=new Date();
        }
        long time=MAX_TIME-betTime.getTime();//反转时间戳，时间越早，排名越靠前
        String tr=time+"";
        String substring = tr.substring(5); //截取后面的8位，double在zset，精度只有16位；百万级别内排序精度不丢失
        long parseLong = Long.parseLong(substring);
        BigDecimal timeBigDecimal = new BigDecimal(parseLong);//long转为bigDecimal
        BigDecimal multiply = timeBigDecimal.multiply(mulBigDecimal);//把反转时间戳转为小数0.00111...格式，小数点
        BigDecimal addBigDecimal = new BigDecimal(String.valueOf(amount));
        bigDecimal = addBigDecimal.add(multiply);// 12.04+0.00111... 格式为 12.04111...
        return  bigDecimal;
    }

    /**
     * hd105排行榜，派奖成功后调用，往zset加入总额
     * @param userId
     * @param score
     */
    public Long addHd105Rank(Long userId, BigDecimal score,Date betTime){
        /**step1、 前置活动是否有存在 */
        HdInfo hdInfo = hdInfoServiceClient.queryByCode(105).getModel();
        if (hdInfo == null) {
            logger.info("没有查询到105活动");
            return -1L;
        }
        /**step2 活动是否有效 */
        if (hdInfo.getStatus()!= HdEnable.TRUE.getIndex()) {
            logger.info("105活动是无效的或暂停的");
            return -1L;
        }
        QuizWinTopResponse updateRank = updateRank(userId, score,betTime);
        return updateRank.getRank();
    }

    /**
     * hd105排行榜，派奖扣回后调用，减少个人zset总额
     * @param userId
     * @param amount
     */
    public QuizWinTopResponse reduceScoreRank(Long userId, BigDecimal amount){
        //step1 先查用户是否存在
        QuizWinTopResponse quizRank = getRank(userId);
        BigDecimal allScore = new BigDecimal(0.00);
        if (quizRank.getRank()<0){ //不存在，不处理rank
            return new QuizWinTopResponse(-1L, allScore, userId);
        }else {
            //存在排行榜
            BigDecimal rankScore = quizRank.getScore();
            //总额>扣减，减少总额
            if (rankScore.compareTo(amount)>0){
                BigDecimal bigDecimal= rankScore.setScale(2,BigDecimal.ROUND_DOWN);//去除时间戳
                BigDecimal delScore = bigDecimal.subtract(amount);//相减
                allScore = addAmount(delScore, new Date());//加上时间戳
                // 因为zset默认积分小的在前面，所以我们对score进行取反，这样用户的积分越大，对应的score越小，排名越高
                redisClientManager.zAdd(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId), -allScore.doubleValue());
            }else {
                // 总额<扣减,将该用户移除排行榜
                redisClientManager.zRemove(ESPORT_QUIZ_WIN_RANK, userId.toString());
                return new QuizWinTopResponse(-1L, allScore, userId);
            }
        }
        Long rank = redisClientManager.zRank(ESPORT_QUIZ_WIN_RANK, String.valueOf(userId));
        return new QuizWinTopResponse(rank + 1, allScore, userId);
    }



}

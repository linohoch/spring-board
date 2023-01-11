package com.example.springboard.proc;


import com.example.springboard.vo.UserInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    List<UserInfoVO> selectAll();
    @ResultMap({"ResultMap.UserInfoVO"})
    @Select("select * from user_sign where user_id=#{userId}")
    UserInfoVO selectUser(String userId);

    @ResultMap("ResultMap.integer")
    @Select("select count(*) from user_sign where user_id=#{userId}")
    int countUserById(String userId);

    @ResultMap({"ResultMap.UserInfoVO"})
    @Select("CALL p_user_info_sel(#{userId})")
    UserInfoVO selectUserById(String userId);

    @Select("SELECT user_no FROM user_basic WHERE user_id=#{userId}")
    UserInfoVO selectUserNoById(String userId);

    @ResultMap("ResultMap.integer")
    @Select("CALL user_signup_ins(#{userId},#{firstName},#{lastName},#{userBirthDate},#{userSex},#{ipAddress},#{pw})")
    int createUser(UserInfoVO pj07UserInfoVO);

    @Select("CALL pj1.p_user_social_signup_ins(#{userId},#{firstName},#{lastName},#{providerType})")
    int createSocialUser(UserInfoVO pj07UserInfoVO);

    @Select("CALL p_user_pw_enc_upd(#{userNo},#{pw})")
    int updateUserPwEnc(int userNo, String userPw);

    @Select("CALL pj1.p_user_login_v1(#{userId},#{inputPw})")
    String userLogin(String userId, String inputPw);

    @Update("update pj1.user_basic set last_login=current_timestamp where user_no=#{userNo};")
    int updateLoginTimestamp(int userNo);

    @ResultMap("ResultMap.map")
    @Select("select * from pj1.socail_token where soial_id=#{socialId} order by ins_date DESC limit 1")
    Map<String, Object> selectRefreshToken(String socialId);

    @Select("CALL pj1.p_refresh_token_ins(#{userId},#{provider},#{refreshToken},#{expDate})")
    void insertRefreshToken(String userId, String provider,String refreshToken, String expDate);

    @Select("CALL pj1.p_invalidate_refresh_token_del(#{userNo})")
    void deleteRefreshToken(int userNo);
}


package com.eighteen.fecom;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitAPI {
    @GET("/fecom/api/user/login.php")
    Call<String> getUserInfo(@Query("email") String userEmail, @Query("pw") String userPW);         //로그인

    @POST("/fecom/api/user/register.php")
    Call<String> postUserInfo(@Body JsonObject userData);               //회원가입

    @GET("/fecom/api/user/duplicate_check_nickname.php")
    Call<String> getCheckNick(@Query("nickname") String userNick);      //회원가입 시 닉네임 중복 확인

    @GET("/fecom/api/user/duplicate_check_email.php")
    Call<String> getCheckEmail(@Query("email") String userEmail);       //회원가입 시 이메일ID 중복 확인

    
    @GET("/fecom/api/board/search_all.php")
    Call<String> getBoard(@Query("user_id") int userID);                //전체 게시판 조회

    @FormUrlEncoded
    @POST("/fecom/api/board/register.php")
    Call<String> postAddBoard(@Field("board_name") String boardName, @Field("essential") int essential);        //게시판 추가


    @GET("/fecom/api/post/search_by_board.php")
    Call<String> getPosts(@Query("user_id") int userID, @Query("board_id") int boardID);            //해당 게시판의 게시글 조회

    @POST("/fecom/api/post/register.php")
    Call<String> postPostInfo(@Body JsonObject postData);               //게시글 등록
}

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


    @GET("/fecom/api/board/show_subscribe.php")
    Call<String> getSubscribeBoard(@Query("user_id") int userID);       //즐겨찾는 게시판 조회
    
    @GET("/fecom/api/board/search_all.php")
    Call<String> getBoard(@Query("user_id") int userID);                //전체 게시판 조회

    @FormUrlEncoded
    @POST("/fecom/api/board/register.php")
    Call<String> postAddBoard(@Field("board_name") String boardName, @Field("essential") int essential);        //게시판 추가

    @FormUrlEncoded
    @POST("/fecom/api/board/register_subscribe.php")
    Call<String> postSubscribeBoard(@Field("user_id") int userID, @Field("board_id") int boardID);           //게시판 구독

    @FormUrlEncoded
    @POST("/fecom/api/board/delete_subscribe.php")
    Call<String> postDeleteSubscribeB(@Field("user_id") int userID, @Field("board_id") int boardID);         //게시판 구독 취소

    @GET("/fecom/api/post/search_by_board.php")
    Call<String> getPosts(@Query("user_id") int userID, @Query("board_id") int boardID);            //해당 게시판의 게시글 조회

    @POST("/fecom/api/post/register.php")
    Call<String> postPostInfo(@Body JsonObject postData);               //게시글 등록

    @POST("/fecom/api/post/show_detail.php")
    Call<String> getPostInfo(@Query("user_id") int userID, @Query("post_id") int postID);                //게시글 세부 정보 조회
    
    @FormUrlEncoded
    @POST("/fecom/api/post/delete.php")
    Call<String> postDeletePost(@Field("post_id") int postID);          //게시글 삭제


    @FormUrlEncoded
    @POST("/fecom/api/postcomment/delete.php")
    Call<String> postDeleteComment(@Field("comment_id") int commentID); //댓글 삭제         -> TODO: 확인 바람!!! 시도 안해봄!
}

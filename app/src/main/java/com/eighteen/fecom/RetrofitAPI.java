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
    Call<String> getUserInfo(@Query("email") String userEmail, @Query("pw") String userPW);                     //로그인

    @GET("/fecom/api/user/search_by_id.php")
    Call<String> getUserInfoByID(@Query("user_id") int userID);         //회원 정보 받아오기

    @POST("/fecom/api/user/register.php")
    Call<String> postUserInfo(@Body JsonObject userData);               //회원가입

    @GET("/fecom/api/user/duplicate_check_nickname.php")
    Call<String> getCheckNick(@Query("nickname") String userNick);      //닉네임 중복 확인

    @GET("/fecom/api/user/duplicate_check_email.php")
    Call<String> getCheckEmail(@Query("email") String userEmail);       //이메일ID 중복 확인

    @FormUrlEncoded
    @POST("/fecom/api/user/edit_nickname.php")
    Call<String> postChangeNick(@Field("user_id") int userID, @Field("nickname") String nick);                  //닉네임 변경

    @FormUrlEncoded
    @POST("/fecom/api/user/edit_pw.php")
    Call<String> postChangePW(@Field("user_id") int userID, @Field("pw") String pw);                            //비밀번호 변경


    @GET("/fecom/api/notification/search.php")
    Call<String> getNotice(@Query("user_id") int userID);               //알림 조회


    @GET("/fecom/api/board/show_subscribe.php")
    Call<String> getSubscribeBoard(@Query("user_id") int userID);       //즐겨찾는 게시판 조회
    
    @GET("/fecom/api/board/search_all.php")
    Call<String> getBoard(@Query("user_id") int userID);                //전체 게시판 조회

    @GET("/fecom/api/board/search_by_keyword.php")
    Call<String> getSearchBoards(@Query("user_id") int userID, @Query("keyword") String keyword);            //키워드로 게시판 검색

    @FormUrlEncoded
    @POST("/fecom/api/board/register.php")
    Call<String> postAddBoard(@Field("board_name") String boardName, @Field("essential") int essential);        //게시판 추가

    @FormUrlEncoded
    @POST("/fecom/api/board/register_subscribe.php")
    Call<String> postSubscribeBoard(@Field("user_id") int userID, @Field("board_id") int boardID);              //게시판 구독

    @FormUrlEncoded
    @POST("/fecom/api/board/delete_subscribe.php")
    Call<String> postDeleteSubscribeB(@Field("user_id") int userID, @Field("board_id") int boardID);            //게시판 구독 취소


    @GET("/fecom/api/post/search_by_board.php")
    Call<String> getBoardPosts(@Query("user_id") int userID, @Query("board_id") int boardID);                   //해당 게시판의 게시글 조회

    @POST("/fecom/api/post/register.php")
    Call<String> postPostInfo(@Body JsonObject postData);               //게시글 등록

    @POST("/fecom/api/post/show_detail.php")
    Call<String> getPostInfo(@Query("user_id") int userID, @Query("post_id") int postID);                       //게시글 세부 정보 조회
    
    @FormUrlEncoded
    @POST("/fecom/api/post/delete.php")
    Call<String> postDeletePost(@Field("post_id") int postID);          //게시글 삭제

    @POST("/fecom/api/post/edit.php")
    Call<String> postEditPostInfo(@Body JsonObject postData);           //게시글 수정

    @GET("/fecom/api/post/search_by_keyword.php")
    Call<String> getSearchPosts(@Query("user_id") int userID, @Query("board_id") int boardID, @Query("keyword") String keyword);   //해당 게시판의 키워드를 가진 게시글 조회


    @FormUrlEncoded
    @POST("/fecom/api/postlike/register.php")
    Call<String> postRegisterLikeP(@Field("user_id") int userID, @Field("post_id") int postID);                 //게시글 좋아요 추가

    @FormUrlEncoded
    @POST("/fecom/api/postlike/delete.php")
    Call<String> postDeleteLikeP(@Field("user_id") int userID, @Field("post_id") int postID);                   //게시글 좋아요 삭제


    @POST("/fecom/api/postcomment/register.php")
    Call<String> postRegisterCommentB(@Body JsonObject commentData);    //댓글 등록

    @FormUrlEncoded
    @POST("/fecom/api/postcomment/delete.php")
    Call<String> postDeleteCommentB(@Field("comment_id") int commentID);//댓글 삭제

    @POST("/fecom/api/postcomment/edit.php")
    Call<String> postEditComment(@Body JsonObject commentData);         //댓글 수정


    @FormUrlEncoded
    @POST("/fecom/api/postcommentexpression/register_like.php")
    Call<String> postRegisterLikeBC(@Field("user_id") int userID, @Field("comment_id") int commentID);          //댓글 좋아요 추가

    @FormUrlEncoded
    @POST("/fecom/api/postcommentexpression/delete_like.php")
    Call<String> postDeleteLikeBC(@Field("user_id") int userID, @Field("comment_id") int commentID);            //댓글 좋아요 삭제

    @FormUrlEncoded
    @POST("/fecom/api/postcommentexpression/register_dislike.php")
    Call<String> postRegisterNotLikeBC(@Field("user_id") int userID, @Field("comment_id") int commentID);       //댓글 싫어요 추가

    @FormUrlEncoded
    @POST("/fecom/api/postcommentexpression/delete_dislike.php")
    Call<String> postDeleteNotLikeBC(@Field("user_id") int userID, @Field("comment_id") int commentID);         //댓글 싫어요 삭제


    @GET("/fecom/api/dailytalk/show_top10.php")
    Call<String> getTop10Talks(@Query("user_id") int userID);           //가장 좋아요가 많은 10개의 데일리톡 조회

    @GET("/fecom/api/dailytalk/search_all.php")
    Call<String> getDailyTalks(@Query("user_id") int userID);           //전체 데일리톡 조회
    
    @POST("/fecom/api/dailytalk/register.php")
    Call<String> postRegisterTalk(@Body JsonObject talkData);           //데일리톡 등록

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/delete.php")
    Call<String> postDeleteTalk(@Field("daily_id") int talkID);         //데일리톡 삭제

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/like/register.php")
    Call<String> postRegisterLikeT(@Field("user_id") int userID, @Field("daily_id") int talkID);                //데일리톡 좋아요 추가

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/like/delete.php")
    Call<String> postDeleteLikeT(@Field("user_id") int userID, @Field("daily_id") int talkID);                  //데일리톡 좋아요 삭제

    @GET("/fecom/api/dailytalk/comment/search.php")
    Call<String> getTalkComments(@Query("user_id") int userID, @Query("daily_id") int talkID);                  //데일리톡 댓글 조회

    @POST("/fecom/api/dailytalk/comment/register.php")
    Call<String> postRegisterCommentT(@Body JsonObject commentData);    //데일리톡 댓글 등록

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/comment/delete.php")
    Call<String> postDeleteCommentT(@Field("comment_id") int commentID);//데일리톡 댓글 삭제

    @POST("/fecom/api/dailytalk/comment/edit.php")
    Call<String> postEditCommentT(@Body JsonObject commentData);        //데일리톡 댓글 수정

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/comment/like/register.php")
    Call<String> postRegisterLikeTC(@Field("user_id") int userID, @Field("comment_id") int commentID);          //데일리톡 댓글 좋아요 추가

    @FormUrlEncoded
    @POST("/fecom/api/dailytalk/comment/like/delete.php")
    Call<String> postDeleteLikeTC(@Field("user_id") int userID, @Field("comment_id") int comment_id);           //데일리톡 댓글 좋아요 삭제


    @GET("/fecom/api/discussion/search_order_latest.php")
    Call<String> getLatestDiscussList(@Query("user_id") int userID);    //최신순으로 된 대결 목록 조회

    @GET("/fecom/api/discussion/search_order_popular.php")
    Call<String> getPopularDiscussList(@Query("user_id") int userID);   //인기순으로 된 대결 목록 조회

    @POST("/fecom/api/discussion/register.php")
    Call<String> postRegisterDiscuss(@Body JsonObject discussData);     //대결 신청

    @FormUrlEncoded
    @POST("/fecom/api/discussion/expression/register_pro.php")
    Call<String> postExpressPro(@Field("user_id") int userID, @Field("discussion_id") int discussionID);        //대결 찬성 추가

    @FormUrlEncoded
    @POST("/fecom/api/discussion/expression/delete_pro.php")
    Call<String> postDeletePro(@Field("user_id") int userID, @Field("discussion_id") int discussionID);         //대결 찬성 삭제

    @FormUrlEncoded
    @POST("/fecom/api/discussion/expression/register_con.php")
    Call<String> postExpressCon(@Field("user_id") int userID, @Field("discussion_id") int discussionID);        //대결 반대 추가

    @FormUrlEncoded
    @POST("/fecom/api/discussion/expression/delete_con.php")
    Call<String> postDeleteCon(@Field("user_id") int userID, @Field("discussion_id") int discussionID);         //대결 반대 삭제


    @GET("/fecom/api/discussion/comment/search_order_latest.php")
    Call<String> getLatestDiscussComments(@Query("user_id") int userID, @Query("discussion_id") int discussionID);       //대결 댓글 최신순 조회
    
    @GET("/fecom/api/discussion/comment/search_order_popular.php")
    Call<String> getPopularDiscussComments(@Query("user_id") int userID, @Query("discussion_id") int discussionID);      //대결 댓글 인기순 조회

    @POST("/fecom/api/discussion/comment/register.php")
    Call<String> postRegisterCommentD(@Body JsonObject commentData);    //대결 댓글 등록

    @FormUrlEncoded
    @POST("/fecom/api/discussion/comment/delete.php")
    Call<String> postDeleteCommentD(@Field("comment_id") int commentID);//대결 댓글 삭제

    @POST("/fecom/api/discussion/comment/edit.php")
    Call<String> postEditCommentD(@Body JsonObject commentData);        //대결 댓글 수정

    @FormUrlEncoded
    @POST("/fecom/api/discussion/comment/expression/register_like.php")
    Call<String> postRegisterLikeDC(@Field("user_id") int userID, @Field("comment_id") int commentID);          //대결 댓글 좋아요 추가

    @FormUrlEncoded
    @POST("/fecom/api/discussion/comment/expression/delete_like.php")
    Call<String> postDeleteLikeDC(@Field("user_id") int userID, @Field("comment_id") int commentID);            //대결 댓글 좋아요 삭제

    @FormUrlEncoded
    @POST("/fecom/api/discussion/comment/expression/register_dislike.php")
    Call<String> postRegisterNotLikeDC(@Field("user_id") int userID, @Field("comment_id") int commentID);       //대결 댓글 싫어요 추가

    @FormUrlEncoded
    @POST("/fecom/api/discussion/comment/expression/delete_dislike.php")
    Call<String> postDeleteNotLikeDC(@Field("user_id") int userID, @Field("comment_id") int commentID);         //대결 댓글 싫어요 삭제
}

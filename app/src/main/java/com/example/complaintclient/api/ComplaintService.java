package com.example.complaintclient.api;

import com.example.complaintclient.models.Comment;
import com.example.complaintclient.models.Complaint;
import com.example.complaintclient.models.Instance;
import com.example.complaintclient.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ComplaintService {

    //auth
    @POST("auth")
    Call<User> login(@Body User user);

    @POST("auth/register")
    Call<ResponseBody> register(@Body User user);

    //users
    @GET("users")
    Call<User> getUser(@Query("username") String username);

    //complaint
    @GET("complaints")
    Call<List<Complaint>> getComplaints();

    @GET("complaints")
    Call<List<Complaint>> getComplaintsByInstance(@Query("instance") String InstanceName);

    @GET("complaints")
    Call<List<Complaint>> getComplaintsByNegative(@Query("negative") boolean negative);

    @GET("complaints/{id}")
    Call<Complaint> getComplaint(@Path("id") Integer complaintId);

    @POST("complaints")
    Call<String> createComplaint(@Body Complaint complaint);

    @PUT("complaints/{id}")
    Call<String> editComplaint(@Body Complaint complaint, @Path("id") Integer id);

    //Instance
    @GET("instances")
    Call<List<Instance>> getInstances();

    //Comment
    @POST("comments/{complaintId}")
    Call<String> createComment(@Body Comment comment, @Path("complaintId") Integer id);

    @GET("comments/{complaintId}")
    Call<List<Comment>> getCommentsByComplaintId(@Path("complaintId") Integer id);
}

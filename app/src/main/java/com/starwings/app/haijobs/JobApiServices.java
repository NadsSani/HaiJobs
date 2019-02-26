package com.starwings.app.haijobs;

import com.starwings.app.haijobs.responsePojo.DistrictsResponse;
import com.starwings.app.haijobs.responsePojo.EmployerRegistrationPojo;

import com.starwings.app.haijobs.responsePojo.ExperienceEntryResponse;
import com.starwings.app.haijobs.responsePojo.FetchDistrictsResponse;
import com.starwings.app.haijobs.responsePojo.FetchExperienceResponse;
import com.starwings.app.haijobs.responsePojo.FetchExperienceYears;
import com.starwings.app.haijobs.responsePojo.FetchJobRoleResponse;
import com.starwings.app.haijobs.responsePojo.FetchProfilesResponse;
import com.starwings.app.haijobs.responsePojo.FetchStatesResponse;
import com.starwings.app.haijobs.responsePojo.ImageUploadResponse;
import com.starwings.app.haijobs.responsePojo.JobPostResponse;
import com.starwings.app.haijobs.responsePojo.JobTypeResponse;
import com.starwings.app.haijobs.responsePojo.LoginResponse;
import com.starwings.app.haijobs.responsePojo.NoticePeriodResponse;
import com.starwings.app.haijobs.responsePojo.RegisterResponse;
import com.starwings.app.haijobs.responsePojo.ResumeUploadResponse;
import com.starwings.app.haijobs.responsePojo.ScaleOfPayResponse;
import com.starwings.app.haijobs.responsePojo.StatusChangeResponse;
import com.starwings.app.haijobs.responsePojo.VerificationResponse;
import com.starwings.app.haijobs.responses.CategoryResponse;
import com.starwings.app.haijobs.responses.SubCategoryResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface JobApiServices {
    @POST("v20/qualification")
    Call<CategoryResponse> getQualificationList();

    @POST("v20/spqualification")
    Call<SubCategoryResponse> getQualificationStreams();

    @FormUrlEncoded
    @POST("v20/register")
    Call<RegisterResponse> insertUser(
            @Field("name_of_user") String name,
            @Field("dob") String dob,
            @Field("gender") int gender,
            @Field("qualification") String qualification,
            @Field("streams") String streams,
            @Field("contactno") String contactno,
            @Field("email") String email,
            @Field("password") String password,
            @Field("experiencelevel") int experiencelevel,
            @Field("place") String place,
            @Field("district") int district,
            @Field("state") int state,
            @Field("pincode") String pincode);


    @FormUrlEncoded
    @POST("v20/login")
    Call<LoginResponse> checkLogin(
            @Field("mobile") String mobile,
            @Field("password") String password,
            @Field("api_key") String api_key
           );
    @FormUrlEncoded
    @POST("v20/fetchExperience")
    Call<FetchExperienceResponse> fetchExperience(
            @Field("api_key") String api_key
    );


    @Multipart
    @POST("v20/upload")
    Call<ImageUploadResponse> postImage(@Part("api_key") RequestBody api_key,
                                        @Part MultipartBody.Part picture
                                           );

    @Multipart
    @POST("v20/resumeupload")
    Call<ResumeUploadResponse> uploadResume(@Part("api_key") RequestBody api_key,
                                            @Part MultipartBody.Part resume
    );

    @FormUrlEncoded
    @POST("v20/addexperienceinfo")
    Call<ExperienceEntryResponse> addExperience(
            @Field("name_of_organisation") String mobile,
            @Field("worked_from") String worked_from,
            @Field("worked_to") String worked_to,
            @Field("job_position") String job_position,
            @Field("currentFirm") int currentFirm,
            @Field("apikey") String apikey
    );

    @FormUrlEncoded
    @POST("v20/updatePassword")
    Call<StatusChangeResponse> updatePassword(
            @Field("password") String password,
            @Field("apikey") String apikey
    );
    @FormUrlEncoded
    @POST("v20/updatePasswordViaPhone")
    Call<StatusChangeResponse> updatePasswordViaPhone(
            @Field("mobile") String mobile,
            @Field("password") String password
    );
    @FormUrlEncoded
    @POST("v20/updateActiveStatus")
    Call<StatusChangeResponse> updateStatus(
            @Field("active") int active,
            @Field("apikey") String apikey
    );
    @FormUrlEncoded
    @POST("v20/updateExperienceStatus")
    Call<StatusChangeResponse> updateExperienceStatus(
            @Field("experience") int experience,
            @Field("apikey") String apikey
    );
    @FormUrlEncoded
    @POST("v20/updateQualification")
    Call<StatusChangeResponse> updateQualification(
            @Field("categoryids") String  categoryids,
            @Field("subcategoryids") String  subcategoryids,
            @Field("apikey") String apikey
    );
    @FormUrlEncoded
    @POST("v20/updateAddress")
    Call<StatusChangeResponse> updateAddress(
            @Field("place") String  place,
            @Field("state") int  state,
            @Field("district") int district,
            @Field("pin") String pin,
            @Field("apikey") String apikey

    );
    @FormUrlEncoded
    @POST("v20/verifyUserID")
    Call<VerificationResponse> verifyUserID(
            @Field("contactno") String  contactno
    );
    @FormUrlEncoded
    @POST("v20/updatePersonalInfo")
    Call<StatusChangeResponse> updatePersonalInfo(
            @Field("dob") String dob,
            @Field("gender") int gender,
            @Field("contactno") String contactno,
            @Field("emailid") String emailid,
            @Field("apikey") String apikey
    );

    @FormUrlEncoded
    @POST("v20/updatePhoneVerificationStatus")
    Call<StatusChangeResponse> updatePhoneVerificationStatus(
            @Field("phonestatus") int phonestatus,
            @Field("apikey") String apikey
    );


    @FormUrlEncoded
    @POST("v20/updateExperienceInfo")
    Call<ExperienceEntryResponse> updateExperience(
                                                     @Field("slno") int slno,
                                                     @Field("name_of_organisation") String name_of_organisation,
                                                     @Field("worked_from") String worked_from,
                                                     @Field("worked_to") String worked_to,
                                                     @Field("job_position") String job_position,
                                                     @Field("currentFirm") int currentFirm,
                                                     @Field("apikey") String apikey);



    /*Employer APIS*/

    @FormUrlEncoded
    @POST("v20/addEmployerDetails")
    Call<EmployerRegistrationPojo> insertEmployer(
            @Field("employerName") String employerName,
            @Field("AddressLine1") String AddressLine1,
            @Field("AddressLine2") String AddressLine2,
            @Field("pincode") String pincode,
            @Field("employerContact") String employerContact,
            @Field("employerMail") String employerMail,
            @Field("employerWeb") String employerWeb,
            @Field("personToContact") String personToContact,
            @Field("numberToContact") String numberToContact,
            @Field("shortDecription") String shortDecription,
            @Field("empPassword") String empPassword);


    @FormUrlEncoded
    @POST("v20/verifyEmployer")
    Call<EmployerRegistrationPojo> verifyEmployer(
            @Field("empPassword") String empPassword,
            @Field("empLoginID") String empLoginID
    );

    @FormUrlEncoded
    @POST("v20/fetchProfiles")
    Call<FetchProfilesResponse> fetchProfiles(
            @Field("apikey") String apikey

    );

    @FormUrlEncoded
    @POST("v20/fetchJobRoles")
    Call<FetchJobRoleResponse> fetchJobRoles(
            @Field("apikey") String apikey

    );

    @POST("v20/fetchDistricts")
    Call<DistrictsResponse> fetchDistricts();
    @FormUrlEncoded
    @POST("v20/fetchDistrictsByStates")
    Call<DistrictsResponse> fetchDistrictsByStates(
            @Field("apikey") String apikey,
            @Field("statecode") int statecode

    );

    @POST("v20/fetchStates")
    Call<FetchStatesResponse> fetchStates();
    @FormUrlEncoded
    @POST("v20/fetchExperienceItems")
    Call<FetchExperienceYears> fetchExperiencYears(
            @Field("apikey") String apikey

    );
    @FormUrlEncoded
    @POST("v20/fetchScaleOfPay")
    Call<ScaleOfPayResponse> fetchScaleOfPay(
            @Field("apikey") String apikey

    );

    @FormUrlEncoded
    @POST("v20/fetchJobTypes")
    Call<JobTypeResponse> fetchJobTypes(
            @Field("apikey") String apikey

    );
    @FormUrlEncoded
    @POST("v20/fetchNoticePeriod")
    Call<NoticePeriodResponse> fetchNoticePeriods(
            @Field("apikey") String apikey

    );

    @FormUrlEncoded
    @POST("v20/postJobVacancy")
    Call<JobPostResponse> postJobVacancy(
            @Field("apikey") String apikey,
            @Field("jobTitle") String jobTitle,
            @Field("jobRole") int jobRole,
            @Field("jobDistrict") int jobDistrict,
            @Field("jobState") int jobState,
            @Field("eduCategories") String eduCategories,
            @Field("eduSubCatgories") String eduSubCatgories,
            @Field("gender") int gender,
            @Field("jobNature") int jobNature,
            @Field("scaleOfPay") int scaleOfPay,
            @Field("totalVacancies") int totalVacancies,
            @Field("noticePeriod") int noticePeriod,
            @Field("jobDescription") String jobDescription

    );

}

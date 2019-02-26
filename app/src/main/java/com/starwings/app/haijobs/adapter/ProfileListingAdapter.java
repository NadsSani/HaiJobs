package com.starwings.app.haijobs.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.starwings.app.haijobs.HaiJobApp;
import com.starwings.app.haijobs.R;
import com.starwings.app.haijobs.api.ApiLinks;
import com.starwings.app.haijobs.data.Categories;
import com.starwings.app.haijobs.data.QualificationData;
import com.starwings.app.haijobs.data.SubCategories;
import com.starwings.app.haijobs.data.User;
import com.starwings.app.haijobs.employerPages.CandidateProfile;
import com.starwings.app.haijobs.employerPages.ProfilesListing;

import java.util.ArrayList;
import java.util.List;

public class ProfileListingAdapter extends RecyclerView.Adapter {
    List<User> profileList;
    ProfilesListing parentObj;
    HaiJobApp hijobapp;

    public ProfileListingAdapter( List<User> pList,ProfilesListing pObj)
    {
        profileList=pList;
        parentObj=pObj;
        hijobapp=(HaiJobApp)parentObj.getApplication();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(parentObj).inflate(R.layout.profile_listing_row, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        // set the view's size, margins, paddings and layout parameters
        ProfileListingAdapter.ItemHolder vh = new ProfileListingAdapter.ItemHolder(v); // pass the view to View Holder
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder,final int i) {
        ItemHolder vholder=(ItemHolder)viewHolder;
        SetProfiles(vholder,i);

    }
private void SetProfiles(ItemHolder vholder,final int i)
{
    vholder.txtname.setText(profileList.get(i).getName());
    Log.e("Error",profileList.get(i).getName());
    vholder.txtlocation.setText(profileList.get(i).getPlace()+","+hijobapp.getDistrictsList().get(profileList.get(i).getDistrict()-1).getDistrictname());
    vholder.txtqualification.setText(populateQualification(profileList.get(i)));
    if (!profileList.get(i).getProfilepic().equals("NA")) {

        Log.e("ProfilePic",ApiLinks.imgbasepath + profileList.get(i).getProfilepic());
        Glide.with(parentObj)
                .load(ApiLinks.imgbasepath + profileList.get(i).getProfilepic())
                .into( vholder.imgprofile);
    }
    else
    {
        vholder.imgprofile.setImageResource(R.drawable.nophoto);
    }

    vholder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                showFullProfilePage(profileList.get(i));
                                            }
                                        }
    );
}
    private void showFullProfilePage(User user) {

        //SelectedUser
        Intent fullProfile=new Intent(parentObj,CandidateProfile.class);
        fullProfile.putExtra("SelectedUser",user);
        parentObj.startActivity(fullProfile);
    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public String populateQualification(User user)
    {
        ArrayList<String> result=new ArrayList<String>();
        ArrayList<QualificationData> tmpfull=((HaiJobApp)(parentObj.getApplication())).getQualificationData();
        String mainCategories[]=user.getQualification().split(",");
        String resultText="";
        String streams[]=user.getStreams().split(",");
        for(int i=0;i<tmpfull.size();i++)
        {
            Categories tmpcat=tmpfull.get(i).getCategoryObject();
            String catText="";
            for(int j=0;j<mainCategories.length;j++)
            {
                if(tmpcat.getCategoryID()==Integer.parseInt(mainCategories[j].trim()))
                {
                    catText=catText+tmpcat.getCategoryName();
                    if(tmpcat.getHasSub()==1)
                    {
                        if(!((HaiJobApp)(parentObj.getApplication())).getRegisteredUser().getStreams().equals("NA"))
                        {
                            Log.e("BioData",((HaiJobApp)(parentObj.getApplication())).getRegisteredUser().getName()+"-"+((HaiJobApp)(parentObj.getApplication())).getRegisteredUser().getStreams());
                            ArrayList<SubCategories> tmpsubcat=tmpfull.get(i).getSubCategoryList();
                            for(int k=0;k<tmpsubcat.size();k++)
                            {
                                SubCategories tmpsubcatitem=tmpsubcat.get(k);
                                for(int l=0;l<streams.length;l++)
                                {
                                    String subcatText="";
                                    if(tmpsubcatitem.getCategoryID()==Integer.parseInt(streams[l].trim()))
                                    {
                                        subcatText= catText+" "+tmpsubcatitem.getCategoryName();
                                        result.add(subcatText);
                                    }
                                }
                            }
                        }

                    }
                    else
                    {
                        result.add(catText);
                    }


                }
            }
        }

        for(int i=0;i<result.size();i++)
        {
            resultText=resultText+result.get(i)+"/";
        }
        if(resultText.length()==0)
        {
            resultText="NA";
        }
        else
        {
            resultText=resultText.substring(0,resultText.length()-1);
        }
        return resultText;
    }
    public static class ItemHolder extends RecyclerView.ViewHolder {

        TextView txtname,txtqualification,txtlocation;
        ImageView imgprofile;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            this.txtname = (TextView) itemView.findViewById(R.id.candidate_name);
            this.txtqualification = (TextView) itemView.findViewById(R.id.candidate_qualification);
            this.txtlocation = (TextView) itemView.findViewById(R.id.candidate_location);
            this.imgprofile = (ImageView) itemView.findViewById(R.id.img_profile);
        }
    }
}

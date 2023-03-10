package com.project.healingEars.activity.ui;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.project.healingEars.api.retrofitClient;
import com.project.healingEars.http.dto.SessionDTO;
import com.project.healingEars.http.dto.SignUpDTO;
import com.project.healingEars.http.dto.StationDTO;
import com.project.healingEars.http.dto.UserDTO;
import com.project.healingEars.http.repository.UserRepository;
import com.project.healingEars.http.service.StationService;
import com.project.healingEars.http.service.UserService;
import com.project.healingEars.http.vo.StationListVO;
import com.project.healingEars.http.vo.UserVO;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import okhttp3.Cookie;
import retrofit2.Response;

public class VmShareViewModel extends ViewModel {
    static UserRepository.UserAPI retrofit2 = UserRepository.getRetrofit();
    public MutableLiveData<String> userNickName;
    public MutableLiveData<String> session;
    public MutableLiveData<String> mText;

    public MutableLiveData<UserVO> userVO;
    public MutableLiveData<List<StationListVO>> stationListVO;
    public Context context;


    public MutableLiveData<String> loginState;
    public MutableLiveData<String> signUpState;
    //public List<StationListVO> stationListVO;

    public VmShareViewModel() {
        userVO = new MutableLiveData<>();
        mText = new MutableLiveData<>();
        userNickName = new MutableLiveData<>();
        session = new MutableLiveData<>();
        loginState = new MutableLiveData<>();
        stationListVO = new MutableLiveData<>();
        signUpState = new MutableLiveData<>();

        UserVO user = new UserVO("????????? ????????????");
        user.userType = "1";
        userVO.setValue(user);
        session.setValue("?????????");
        mText.setValue("???????????????");
        userNickName.setValue("Android Studio");
        loginState.setValue("LOGOUT");
        signUpState.setValue("?????????");
    }

    public void Login(String userId, String userPassword) {
        try {
            Response<UserDTO> result = new UserService.LoginTask().execute(userId, userPassword).get();


            if ((result.body().result).equals("SUCCESS")) {
                mText.setValue("???????????????");
                userVO.setValue(result.body().user);
                userNickName.setValue(userVO.getValue().userMail);
                loginState.setValue("LOGIN");
            } else if ((result.body().result).equals("FAIL")) {
                loginState.setValue("FAIL");
                mText.setValue("???????????????");
            } else {
                mText.setValue("??????");
            }
        } catch (Exception ignored) {
            loginState.setValue("ERROR");
            mText.setValue("????????????");
        }
    }

    public void SignUp(String... params) {
        try {
            Response<SignUpDTO> result = new UserService.SignUp().execute(params).get();

            if ((result.body().result).equals("SUCCESS")) {
                signUpState.setValue("??????????????????");
            } else if ((result.body().result).equals("FAIL")) {
                signUpState.setValue("??????????????????");
            } else {
                signUpState.setValue("??????");
            }
        } catch (Exception ignored) {
            signUpState.setValue("????????????");
        }
    }

    public void Logout() {
        try {
            String result = new UserService.Logout().execute().get();

            if(result.equals("SUCCESS")){
                UserVO user = new UserVO("???????????? ????????????.");
                user.userType = "1";
                userVO.setValue(user);
                mText.setValue("???????????? ??????");
                loginState.setValue("LOGOUT");
            } else if (result.equals("FAIL")) {
                mText.setValue("???????????? ??????");
                //loginState.setValue("LOGIN");
                //loginResult.setValue("???????????? ??????");
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void SessionInfo() {
        try {
            Response<SessionDTO> result = new UserService.SessionInfo().execute().get();

            if ((result.body().loginState).equals("OK")) {
                session.setValue(result.body().sessionGetID);
                //session.setValue(result.headers().get("Set-Cookie"));
            } else if((result.body().loginState).equals("FAIL")){
                session.setValue("????????? ?????? ??????");
            } else {
                session.setValue("??????");
            }
        } catch (Exception ignored) {
            session.setValue("??????");
        }
    }


    public void getStationList() {
        try {
            Response<StationDTO> result = new StationService.getAllStation().execute().get();
            if((result.body().result).equals("SUCCESS")) {
                stationListVO.setValue(result.body().station);
            } else
                mText.setValue("??????");
        } catch (Exception ignored) {
            mText.setValue("??????");
        }
    }

    public LiveData<String> getmText() { return mText; }

    public LiveData<String> getUserNickName() {
        return userNickName;
    }

    public MutableLiveData<UserVO> getUserVO() { return userVO; }

   // public List<StationListVO> getStation() { return (List)stationListVO.getValue(); }
}

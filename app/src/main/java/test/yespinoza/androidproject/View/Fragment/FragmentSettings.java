package test.yespinoza.androidproject.View.Fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import org.json.JSONObject;

import test.yespinoza.androidproject.View.Activity.ChangePassword;
import test.yespinoza.androidproject.View.Activity.Index;

import test.yespinoza.androidproject.Model.Response.UserResponse;
import test.yespinoza.androidproject.Model.Entity.User;
import test.yespinoza.androidproject.Model.Utils.Helper;
import test.yespinoza.androidproject.Model.Utils.HttpApiResponse;
import test.yespinoza.androidproject.Model.Utils.HttpClientManager;
import test.yespinoza.androidproject.Project;
import test.yespinoza.androidproject.R;

public class FragmentSettings extends Fragment {
    private View rootView;
    private int blackColor;
    private int disabledColor;
    private Drawable textViewEnabled;
    private Drawable textViewDisabled;
    private User oUser;
    private ProgressDialog progress;

    private OnFragmentInteractionListener mListener;

    public FragmentSettings() {
        // Required empty public constructor
    }

    public static FragmentSettings newInstance() {
        FragmentSettings fragment = new FragmentSettings();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        rootView.findViewById(R.id.btnSend)
                .setOnClickListener(new
                                            View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    onSendButtonListener(v);
                                                }
                                            });
        rootView.findViewById(R.id.btnChangePassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangePasswordButtonListener(v);
            }
        });
        blackColor = rootView.getResources().getColor(R.color.colorBlack);
        disabledColor = rootView.getResources().getColor(R.color.colorDisabled);
        textViewEnabled = rootView.getResources().getDrawable(R.drawable.text_view_shape);
        textViewDisabled = rootView.getResources().getDrawable(R.drawable.disabled_text_view_shape);
        loadUser();
        EnableForm(false);
        progress  = new ProgressDialog(rootView.getContext());
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public void onChangePasswordButtonListener(View pView){
        startActivity(new Intent(rootView.getContext(), ChangePassword.class));
    }
    public void onSendButtonListener(View pView) {

        Button btnSend = (Button) pView.findViewById(R.id.btnSend);
        boolean isSaving = btnSend.getText().toString().toUpperCase().equals(getString(R.string.btnSettingsSave).toUpperCase());

        try {
            if (isSaving) {
                //Save Method
                oUser = Project.getInstance().getCurrentUser();
                EditText editText = rootView.findViewById(R.id.etName);
            oUser.setName(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etLastName);
            oUser.setLastName(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etPhone);
            oUser.setPhone(Helper.TextFormat(editText.getText().toString()));
            editText = rootView.findViewById(R.id.etDateOfBirth);
            oUser.setDateOfBirth(editText.getText().toString().trim());
            oUser.setEmail(((TextView)rootView.findViewById(R.id.tvEmail)).getText().toString());
            editText = rootView.findViewById(R.id.etAddress);
            oUser.setAddress(Helper.TextFormat(editText.getText().toString()));

            if (oUser.getName().equals("") ||
                    oUser.getLastName().equals("") ||
                    oUser.getEmail().equals("") ||
                    oUser.getPhone().equals("") ||
                    oUser.getDateOfBirth().equals("")||
                    oUser.getAddress().equals("")) {
                Toast.makeText(rootView.getContext(), getString(R.string.completeFieldsMsg), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!Helper.emailValidation(oUser.getEmail())){
                Toast.makeText(rootView.getContext(), getString(R.string.invalidEmail), Toast.LENGTH_SHORT).show();
                return;
            }

            if(!Helper.dateValidation(oUser.getDateOfBirth())){
                Toast.makeText(rootView.getContext(), getString(R.string.invalidDate), Toast.LENGTH_SHORT).show();
                return;
            }

                //region BackEnd Called
                ShowProgressDialog(getString(R.string.user_update_title), getString(R.string.user_validation_description));
                Response.Listener<JSONObject> callBack_OK = new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        UserResponse oResponse = new Gson().fromJson(response.toString(), UserResponse.class);
                        if (Integer.parseInt(oResponse.getCode()) == HttpApiResponse.SUCCES_CODE) {
                            Toast.makeText(rootView.getContext(), getString(R.string.SettingsSaveSuccess), Toast.LENGTH_SHORT).show();
                            Project.getInstance().setCurrentUser(oUser);
                            Index.getInstance().userData();
                        } else
                            Toast.makeText(rootView.getContext(), getString(R.string.SettingsSaveFailed), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                };

                Response.ErrorListener callBack_ERROR = new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(rootView.getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                };
                HttpClientManager proxy = new HttpClientManager(rootView.getContext());
                proxy.BACKEND_API_POST(HttpClientManager.BKN_UPDATE_USER, new JSONObject(new Gson().toJson(oUser)), callBack_OK, callBack_ERROR);
                //endregion
            }
        } catch (Exception oException) {
            Toast.makeText(rootView.getContext(), getString(R.string.somethingWentWrong), Toast.LENGTH_SHORT).show();
        }
		
        EnableForm(!isSaving);
        btnSend.setText(isSaving ? getString(R.string.btnSettingsEdit) : getString(R.string.btnSettingsSave));
    }

    private void EnableForm(boolean enable){
        EditText editText = rootView.findViewById(R.id.etName);
        editText.setEnabled(enable);
        int color = enable ? blackColor : disabledColor;
        Drawable textViewShape = enable ? textViewEnabled : textViewDisabled;
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etLastName);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etPhone);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etDateOfBirth);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

        editText = rootView.findViewById(R.id.etAddress);
        editText.setEnabled(enable);
        editText.setTextColor(color);
        editText.setBackground(textViewShape);

    }

    private void loadUser(){
        User user = Project.getInstance().getCurrentUser();
        if(user != null) {
            ((EditText)rootView.findViewById(R.id.etIdNumber)).setText(user.getIdNumber());
            ((EditText)rootView.findViewById(R.id.etName)).setText(user.getName());
            ((EditText)rootView.findViewById(R.id.etLastName)).setText(user.getLastName());
            ((EditText)rootView.findViewById(R.id.etDateOfBirth)).setText(user.getDateOfBirth());
            ((EditText)rootView.findViewById(R.id.etPhone)).setText(user.getPhone());
            ((TextView)rootView.findViewById(R.id.tvEmail)).setText(user.getEmail());
            ((EditText)rootView.findViewById(R.id.etAddress)).setText(user.getAddress());
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity oActivity;

        if (context instanceof Activity){
            oActivity=(Activity) context;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void ShowProgressDialog(String tittle, String message){
        progress.setTitle(tittle);
        progress.setMessage(message);
        progress.setCancelable(false);
        progress.show();
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}

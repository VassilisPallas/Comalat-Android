package org.sakaiproject.sakai;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.sakaiproject.api.internet.NetWork;
import org.sakaiproject.api.user.User;
import org.sakaiproject.api.user.update.CheckPassword;
import org.sakaiproject.api.user.update.OldPasswordMatch;
import org.sakaiproject.api.user.update.OnDataChanged;
import org.sakaiproject.api.user.update.UpdateAccountInfoService;
import org.sakaiproject.helpers.ActionsHelper;

import java.io.IOException;


/**
 * Created by vspallas on 27/02/16.
 */
public class AccountFragment extends Fragment implements View.OnClickListener, OnDataChanged, OldPasswordMatch {
    private ImageView edit;
    private ProgressBar validPasswordProgressBar;
    private TextView emailError, passwordError, verifyNewPasswordError;
    private TextView eidTextView, nameTextView, surnameTextView, emailTextView, passwordTextView;
    private EditText nameEditText, surnameEditText, emailEditText, passwordEditText, newPasswordEditText, confirmNewPasswordEditText;
    private LinearLayout newPasswordLayout, confirmNewPasswordLayout, buttonsLayout;
    private Button save, cancel;
    private OnDataChanged onDataChangecallback = this;
    private OldPasswordMatch oldPasswordMatchcallback = this;
    private boolean isEmailValid = true, isPasswordCorrect = true, isPasswordEqual = true;
    private FrameLayout root;

    public AccountFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_account, container, false);
        initialize(v);
        return v;
    }

    private void initialize(View v) {

        root = (FrameLayout) v.findViewById(R.id.root);

        edit = (ImageView) v.findViewById(R.id.edit_account);
        edit.setImageDrawable(ActionsHelper.setCustomDrawableColor(getContext(), R.mipmap.ic_create, Color.parseColor("#43C84E")));
        edit.setOnClickListener(this);

        eidTextView = (TextView) v.findViewById(R.id.user_id_value);
        nameTextView = (TextView) v.findViewById(R.id.first_name_value);
        surnameTextView = (TextView) v.findViewById(R.id.last_name_value);
        emailTextView = (TextView) v.findViewById(R.id.email_value);
        passwordTextView = (TextView) v.findViewById(R.id.password_value);

        nameEditText = (EditText) v.findViewById(R.id.first_name_change);
        surnameEditText = (EditText) v.findViewById(R.id.last_name_change);

        emailEditText = (EditText) v.findViewById(R.id.email_change);
        emailEditText.addTextChangedListener(new Watcher(emailEditText));

        passwordEditText = (EditText) v.findViewById(R.id.old_password);
        passwordEditText.addTextChangedListener(new Watcher(passwordEditText));

        newPasswordEditText = (EditText) v.findViewById(R.id.new_password_change);
        newPasswordEditText.addTextChangedListener(new Watcher(newPasswordEditText));

        confirmNewPasswordEditText = (EditText) v.findViewById(R.id.confirm_new_password_change);
        confirmNewPasswordEditText.addTextChangedListener(new Watcher(confirmNewPasswordEditText));

        newPasswordLayout = (LinearLayout) v.findViewById(R.id.new_password_layout);
        confirmNewPasswordLayout = (LinearLayout) v.findViewById(R.id.confirm_new_password_layout);
        buttonsLayout = (LinearLayout) v.findViewById(R.id.account_info_buttons);

        save = (Button) v.findViewById(R.id.account_save_changes_button);
        save.setOnClickListener(this);
        cancel = (Button) v.findViewById(R.id.account_cancel_button);
        cancel.setOnClickListener(this);

        validPasswordProgressBar = (ProgressBar) v.findViewById(R.id.valid_password_progressbar);
        passwordError = (TextView) v.findViewById(R.id.password_error);
        emailError = (TextView) v.findViewById(R.id.email_error);
        verifyNewPasswordError = (TextView) v.findViewById(R.id.verify_new_password_error);

        fill();
    }

    private void fill() {
        eidTextView.setText(User.getUserEid());
        nameTextView.setText(User.getFirstName());
        surnameTextView.setText(User.getLastName());
        emailTextView.setText(User.getEmail());
        nameEditText.setText(User.getFirstName());
        surnameEditText.setText(User.getLastName());
        emailEditText.setText(User.getEmail());
    }

    private void enableEdit() {
        if (NetWork.getConnectionEstablished()) {
            nameTextView.setVisibility(View.GONE);
            surnameTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);
            passwordTextView.setVisibility(View.GONE);

            nameEditText.setVisibility(View.VISIBLE);
            surnameEditText.setVisibility(View.VISIBLE);
            emailEditText.setVisibility(View.VISIBLE);
            passwordEditText.setVisibility(View.VISIBLE);

            newPasswordLayout.setVisibility(View.VISIBLE);
            confirmNewPasswordLayout.setVisibility(View.VISIBLE);

            buttonsLayout.setVisibility(View.VISIBLE);
        } else {
            Snackbar.make(root, getResources().getString(R.string.no_internet), Snackbar.LENGTH_LONG)
                    .setAction(getResources().getText(R.string.can_not_sync), null).show();
        }
    }

    private void disableEdit() {
        nameTextView.setVisibility(View.VISIBLE);
        surnameTextView.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.VISIBLE);
        passwordTextView.setVisibility(View.VISIBLE);

        nameEditText.setVisibility(View.GONE);
        surnameEditText.setVisibility(View.GONE);
        emailEditText.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);

        newPasswordLayout.setVisibility(View.GONE);
        confirmNewPasswordLayout.setVisibility(View.GONE);

        buttonsLayout.setVisibility(View.GONE);
    }

    private void cancel() {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

        adb.setTitle(getContext().getResources().getString(R.string.cancel_edits));

        adb.setPositiveButton(getContext().getApplicationContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                disableEdit();
            }
        });

        adb.setNegativeButton(getContext().getResources().getString(R.string.no), null);

        Dialog d = adb.show();
    }

    private void save() {
        if (validation()) {

            AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());

            adb.setTitle(getContext().getResources().getString(R.string.save_edits));

            adb.setPositiveButton(getContext().getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    UpdateAccountInfoService service = new UpdateAccountInfoService(getContext(),
//                            nameEditText.getText().toString(),
//                            surnameEditText.getText().toString(),
//                            emailEditText.getText().toString(),
//                            !passwordEditText.getText().toString().equals("") ? confirmNewPasswordEditText.getText().toString() : null,
//                            onDataChangecallback);
//
//                    try {
//                        service.update(getContext().getResources().getString(R.string.url) + "user/" + User.getUserId() + ".json");
//                    } catch (IOException | JSONException e) {
//                        e.printStackTrace();
//                    }
                }
            });

            adb.setNegativeButton(getContext().getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    disableEdit();
                }
            });

            adb.setNeutralButton(getContext().getResources().getString(R.string.cancel), null);

            Dialog d = adb.show();
        } else {
            Toast.makeText(getContext(), "Oups", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_account:
                enableEdit();
                break;
            case R.id.account_save_changes_button:
                save();
                break;
            case R.id.account_cancel_button:
                cancel();
                break;
        }

    }

    public boolean validation() {
        return isEmailValid && isPasswordEqual && isPasswordCorrect;
    }

    private void validateOldPass() {
        if (!passwordEditText.getText().toString().equals("")) {
            new CheckPassword(oldPasswordMatchcallback, passwordEditText.getText().toString(), getContext()).check();
            validation();
        }else
            isPasswordCorrect = true;

    }

    private void emailValidation() {
        String email = emailEditText.getText().toString();
        if (!isValidEmail(email)) {
            isEmailValid = false;
            emailError.setVisibility(View.VISIBLE);
            return;
        }
        isEmailValid = true;
        emailError.setVisibility(View.GONE);
    }

    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void passwordValidation() {
        if (isPasswordCorrect) {
            String password = newPasswordEditText.getText().toString();
            if (!password.equals(confirmNewPasswordEditText.getText().toString())) {
                isPasswordEqual = false;
                verifyNewPasswordError.setVisibility(View.VISIBLE);
            } else {
                isPasswordEqual = true;
                verifyNewPasswordError.setVisibility(View.GONE);
            }
        }else{
            isPasswordEqual = true;
        }
    }

    @Override
    public void updateUI() {
        Toast.makeText(getContext(), getContext().getResources().getString(R.string.data_changed), Toast.LENGTH_SHORT).show();
        fill();
        disableEdit();
    }

    @Override
    public void update(boolean match) {
        isPasswordCorrect = match;
        if (isPasswordCorrect)
            passwordError.setVisibility(View.GONE);
        else
            passwordError.setVisibility(View.VISIBLE);
        validation();
    }

    private class Watcher implements TextWatcher {

        View v;

        public Watcher(View v) {
            this.v = v;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (v.getId()) {
                case R.id.old_password:
                    validateOldPass();
                    break;
                case R.id.email_change:
                    emailValidation();
                    break;
                case R.id.new_password_change:
                    passwordValidation();
                    break;
                case R.id.confirm_new_password_change:
                    passwordValidation();
                    break;
            }
            validation();
        }
    }
}

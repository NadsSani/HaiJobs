package com.starwings.app.haijobs;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.msg91.sendotp.library.PhoneNumberFormattingTextWatcher;
import com.msg91.sendotp.library.PhoneNumberUtils;
import com.msg91.sendotp.library.internal.Iso2Phone;
import com.starwings.app.haijobs.components.CountrySpinner;

import java.util.Locale;

public class RegistrationPhoneEntryActivity extends HaiJobsActivity {

  public static final String INTENT_PHONENUMBER = "mobile";
  public static final String INTENT_COUNTRY_CODE = "code";

  private EditText mPhoneNumber;
  private Button mSmsButton;
  private String mCountryIso;
  private TextWatcher mNumberTextWatcher;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_phone_entry);

    String mphone=getIntent().getStringExtra("phone");
    mPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
    mPhoneNumber.setEnabled(false);

    SharedPreferences userPreferences=getSharedPreferences("USER_PREG",MODE_PRIVATE);

    mSmsButton = (Button) findViewById(R.id.smsVerificationButton);

    mCountryIso = PhoneNumberUtils.getDefaultCountryIso(this);
    final String defaultCountryName = new Locale("", mCountryIso).getDisplayName();
    final CountrySpinner spinner = (CountrySpinner) findViewById(R.id.spinner);
    spinner.init(defaultCountryName);
    spinner.addCountryIsoSelectedListener(new CountrySpinner.CountryIsoSelectedListener() {
      @Override
      public void onCountryIsoSelected(String selectedIso) {
        if (selectedIso != null) {
          mCountryIso = selectedIso;
          resetNumberTextWatcher(mCountryIso);
          // force update:
          mNumberTextWatcher.afterTextChanged(mPhoneNumber.getText());
        }
      }
    });
    resetNumberTextWatcher(mCountryIso);

    tryAndPrefillPhoneNumber();

    mphone=userPreferences.getString("username","NA");
    if(!mphone.equals("NA"))
    {
      mPhoneNumber.setText(mphone);
    }

  }


  private void tryAndPrefillPhoneNumber() {
    if (checkCallingOrSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
      TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
      mPhoneNumber.setText(manager.getLine1Number());
    } else {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 0);
    }
  }

  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      tryAndPrefillPhoneNumber();
    } else {
      if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
        Toast.makeText(this, "This application needs permission to read your phone number to automatically "
            + "pre-fill it", Toast.LENGTH_LONG).show();
      }
    }
  }

  private void openActivity(String phoneNumber) {
    Intent verification = new Intent(this, RegistrationOTPVerification.class);
    verification.putExtra(INTENT_PHONENUMBER, phoneNumber);
    verification.putExtra(INTENT_COUNTRY_CODE, Iso2Phone.getPhone(mCountryIso));
    startActivity(verification);
    finish();
  }

  private void setButtonsEnabled(boolean enabled) {
    mSmsButton.setEnabled(enabled);
  }

  public void onButtonClicked(View view) {
    openActivity(getE164Number());
  }

  private void resetNumberTextWatcher(String countryIso) {

    if (mNumberTextWatcher != null) {
      mPhoneNumber.removeTextChangedListener(mNumberTextWatcher);
    }

    mNumberTextWatcher = new PhoneNumberFormattingTextWatcher(countryIso) {
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        super.onTextChanged(s, start, before, count);
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        super.beforeTextChanged(s, start, count, after);
      }

      @Override
      public synchronized void afterTextChanged(Editable s) {
        super.afterTextChanged(s);
        if (isPossiblePhoneNumber()) {
          setButtonsEnabled(true);
          mPhoneNumber.setTextColor(Color.BLACK);
        } else {
          setButtonsEnabled(false);
          mPhoneNumber.setTextColor(Color.RED);
        }
      }
    };

    mPhoneNumber.addTextChangedListener(mNumberTextWatcher);
  }

  private boolean isPossiblePhoneNumber() {
    return PhoneNumberUtils.isPossibleNumber(mPhoneNumber.getText().toString(), mCountryIso);
  }

  private String getE164Number() {
    return mPhoneNumber.getText().toString().replaceAll("\\D", "").trim();
    // return PhoneNumberUtils.formatNumberToE164(mPhoneNumber.getText().toString(), mCountryIso);
  }
}

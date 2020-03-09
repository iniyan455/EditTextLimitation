package com.iniyan.edittextlimitation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
 public final int EDIT_TEXT_LIMIT=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edt_input= findViewById(R.id.input);

        edt_input.addTextChangedListener(new DecimalInputTextWatcher(edt_input, EDIT_TEXT_LIMIT));
    }

    public static class DecimalInputTextWatcher implements TextWatcher {

        private String mPreviousValue;
        private int mCursorPosition;
        private boolean mRestoringPreviousValueFlag;
        private int mDigitsAfterZero;
        private EditText mEditText;

        DecimalInputTextWatcher(EditText editText, int digitsAfterZero) {
            mDigitsAfterZero = digitsAfterZero;
            mEditText = editText;
            mPreviousValue = "";
            mRestoringPreviousValueFlag = false;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (!mRestoringPreviousValueFlag) {
                mPreviousValue = s.toString();
                mCursorPosition = mEditText.getSelectionStart();
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!mRestoringPreviousValueFlag) {

                if (!isValid(s.toString())) {
                    mRestoringPreviousValueFlag = true;
                    restorePreviousValue();
                }

            } else {
                mRestoringPreviousValueFlag = false;
            }
        }

        private void restorePreviousValue() {
            mEditText.setText(mPreviousValue);
            mEditText.setSelection(mCursorPosition);
        }

        private boolean isValid(String s) {
            Pattern patternWithDot = Pattern.compile("[0-9]*((\\.[0-9]{0," + mDigitsAfterZero + "})?)||(\\.)?");
            Pattern patternWithComma = Pattern.compile("[0-9]*((,[0-9]{0," + mDigitsAfterZero + "})?)||(,)?");

            Matcher matcherDot = patternWithDot.matcher(s);
            Matcher matcherComa = patternWithComma.matcher(s);
            return matcherDot.matches() || matcherComa.matches();
        }
    }


}

package me.sweetll.v2ex;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.codetail.animation.arcanimator.ArcAnimator;
import io.codetail.animation.arcanimator.Side;
import me.sweetll.v2ex.Authentication.AccountGeneral;
import me.sweetll.v2ex.Utils.GlobalClass;

import static me.sweetll.v2ex.Authentication.AccountGeneral.ACCOUNT_TYPE;
import static me.sweetll.v2ex.Authentication.AccountGeneral.AUTH_TOKEN_TYPE;
import static me.sweetll.v2ex.Authentication.AccountGeneral.mServerAuthenticate;
/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    @Bind(R.id.username) AutoCompleteTextView mUsernameView;
    @Bind(R.id.password) EditText mPasswordView;
    @Bind(R.id.login_form) View mProgressView;
    @Bind(R.id.login_progress) View mLoginFormView;
    @Bind(R.id.sign_in_button) Button mSignInButton;
    @Bind(R.id.sign_in_form) CardView mSignInForm;
    @Bind(R.id.sign_up_button) Button mSignUpButton;
    @Bind(R.id.sign_up_form) CardView mSignUpForm;
    @Bind(R.id.sign_up_toggle_button) FloatingActionButton mSignUpToggleButton;

    private Boolean isSignUpShow = false;
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAccountManager = AccountManager.get(this);

        // Set up the login form.
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mSignUpToggleButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSignUpShow) {
                    showSignUp();
                    isSignUpShow = true;
                } else {
                    hideSignUp();
                    isSignUpShow = false;
                }
            }
        });

    }

    private void showSignUp() {
        ArcAnimator mArcAnimator = ArcAnimator.createArcAnimator(mSignUpToggleButton, 880, 580, 180, Side.LEFT)
                .setDuration(400);
        mArcAnimator.setInterpolator(new AccelerateInterpolator());
        mArcAnimator.start();

        mSignUpToggleButton.animate()
                .rotation(45)
                .setDuration(400)
                .start();

        mSignInForm.animate().scaleX(0.9f).scaleY(0.9f).start();

        int cx = mSignUpForm.getWidth();
        int cy = 350;
        float finalRadius = (float) Math.hypot(cx, mSignInForm.getHeight() - cy);

        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(mSignUpForm, cx, cy, 0, finalRadius);
        revealAnimator.setDuration(400).setInterpolator(new AccelerateInterpolator());
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                mSignUpForm.setVisibility(View.VISIBLE);
            }
        });
        revealAnimator.start();
    }

    private void hideSignUp() {
        ArcAnimator mArcAnimator = ArcAnimator.createArcAnimator(mSignUpToggleButton, 960, 780, 180, Side.LEFT)
                .setDuration(400);
        mArcAnimator.setInterpolator(new AccelerateInterpolator());
        mArcAnimator.start();

        mSignUpToggleButton.animate()
                .rotation(0)
                .setDuration(400)
                .start();
        mSignInForm.animate().scaleX(1.0f).scaleY(1.0f).start();


        int cx = mSignUpForm.getWidth() - 50;
        int cy = 350;
        float initialRadius = (float) Math.hypot(cx, mSignInForm.getHeight() - cy);

        Animator revealAnimator = ViewAnimationUtils.createCircularReveal(mSignUpForm, cx, cy, initialRadius, 0);
        revealAnimator.setDuration(400).setInterpolator(new AccelerateInterpolator());
        revealAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mSignUpForm.setVisibility(View.INVISIBLE);
            }
        });
        revealAnimator.start();
    }

    private void addNewAccount(String accountType, String authTokenType) {
        final AccountManagerFuture<Bundle> future = mAccountManager.addAccount(accountType, authTokenType, null, null, this, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {

            }
        }, null);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isUsernameValid(String username) {
        String pattern= "^[a-z0-9]*$";
        return username.matches(pattern);
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            String authToken = null;
            Bundle data = new Bundle();

            try {
                authToken = mServerAuthenticate.userSignIn(mUsername, mPassword, AUTH_TOKEN_TYPE);

                data.putString(AccountManager.KEY_ACCOUNT_TYPE, ACCOUNT_TYPE);
                data.putString(AccountManager.KEY_ACCOUNT_NAME, mUsername);
                data.putString(AccountManager.KEY_AUTHTOKEN, authToken);
                data.putString(AccountManager.KEY_PASSWORD, mPassword);

                finishLogin(data);
            } catch (Exception e) {
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);

            if (success) {
                setResult(GlobalClass.RESULT_SUCCESS);
                finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    private void finishLogin(Bundle data) {
        String accountName = data.getString(AccountManager.KEY_ACCOUNT_NAME);
        String accountPassword = data.getString(AccountManager.KEY_PASSWORD);
        final Account account = new Account(accountName, data.getString(AccountManager.KEY_ACCOUNT_TYPE));

        String authToken = data.getString(AccountManager.KEY_AUTHTOKEN);
        String authTokenType = AUTH_TOKEN_TYPE;

        mAccountManager.addAccountExplicitly(account, accountPassword, null);
        mAccountManager.setAuthToken(account, authTokenType, authToken);

        SharedPreferences sharedPref = getSharedPreferences(AccountGeneral.PREF_ACCOUNT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(AccountManager.KEY_ACCOUNT_NAME, accountName);
        editor.commit();
    }
}


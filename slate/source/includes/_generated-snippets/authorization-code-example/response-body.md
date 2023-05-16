```
    <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    <html xmlns="http://www.w3.org/1999/xhtml" class="login-pf" lang="en">

    <head>
        <meta charset="utf-8">
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="robots" content="noindex, nofollow">

                <meta name="viewport" content="width=device-width,initial-scale=1"/>
        <title>Log in to boclips</title>
        <link rel="icon" href="/resources/qmv95/login/boclips/img/favicon.ico"/>
                <link href="/resources/qmv95/login/boclips/node_modules/patternfly/dist/css/patternfly.css" rel="stylesheet"/>
                <link href="/resources/qmv95/login/boclips/node_modules/patternfly/dist/css/patternfly-additions.css" rel="stylesheet"/>
                <link href="/resources/qmv95/login/boclips/lib/zocial/zocial.css" rel="stylesheet"/>
                <link href="/resources/qmv95/login/boclips/css/login-styles.css" rel="stylesheet"/>

        <script>
            window.MIXPANEL_CUSTOM_LIB_URL = '/resources/qmv95/login/boclips/js/mp.min.js';
        </script>
        <script>

            const viewPassword = (passwordElementId) => {
                let passwordInput = document.getElementById(passwordElementId);
                let passStatus = document.getElementById('toggle-' + passwordElementId + '-icon');
                if (passwordInput.type === 'password') {
                    passwordInput.type = 'text';
                    passStatus.className = 'toggle-password-icon password-visible';
                } else {
                    passwordInput.type = 'password';
                    passStatus.className = 'toggle-password-icon password-hidden';
                }
            }
        </script>
        <script src="/resources/qmv95/login/boclips/js/mp-jslib-snippet.min.js" type="text/javascript"></script>
        <script src="/resources/qmv95/login/boclips/js/mp-config.js" type="text/javascript"></script>

        <!-- Start of HubSpot Embed Code -->
        <script type="text/javascript" id="hs-script-loader" async defer src="//js.hs-scripts.com/4854096.js"></script>
        <!-- End of HubSpot Embed Code -->

    </head>

    <body class="">

    <div class="login-pf-page">

        <div id="kc-header" class="login-pf-page-header">
            <div id="kc-header-wrapper"
                 class="">boclips</div>
        </div>

        <div>
            <header class="login-pf-header">
            </header>
            <div id="kc-content">
                <div id="kc-content-wrapper">
                    <section class="boclips-logo-wrapper">
                            <img src="/resources/qmv95/login/boclips/img/base-logo.svg" alt="boclips logo"/>
                    </section>
                    <h1 class="boclips-action-label">        Log in to your account
</h1>
            <form id="kc-form-login"
                  onsubmit="login.disabled = true; return true;"
                  action="https://login.staging-boclips.com/realms/boclips/login-actions/authenticate?session_code=3C8A9icljDBYXVNQgrU-jT67b2KM5QOvhpTRd_BDBdg&amp;execution=71ed839b-d34c-414d-b008-a7e6612fe9ef&amp;client_id=teachers&amp;tab_id=Us4Kv9CbDog"
                  class="boclips-login"
                  method="post"
            >
                    <section>
                        <div class="form-group">
                            <label for="username"
                                   class="control-label">
                                Email address
                            </label>
                            <input data-qa="email"
                                   placeholder="Enter your email"
                                   id="username"
                                   class="form-control" name="username"
                                   value=""
                                   type="email" autofocus autocomplete="on"/>
                        </div>

                        <div class="form-group">
                            <label for="password"
                                   class="control-label"
                            >
                                Password
                            </label>
                            <div style="position:relative">
                                <input data-qa="password"
                                       placeholder="Enter your password"
                                       id="password"
                                       class="form-control"
                                       name="password"
                                       type="password"
                                       autocomplete="on"
                                />
                                <button id="toggle-password-icon" role="switch" aria-label="show password"
                                      class="toggle-password-icon password-hidden"
                                      onClick="viewPassword('password')">
                                </button>
                            </div>
                        </div>
                    </section>

                    <section class="form-group">
                        <div id="kc-form-buttons">
                            <input data-qa="login-button"
                                   class="boclips-login-button btn btn-primary btn-block btn-lg"
                                   name="login"
                                   id="kc-login"
                                   type="submit"
                                   value="Log&nbsp;in"/>
                        </div>
                            <div class="form-group">
                                <div class="boclips-forgot-password-container ">
                                <span>
                                    <a data-qa="forgot-password"
                                       class="forgot-password"
                                       href="/realms/boclips/login-actions/reset-credentials?client_id=teachers&amp;tab_id=Us4Kv9CbDog"
                                    >
                                        I forgot my password
                                    </a>
                                </span>
                                </div>
                            </div>
                    </section>

                        <label class="boclips-social-divider-label boclips-action-label">Or continue with</label>

                        <div id="kc-social-providers" class="boclips-social-container">
                            <ul>
                                    <li>
                                        <a href="/realms/boclips/broker/google/login?client_id=teachers&amp;tab_id=Us4Kv9CbDog&amp;session_code=3C8A9icljDBYXVNQgrU-jT67b2KM5QOvhpTRd_BDBdg"
                                           id="zocial-google"
                                           class=" boclips-social zocial google"
                                        >
                                            <span>Google</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="/realms/boclips/broker/microsoft/login?client_id=teachers&amp;tab_id=Us4Kv9CbDog&amp;session_code=3C8A9icljDBYXVNQgrU-jT67b2KM5QOvhpTRd_BDBdg"
                                           id="zocial-microsoft"
                                           class=" boclips-social zocial microsoft"
                                        >
                                            <span>Microsoft</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="/realms/boclips/broker/moe-uae/login?client_id=teachers&amp;tab_id=Us4Kv9CbDog&amp;session_code=3C8A9icljDBYXVNQgrU-jT67b2KM5QOvhpTRd_BDBdg"
                                           id="zocial-moe-uae"
                                           class=" boclips-social zocial oidc"
                                        >
                                            <span>MOE UAE</span>
                                        </a>
                                    </li>
                                    <li>
                                        <a href="/realms/boclips/broker/viewsonic/login?client_id=teachers&amp;tab_id=Us4Kv9CbDog&amp;session_code=3C8A9icljDBYXVNQgrU-jT67b2KM5QOvhpTRd_BDBdg"
                                           id="zocial-viewsonic"
                                           class=" boclips-social zocial saml"
                                        >
                                            <span>ViewSonic</span>
                                        </a>
                                    </li>
                            </ul>
                        </div>
            </form>
                </div>
            </div>

        </div>
    </div>
    </body>
    <script>
        const togglePassword = document.querySelector('#toggle-password-icon');

        togglePassword.addEventListener('click', (e) => {
            e.preventDefault()
            if (togglePassword.getAttribute("aria-checked") === "true") {
                togglePassword.setAttribute("aria-checked", "false");
            } else {
                togglePassword.setAttribute("aria-checked", "true");
            }
        })


    </script>
    </html>

```
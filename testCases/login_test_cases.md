# Manual Test Cases - PhotoPixels Web Login

These are the manual test cases corresponding to the automated tests in `src/test/java/com/photopixels/web`.

---

## TC001 - Login with Valid Credentials

- **Automated Test**: `loginUserSuccessfullyTest`
- **Module**: Login
- **Preconditions**: User is registered with valid credentials
- **Steps**:
    1. Open PhotoPixels web app
    2. Enter valid email and password
    3. Click "Login"
- **Expected Result**:
    - User is redirected to the Overview page
    - User's full name is displayed
    - Overview header matches expected text
- **Status**: Passed

---

## TC002 - Login with Wrong Password

- **Automated Test**: `loginWrongPasswordWebTest`
- **Module**: Login
- **Preconditions**: User account exists
- **Steps**:
    1. Open PhotoPixels web app
    2. Enter registered email and incorrect password
    3. Click "Login"
- **Expected Result**:
    - Login fails
    - Error message "Wrong credentials" is shown
- **Status**: Passed

---

## TC003 - Login with Missing Email

- **Automated Test**: `loginMissingEmailTest`
- **Module**: Login
- **Preconditions**: None
- **Steps**:
    1. Open PhotoPixels web app
    2. Leave the email field empty
    3. Enter a valid password
    4. Check if login button is enabled
- **Expected Result**:
    - Login button is disabled
- **Status**: Passed

---

## TC004 - Login with Missing Password

- **Automated Test**: `loginMissingPasswordTest`
- **Module**: Login
- **Preconditions**: None
- **Steps**:
    1. Open PhotoPixels web app
    2. Enter a valid email
    3. Leave the password field empty
    4. Check if login button is enabled
- **Expected Result**:
    - Login button is disabled
- **Status**: Passed

---

## TC005 - Login with Unregistered Email

- **Automated Test**: `loginNotRegisteredUserWebTest`
- **Module**: Login
- **Preconditions**: Email is randomly generated and not registered
- **Steps**:
    1. Open PhotoPixels web app
    2. Enter random email (not registered) and valid password
    3. Click "Login"
- **Expected Result**:
    - Error message "Wrong credentials" is shown
    - Note: Currently under review — might need a more descriptive message
- **Status**: Potential bug noted in TODO

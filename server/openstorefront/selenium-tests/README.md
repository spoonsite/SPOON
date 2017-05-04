## Running Tests:

### Test Packages:

 * __admin:__  Admin Tools, tests that require administrator permissions 

 * __evaluator:__  Evaluator Tools, forms, etc.  
 
 * __security:__  Security Profiles, forgot password, new account, ensuring proper user and group rights, etc.

 * __user:__ User Tools, dashboard, sub-menu functionality.

### Files in the packages:

 * __xTest:__  The individual test.  Extends the xTestBase class. 

 * __xTestBase:__ calls @BeforeClass (to Login, for example) Extends BrowserTestBase.  Setup and Cleanup for the sub-suite takes place here; Calling Login, Logout, Screenshot, etc.

 * __xTestSuite:__  Contains the names of all of the Tests within this sub-test suite to run.

 * __BrowserTestBase:__	Contains:  Login, Logout, TakeScreenShot, etc.

 * __MasterTestSuite:__	Contains names of all of the test sub-suites to run. @AfterClass will Generate Reports (future TODO:)


### HTML Results are accessible at:
file:///[local code path]/selenium-tests/target/site/swat_results.html


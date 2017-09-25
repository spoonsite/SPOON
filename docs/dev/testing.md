# Testing
----

## Layers

1. Unit Tests (Server-side) - Unit test should cover Core libraries and utility classes. Test cases should be added for bug fixes and new features whenever possible.
2. Integration/Container Tests (openstorefront/test/ServiceTest.action) - These test the server-side service/business layer. They are run on the Integration server as part of the Jenkins build. Integration tests should be added for each new feature.
3. API Tests - These tests use a client that communicates to the rest api (work in progress)
4. Acceptance ticket tests -  We write this as part of the JIRA ticket for QA.
5. Regression tests - These are maintain and exercised by the QA team.
Regression tests are captured in a separate document. [Regression Docs](https://confluence.di2e.net/display/STORE/Regression+Test+Plans) Each ticket should include basic testing instructions to ensure correctness. These cases will be used as a starting point for the formal Regression test.
6. Auto UI tests will be written using Selenium where ever they make sense.

## Roles
1. Developers:
   * Write and maintain unit tests
   * Write and maintain automated integration tests
   * Ensure all automated tests are passing upon completion of a task
   * Ensure Regression tests for the feature are passing
2. Testers:
   * Verify all automated tests pass
   * Walk through all Regression and Acceptance tests and verify they pass
   * If a failure is found verify if it is a code bug or test issue if possible.
   * Return to developers providing steps to reproduce failure.

## Selenium Tests
How to run Selenium tests:
1. These steps assume you have the latest code for your branch.
2. Go to your testconfig.properties file found in your var directory (/var/openstorefront/config/testconfig).  Update the email field in the following section:
test.server=http://localhost:8080/openstorefront/
#EDGE, CHROME, IE, FIREFOX, ALL
test.drivers=CHROME
test.username=admin
test.password=Secret1@
test.newuseremail= *_your email_*
report.dir=/var/openstorefront/testreports
3. Storefront and ElasticSearch must be running in order to run a Selenium test.
4. Right click on *selenium-tests* found in the Projects tab of NetBeans.
5. Select *Run Selenium Tests*.
6. To run an individual Selenium test: locate the test file, right click on it, and select *Test File*.
7. Note: Selenium tests end with _IT.java_.

## Unit Tests
How to run Unit Tests:
1. Note: Unit test filenames end with _Test.java_.
2. To run an individual unit test: locate the file, right click and select *Test File*.
3. To run a group of unit tests: locate the project, right click and select *Test*.
4. All unit tests will be run during a project build.

## Integration Tests/Container Page
How to run integration tests:
1. Storefront and ElasticSearch must be running in order to run this group of tests.
2. Go to http://localhost:8080/openstorefront/test/ServiceTest.action
3. Login as admin if you haven't already.
4. Container page allows you to *Run All Tests*, a group of tests (e.g. Alert Tests), or individual tests (e.g. Find Lookup Test).


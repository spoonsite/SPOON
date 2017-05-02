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

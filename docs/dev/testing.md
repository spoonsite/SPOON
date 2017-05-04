# Testing
----

## Layers

1. Unit Tests (Server-side) - Unit test should cover Core libraries and utility classes.
2. Integration/Container Tests (openstorefront/test/ServiceTest.action) - These test the server-side service/business layer.
they are run on the Integration server as part of the Jenkins build.
3. API Tests - These tests use a client that communicates to the rest api (work in progress)
4. Acceptance ticket tests -  We write this as part of the JIRA ticket for QA. 
5. Regression tests - These are maintain and exercised by the QA team. 
Regression tests are captured in a separate document. [Regression Docs](https://confluence.di2e.net/display/STORE/Regression+Test+Plans)


Note: In the future we may consider some Auto UI tests where they make sense.

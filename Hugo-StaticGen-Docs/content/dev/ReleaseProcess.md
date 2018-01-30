+++
title = "Release Process"
description = ""
weight = 9
+++

This is an internal document but, but outside developer/reuses may find this of
interest.

## General

Once code is complete and regression is done.

0. Make sure build ticket is current and ticket are properly mark as to the version.

1. Update documentation versions, migration guide and any missing documentation.

2. Make sure github doc are clean and built (see Hugo-StaticGen-Docs)

3. Follow git flow merge (release branch back through master and dev)

4. Tag Master with release number (In GitHub)

	a) Add JIRA Release notes

	b) Post Release Artifacts (WAR)

5. Notify interest parties of the release.


## DI2E

1. All instructions should be in the build ticket.  (Beware of the current state 
of production.)

2. The target is storefront-staging the DI2E admins will perform the move 
to staging and production.

3. Beware of the DI2E environments in the move instructions as something may only
apply in certain environments.


## SPOON

1. Any SPOON specifics should documented in a separate ticket.

2. Moves are handled internally...however move instruction should be clear. 








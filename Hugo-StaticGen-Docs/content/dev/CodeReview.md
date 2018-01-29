+++
title = "Code Review"
description = ""
weight = 8
+++

# Code Review Process

1. Branch build is passed. (Green)

2. It's expected the formating and basic style is handled by the IDE setting.
Check that those were apply. (quick scan)

3. Check vs Coding Standards (Front-end and Server)

4. Check UI vs Style Guide

5. Check for known business rule breaks (missing cross cutting concerns; regression is expected to catch some of this)

6. Make sure any required documentation changes are made.

(It may NOT be all required documentation needed for final release; just the documentation related to the ticket)

7. Move ticket as ready for testing (meaning code review passed) or back to fix for adjustments.

On Failure:

Note: specifically what failed and why.  Also, suggest a correction path; where possible.

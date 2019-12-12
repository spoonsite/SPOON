+++
title = "Server Code Standard"
description = ""
weight = 7
+++

These are special notes when making additions to the server-side code.
<!--more-->

## Java Style Guide

In general, applying the IDE Formating and its default warnings are sufficient to follow and keep the code in-line. These standards borrow ideas from [https://google.github.io/styleguide/javaguide.html](https://google.github.io/styleguide/javaguide.html)

### Special escape sequences

For any character that has a special escape sequence (\b, \t, \n, \f, \r, \", \' and \\), that sequence is used rather than the corresponding octal (e.g., `\012`) or Unicode (e.g., `\u000a`) escape.

### Non-ASCII characters

For the remaining non-ASCII characters, either the actual Unicode character (e.g., `?`) or the equivalent Unicode escape (e.g., `\u221e`) is used. The choice depends only on which makes the code easier to read and understand, although Unicode escapes outside string literals and comments are strongly discouraged.

Tip: In the Unicode escape case, and occasionally even when actual Unicode characters are used, an explanatory comment can be very helpful.

### Source file structure

A source file consists of, in order:

1. License or copyright information (use Apache V2 on java side, GPL on front-end; the whole project is GPL)
2. Package statement
3. Import statements
4. Exactly one top-level class

**Exactly one blank line** separates each section that is present

### Wildcard Imports

- Avoid wildcard imports, let the IDE import the required classes
- Let the IDE order the imports

### Braces

- Use IDE Formating
- Change need to be agreed upon and update in the formatter

### Naming Conventions

- Names need to be descriptive
- Lower-case to start and camelCase
- The ordering of the name is object owner and then object name. For example: componentResource, means resource is part of or owned by component.
- Typically, classes are labeled according to function (e.g., ComponentService)

#### Package names

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example, com.example.deepspace, not com.example.deepSpace or com.example.deep_space.

#### Class names

- Class names are written in UpperCamelCase.
- Class names are typically nouns or noun phrases (e.g., Character or ImmutableList)
- Interface names may also be nouns or noun phrases (e.g., List), but may sometimes be adjectives or adjective phrases instead (e.g., Readable)
- Test classes are named starting with the name of the class they are testing, and ending with Test (e.g., HashTest or HashIntegrationTest)
- There are no specific rules or even well-established conventions for naming annotation types

#### Method names

- Method names are written in lowerCamelCase.
- Method names are typically verbs or verb phrases (e.g., sendMessage or stop)
- Underscores may appear in JUnit test method names to separate logical components of the name, with each component written in lowerCamelCase. One typical pattern is <methodUnderTest>_<state>, for example pop_emptyStack. There is no one correct way to name test methods.

#### Constant names

Constant names use CONSTANT_CASE: all uppercase letters, with each word separated from the next by a single underscore.

Constants are static final fields whose contents are deeply immutable and whose methods have no detectable side effects. This includes primitives, Strings, immutable types, and immutable collections of immutable types. If any of the instance's observable state can change, it is not a constant. Merely intending to never mutate the object is not enough.

#### Non-constant field names

- Non-constant field names (static or otherwise) are written in lowerCamelCase
- These names are typically nouns or noun phrases (e.g., computedValues or index)

#### Parameter names

- Parameter names are written in lowerCamelCase
- One-character parameter names in public methods should be avoided

#### Local variable names

- Local variable names are written in lowerCamelCase
- Even when final and immutable, local variables are not considered to be constants, and should not be styled as constants

#### Type variable names

Each type variable is named in one of two styles:

- A single capital letter, optionally followed by a single numeral (e.g., E, T, X, T2)
- A name in the form used for classes (see [Class names](#class-names)), followed by the capital letter T (e.g., RequestT, FooBarT).

### Caught exceptions (not ignored)

Except as noted below, it is very rarely correct to do nothing in response to a caught exception. (Typical responses are to log it, or if it is considered "impossible," rethrow it as an AssertionError)

When it truly is appropriate to take no action whatsoever in a catch block, the reason this is justified is explained in a comment.

```js
try {
  int i = Integer.parseInt(response);
  return handleNumericResponse(i);
} catch (NumberFormatException ok) {
  // it's not numeric; that's fine, just continue
}
return handleTextResponse(response);
```

### Static members (qualified using class)

When a reference to a static class member must be qualified, it is qualified with that class's name, not with a reference or expression of that class's type.

```js
Foo aFoo = ...;
Foo.aStaticMethod(); // good
aFoo.aStaticMethod(); // bad
somethingThatYieldsAFoo().aStaticMethod(); // very bad
```

### Finalizers (not used)

It is extremely rare to override `Object.finalize`.

Tip: Don't do it. If you absolutely must, first read and understand _Effective Java_ by Joshua Bloch, Item 7, "Avoid Finalizers," very carefully, and then don't do it.

### Javadocs

Javadocs are used on

- Public API points
- Protected methods
- Getters or setters that have different behavior than simply wrapping field access
- DB Entities for documenting what the field is used for

Also see adding APIDescription on the field as that is rendered  out to the API documentation.

Exception: overrides - Javadoc is not always present on a method that overrides a supertype method.

Use Block tags:

Any of the standard "block tags" that are used appear in the order @param, @return, @throws, @deprecated, and these four types never appear with an empty description. When a block tag doesn't fit on a single line, continuation lines are indented four (or more) spaces from the position of the @.

For Example:

```java
/**
 * Returns an Image object that can then be painted on the screen.
 * The url argument must specify an absolute {@link URL}. The name
 * argument is a specifier that is relative to the url argument.
 * <p>
 * This method always returns immediately, whether or not the
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives
 * that draw the image will incrementally paint on the screen.
 *
 * @param  url  an absolute URL giving the base location of the image
 * @param  name the location of the image, relative to the url argument
 * @return      the image at the specified URL
 *
 * @throws MalformedURLException
 * @deprecated
*/
public Image getImage(URL url, String name) {
      try {
          return getImage(new URL(url, name));
      } catch (MalformedURLException e) {
          return null;
      }
}
```

\* Taken from [How to Write Doc Comments for the Javadoc Tool](https://www.oracle.com/technetwork/java/javase/documentation/index-137868.html) and edited

## General

1. Business logic, Transactions, rules should be handled in the service code (core-service)
2. API Interface should have documentation (Javadocs)
3. SimpleDateFormat is not thread-safe, create new instances; don't make static
4. For decimal numbers use BigDecimal, and use valueOf to instantiate
5. Make sure any new code is placed in the appropriate module. Be cautious of dependencies
6. External resources should be handled by a Manager that in turn handles the lifecycle
7. Use example query framework where possible. Keep custom queries to a minimum as this allows for greater flexibility as well as type safety on the query, which in turn makes refactoring easier and reduces errors
8. Beware of any potential infinite loops
9. Pay attention to Transaction boundaries. For the most part it's automatic but, you can carry over transaction where it's unintended
10. Prefer constants over hard-coded strings. Keep constants with the entity/object they relate to.  For general constants use OpenstorefrontConstant
11. Focus comments on WHY (Business Rule) rather than HOW for internal comments.  The how should be clear from the code

## Entities

1. Entities should have all validation annotations marked and have documentation annotations as well
2. Use "get" and "set" and not "is" as are a lot of automatic processing (reflection) on entities
3. Most entities should extends the standard entity
4. Preference is to keep model flat, but there are cases where complex models are fine (meaning care should be taken with embedded complex classes)
    - If there are embedded entities, make to use Casacde, OneToX annotation
    - Beware of db issues
    - The DB and moxy doesn't handling List of primitives (meaning Boxed primitives) well (e.g. `List<String>` should be `List<EmailAddresses>`)
5. Document Entities with @APIDocument to update the API docs
6. Prefer composition of entities in view rather than inheritance. Use inheritance for substitutability
7. All storage entities must be registered with the DB.  This is automatic for class in the entity package of the API module

## REST API

1. Avoid naming entity and view model fields (type or ID).  It can break the serialization
2. Use the API annotations to document the service. All REST interfaces need documentation as to purpose
3. All consumed entities must have a root object. For Example, String (fail),  TextObject with a field String (Valid)
4. Response also needs a root object.  A GenericEntity can be used
5. Resource endpoints map to entities where Services act on the system or are cross entities
6. "Actions" supplement the API and are used for internal operations.  Also, they use to handle uploads as there is no JAX-RS standard for that

## Cache Handling

1. All cache handling should be done in the Services rather than leaking it up a layer of abstraction
2. Beware to not modify the cached version of the data.  There is a "weak reference" to the object so it can end up applying unwanted changes
3. All caches should be set up in the cache manager

## Logging

Each level has a different audience (See java Docs). Add logging message appropriate for each level. Focus on logging thing that would aid in debugging (When, What, the State was at the time of the error, and Data owner, e.g., what component does it belong to).

*SEVERE* - System Admin and End-User; the operation failed; something prevented normal execution;  If throwing an Openstorefront error please state the error and note anything that can be done to correct it

*WARNING* - Potential Problems, System Admin or End-users, the system should be able to continue but desired effect may not have been achieved

*INFO* - Message to System Admin usually for information about operation completed

*CONFIG* - Messages are intended to provide a variety of static configuration information (mainly initialization of a system item)

*FINE* - Debugging information for developers

*FINER* - Detailed debugging information

*FINEST* - Trace message; highly detailed.  If gathering the information is time consuming (CPU wise), then check logging level state first.  If (log.isLoggable(Level.FINEST))

**If you don't add logging it is not available** Thus, logging needs to be added where appropriate where appropriate, typically, on anti-conditions.
Think, "If I was troubleshooting this in production could I get enough information to solve an issue?"
Log the data owner and state.

Note: Tomcat's loghandler doesn't seem to handle "{0}" substitution.  Use MessageFormat.format to handle that.

Do not use an `'` in log comments as it affects the formatter. See [https://stackoverflow.com/questions/22670627/java-logger-apostrophe-issues-with-tokens](https://stackoverflow.com/questions/22670627/java-logger-apostrophe-issues-with-tokens)

### How To Use Logging Tools

Java logging has two components:

1. Logger - These are hierarchical, meaning setting the level will be achieved by rolling upwards until the next setting.
2. Handler - These are associated with a Logger

The logger has a Level and each handler has a level. Both must be set to get the output to show the level.
Logs by default show in the console output for the application server, which is typically in /logs directory but,
can be in the var server output that installs via yum.

Built into the application is a DB Logger which when activated will log all components in the system. At Level ALL.
Keep in mind the logger may be set at a different level.

It will capture the logs in the DB, and the Admin system logs can be used to view that.

The UI allows for setting each logger's level.  Setting Handler levels is not currently supported.

Note the logging.properties file (currently not loaded) sets defaults. By default the console handler is set to INFO.

## Cross Cutting Concerns

**Security** - Ensure permission are applied to the REST/External API and UI features.

**Logging** - Security changes/sensitivity must have audit logging (Log action, who performed it).

**Data Restriction** - Ensure data that should be restricted is restricted at the appropriate level.  This is not applied universally, as there are complications that require specific handling.

**Change History** - Entries and Evaluations are supported. Log field changes, adds, removes, etc.

**Alerts** - Certain events must be tracked for administration alerts.

Use caution when modifying the code to keep these features/requirements intact.

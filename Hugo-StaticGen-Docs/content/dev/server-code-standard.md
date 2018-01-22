+++
title = "Server Code Standard"
description = ""
weight = 7
+++

## Java Style Guide 

For most part applying the IDE Formating and it's default warning are sufficient 
to follow and keep the code in-line.  However, here is a lists of items to follow.
Borrows ideas from https://google.github.io/styleguide/javaguide.html

### Special escape sequences

For any character that has a special escape sequence (\b, \t, \n, \f, \r, \", \' and \\), that sequence is used rather than the corresponding octal (e.g. \012) or Unicode (e.g. \u000a) escape.

### Non-ASCII characters

For the remaining non-ASCII characters, either the actual Unicode character (e.g. ?) or the equivalent Unicode escape (e.g. \u221e) is used. The choice depends only on which makes the code easier to read and understand, although Unicode escapes outside string literals and comments are strongly discouraged.

Tip: In the Unicode escape case, and occasionally even when actual Unicode characters are used, an explanatory comment can be very helpful.

### Source file structure

A source file consists of, in order:

1.  License or copyright information  (Use Apache V2 on java side, GPL on front-end...the whole project is GPL)
2.  Package statement
3.  Import statements
4.  Exactly one top-level class

**Exactly one blank line** separates each section that is present.

### Wildcard imports

Avoid (let the IDE handle).
Let the IDE order.

### Braces

Use IDE Formating.  Change need to be agreed upon and update in the formatter. 
However, there is need for a change let the team know.

### Naming

Need to be descriptive. Lower-case to start and camelCase.  
(objectowner)(objectname)  Eg.  componentResource   Meaning: resource is part of or owned by component.

Typically, class are label according to function.  Eg.  ComponentService

#### Package names: 

Package names are all lowercase, with consecutive words simply concatenated together (no underscores). For example, com.example.deepspace, not com.example.deepSpace or com.example.deep_space

#### Class names:

Class names are written in UpperCamelCase.

Class names are typically nouns or noun phrases. For example, Character or ImmutableList. Interface names may also be nouns or noun phrases (for example, List), but may sometimes be adjectives or adjective phrases instead (for example, Readable).

There are no specific rules or even well-established conventions for naming annotation types.

Test classes are named starting with the name of the class they are testing, and ending with Test. For example, HashTest or HashIntegrationTest

#### Method names

Method names are written in lowerCamelCase.

Method names are typically verbs or verb phrases. For example, sendMessage or stop.

Underscores may appear in JUnit test method names to separate logical components of the name, with each component written in lowerCamelCase. One typical pattern is <methodUnderTest>_<state>, for example pop_emptyStack. There is no One Correct Way to name test methods.

#### Constant names

Constant names use CONSTANT_CASE: all uppercase letters, with each word separated from the next by a single underscore. But what is a constant, exactly?

Constants are static final fields whose contents are deeply immutable and whose methods have no detectable side effects. This includes primitives, Strings, immutable types, and immutable collections of immutable types. If any of the instance's observable state can change, it is not a constant. Merely intending to never mutate the object is not enough. 

#### Non-constant field names

Non-constant field names (static or otherwise) are written in lowerCamelCase.
These names are typically nouns or noun phrases. For example, computedValues or index.

#### Parameter names

Parameter names are written in lowerCamelCase.
One-character parameter names in public methods should be avoided.

#### Local variable names

Local variable names are written in lowerCamelCase.
Even when final and immutable, local variables are not considered to be constants, and should not be styled as constants.

#### Type variable names

Each type variable is named in one of two styles:

A single capital letter, optionally followed by a single numeral (such as E, T, X, T2)
A name in the form used for classes (see Section 5.2.2, Class names), followed by the capital letter T (examples: RequestT, FooBarT).


### Caught exceptions: not ignored

Except as noted below, it is very rarely correct to do nothing in response to a caught exception. (Typical responses are to log it, or if it is considered "impossible", rethrow it as an AssertionError.)

When it truly is appropriate to take no action whatsoever in a catch block, the reason this is justified is explained in a comment.

try {
  int i = Integer.parseInt(response);
  return handleNumericResponse(i);
} catch (NumberFormatException ok) {
  // it's not numeric; that's fine, just continue
}
return handleTextResponse(response);

### Static members: qualified using class

When a reference to a static class member must be qualified, it is qualified with that class's name, not with a reference or expression of that class's type.

Foo aFoo = ...;
Foo.aStaticMethod(); // good
aFoo.aStaticMethod(); // bad
somethingThatYieldsAFoo().aStaticMethod(); // very bad

### Finalizers: not used

It is extremely rare to override Object.finalize.
Tip: Don't do it. If you absolutely must, first read and understand Effective Java Item 7, "Avoid Finalizers," very carefully, and then don't do it.



### Where Javadoc is used

Public API points.  Also, on protected method.
No comments on get or setters is needed unless there different behavior than simply 
wrapping field access.

Exception: overrides - Javadoc is not always present on a method that overrides a supertype method. 

Use Block tags:

Any of the standard "block tags" that are used appear in the order @param, @return, @throws, @deprecated, and these four types never appear with an empty description. When a block tag doesn't fit on a single line, continuation lines are indented four (or more) spaces from the position of the @.


##General 

1. Business logic, Transactions, rules should be handled in the service code. (core-service)
2. API Interface should have documentation (javadocs)
3. SimpleDateFormat is not thread-safe create new instances; don't make static.
4. For decimal numbers use BigDecimal and use valueOf to instantiate.
5. Make sure any new code is placed in the appropriate module. Be cautious of dependencies. 
6. External resources should be handled by a Manager that in-turn handles the life-cycle.
7. Use example query framework where possible. Keep custom queries to a minimum as this allow for greater flexibility as well as type safety on the query.  Which in turn make refactoring easier and reduces errors.
8. Beware of any potential infinite loops.
9. Pay attention to Transaction boundaries. For the most part it's automatic but, you can carrier over transaction where it's unintended. 
10. Prefer constants over hard coded strings. Keep constants with entity/object they relate to.  For general constants use OpenstorefrontConstant.
11. Focus comments on WHY (Business Rule) rather than HOW for internal comments.  The how should be clear from the code. 

## Entities

1. Entities should have all validation annotations marked and have documentation annotations as well.
2. Use 'get' and 'set' and not 'is' as there's a lot of automatic processing (reflection) on entities. Also, no need to add javadocs on getters and setters.  Unless there is special handling.
3. Most entities should extends the standard entity.
4. Preference is to keep model flat (meaning don't embedded complex classes)  If there is embedded entities, make to use Casacde, OneToX annotation.  Also, beware of db issues.  Note, the DB and moxy doesn't handling List of primatives (Meaning Boxed primatives) well.  Eg. List<String> should be List<EmailAddresses> 
5. Document Entities with @APIdocument to update the api docs.
6. Prefer composition of entities in view rather than inheritance. Use inheritance for substitutability.
7. All storage entities must be registered with the DB.  This automatic for class in the entity package of the api module.


## REST API

1. Avoid naming entity and view model fields (type or id).  It can break the serialization.
2. Use the API annotations to document the service. All REST interfaces need documentation as to purpose.
3. All consumed entities must have a root object.  Eg.  String (fail),  TextObject with a field String (Valid).
4. Response also needs a root object.  A GenericEntity can be used.
5. Resource endpoints map to entities where Services act on the system or are cross entities.
6. "Actions" supplement the API and are used for internal operations.  Also, they use to handle uploads as there's no JAX-RS standard for that.


## Cache Handling

1. All cache handling should be done in the Services rather than leaking it up a layer of abstraction
2. Beware to not modify cached version of the data.  There is a 'weak reference' to the object so it can end up applying unwanted changes.
3. All caches should be setup in the cache manager. 

## Logging

Each levels has a different audience. (See java Docs)  Add logging message appropriate for each level.   Focus on logging thing that would aid in debugging (When, What, the State was at the time of the error, and Data owner Eg. what component does it belong to)

*SEVERE* - System Admin and End-User; the operation failed; something prevent normal execution;  If throwing an Openstorefront error please state the error and note any thing that can be done to correct it.

*WARNING* - Potential Problems, System Admin or End-users, The system should be able to continue but desired effect may not have been achieved.

*INFO* - Message to System Admin usually for information about operation completed.  

*CONFIG* - messages are intended to provide a variety of static configuration information (Mainly initialization of a system stuff)

*FINE* - Debugging information for developers.

*FINER* - Detailed debugging information

*FINEST* - Trace message; highly detail.  If gathering the information is time consuming (CPU wise) then check logging level state first.  If (log.isLoggable(Level.FINEST)).

**If you don't add logging it not avaliable.**  So we need add logging where appropriate.  Typically, on anti-conditions.  
Think, "If I was troubleshooting this in production could I get enough information to solve an issue?" 
Log the data owner and state.
Note: Tomcat's loghandler doesn't seem to handle "{0}" substitution.  Use MessageFormat.format to handle that.

## Cross Cutting Concerns

**Security** - Make sure permission are applied to the REST/External API and UI features.

**Logging** - Security changes/sensitivity are must have an audit logging. (Log action, who performed it)

**Data Restriction** - Make sure data that should be restricted is and restricted at the appropriate level.  This is not applied universally, as there's complications that require specific handling.

**Change History** - Entries and Evaluations are supported. Log field changes, adds, removes...etc

**Alerts** - Certain event must be track for administration alerts.

Use caution when modifying the code to keep these feature/requirements intact.    


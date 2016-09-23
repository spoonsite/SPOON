#Server code standard
-----

1. Business logic, Transactions, rules should be handled in the service code. (core-service)
2. API Interface should have documentation (javadocs)
3. SimpleDateFormat is not thread-safe create new instances don't make static.
4. For decimal numbers use BigDecimal and use valueOf to instantiate.
5. Make sure any new code is placed in the appropriate module. Be cautious of dependencies. 
6. External resources should be handled by a manager that in-turn handles the life-cycle.
7. Use example query framework where possible. Keep custom queries to a minimum as this allow for greater flexibility as well as type safety on the query.  Which in turn make refactoring easier and reduces errors.

##Entities

1. Entities should have all validation annotations marked and have documentation annotations as well.
2. Use 'get' and 'set' and not 'is' as there's a lot of automatic processing (reflection) on entities. Also, no need to add javadocs on getters and setters.  Unless there is a special handling.
3. Most entities should extends the standard entity.
4. Preference is to keep model flat (meaning don't embedded complex classes)  If embedded make to use Casacde, OneToX annotation as beware of db issue.  Also note the DB and moxy doesn't handling List of primatives well.  Eg. List<String> should be List<EmailAddresses> 
5. Document Entites with @APIdocument to have update for the api docs.
6. Prefer composition of entities in view rather than inheritance.
7. All storage entity must be register with the DB.  This automatic for class in the entity package of the api module.


##REST API

1. Avoid naming entity fields (type or id).   It can break the serialization.
2. Use the API annotations to document the service. All REST interfaces need documentations as to purpose.
3. All consumed entity must have a root object.  Eg.  String (fail),  TextObject with a field String (Valid).
4. Produce also needs a root object.  A GenericEntity can be used.
5. Resource endpoint map to entities where Services act on the system or are cross entities.
6. "Actions" supplement the API and are used for internal operations.  Also, use to handle uploads.


##Cache Handling

1. All cache handling should be done in the services rather than leaking it up a layer of abstraction
2. Beware to not modify cached version of the data.  There is a weak reference to the object so it can end up applying unwanted changes.
3. All caches should be setup in the cache manager. 

##Loggin

Each levels has a different audience. (See java Docs)  Add logging message appropriate for each level.   Focus on logging thing that would aid in debbuging (When, What, the State was at the time of the error, and Data owner Eg. what component does it belong to)

*SEVERE* - System Admin and End-User; the operation failed; something prevent normal execution;  If throwing an Openstorefront error please state the error and note any thing that can be done to correct it.

*WARNING* - Potential Problems, System Admin or End-users, The system should be able to continue but desired effect may not have been achieved.

*INFO* - Message to System Admin usually for information about operation completed.  

*CONFIG* - messages are intended to provide a variety of static configuration information (Mainly initialization of a system stuff)

*FINE* - Debugging information for developers.

*FINER* - Detailed debugging information

*FINEST* - Trace message; highly detail.  If gather the information is time consuming (cpu wise) then check loggin state first.  If (log.isLoggable(Level.FINIEST)).





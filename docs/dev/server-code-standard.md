#Server code standard
-----

1. Business logic, Transactions, rules should be handled in the service code. (core-service)
2. API Interface should have documentation (javadocs)
3. SimpleDateFormat is not thread-safe create new instances don't make static.
4. For decimal numbers use BigDecimal and use valueOf to instantiate.
5. Make sure any new code is placed in the appropriate module. Be cautious of dependencies. 
6. External resources should be handled by a manager that inturn handles the lifecycle.
7. Use example query framework where possible. Keep custom queries to a mininum as this allow for greater flexbily aas well as type safety on the query.  Which in turn make refactor easier reduces errors.

##Entities

1. Entities should have all validation annotations marked and have documentation annotations as well.
2. Use 'get' and 'set' and not 'is' as there's a lot of automatic processing (reflection) on entities. Also, no need to add javadocs on getters and setters.  Unless there is a special handling.
3. Most entiites should extends the standard entity.
4. Preference is to keep model flat (meaning don't embedded complex classes)  If embedded make to use Casacde, OneToX annotation as beware of db issue.  Also note the DB and moxy doesn't handling List of primatives well.  Eg. List<String> should be List<EmailAddresses> 
5. Document Entites with APIdocumnet to have update for the api docs.
6. Prefer componsition of entities in view rather than inhertance.
7. All storage entity must be register with the DB.  This automatic for class in the entity package of the api module.


##REST API

1. Avoid naming entity fields (type or id).   It can break the serialization.
2. Use the API annotations to document the service. All REST interfaces need documentations as to purpose.
3. All consumed entity must have a root object.  Eg.  String (fail),  TextObject with a field String (Valid).
4. Produce also needs a root object.  A GenericEntity can be used.
5. Resource endpoint map to entities where Services act on the system or are cross entities.
6. "Actions" supplement the API and are used for internal operations.  Also, use to handle uploads.


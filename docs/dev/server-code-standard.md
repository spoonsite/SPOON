#Server code standard
-----

1. Business logic, Transactions, rules should be handled in the service code. (core-service)
2. API Interface should have documentation (javadocs)
3. Entities should have all validation annotations marked and have documentation annotations as well.
4. All REST interfaces need documentation as to purpose.
5. SimpleDateFormat is not thread-safe create new instances don't make static.
6. For decimal numbers use BigDecimal and use valueOf to instantiate.
7. Make sure any new code is placed in the appropriate module. Be cautious of dependencies. 
8. Use 'get' and 'set' and not 'is' as there's a lot of automatic processing (reflection) on entities. Also, no need to add javadocs on getters and setters.  Unless there is a special handling.
9. External resources should be handled by a manager that inturn handles the lifecycle.


REST API

1. Avoid naming entity fields (type or id).   It can break the serialization.
2. Use the API annotations to document the service.
3. All consumed entity must have a root object.  Eg.  String (fail),  TextObject with a field String (Valid).
4. Produce also needs a root object.  A GenericEntity can be used.
5. Resource endpoint map to entities where Services act on the system or are cross entities.
6. "Actions" supplement the API and are used for internal operations.  Also, use to handle uploads.


+++
title = "Data Migration Guide"
description = ""
weight = 5
+++

This guide provide help for importing data from external sources as well as moving data from one instance to another instance.

<!--more-->

## Importing/Export (Third parties)

The application provides set of standard format that it accepts to import Attributes/Categories as well as Entries. It also provides a set of custom formats that can have mappings applied. These custom formats allow taking a variety of data with so simple handling.

See Admin Tools -> Data Management -> Imports

### Custom Mapping

{{% notice warning %}}
This feature is in beta.
{{% /notice %}}

An admin can create field mapping from a file field to a field in application. Some pre-defined transformation can be applied such as lowercasing the field.

In some cases, field mapping is insufficient to import a source. A plugin can be created to provide more handling control (see Developer Guide).

## System Archives (System to System / restore)

All system exports are handled through MongoDB. See the documentation on how to import and export the database: [mongoimport](https://docs.mongodb.com/manual/reference/program/mongoimport/) and [mongoexport](https://docs.mongodb.com/manual/reference/program/mongoexport/).

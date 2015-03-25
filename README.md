# Marketo REST SDK
This module is to provide Java API for Marketo Platform. Only asset's operations are planned to be covered by this library.
Currently you can:
 * list all e-mails,
 * fetch e-mail data by it's ID,
 * load e-mail content,
 * clone existing e-mail,
 * update email-content

## Building the codebase

To build the project:

    mvn clean install
    
The same with running integration tests:

    mvn -Pintegration-test clean install -Dmarketo.identity=<...> -Dmarketo.rest=<...> -Dmarketo.clientId=<...> -Dmarketo.clientSecret=<...>

where
  * `marketo.identity` - URL for Marketo Identity Service,
  * `marketo.rest` - URL for Marketo REST endpoint,
  * `marketo.clientId` - [Custom Service](http://developers.marketo.com/documentation/rest/custom-service/) client ID,
  * `marketo.clientSecret`- [Custom Service](http://developers.marketo.com/documentation/rest/custom-service/) client secret.

## Using the client

    TODO

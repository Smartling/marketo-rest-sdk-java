# Marketo REST SDK

## Building the codebase

To build the project:

    mvn clean install
    
The same with running intergation tests:

    mvn -Pintegration-test clean install -Dmarketo.identity=<...> -Dmarketo.rest=<...> -Dmarketo.clientId=<...> -Dmarketo.clientSecret=<...>

where
  * `marketo.identity` - URL for Marketo Identiry Service,
  * `marketo.rest` - URL for Marketo REST endpoint,
  * `marketo.clientId` - [Custom Service](http://developers.marketo.com/documentation/rest/custom-service/) client ID,
  * `marketo.clientSecret`- [Custom Service](http://developers.marketo.com/documentation/rest/custom-service/) client secret.

## Using the client

    TODO

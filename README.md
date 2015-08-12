# Marketo REST SDK
This module is to provide Java API for Marketo Platform. Only [asset's operations](http://developers.marketo.com/documentation/asset-api/) are planned to be covered by this library.
Currently you can:
 * Emails
   * [List all emails](#listing-all-emails),
   * [Fetch email data by it's ID](#loading-email-info),
   * Load email content,
   * Clone existing email,
   * Update email-content
 * Snippets
   * List snippets
   * Get Snippet by Id
   * Clone Snippet
   * Get Snippet Content by Id
   * Update Snippet Content by Id
 * Folders
   * Browse folders

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

## Using the client library

### Creating instance of Client

    MarketoClient client = MarketoRestClient.create("https://xxx-xxx-xxx.mktorest.com/identity", "https://xxx-xxx-xxx.mktorest.com/rest")
            .withCredentials("XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX", "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

### Listing all emails

Requesting 20 emails, string from the first one (i.e. with no offset):

    List<Email> page = client.listEmails(0, 20);

Requesting all emails page-by-page. Empty list will be returned when the end is reached:

    List<Email> page;
    int offset = 0;
    do {
        page = client.listEmails(offset, 20);
        offset += 20;
    } while(page.size() > 0);

### Loading email info

    TODO
